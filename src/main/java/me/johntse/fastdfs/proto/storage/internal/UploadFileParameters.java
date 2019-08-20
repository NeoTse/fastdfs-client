package me.johntse.fastdfs.proto.storage.internal;

import me.johntse.fastdfs.proto.AbstractRequestParameters;
import me.johntse.fastdfs.proto.Header;
import me.johntse.fastdfs.proto.ProtocolConstants;
import me.johntse.fastdfs.proto.base.Field;

/**
 * 上传文件参数.
 *
 * @author johntse
 * @since 0.1.0
 */
public class UploadFileParameters extends AbstractRequestParameters {
    /**
     * 上传文件存储的storage节点.
     */
    @Field(index = 0, size = 1)
    private byte storageIndex;

    /**
     * 上传文件大小.
     */
    @Field(index = 1, size = 8)
    private long fileSize;

    /**
     * 上传文件扩展名.
     */
    @Field(index = 2, size = ProtocolConstants.FDFS_FILE_EXT_NAME_MAX_LEN)
    private String fileExtName;

    public UploadFileParameters() {
        super(ProtocolConstants.STORAGE_PROTO_CMD_UPLOAD_FILE);
    }

    /**
     * 构造函数.
     *
     * @param storageIndex 上传文件存储的storage节点
     * @param fileSize     上传文件大小
     * @param fileExtName  上传文件扩展名
     * @param isAppendFile 是否为可追加文件
     */
    public UploadFileParameters(byte storageIndex, long fileSize, String fileExtName, boolean isAppendFile) {
        super(isAppendFile ? ProtocolConstants.STORAGE_PROTO_CMD_UPLOAD_APPENDER_FILE :
                ProtocolConstants.STORAGE_PROTO_CMD_UPLOAD_FILE);

        if (fileSize <= 0) {
            throw new IllegalArgumentException("UploadFileParameters: file size must be a positive number.");
        }

        if (fileExtName == null || fileExtName.isEmpty()
                || fileExtName.length() > ProtocolConstants.FDFS_FILE_EXT_NAME_MAX_LEN) {
            throw new IllegalArgumentException("UploadFileParameters: file extend name must not be empty,"
                    + " and the length must be less than " + ProtocolConstants.FDFS_FILE_EXT_NAME_MAX_LEN + 1);
        }

        this.storageIndex = storageIndex;
        this.fileSize = fileSize;
        this.fileExtName = fileExtName;
    }

    @Override
    protected Header generateHeader(int parameterLength) {
        return new Header(parameterLength + fileSize, type);
    }

    public byte getStorageIndex() {
        return storageIndex;
    }

    public void setStorageIndex(byte storageIndex) {
        this.storageIndex = storageIndex;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileExtName() {
        return fileExtName;
    }

    public void setFileExtName(String fileExtName) {
        this.fileExtName = fileExtName;
    }
}
