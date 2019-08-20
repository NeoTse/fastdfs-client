package me.johntse.fastdfs.service;

import me.johntse.fastdfs.conn.ConnectionManager;
import me.johntse.fastdfs.proto.CallBack;
import me.johntse.fastdfs.proto.DownloadStream;
import me.johntse.fastdfs.proto.FastDfsRequest;
import me.johntse.fastdfs.proto.FastDfsResponse;
import me.johntse.fastdfs.proto.ProtocolConstants;
import me.johntse.fastdfs.proto.UploadStream;
import me.johntse.fastdfs.proto.storage.DeleteFileHandler;
import me.johntse.fastdfs.proto.storage.DownloadFileHandler;
import me.johntse.fastdfs.proto.storage.GetMetaDataHandler;
import me.johntse.fastdfs.proto.storage.SetMetaDataHandler;
import me.johntse.fastdfs.proto.storage.UploadFileHandler;
import me.johntse.fastdfs.proto.struct.MetaData;
import me.johntse.fastdfs.proto.struct.StorageNode;
import me.johntse.fastdfs.proto.struct.StorageNodeLite;
import me.johntse.fastdfs.proto.struct.StoragePath;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Set;

/**
 * Storage客户端.
 *
 * @author johntse
 * @since 0.1.0
 */
public class StorageClient {
    /**
     * 连接管理器.
     */
    private final ConnectionManager connectionManager;

    public StorageClient(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    /**
     * 上传文件到指定Storage节点.
     * 其中元数据为可选项
     *
     * @param node           指定Storage节点
     * @param inputStream    文件输入流
     * @param fileSize       文件大小
     * @param fileExtendName 文件扩展名
     * @param metaData       文件元数据，可以为空
     * @return 上传成功的文件存储路径
     */
    public StoragePath uploadFile(StorageNode node, FileInputStream inputStream,
                                  long fileSize, String fileExtendName, Set<MetaData> metaData) {
        CallBack uploadStream = new UploadStream(inputStream, fileSize);

        UploadFileHandler handler =
                new UploadFileHandler(node.getPathIndex(), fileSize, fileExtendName, false,
                        new FastDfsRequest(uploadStream), new FastDfsResponse());
        StoragePath storagePath = connectionManager.remoteExecute(node.getSocketAddress(), handler);
        if (metaData != null && !metaData.isEmpty()) {
            setMetaData(new StorageNodeLite(node.getGroupName(), node.getIp(), node.getPort()),
                    storagePath.getPath(), metaData,
                    ProtocolConstants.OperationFlag.STORAGE_SET_METADATA_FLAG_OVERWRITE);
        }

        return storagePath;
    }

    public StoragePath uploadFile(StorageNode node, FileInputStream inputStream,
                                  long fileSize, String fileExtendName) {
        return this.uploadFile(node, inputStream, fileSize, fileExtendName, null);
    }

    /**
     * 下载指定路径的文件.
     *
     * @param node         文件所在的Storage节点
     * @param filePath     指定文件路径
     * @param outputStream 文件内容输出流
     */
    public void downloadFile(StorageNodeLite node, String filePath, FileOutputStream outputStream) {
        downloadFile(node, filePath, 0, 0, outputStream);
    }

    /**
     * 下载指定路径的文件，并指定下载内容的偏移量和大小.
     *
     * @param node         文件所在的Storage节点
     * @param filePath     指定文件路径
     * @param offset       内容偏移量
     * @param fileSize     文件大小
     * @param outputStream 文件内容输出流
     */
    public void downloadFile(StorageNodeLite node, String filePath, long offset, long fileSize,
                             FileOutputStream outputStream) {
        CallBack downloadStream = new DownloadStream(outputStream);
        DownloadFileHandler handler =
                new DownloadFileHandler(offset, fileSize, node.getGroupName(), filePath,
                        new FastDfsRequest(), new FastDfsResponse(downloadStream));

        connectionManager.remoteExecute(node.getSocketAddress(), handler);
    }

    /**
     * 设置指定文件的元数据.
     *
     * @param node     更新的Storage节点
     * @param filePath 指定文件路径
     * @param metaData 设置的文件元数据
     * @param flag     元数据覆盖还是更新
     */
    public void setMetaData(StorageNodeLite node, String filePath, Set<MetaData> metaData,
                            ProtocolConstants.OperationFlag flag) {
        SetMetaDataHandler handler =
                new SetMetaDataHandler(node.getGroupName(), filePath, metaData, flag);
        connectionManager.remoteExecute(node.getSocketAddress(), handler);
    }

    /**
     * 获取指定文件的元数据.
     *
     * @param node     更新的Storage节点
     * @param filePath 指定文件路径
     * @return 指定文件的元数据
     */
    public Set<MetaData> getMetaData(StorageNodeLite node, String filePath) {
        GetMetaDataHandler handler =
                new GetMetaDataHandler(node.getGroupName(), filePath);

        return connectionManager.remoteExecute(node.getSocketAddress(), handler);
    }

    /**
     * 删除指定文件.
     *
     * @param node     更新的Storage节点
     * @param filePath 指定文件路径
     */
    public void deleteFile(StorageNodeLite node, String filePath) {
        DeleteFileHandler handler = new DeleteFileHandler(node.getGroupName(), filePath);

        connectionManager.remoteExecute(node.getSocketAddress(), handler);
    }
}
