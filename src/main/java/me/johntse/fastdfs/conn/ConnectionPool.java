package me.johntse.fastdfs.conn;

import org.apache.commons.pool2.KeyedPooledObjectFactory;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;

import java.net.InetSocketAddress;
import java.util.Map;

/**
 * 连接池，使用代理模式.
 *
 * @author johntse
 * @since 0.1.0
 */
public class ConnectionPool {
    private final GenericKeyedObjectPool<InetSocketAddress, Connection> actualPool;

    /**
     * 使用默认的配置构建一个连接池.
     *
     * @param factory 代理的实际连接池
     */
    public ConnectionPool(KeyedPooledObjectFactory<InetSocketAddress, Connection> factory) {
        actualPool = new GenericKeyedObjectPool<InetSocketAddress, Connection>(factory);
    }

    /**
     * 连接池构建函数，通过提供的对应的对象池创建工厂和配置，初始化连接池.
     *
     * @param factory 对象池创建工厂，此处为带键的对象池工厂。其中键为连接的服务端地址
     * @param config  连接池相关配置
     */
    public ConnectionPool(KeyedPooledObjectFactory<InetSocketAddress, Connection> factory,
                          ConnectionPoolConfig config) {
        GenericKeyedObjectPoolConfig poolConfig = new GenericKeyedObjectPoolConfig();
        poolConfig.setMaxTotal(config.maxTotal);
        poolConfig.setMaxIdlePerKey(config.maxPerIdle);
        poolConfig.setMinIdlePerKey(config.minPerIdle);
        poolConfig.setTestWhileIdle(config.testWhileIdle);
        poolConfig.setBlockWhenExhausted(config.blockWhenExhausted);
        poolConfig.setMaxWaitMillis(config.maxWaitMillis);
        poolConfig.setMinEvictableIdleTimeMillis(config.minEvictableIdleTimeMillis);
        poolConfig.setNumTestsPerEvictionRun(config.numTestsPerEvictionRun);
        poolConfig.setTimeBetweenEvictionRunsMillis(config.timeBetweenEvictionRunsMillis);

        actualPool = new GenericKeyedObjectPool<InetSocketAddress, Connection>(factory, poolConfig);
    }

    public Connection borrowObject(InetSocketAddress key) throws Exception {
        return actualPool.borrowObject(key);
    }

    public void returnObject(InetSocketAddress key, Connection obj) throws Exception {
        actualPool.returnObject(key, obj);
    }

    public void invalidateObject(InetSocketAddress key, Connection obj) throws Exception {
        actualPool.invalidateObject(key, obj);
    }

    public void addObject(InetSocketAddress key) throws Exception {
        actualPool.addObject(key);
    }

    public int getNumIdle() {
        return actualPool.getNumIdle();
    }

    public int getNumIdle(InetSocketAddress key) {
        return actualPool.getNumIdle(key);
    }

    public int getNumActive() {
        return actualPool.getNumActive();
    }

    public int getNumActive(InetSocketAddress key) {
        return actualPool.getNumActive(key);
    }

    public Map<String, Integer> getNumActivePerKey() {
        return actualPool.getNumActivePerKey();
    }

    public long getBorrowedCount() {
        return actualPool.getBorrowedCount();
    }

    public long getReturnedCount() {
        return actualPool.getReturnedCount();
    }

    public long getCreatedCount() {
        return actualPool.getCreatedCount();
    }

    public long getDestroyedCount() {
        return actualPool.getDestroyedCount();
    }

    public int getNumWaiters() {
        return actualPool.getNumWaiters();
    }

    public void clear() throws Exception, UnsupportedOperationException {
        actualPool.clear();
    }

    public void clear(InetSocketAddress key) throws Exception, UnsupportedOperationException {
        actualPool.clear(key);
    }

    public void close() {
        actualPool.close();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this;
    }
}
