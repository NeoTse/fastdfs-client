package me.johntse.fastdfs;

import me.johntse.fastdfs.conn.ConnectionManager;
import me.johntse.fastdfs.conn.ConnectionPool;
import me.johntse.fastdfs.conn.ConnectionPoolConfig;
import me.johntse.fastdfs.conn.TcpConnectionPoolFactory;
import me.johntse.fastdfs.proto.ProtocolConstants;
import me.johntse.fastdfs.proto.struct.MetaData;
import me.johntse.fastdfs.proto.struct.StoragePath;
import me.johntse.fastdfs.service.StorageClient;
import me.johntse.fastdfs.service.TrackerClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Set;

/**
 * fast dfs客户端API.
 *
 * @author johntse
 * @since 0.1.0
 */
public final class FastDfsClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(FastDfsClient.class);

    private ConnectionManager connectionManager;
    private TrackerClient trackerClient;
    private StorageClient storageClient;

    private FastDfsClient(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        trackerClient = new TrackerClient(connectionManager);
        storageClient = new StorageClient(connectionManager);
    }

    /**
     * 获取一个客户端构造器.
     *
     * @param trackers           tracker地址列表
     * @param maxTotal           连接池最大连接数
     * @param blockWhenExhausted 连接池耗尽时是否阻塞
     * @return 客户端构造器
     */
    public static FastDfsClient.Builder newBuilder(List<InetSocketAddress> trackers,
                                                   int maxTotal, boolean blockWhenExhausted) {
        return new FastDfsClient.Builder(trackers, maxTotal, blockWhenExhausted);
    }

    /**
     * 上传文件到fastDFS.
     *
     * @param file           要上传的文件
     * @param fileExtendName 文件扩展名
     * @param metaData       文件元数据
     * @return 上传成功，文件存储路径信息
     */
    public StoragePath uploadFile(File file, String fileExtendName, Set<MetaData> metaData) {
        FileInputStream inputStream = null;

        try {
            inputStream = new FileInputStream(file);

            LOGGER.debug("Upload file {}", file);
            return storageClient.uploadFile(trackerClient.getStorage(), inputStream,
                    file.length(), fileExtendName, metaData);
        } catch (FileNotFoundException e) {
            LOGGER.error("Upload file failed.", e);
            return null;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
    }

    public StoragePath uploadFile(File file, String fileExtendName) {
        return uploadFile(file, fileExtendName, null);
    }

    /**
     * 从fastDFS上下载指定文件.
     *
     * @param groupName 分组名称
     * @param filePath  文件存储路径
     * @param offset    偏移量
     * @param fileSize  文件大小，0表示从偏移量开始所有剩余大小
     * @param saveFile  下载文件保存的本地文件
     */
    public void downloadFile(String groupName, String filePath,
                             long offset, long fileSize, File saveFile) {
        FileOutputStream outputStream = null;

        try {
            outputStream = new FileOutputStream(saveFile);

            LOGGER.debug("Download file {}", filePath);
            storageClient.downloadFile(trackerClient.fetchOneStorage(groupName, filePath),
                    filePath, offset, fileSize, outputStream);
        } catch (FileNotFoundException e) {
            LOGGER.error("Download file failed. ", e);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
    }

    public void downloadFile(String groupName, String filePath, File saveFile) {
        downloadFile(groupName, filePath, 0, 0, saveFile);
    }

    /**
     * 删除指定文件.
     *
     * @param groupName 文件所在分组名称
     * @param filePath  文件存储路径
     */
    public void deleteFile(String groupName, String filePath) {
        LOGGER.debug("Delete file {}", filePath);
        storageClient.deleteFile(trackerClient.getUpdateStorage(groupName, filePath), filePath);
    }

    /**
     * 设置指定文件的元数据信息.
     *
     * @param groupName   文件所在分组名称
     * @param filePath    文件存储路径
     * @param metaData    要设置的元数据信息
     * @param isOverWrite 是否覆盖原有的元数据，如果存在旧元数据的话
     */
    public void setMetaData(String groupName, String filePath,
                            Set<MetaData> metaData, boolean isOverWrite) {
        LOGGER.debug("Set meta of file {}", filePath);
        storageClient.setMetaData(trackerClient.getUpdateStorage(groupName, filePath), filePath, metaData,
                isOverWrite ? ProtocolConstants.OperationFlag.STORAGE_SET_METADATA_FLAG_OVERWRITE :
                        ProtocolConstants.OperationFlag.STORAGE_SET_METADATA_FLAG_MERGE);
    }

    /**
     * 获取指定文件的元数据信息.
     *
     * @param groupName 文件所在分组名称
     * @param filePath  文件存储路径
     * @return 文件对应的元数据信息
     */
    public Set<MetaData> getMetaData(String groupName, String filePath) {
        LOGGER.debug("Get meta of file {}", filePath);
        return storageClient.getMetaData(trackerClient.getUpdateStorage(groupName, filePath),
                filePath);
    }

    /**
     * 生成目前连接池使用情况.
     */
    public void report() {
        connectionManager.snapshot();
    }

    /**
     * 销毁资源.
     */
    public void destroy() {
        if (connectionManager != null) {
            connectionManager.close();
        }
    }

    public TrackerClient getTrackerClient() {
        return trackerClient;
    }

    public StorageClient getStorageClient() {
        return storageClient;
    }

    public static class Builder {
        private List<InetSocketAddress> trackers;
        private ConnectionManager.TrackerConnectionSelectStrategy selectStrategy;
        private ConnectionPoolConfig.Builder poolConfigBuilder;
        private Charset charset;
        private int soTimeout;
        private int connectionTimeout;

        /**
         * 客户端构建器.
         *
         * @param trackers           tracker地址列表
         * @param maxTotal           连接池最大连接数
         * @param blockWhenExhausted 连接池耗尽时是否阻塞
         */
        public Builder(List<InetSocketAddress> trackers, int maxTotal, boolean blockWhenExhausted) {
            if (trackers == null || trackers.isEmpty()) {
                throw new IllegalArgumentException("There is no any tracker.");
            }

            this.trackers = trackers;
            this.poolConfigBuilder = new ConnectionPoolConfig.Builder(maxTotal, blockWhenExhausted);
            this.charset = Charset.forName("utf8");
            this.soTimeout = 30 * 1000; //30s
            this.connectionTimeout = 60 * 1000; // 60s
        }

        public Builder setTrackerSelectStrategy(ConnectionManager.TrackerConnectionSelectStrategy selectStrategy) {
            this.selectStrategy = selectStrategy;
            return this;
        }

        public Builder setCharset(String charset) {
            this.charset = Charset.forName(charset);
            return this;
        }

        public Builder setSoTimeout(int soTimeout) {
            this.soTimeout = soTimeout;
            return this;
        }

        public Builder setConnectionTimeout(int connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
            return this;
        }

        public Builder setConnectionPoolMaxPerIdle(int maxPerIdle) {
            poolConfigBuilder.setMaxPerIdle(maxPerIdle);
            return this;
        }

        public Builder setConnectionPoolMinPerIdle(int minPerIdle) {
            poolConfigBuilder.setMinPerIdle(minPerIdle);
            return this;
        }

        public Builder setConnectionPoolTestWhileIdle(boolean testWhileIdle) {
            poolConfigBuilder.setTestWhileIdle(testWhileIdle);
            return this;
        }

        public Builder setConnectionPoolNumTestsPerEvictionRun(int numTestsPerEvictionRun) {
            poolConfigBuilder.setNumTestsPerEvictionRun(numTestsPerEvictionRun);
            return this;
        }

        public Builder setConnectionPoolMaxWaitMillis(int maxWaitMillis) {
            poolConfigBuilder.setMaxWaitMillis(maxWaitMillis);
            return this;
        }

        public Builder setConnectionPoolMinEvictableIdleTimeMillis(int minEvictableIdleTimeMillis) {
            poolConfigBuilder.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
            return this;
        }

        public Builder setConnectionPoolTimeBetweenEvictionRunsMillis(int timeBetweenEvictionRunsMillis) {
            poolConfigBuilder.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
            return this;
        }

        /**
         * 根据参数设置构建出客户端.
         *
         * @return 符合要求的客户端
         */
        public FastDfsClient build() {
            TcpConnectionPoolFactory poolFactory =
                    new TcpConnectionPoolFactory(soTimeout, connectionTimeout, charset);
            ConnectionPool pool = new ConnectionPool(poolFactory, poolConfigBuilder.build());
            ConnectionManager connectionManager = new ConnectionManager(pool, trackers, selectStrategy);

            return new FastDfsClient(connectionManager);
        }
    }
}
