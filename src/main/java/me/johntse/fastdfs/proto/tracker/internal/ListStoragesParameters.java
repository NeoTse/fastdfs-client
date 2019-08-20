package me.johntse.fastdfs.proto.tracker.internal;

import me.johntse.fastdfs.proto.AbstractRequestParameters;
import me.johntse.fastdfs.proto.Header;
import me.johntse.fastdfs.proto.ProtocolConstants;
import me.johntse.fastdfs.proto.base.Field;
import me.johntse.fastdfs.proto.base.FieldType;

/**
 * 获取指定分组下的Storage Server信息所需要的参数.
 *
 * @author johntse
 * @since 0.1.0
 */
public class ListStoragesParameters extends AbstractRequestParameters {
    /**
     * 指定的分组名称.
     */
    @Field(index = 0, size = ProtocolConstants.FDFS_GROUP_NAME_MAX_LEN)
    private String groupName;

    /**
     * 指定要查询信息的Storage Server的IP地址.
     */
    @Field(index = 1, size = ProtocolConstants.FDFS_IPADDR_SIZE - 1, type = FieldType.VARIABLE)
    private String storageIpAddress;

    public ListStoragesParameters() {
        super(ProtocolConstants.TRACKER_PROTO_CMD_SERVER_LIST_STORAGE);
    }

    public ListStoragesParameters(String groupName) {
        this(groupName, "");
    }

    /**
     * 参数构造.
     *
     * @param groupName        指定的分组名称
     * @param storageIpAddress 指定要查询信息的Storage Server的IP地址
     */
    public ListStoragesParameters(String groupName, String storageIpAddress) {
        this();

        if (groupName == null || groupName.isEmpty()) {
            throw new IllegalArgumentException("ListStoragesParameters: group name can't be empty.");
        }

        if (storageIpAddress == null) {
            storageIpAddress = "";
        }

        this.groupName = groupName;
        this.storageIpAddress = storageIpAddress;
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

    public String getStorageIpAddress() {
        return storageIpAddress;
    }

    public void setStorageIpAddress(String storageIpAddress) {
        this.storageIpAddress = storageIpAddress;
    }
}
