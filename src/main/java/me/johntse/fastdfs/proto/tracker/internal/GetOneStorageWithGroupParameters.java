package me.johntse.fastdfs.proto.tracker.internal;

import me.johntse.fastdfs.proto.AbstractRequestParameters;
import me.johntse.fastdfs.proto.Header;
import me.johntse.fastdfs.proto.ProtocolConstants;
import me.johntse.fastdfs.proto.base.Field;

/**
 * 查询指定Group中一个可用Storage参数对象.
 *
 * @author johntse
 * @since 0.1.0
 */
public class GetOneStorageWithGroupParameters extends AbstractRequestParameters {
    /**
     * 指定的Group名称.
     */
    @Field(index = 0, size = ProtocolConstants.FDFS_GROUP_NAME_MAX_LEN)
    private String groupName;

    public GetOneStorageWithGroupParameters() {
        super(ProtocolConstants.TRACKER_PROTO_CMD_SERVICE_QUERY_STORE_WITH_GROUP_ONE);
    }

    /**
     * 构建函数.
     *
     * @param groupName 查询指定的Group名称
     */
    public GetOneStorageWithGroupParameters(String groupName) {
        this();

        if (groupName == null || groupName.isEmpty()) {
            throw new IllegalArgumentException("GetOneStorageWithGroupParameters: group name can't be empty.");
        }

        this.groupName = groupName;
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
}
