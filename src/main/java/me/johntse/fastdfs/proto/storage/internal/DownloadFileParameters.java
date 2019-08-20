package me.johntse.fastdfs.proto.storage.internal;

import me.johntse.fastdfs.proto.AbstractRequestParameters;
import me.johntse.fastdfs.proto.Header;
import me.johntse.fastdfs.proto.ProtocolConstants;
import me.johntse.fastdfs.proto.base.Field;
import me.johntse.fastdfs.proto.base.FieldType;

/**
 * 下载参数.
 *
 * @author johntse
 * @since 0.1.0
 */
public class DownloadFileParameters extends AbstractRequestParameters {
    /**
     * 下载文件偏移.
     */
    @Field(index = 0, size = 8)
    private long fileOffset;

    /**
     * 下载文件大小.
     */
    @Field(index = 1, size = 8)
    private long fileSize;

    /**
     * 下载文件所在group名称.
     */
    @Field(index = 2, size = ProtocolConstants.FDFS_GROUP_NAME_MAX_LEN)
    private String groupName;

    /**
     * 下载文件路径.
     */
    @Field(index = 3, type = FieldType.VARIABLE)
    private String filePath;

    public DownloadFileParameters() {
        super(ProtocolConstants.STORAGE_PROTO_CMD_DOWNLOAD_FILE);
    }

    /**
     * 构造函数.
     *
     * @param fileOffset 下载文件偏移
     * @param fileSize   下载文件大小，0表示下载指定偏移量后面的全部内容
     * @param groupName  下载文件所在group名称
     * @param filePath   下载文件路径
     */
    public DownloadFileParameters(long fileOffset, long fileSize, String groupName, String filePath) {
        this();

        if (fileOffset < 0 || fileSize < 0) {
            throw new IllegalArgumentException("DownloadFileParameters: file offset or size must be a positive number");
        }

        if (groupName == null || groupName.isEmpty()
                || filePath == null || filePath.isEmpty()) {
            throw new IllegalArgumentException("DownloadFileParameters: group name or file path must not be empty.");
        }

        this.fileOffset = fileOffset;
        this.fileSize = fileSize;
        this.groupName = groupName;
        this.filePath = filePath;
    }

    @Override
    protected Header generateHeader(int parameterLength) {
        return new Header(parameterLength, type);
    }

    public long getFileOffset() {
        return fileOffset;
    }

    public void setFileOffset(long fileOffset) {
        this.fileOffset = fileOffset;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
