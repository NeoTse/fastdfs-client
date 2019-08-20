package me.johntse.fastdfs.proto.struct;

import me.johntse.fastdfs.proto.ProtocolConstants;
import me.johntse.fastdfs.proto.base.Field;

import java.util.Date;

/**
 * Storage状态信息.
 *
 * @author johntse
 * @since 0.1.0
 */
public class StorageState {

    /**
     * 状态码.
     */
    @Field(index = 0, size = 1)
    private byte status;

    /**
     * Storage标识.
     */
    @Field(index = 1, size = ProtocolConstants.FDFS_STORAGE_ID_MAX_SIZE)
    private String id;

    /**
     * Storage服务器ip地址.
     */
    @Field(index = 2, size = ProtocolConstants.FDFS_IPADDR_SIZE)
    private String ipAddress;

    /**
     * Storage服务器HTTP服务域名.
     */
    @Field(index = 3, size = ProtocolConstants.FDFS_DOMAIN_NAME_MAX_SIZE)
    private String httpDomainName;

    /**
     * 源IP地址.
     */
    @Field(index = 4, size = ProtocolConstants.FDFS_IPADDR_SIZE)
    private String srcIpAddress;

    /**
     * 版本信息.
     */
    @Field(index = 5, size = ProtocolConstants.FDFS_VERSION_SIZE)
    private String version;

    /**
     * Storage加入时间.
     * Unix时间戳
     */
    @Field(index = 6, size = 8)
    private Date joinTime;

    /**
     * Storage服务启动时间.
     */
    @Field(index = 7, size = 8)
    private Date upTime;

    /**
     * 磁盘存储总量.
     * 单位MB
     */
    @Field(index = 8, size = 8)
    private long total;

    /**
     * 磁盘空闲存储量.
     * 单位MB
     */
    @Field(index = 9, size = 8)
    private long free;

    /**
     * 文件上传权重.
     * 用于选择Storage
     */
    @Field(index = 10, size = 8)
    private long uploadPriority;

    /**
     * 单个Storage的存储路径数.
     */
    @Field(index = 11, size = 8)
    private long storePathCount;

    /**
     * 存储路径下的子目录数.
     */
    @Field(index = 12, size = 8)
    private long subDirPerStorePath;

    /**
     * 当前写入动作发生的路径.
     */
    @Field(index = 13, size = 8)
    private long currentWriteStorePath;

    /**
     * Storage存储服务端口.
     */
    @Field(index = 14, size = 8)
    private long storageServerPort;
    /**
     * Storage存储Http Server端口.
     * 不建议使用
     */
    @Field(index = 15, size = 8)
    private long storageHttpPort;

    /**
     * 当前Http Server已分配的连接数(包括正在使用和已经关闭释放的).
     */
    @Field(index = 16, size = 4)
    private int connectionAllocCount;

    /**
     * 当前Http Server活跃的连接数.
     */
    @Field(index = 17, size = 4)
    private int connectionCurrentCount;

    /**
     * 当前Http Server允许的最大连接数.
     */
    @Field(index = 18, size = 4)
    private int connectionMaxCount;

    /**
     * 文件上传总数.
     */
    @Field(index = 19, size = 8)
    private long totalUploadCount;

    /**
     * 文件成功上传数.
     */
    @Field(index = 20, size = 8)
    private long successUploadCount;

    /**
     * 文件合并总数.
     */
    @Field(index = 21, size = 8)
    private long totalAppendCount;

    /**
     * 文件成功合并数.
     */
    @Field(index = 22, size = 8)
    private long successAppendCount;

    /**
     * 文件修改总数.
     */
    @Field(index = 23, size = 8)
    private long totalModifyCount;

    /**
     * 文件成功修改数.
     */
    @Field(index = 24, size = 8)
    private long successModifyCount;

    /**
     * 文件截断总数.
     */
    @Field(index = 25, size = 8)
    private long totalTruncateCount;

    /**
     * 文件成功截断数.
     */
    @Field(index = 26, size = 8)
    private long successTruncateCount;

    /**
     * 元数据设置总数.
     */
    @Field(index = 27, size = 8)
    private long totalSetMetaCount;

    /**
     * 元数据成功设置数.
     */
    @Field(index = 28, size = 8)
    private long successSetMetaCount;

    /**
     * 文件删除总数.
     */
    @Field(index = 29, size = 8)
    private long totalDeleteCount;

    /**
     * 文件成功删除数.
     */
    @Field(index = 30, size = 8)
    private long successDeleteCount;

    /**
     * 文件下载总量.
     */
    @Field(index = 31, size = 8)
    private long totalDownloadCount;

    /**
     * 文件成功下载量.
     */
    @Field(index = 32, size = 8)
    private long successDownloadCount;

    /**
     * 元数据获取总数.
     */
    @Field(index = 33, size = 8)
    private long totalGetMetaCount;

    /**
     * 元数据成功获取数.
     */
    @Field(index = 34, size = 8)
    private long successGetMetaCount;

    /**
     * 链接创建总数.
     */
    @Field(index = 35, size = 8)
    private long totalCreateLinkCount;

    /**
     * 链接成功创建数.
     */
    @Field(index = 36, size = 8)
    private long successCreateLinkCount;

    /**
     * 链接删除总数.
     */
    @Field(index = 37, size = 8)
    private long totalDeleteLinkCount;

    /**
     * 链接成功删除数.
     */
    @Field(index = 38, size = 8)
    private long successDeleteLinkCount;

    /**
     * 上传数据总量.
     * 单位byte
     */
    @Field(index = 39, size = 8)
    private long totalUploadBytes;

    /**
     * 成功上传数据量.
     * 单位byte
     */
    @Field(index = 40, size = 8)
    private long successUploadBytes;

    /**
     * 追加数据总量.
     * 单位byte
     */
    @Field(index = 41, size = 8)
    private long totalAppendBytes;
    /**
     * 成功追加数据量.
     * 单位byte
     */
    @Field(index = 42, size = 8)
    private long successAppendBytes;

    /**
     * 修改数据总量.
     * 单位byte
     */
    @Field(index = 43, size = 8)
    private long totalModifyBytes;

    /**
     * 成功修改数据量.
     * 单位byte
     */
    @Field(index = 44, size = 8)
    private long successModifyBytes;

    /**
     * 下载数据总量.
     * 单位byte
     */
    @Field(index = 45, size = 8)
    private long totalDownloadBytes;

    /**
     * 成功下载数据量.
     * 单位byte
     */
    @Field(index = 46, size = 8)
    private long successDownloadBytes;

    /**
     * 同步数据量.
     * 单位byte
     */
    @Field(index = 47, size = 8)
    private long totalSyncInBytes;

    /**
     * 成功同步数据量.
     * 单位byte
     */
    @Field(index = 48, size = 8)
    private long successSyncInBytes;


    /**
     * 同步输出数据量.
     * 单位byte
     */
    @Field(index = 49, size = 8)
    private long totalSyncOutBytes;

    /**
     * 成功同步输出数据量.
     * 单位byte
     */
    @Field(index = 50, size = 8)
    private long successSyncOutBytes;

    /**
     * 文件打开数量.
     */
    @Field(index = 51, size = 8)
    private long totalFileOpenCount;

    /**
     * 文件成功打开数量.
     */
    @Field(index = 52, size = 8)
    private long successFileOpenCount;

    /**
     * 文件读取数量.
     */
    @Field(index = 53, size = 8)
    private long totalFileReadCount;

    /**
     * 文件成功读取数量.
     */
    @Field(index = 54, size = 8)
    private long successFileReadCount;

    /**
     * 文件写数量.
     */
    @Field(index = 55, size = 8)
    private long totalFileWriteCount;

    /**
     * 文件成功写数量.
     */
    @Field(index = 56, size = 8)
    private long successFileWriteCount;

    /**
     * 最后上传时间.
     */
    @Field(index = 57, size = 8)
    private Date lastSourceUpdate;

    /**
     * 最后同步时间.
     */
    @Field(index = 58, size = 8)
    private Date lastSyncUpdate;

    /**
     * 最后同步时间戳.
     */
    @Field(index = 59, size = 8)
    private Date lastSyncedTimestamp;

    /**
     * 最后心跳时间.
     */
    @Field(index = 60, size = 8)
    private Date lastHeartBeatTime;

    /**
     * 是否为trunk服务器.
     */
    @Field(index = 61, size = 1)
    private boolean isTrunkServer;

    public StorageState() {

    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getHttpDomainName() {
        return httpDomainName;
    }

    public void setHttpDomainName(String httpDomainName) {
        this.httpDomainName = httpDomainName;
    }

    public String getSrcIpAddress() {
        return srcIpAddress;
    }

    public void setSrcIpAddress(String srcIpAddress) {
        this.srcIpAddress = srcIpAddress;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Date getJoinTime() {
        return new Date(joinTime.getTime());
    }

    public void setJoinTime(Date joinTime) {
        this.joinTime = new Date(joinTime.getTime());
    }

    public Date getUpTime() {
        return new Date(upTime.getTime());
    }

    public void setUpTime(Date upTime) {
        this.upTime = new Date(upTime.getTime());
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getFree() {
        return free;
    }

    public void setFree(long free) {
        this.free = free;
    }

    public int getUploadPriority() {
        return (int) uploadPriority;
    }

    public void setUploadPriority(int uploadPriority) {
        this.uploadPriority = uploadPriority;
    }

    public int getStorePathCount() {
        return (int) storePathCount;
    }

    public void setStorePathCount(int storePathCount) {
        this.storePathCount = storePathCount;
    }

    public int getSubDirPerStorePath() {
        return (int) subDirPerStorePath;
    }

    public void setSubDirPerStorePath(int subDirPerStorePath) {
        this.subDirPerStorePath = subDirPerStorePath;
    }

    public int getCurrentWriteStorePath() {
        return (int) currentWriteStorePath;
    }

    public void setCurrentWriteStorePath(int currentWriteStorePath) {
        this.currentWriteStorePath = currentWriteStorePath;
    }

    public int getStorageServerPort() {
        return (int) storageServerPort;
    }

    public void setStorageServerPort(int storageServerPort) {
        this.storageServerPort = storageServerPort;
    }

    public int getStorageHttpPort() {
        return (int) storageHttpPort;
    }

    public void setStorageHttpPort(int storageHttpPort) {
        this.storageHttpPort = storageHttpPort;
    }

    public int getConnectionAllocCount() {
        return connectionAllocCount;
    }

    public void setConnectionAllocCount(int connectionAllocCount) {
        this.connectionAllocCount = connectionAllocCount;
    }

    public int getConnectionCurrentCount() {
        return connectionCurrentCount;
    }

    public void setConnectionCurrentCount(int connectionCurrentCount) {
        this.connectionCurrentCount = connectionCurrentCount;
    }

    public int getConnectionMaxCount() {
        return connectionMaxCount;
    }

    public void setConnectionMaxCount(int connectionMaxCount) {
        this.connectionMaxCount = connectionMaxCount;
    }

    public long getTotalUploadCount() {
        return totalUploadCount;
    }

    public void setTotalUploadCount(long totalUploadCount) {
        this.totalUploadCount = totalUploadCount;
    }

    public long getSuccessUploadCount() {
        return successUploadCount;
    }

    public void setSuccessUploadCount(long successUploadCount) {
        this.successUploadCount = successUploadCount;
    }

    public long getTotalAppendCount() {
        return totalAppendCount;
    }

    public void setTotalAppendCount(long totalAppendCount) {
        this.totalAppendCount = totalAppendCount;
    }

    public long getSuccessAppendCount() {
        return successAppendCount;
    }

    public void setSuccessAppendCount(long successAppendCount) {
        this.successAppendCount = successAppendCount;
    }

    public long getTotalModifyCount() {
        return totalModifyCount;
    }

    public void setTotalModifyCount(long totalModifyCount) {
        this.totalModifyCount = totalModifyCount;
    }

    public long getSuccessModifyCount() {
        return successModifyCount;
    }

    public void setSuccessModifyCount(long successModifyCount) {
        this.successModifyCount = successModifyCount;
    }

    public long getTotalTruncateCount() {
        return totalTruncateCount;
    }

    public void setTotalTruncateCount(long totalTruncateCount) {
        this.totalTruncateCount = totalTruncateCount;
    }

    public long getSuccessTruncateCount() {
        return successTruncateCount;
    }

    public void setSuccessTruncateCount(long successTruncateCount) {
        this.successTruncateCount = successTruncateCount;
    }

    public long getTotalSetMetaCount() {
        return totalSetMetaCount;
    }

    public void setTotalSetMetaCount(long totalSetMetaCount) {
        this.totalSetMetaCount = totalSetMetaCount;
    }

    public long getSuccessSetMetaCount() {
        return successSetMetaCount;
    }

    public void setSuccessSetMetaCount(long successSetMetaCount) {
        this.successSetMetaCount = successSetMetaCount;
    }

    public long getTotalDeleteCount() {
        return totalDeleteCount;
    }

    public void setTotalDeleteCount(long totalDeleteCount) {
        this.totalDeleteCount = totalDeleteCount;
    }

    public long getSuccessDeleteCount() {
        return successDeleteCount;
    }

    public void setSuccessDeleteCount(long successDeleteCount) {
        this.successDeleteCount = successDeleteCount;
    }

    public long getTotalDownloadCount() {
        return totalDownloadCount;
    }

    public void setTotalDownloadCount(long totalDownloadCount) {
        this.totalDownloadCount = totalDownloadCount;
    }

    public long getSuccessDownloadCount() {
        return successDownloadCount;
    }

    public void setSuccessDownloadCount(long successDownloadCount) {
        this.successDownloadCount = successDownloadCount;
    }

    public long getTotalGetMetaCount() {
        return totalGetMetaCount;
    }

    public void setTotalGetMetaCount(long totalGetMetaCount) {
        this.totalGetMetaCount = totalGetMetaCount;
    }

    public long getSuccessGetMetaCount() {
        return successGetMetaCount;
    }

    public void setSuccessGetMetaCount(long successGetMetaCount) {
        this.successGetMetaCount = successGetMetaCount;
    }

    public long getTotalCreateLinkCount() {
        return totalCreateLinkCount;
    }

    public void setTotalCreateLinkCount(long totalCreateLinkCount) {
        this.totalCreateLinkCount = totalCreateLinkCount;
    }

    public long getSuccessCreateLinkCount() {
        return successCreateLinkCount;
    }

    public void setSuccessCreateLinkCount(long successCreateLinkCount) {
        this.successCreateLinkCount = successCreateLinkCount;
    }

    public long getTotalDeleteLinkCount() {
        return totalDeleteLinkCount;
    }

    public void setTotalDeleteLinkCount(long totalDeleteLinkCount) {
        this.totalDeleteLinkCount = totalDeleteLinkCount;
    }

    public long getSuccessDeleteLinkCount() {
        return successDeleteLinkCount;
    }

    public void setSuccessDeleteLinkCount(long successDeleteLinkCount) {
        this.successDeleteLinkCount = successDeleteLinkCount;
    }

    public long getTotalUploadBytes() {
        return totalUploadBytes;
    }

    public void setTotalUploadBytes(long totalUploadBytes) {
        this.totalUploadBytes = totalUploadBytes;
    }

    public long getSuccessUploadBytes() {
        return successUploadBytes;
    }

    public void setSuccessUploadBytes(long successUploadBytes) {
        this.successUploadBytes = successUploadBytes;
    }

    public long getTotalAppendBytes() {
        return totalAppendBytes;
    }

    public void setTotalAppendBytes(long totalAppendBytes) {
        this.totalAppendBytes = totalAppendBytes;
    }

    public long getSuccessAppendBytes() {
        return successAppendBytes;
    }

    public void setSuccessAppendBytes(long successAppendBytes) {
        this.successAppendBytes = successAppendBytes;
    }

    public long getTotalModifyBytes() {
        return totalModifyBytes;
    }

    public void setTotalModifyBytes(long totalModifyBytes) {
        this.totalModifyBytes = totalModifyBytes;
    }

    public long getSuccessModifyBytes() {
        return successModifyBytes;
    }

    public void setSuccessModifyBytes(long successModifyBytes) {
        this.successModifyBytes = successModifyBytes;
    }

    public long getTotalDownloadBytes() {
        return totalDownloadBytes;
    }

    public void setTotalDownloadBytes(long totalDownloadBytes) {
        this.totalDownloadBytes = totalDownloadBytes;
    }

    public long getSuccessDownloadBytes() {
        return successDownloadBytes;
    }

    public void setSuccessDownloadBytes(long successDownloadBytes) {
        this.successDownloadBytes = successDownloadBytes;
    }

    public long getTotalSyncInBytes() {
        return totalSyncInBytes;
    }

    public void setTotalSyncInBytes(long totalSyncInBytes) {
        this.totalSyncInBytes = totalSyncInBytes;
    }

    public long getSuccessSyncInBytes() {
        return successSyncInBytes;
    }

    public void setSuccessSyncInBytes(long successSyncInBytes) {
        this.successSyncInBytes = successSyncInBytes;
    }

    public long getTotalSyncOutBytes() {
        return totalSyncOutBytes;
    }

    public void setTotalSyncOutBytes(long totalSyncOutBytes) {
        this.totalSyncOutBytes = totalSyncOutBytes;
    }

    public long getSuccessSyncOutBytes() {
        return successSyncOutBytes;
    }

    public void setSuccessSyncOutBytes(long successSyncOutBytes) {
        this.successSyncOutBytes = successSyncOutBytes;
    }

    public long getTotalFileOpenCount() {
        return totalFileOpenCount;
    }

    public void setTotalFileOpenCount(long totalFileOpenCount) {
        this.totalFileOpenCount = totalFileOpenCount;
    }

    public long getSuccessFileOpenCount() {
        return successFileOpenCount;
    }

    public void setSuccessFileOpenCount(long successFileOpenCount) {
        this.successFileOpenCount = successFileOpenCount;
    }

    public long getTotalFileReadCount() {
        return totalFileReadCount;
    }

    public void setTotalFileReadCount(long totalFileReadCount) {
        this.totalFileReadCount = totalFileReadCount;
    }

    public long getSuccessFileReadCount() {
        return successFileReadCount;
    }

    public void setSuccessFileReadCount(long successFileReadCount) {
        this.successFileReadCount = successFileReadCount;
    }

    public long getTotalFileWriteCount() {
        return totalFileWriteCount;
    }

    public void setTotalFileWriteCount(long totalFileWriteCount) {
        this.totalFileWriteCount = totalFileWriteCount;
    }

    public long getSuccessFileWriteCount() {
        return successFileWriteCount;
    }

    public void setSuccessFileWriteCount(long successFileWriteCount) {
        this.successFileWriteCount = successFileWriteCount;
    }

    public Date getLastSourceUpdate() {
        return new Date(lastSourceUpdate.getTime());
    }

    public void setLastSourceUpdate(Date lastSourceUpdate) {
        this.lastSourceUpdate = new Date(lastSourceUpdate.getTime());
    }

    public Date getLastSyncUpdate() {
        return new Date(lastSyncUpdate.getTime());
    }

    public void setLastSyncUpdate(Date lastSyncUpdate) {
        this.lastSyncUpdate = new Date(lastSyncUpdate.getTime());
    }

    public Date getLastSyncedTimestamp() {
        return new Date(lastSyncedTimestamp.getTime());
    }

    public void setLastSyncedTimestamp(Date lastSyncedTimestamp) {
        this.lastSyncedTimestamp = new Date(lastSyncedTimestamp.getTime());
    }

    public Date getLastHeartBeatTime() {
        return new Date(lastHeartBeatTime.getTime());
    }

    public void setLastHeartBeatTime(Date lastHeartBeatTime) {
        this.lastHeartBeatTime = new Date(lastHeartBeatTime.getTime());
    }

    public boolean isTrunkServer() {
        return isTrunkServer;
    }

    public void setTrunkServer(boolean trunkServer) {
        isTrunkServer = trunkServer;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj != null && obj.getClass() == getClass()) {
            StorageState other = (StorageState) obj;

            return id != null && id.equals(other.id)
                    && ipAddress != null && ipAddress.equals(other.ipAddress);
        }

        return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((ipAddress == null) ? 0 : ipAddress.hashCode());
        return result;
    }
}
