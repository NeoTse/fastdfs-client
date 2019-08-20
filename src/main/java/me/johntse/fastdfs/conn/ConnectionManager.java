package me.johntse.fastdfs.conn;

import me.johntse.fastdfs.exception.FdfsConnectException;
import me.johntse.fastdfs.exception.FdfsIOException;
import me.johntse.fastdfs.proto.AbstractHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 连接管理器，对连接池进行管理，提供连接池简单使用.
 *
 * @author johntse
 * @since 0.1.0
 */
public final class ConnectionManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionManager.class);

    private final ConnectionPool pool;
    private final List<InetSocketAddress> masterConnections;
    private final TrackerConnectionSelectStrategy strategy;

    /**
     * 通过给定的连接池和Tracker服务器地址构建一个连接管理器.
     * Master服务器地址分配使用默认（round robin）策略，由于只有一个地址，即只会返回该地址
     *
     * @param pool           连接池
     * @param trackerAddress 单个Tracker服务器地址
     */
    public ConnectionManager(ConnectionPool pool, InetSocketAddress trackerAddress) {
        if (pool == null || trackerAddress == null) {
            throw new IllegalArgumentException("pool or master address can't be empty.");
        }

        this.pool = pool;
        this.masterConnections = new ArrayList<>(4);
        masterConnections.add(trackerAddress);

        this.strategy = new DefaultSelectStrategy();
    }

    /**
     * 通过给定的连接池和Tracker服务器地址构建一个连接管理器.
     * Master服务器地址分配使用默认（round robin）策略
     *
     * @param pool             连接池
     * @param trackerAddresses 多个Tracker服务器地址
     */
    public ConnectionManager(ConnectionPool pool, Collection<InetSocketAddress> trackerAddresses) {
        this(pool, trackerAddresses, new DefaultSelectStrategy());
    }

    /**
     * 通过给定的连接池和Tracker服务器地址构建一个连接管理器.
     * Master服务器地址分配使用默认strategy指定的策略
     *
     * @param pool             连接池
     * @param trackerAddresses 多个Tracker服务器地址
     * @param strategy         Tracker服务器地址分配策略
     */
    public ConnectionManager(ConnectionPool pool, Collection<InetSocketAddress> trackerAddresses,
                             TrackerConnectionSelectStrategy strategy) {
        if (pool == null || trackerAddresses == null) {
            throw new IllegalArgumentException("pool or master address can't be empty.");
        }

        this.pool = pool;
        this.masterConnections = new ArrayList<>(trackerAddresses);
        this.strategy = strategy == null ? new DefaultSelectStrategy() : strategy;
    }

    /*public void addMasterConnection(InetSocketAddress masterAddress) {
        if (masterAddress == null) {
            return;
        }

        boolean isContains = masterConnections.contains(masterAddress);
        if (!isContains) {
            synchronized (masterConnections) {
                isContains = masterConnections.contains(masterAddress);
                if (!isContains) {
                    masterConnections.add(masterAddress);
                }
            }
        }
    }

    public void addMasterConnection(Collection<InetSocketAddress> masterAddresses) {
        if (masterAddresses == null || masterAddresses.isEmpty()) {
            return;
        }

        for (InetSocketAddress masterAddress : masterAddresses) {
            addMasterConnection(masterAddress);
        }
    }

    public void deleteMasterConnection(InetSocketAddress masterAddress) {
        if (masterAddress == null || masterConnections.isEmpty()) {
            return;
        }

        boolean isContains = masterConnections.contains(masterAddress);
        if (isContains) {
            synchronized (masterConnections) {
                isContains = masterConnections.contains(masterAddress);
                if (isContains) {
                    masterConnections.remove(masterAddress);
                }
            }
        }
    }

    public void deleteMasterConnection(Collection<InetSocketAddress> masterAddresses) {
        if (masterAddresses == null || masterAddresses.isEmpty() || masterConnections.isEmpty()) {
            return;
        }

        for (InetSocketAddress masterAddress : masterAddresses) {
            deleteMasterConnection(masterAddress);
        }
    }*/

    /**
     * 使用指定策略<code>strategy</code>获取一个tracker地址.
     *
     * @return 分配获取的tracker地址
     */
    public InetSocketAddress getOneTrackerAddress() {
        return strategy.alloc(masterConnections);
    }

    /**
     * 在远程<code>address</code>执行特定动作<code>handler</code>.
     *
     * @param address 连接目的地址
     * @param handler 待执行的动作
     * @param <T>     命令执行好返回的值类型
     * @return 执行结果
     */
    @SuppressWarnings("unchecked")
    public <T> T remoteExecute(InetSocketAddress address, AbstractHandler handler) {
        Connection conn = getConnection(address);

        LOGGER.debug("Execute command {} @connection {}", handler.getName(), conn);
        LOGGER.debug("Activate connection number: {}", pool.getNumActive(address));
        LOGGER.debug("Idle connection number: {}", pool.getNumIdle(address));

        try {
            handler.handle(conn);
            return (T) handler.getResult(conn.getCharset());
        } catch (Exception e) {
            LOGGER.error(String.format("Execute command %s failed.", handler.getName()), e);
            throw new FdfsIOException(String.format("Execute command %s @connection %s",
                    handler.getName(), conn), e);
        } finally {
            try {
                if (null != conn) {
                    pool.returnObject(address, conn);
                    LOGGER.debug("Return pooled connection.");
                }
            } catch (Exception e) {
                LOGGER.error("Return pooled connection error", e);
            }
        }
    }

    /**
     * 连接池状态快照.
     */
    public void snapshot() {
        LOGGER.info("------------Connection Pool Snapshot------------");
        LOGGER.info("Activate connection number: {}", pool.getNumActive());
        LOGGER.info("Idle connection number: {}", pool.getNumIdle());
        LOGGER.info("Waiters count: {}", pool.getNumWaiters());
        LOGGER.info("Created count: {}", pool.getCreatedCount());
        LOGGER.info("Borrowed count: {}", pool.getBorrowedCount());
        LOGGER.info("Returned count: {}", pool.getReturnedCount());
        LOGGER.info("Destroyed count: {}", pool.getDestroyedCount());
    }

    /**
     * 关闭所管理的连接池.
     */
    public void close() {
        if (pool != null) {
            pool.close();
        }
    }

    private Connection getConnection(InetSocketAddress address) {
        Connection conn;

        try {
            conn = pool.borrowObject(address);
        } catch (Exception e) {
            LOGGER.error("Unable to borrow buffer from pool", e);
            throw new FdfsConnectException("Unable to borrow buffer from pool", e);
        }

        return conn;
    }

    public interface TrackerConnectionSelectStrategy {
        /**
         * 根据具体实现策略分配一个可用的Tracker连接.
         *
         * @param trackerConnections 可用于分配的Tracker连接
         * @return 根据策略获取的Tracker连接
         */
        InetSocketAddress alloc(List<InetSocketAddress> trackerConnections);
    }

    /**
     * 默认分配策略（round robin）.
     */
    public static class DefaultSelectStrategy implements TrackerConnectionSelectStrategy {
        AtomicInteger preIndex = new AtomicInteger(0);

        @Override
        public InetSocketAddress alloc(List<InetSocketAddress> trackerConnections) {
            int size = trackerConnections.size();
            if (size == 1) {
                return trackerConnections.get(0);
            }

            InetSocketAddress address =
                    trackerConnections.get(preIndex.getAndSet((preIndex.get() + 1) % size));

            LOGGER.debug("{} alloc {}", DefaultSelectStrategy.class.getName(), address);

            return address;
        }
    }

    /**
     * 随机分配策略.
     */
    public static class RandomSelectStrategy implements TrackerConnectionSelectStrategy {
        static final Random RANDOM = new Random(System.currentTimeMillis());

        @Override
        public InetSocketAddress alloc(List<InetSocketAddress> trackerConnections) {
            if (trackerConnections.size() == 1) {
                return trackerConnections.get(0);
            }

            int index = RANDOM.nextInt(trackerConnections.size());
            InetSocketAddress address = trackerConnections.get(index);

            LOGGER.debug("{} alloc {}", RandomSelectStrategy.class.getName(), address);

            return address;
        }
    }

    /**
     * 负载均衡分配策略.
     */
    public static class LoadBalanceStrategy implements TrackerConnectionSelectStrategy {
        final ConnectionPool pool;
        final Map<String, InetSocketAddress> addresses;

        public LoadBalanceStrategy(ConnectionManager manager) {
            this.pool = manager.pool;
            addresses = new HashMap<>();
        }

        private static InetSocketAddress valueOf(String address) {
            // work hard
            // InetSocketAddress.toString() format "hostname/ip:port"
            int hostNameEndIndex = address.indexOf("/");
            if (hostNameEndIndex < 0 || hostNameEndIndex >= address.length()) {
                String message = String.format("Convert %s to InetSocketAddress failed.", address);
                LOGGER.error(message);
                throw new FdfsConnectException(message);
            }

            int portStartIndex = address.indexOf(':');
            if (portStartIndex <= 0 || hostNameEndIndex >= portStartIndex
                    || portStartIndex >= address.length()) {
                String message = String.format("Convert %s to InetSocketAddress failed.", address);
                LOGGER.error(message);
                throw new FdfsConnectException(message);
            }

            String hostName = address.substring(0, hostNameEndIndex);
            String ip = address.substring(hostNameEndIndex + 1, portStartIndex);
            int port = Integer.parseInt(address.substring(portStartIndex + 1));

            if (hostName.isEmpty()) {
                return new InetSocketAddress(ip, port);
            } else {
                return new InetSocketAddress(hostName, port);
            }
        }

        @Override
        public InetSocketAddress alloc(List<InetSocketAddress> trackerConnections) {
            if (trackerConnections.size() == 1) {
                return trackerConnections.get(0);
            }

            final Map<String, Integer> numActivePerKey = pool.getNumActivePerKey();

            int min = Integer.MAX_VALUE;
            String result = "";
            for (Map.Entry<String, Integer> entry : numActivePerKey.entrySet()) {
                if (entry.getValue() <= min) {
                    result = entry.getKey();
                    min = entry.getValue();
                }
            }

            if (result.isEmpty()) {
                String message = "LoadBalanceStrategy alloc a master connection failed.";
                LOGGER.error(message);
                throw new FdfsConnectException(message);
            }

            if (!addresses.containsKey(result)) {
                synchronized (addresses) {
                    if (!addresses.containsKey(result)) {
                        addresses.put(result, valueOf(result));
                    }
                }
            }

            InetSocketAddress address = addresses.get(result);

            LOGGER.debug("{} alloc {}", LoadBalanceStrategy.class.getName(), result);

            return address;
        }
    }
}
