package me.johntse.fastdfs.proto.struct;

import me.johntse.fastdfs.proto.ProtocolConstants;
import me.johntse.fastdfs.proto.base.Field;

/**
 * 群组信息.
 *
 * @author johntse
 * @since 0.1.0
 */
public class GroupState {
    /**
     * Group名称.
     */
    @Field(index = 0, size = ProtocolConstants.FDFS_GROUP_NAME_MAX_LEN + 1)
    private String groupName;

    /**
     * 存储空间总量.
     * 单位MB
     */
    @Field(index = 1, size = 8)
    private long total;

    /**
     * 剩余可用空间.
     * 单位MB
     */
    @Field(index = 2, size = 8)
    private long free;

    /**
     * 可用trunk空间.
     * 单位MB
     */
    @Field(index = 3, size = 8)
    private long trunkFree;

    /**
     * 登记的Storage存储总数.
     */
    @Field(index = 4, size = 8)
    private long storageCount;

    /**
     * Storage存储服务端口.
     */
    @Field(index = 5, size = 8)
    private long storageServerPort;

    /**
     * Storage存储上的HTTP服务器端口.
     */
    @Field(index = 6, size = 8)
    private long storageHttpServerPort;

    /**
     * 出于激活可用状态的Storage存储数.
     */
    @Field(index = 7, size = 8)
    private long activeStorageCount;

    /**
     * 当前正在写入的Storage存储数.
     */
    @Field(index = 8, size = 8)
    private long currentWriteStorageCount;

    /**
     * 每个Storage存储上的存储路径数.
     */
    @Field(index = 9, size = 8)
    private long storePathPerStorage;

    /**
     * 每个存储路径下的子目录数.
     */
    @Field(index = 10, size = 8)
    private long subDirPerStorePath;

    /**
     * 当前操作的Trunk文件.
     */
    @Field(index = 11, size = 8)
    private long currentTrunkFileId;

    public GroupState() {
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
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

    public long getTrunkFree() {
        return trunkFree;
    }

    public void setTrunkFree(long trunkFree) {
        this.trunkFree = trunkFree;
    }

    public int getStorageCount() {
        return (int) storageCount;
    }

    public void setStorageCount(int storageCount) {
        this.storageCount = storageCount;
    }

    public int getStorageServerPort() {
        return (int) storageServerPort;
    }

    public void setStorageServerPort(int storageServerPort) {
        this.storageServerPort = storageServerPort;
    }

    public int getStorageHttpServerPort() {
        return (int) storageHttpServerPort;
    }

    public void setStorageHttpServerPort(int storageHttpServerPort) {
        this.storageHttpServerPort = storageHttpServerPort;
    }

    public int getActiveStorageCount() {
        return (int) activeStorageCount;
    }

    public void setActiveStorageCount(int activeStorageCount) {
        this.activeStorageCount = activeStorageCount;
    }

    public int getCurrentWriteStorageCount() {
        return (int) currentWriteStorageCount;
    }

    public void setCurrentWriteStorageCount(int currentWriteStorageCount) {
        this.currentWriteStorageCount = currentWriteStorageCount;
    }

    public int getStorePathPerStorage() {
        return (int) storePathPerStorage;
    }

    public void setStorePathPerStorage(int storePathPerStorage) {
        this.storePathPerStorage = storePathPerStorage;
    }

    public int getSubDirPerStorePath() {
        return (int) subDirPerStorePath;
    }

    public void setSubDirPerStorePath(int subDirPerStorePath) {
        this.subDirPerStorePath = subDirPerStorePath;
    }

    public int getCurrentTrunkFileId() {
        return (int) currentTrunkFileId;
    }

    public void setCurrentTrunkFileId(int currentTrunkFileId) {
        this.currentTrunkFileId = currentTrunkFileId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj != null && obj.getClass() == getClass()) {
            GroupState other = (GroupState) obj;

            return groupName != null && groupName.equals(other.groupName);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return groupName.hashCode();
    }
}
