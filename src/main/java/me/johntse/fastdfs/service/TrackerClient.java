package me.johntse.fastdfs.service;

import me.johntse.fastdfs.conn.ConnectionManager;
import me.johntse.fastdfs.proto.struct.GroupState;
import me.johntse.fastdfs.proto.struct.StorageNode;
import me.johntse.fastdfs.proto.struct.StorageNodeLite;
import me.johntse.fastdfs.proto.struct.StoragePath;
import me.johntse.fastdfs.proto.struct.StorageState;
import me.johntse.fastdfs.proto.tracker.FetchOneStorageHandler;
import me.johntse.fastdfs.proto.tracker.GetOneStorageHandler;
import me.johntse.fastdfs.proto.tracker.GetOneStorageWithGroupHandler;
import me.johntse.fastdfs.proto.tracker.GetUpdateStorageHandler;
import me.johntse.fastdfs.proto.tracker.ListGroupsHandler;
import me.johntse.fastdfs.proto.tracker.ListStoragesHandler;
import me.johntse.fastdfs.proto.tracker.internal.GetOneStorageParameters;
import me.johntse.fastdfs.proto.tracker.internal.ListGroupsParameters;
import me.johntse.fastdfs.proto.tracker.internal.ListStoragesParameters;

import java.util.List;

/**
 * Tracker(目录服务)客户端.
 *
 * @author johntse
 * @since 0.1.0
 */
public class TrackerClient {
    private final ConnectionManager connectionManager;

    public TrackerClient(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    /**
     * 请求一个Tracker获取任意一个可用的Storage.
     * Tracker的分配策略{@link ConnectionManager.TrackerConnectionSelectStrategy}
     * Storage的分配策略参见tracker.conf
     *
     * @return 获取的可用的Storage
     */
    public StorageNode getStorage() {
        GetOneStorageHandler handler = new GetOneStorageHandler(new GetOneStorageParameters());

        return connectionManager.remoteExecute(connectionManager.getOneTrackerAddress(), handler);
    }

    /**
     * 请求一个Tracker获取指定分组下的任意一个可用的Storage.
     * Tracker的分配策略{@link ConnectionManager.TrackerConnectionSelectStrategy}
     * Storage的分配策略参见tracker.conf
     *
     * @param groupName 指定的分组名称
     * @return 获取的可用的Storage
     */
    public StorageNode getStorage(String groupName) {
        GetOneStorageWithGroupHandler handler =
                new GetOneStorageWithGroupHandler(groupName);

        return connectionManager.remoteExecute(connectionManager.getOneTrackerAddress(), handler);
    }

    /**
     * 请求一个Tracker获取所有的分组信息.
     * Tracker的分配策略{@link ConnectionManager.TrackerConnectionSelectStrategy}
     *
     * @return 所有的分组信息
     */
    public List<GroupState> listGroups() {
        ListGroupsHandler handler = new ListGroupsHandler(new ListGroupsParameters());

        return connectionManager.remoteExecute(connectionManager.getOneTrackerAddress(), handler);
    }

    /**
     * 请求一个Tracker获取指定分组下所有的Storage信息.
     * Tracker的分配策略{@link ConnectionManager.TrackerConnectionSelectStrategy}
     *
     * @param groupName 指定的分组名称
     * @return 指定分组下所有的Storage信息
     */
    public List<StorageState> listStorages(String groupName) {
        ListStoragesHandler handler = new ListStoragesHandler(new ListStoragesParameters(groupName));

        return connectionManager.remoteExecute(connectionManager.getOneTrackerAddress(), handler);
    }

    /**
     * 请求一个Tracker获取指定分组下指定Storage的信息.
     *
     * @param groupName        指定的分组名称
     * @param storageIpAddress 指定的Storage地址
     * @return 指定的Storage信息
     */
    public List<StorageState> listStorages(String groupName, String storageIpAddress) {
        ListStoragesHandler handler =
                new ListStoragesHandler(new ListStoragesParameters(groupName, storageIpAddress));

        return connectionManager.remoteExecute(connectionManager.getOneTrackerAddress(), handler);
    }

    /**
     * 根据分组名称和文件存储路径获取一个Storage，以便下载文件.
     *
     * @param groupName 文件所在分组名称
     * @param filePath  文件存储路径
     * @return 获取的Storage
     */
    public StorageNodeLite fetchOneStorage(String groupName, String filePath) {
        FetchOneStorageHandler handler = new FetchOneStorageHandler(groupName, filePath);

        return connectionManager.remoteExecute(connectionManager.getOneTrackerAddress(), handler);
    }

    public StorageNodeLite fetchOneStorage(StoragePath path) {
        return fetchOneStorage(path.getGroupName(), path.getPath());
    }

    /**
     * 根据分组名称和文件存储路径获取一个Storage，以便更新元数据或者删除文件.
     *
     * @param groupName 文件所在分组名称
     * @param filePath  文件存储路径
     * @return 获取的Storage
     */
    public StorageNodeLite getUpdateStorage(String groupName, String filePath) {
        GetUpdateStorageHandler handler = new GetUpdateStorageHandler(groupName, filePath);

        return connectionManager.remoteExecute(connectionManager.getOneTrackerAddress(), handler);
    }

    public StorageNodeLite getUpdateStorage(StoragePath path) {
        return getUpdateStorage(path.getGroupName(), path.getPath());
    }
}
