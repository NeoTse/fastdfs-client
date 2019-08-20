package me.johntse.fastdfs.proto.struct;

import me.johntse.fastdfs.proto.ProtocolConstants;
import me.johntse.fastdfs.proto.base.Field;

import java.net.InetSocketAddress;

/**
 * Storage存储结点信息结构.
 *
 * @author johntse
 * @since 0.1.0
 */
public class StorageNode {
    /**
     * Storage存储结点所在的Storage Group名称.
     */
    @Field(index = 0, size = ProtocolConstants.FDFS_GROUP_NAME_MAX_LEN)
    private String groupName;

    /**
     * Storage存储结点对外服务IP地址.
     */
    @Field(index = 1, size = ProtocolConstants.FDFS_IPADDR_SIZE - 1)
    private String ip;

    /**
     * Storage存储结点对外服务端口.
     * 原有协议如此设计
     *
     */
    @Field(index = 2, size = 8)
    private long port;

    /**
     * Storage存储结点中的存储路径索引号.
     */
    @Field(index = 3, size = 1)
    private byte pathIndex;

    private InetSocketAddress address;

    public StorageNode() {
    }

    /**
     * 构建Storage存储结点结构.
     *
     * @param groupName Storage存储结点所在的Storage Group名称.
     * @param ip        Storage存储结点对外服务IP地址.
     * @param port      Storage存储结点对外服务端口.
     * @param pathIndex Storage存储结点中的存储路径索引号.
     */
    public StorageNode(String groupName, String ip, int port, byte pathIndex) {
        this.groupName = groupName;
        this.ip = ip;
        this.port = port;
        this.pathIndex = pathIndex;
        this.address = new InetSocketAddress(ip, port);
    }

    public InetSocketAddress getSocketAddress() {
        return address == null ? new InetSocketAddress(ip, (int) port) : address;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return (int) port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public byte getPathIndex() {
        return pathIndex;
    }

    public void setPathIndex(byte pathIndex) {
        this.pathIndex = pathIndex;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj != null && obj.getClass() == getClass()) {
            StorageNode other = (StorageNode) obj;

            return groupName != null && groupName.equals(other.groupName)
                    && ip != null && ip.equals(other.ip)
                    && port == other.port && pathIndex == other.pathIndex;
        }

        return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((groupName == null) ? 0 : groupName.hashCode());
        result = prime * result + ((ip == null) ? 0 : ip.hashCode());
        result = prime * result + (int) port;
        result = prime * result + pathIndex;
        return result;
    }

    @Override
    public String toString() {
        return "StorageNode [groupName=" + groupName + ", ip=" + ip + ", port=" + port
                + ", pathIndex=" + pathIndex + "]";
    }
}
