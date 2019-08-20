package me.johntse.fastdfs.proto.struct;

import me.johntse.fastdfs.proto.ProtocolConstants;
import me.johntse.fastdfs.proto.base.Field;
import me.johntse.fastdfs.proto.base.FieldType;

import java.io.File;

/**
 * 文件存储路径.
 *
 * @author johntse
 * @since 0.1.0
 */
public class StoragePath {
    private static final String PATH_SEPARATOR = File.separator;

    /**
     * Storage存储结点所在的Storage Group名称.
     */
    @Field(index = 0, size = ProtocolConstants.FDFS_GROUP_NAME_MAX_LEN)
    private String groupName;

    /**
     * 文件在一个具体Group下的存储路径.
     */
    @Field(index = 1, size = -1, type = FieldType.VARIABLE)
    private String path;

    /**
     * 文件存储全路径，包括Group名称.
     */
    private String fullPath;

    public StoragePath() {

    }

    /**
     * 存储路径对象构造函数.
     * 表示文件最终的存储路径
     *
     * @param groupName 文件存储所在的分组名称
     * @param path      分组下文件具体存储路径
     */
    public StoragePath(String groupName, String path) {
        this.groupName = groupName;
        this.path = path;
        this.fullPath = groupName + PATH_SEPARATOR + path;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFullPath() {
        return fullPath == null ? groupName + PATH_SEPARATOR + path : fullPath;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj != null && obj.getClass() == getClass()) {
            StoragePath other = (StoragePath) obj;

            return groupName != null && groupName.equals(other.groupName)
                    && path != null && path.equals(other.path);
        }

        return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((groupName == null) ? 0 : groupName.hashCode());
        result = prime * result + ((path == null) ? 0 : path.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "StorePath [groupName=" + groupName + ", path=" + path + ", fullPath=" + fullPath + "]";
    }
}
