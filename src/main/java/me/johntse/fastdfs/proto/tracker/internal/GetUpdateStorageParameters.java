package me.johntse.fastdfs.proto.tracker.internal;

import me.johntse.fastdfs.proto.AbstractRequestParameters;
import me.johntse.fastdfs.proto.Header;
import me.johntse.fastdfs.proto.ProtocolConstants;
import me.johntse.fastdfs.proto.base.Field;
import me.johntse.fastdfs.proto.base.FieldType;

/**
 * 获取更新信息操作的Storage时，需要的参数.
 *
 * @author johntse
 * @since 0.1.0
 */
public class GetUpdateStorageParameters extends AbstractRequestParameters {
    /**
     * 文件所在分组名称.
     */
    @Field(index = 0, size = ProtocolConstants.FDFS_GROUP_NAME_MAX_LEN)
    private String groupName;

    /**
     * 文件路径.
     */
    @Field(index = 1, type = FieldType.VARIABLE)
    private String filePath;

    public GetUpdateStorageParameters() {
        super(ProtocolConstants.TRACKER_PROTO_CMD_SERVICE_QUERY_UPDATE);
    }

    /**
     * 使用指定参数构造一个参数对象.
     *
     * @param groupName 文件所在分组名称
     * @param filePath  文件路径
     */
    public GetUpdateStorageParameters(String groupName, String filePath) {
        this();

        if (groupName == null || groupName.isEmpty()) {
            throw new IllegalArgumentException("GetUpdateStorageParameters: group name can't be empty.");
        }

        if (filePath == null || filePath.isEmpty()) {
            throw new IllegalArgumentException("GetUpdateStorageParameters: file path can't be empty.");
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
