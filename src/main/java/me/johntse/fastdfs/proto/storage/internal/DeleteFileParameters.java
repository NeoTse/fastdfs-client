package me.johntse.fastdfs.proto.storage.internal;

import me.johntse.fastdfs.proto.AbstractRequestParameters;
import me.johntse.fastdfs.proto.Header;
import me.johntse.fastdfs.proto.ProtocolConstants;
import me.johntse.fastdfs.proto.base.Field;
import me.johntse.fastdfs.proto.base.FieldType;

/**
 * 文件删除参数对象.
 *
 * @author johntse
 * @since 0.1.0
 */
public class DeleteFileParameters extends AbstractRequestParameters {

    /**
     * 删除文件所在的分组名称.
     */
    @Field(index = 0, size = ProtocolConstants.FDFS_GROUP_NAME_MAX_LEN)
    private String groupName;

    /**
     * 删除文件路径.
     */
    @Field(index = 1, type = FieldType.VARIABLE)
    private String filePath;

    public DeleteFileParameters() {
        super(ProtocolConstants.STORAGE_PROTO_CMD_DELETE_FILE);
    }

    /**
     * 参数对象构造.
     *
     * @param groupName 删除文件所在的分组名称
     * @param filePath  删除文件路径
     */
    public DeleteFileParameters(String groupName, String filePath) {
        this();

        if (groupName == null || groupName.isEmpty()) {
            throw new IllegalArgumentException("DeleteFileParameters: group name can't be empty.");
        }

        if (filePath == null || filePath.isEmpty()) {
            throw new IllegalArgumentException("DeleteFileParameters: file path can't be empty.");
        }

        this.groupName = groupName;
        this.filePath = filePath;
    }

    @Override
    protected Header generateHeader(int parameterLength) {
        return new Header(parameterLength, type);
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
