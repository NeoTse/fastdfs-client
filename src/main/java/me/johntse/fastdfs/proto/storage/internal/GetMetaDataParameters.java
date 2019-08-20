package me.johntse.fastdfs.proto.storage.internal;

import me.johntse.fastdfs.proto.AbstractRequestParameters;
import me.johntse.fastdfs.proto.Header;
import me.johntse.fastdfs.proto.ProtocolConstants;
import me.johntse.fastdfs.proto.base.Field;
import me.johntse.fastdfs.proto.base.FieldType;

/**
 * 获取元数据参数对象.
 *
 * @author johntse
 * @since 0.1.0
 */
public class GetMetaDataParameters extends AbstractRequestParameters {
    /**
     * 获取元数据对应文件所在的分组名称.
     */
    @Field(index = 0, size = ProtocolConstants.FDFS_GROUP_NAME_MAX_LEN)
    private String groupName;

    /**
     * 元数据对应文件的路径.
     */
    @Field(index = 1, type = FieldType.VARIABLE)
    private String filePath;

    public GetMetaDataParameters() {
        super(ProtocolConstants.STORAGE_PROTO_CMD_GET_METADATA);
    }

    /**
     * 参数对象构造函数.
     *
     * @param groupName 获取元数据所属文件所在的分组名称
     * @param filePath  所属文件路径
     */
    public GetMetaDataParameters(String groupName, String filePath) {
        this();

        if (groupName == null || groupName.isEmpty()) {
            throw new IllegalArgumentException("GetMetaDataParameters: group name can't be empty.");
        }

        if (filePath == null || filePath.isEmpty()) {
            throw new IllegalArgumentException("GetMetaDataParameters: file path can't be empty.");
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
