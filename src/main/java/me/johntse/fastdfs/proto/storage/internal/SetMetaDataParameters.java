package me.johntse.fastdfs.proto.storage.internal;

import me.johntse.fastdfs.proto.AbstractRequestParameters;
import me.johntse.fastdfs.proto.Header;
import me.johntse.fastdfs.proto.ProtocolConstants;
import me.johntse.fastdfs.proto.base.Codec;
import me.johntse.fastdfs.proto.base.Field;
import me.johntse.fastdfs.proto.base.FieldInfo;
import me.johntse.fastdfs.proto.base.FieldType;
import me.johntse.fastdfs.proto.base.Fields;
import me.johntse.fastdfs.proto.struct.MetaData;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 元数据设置参数对象.
 *
 * @author johntse
 * @since 0.1.0
 */
public class SetMetaDataParameters extends AbstractRequestParameters {
    /**
     * 文件路径长度.
     * 单位byte
     */
    @Field(index = 0, size = 8)
    private long filePathLength;

    /**
     * 设置的元数据长度.
     * 单位byte
     */
    @Field(index = 1, size = 8)
    private long metaDataLength;

    /**
     * 操作类型（重写/合并）.
     *
     * @see ProtocolConstants
     */
    @Field(index = 2, size = 1)
    private byte opFlag;

    /**
     * 设置的文件所在分组名.
     */
    @Field(index = 3, size = ProtocolConstants.FDFS_GROUP_NAME_MAX_LEN)
    private String groupName;

    /**
     * 设置文件的存储路径.
     */
    @Field(index = 4, type = FieldType.VARIABLE, fixedByIndex = 0)
    private String filePath;

    /**
     * 需要设置的元数据.
     */
    @Field(index = 5, type = FieldType.META, fixedByIndex = 1)
    private Set<MetaData> metaDataSet;

    public SetMetaDataParameters() {
        super(ProtocolConstants.STORAGE_PROTO_CMD_SET_METADATA);
    }

    /**
     * 参数对象构造.
     *
     * @param groupName   设置的文件所在分组名
     * @param filePath    设置文件的存储路径
     * @param metaDataSet 需要设置的元数据
     * @param opFlag      操作类型
     */
    public SetMetaDataParameters(String groupName, String filePath, Set<MetaData> metaDataSet,
                                 ProtocolConstants.OperationFlag opFlag) {
        this();

        if (groupName == null || groupName.isEmpty()) {
            throw new IllegalArgumentException("SetMetaDataParameters: group name can't be empty.");
        }

        if (filePath == null || filePath.isEmpty()) {
            throw new IllegalArgumentException("SetMetaDataParameters: file filePath can't be empty.");
        }

        if (metaDataSet == null || metaDataSet.isEmpty()) {
            throw new IllegalArgumentException("SetMetaDataParameters: meta data can't be empty.");
        }

        this.groupName = groupName;
        this.filePath = filePath;
        this.metaDataSet = metaDataSet;
        this.opFlag = opFlag.getValue();

        this.filePathLength = 0;
        this.metaDataLength = 0;
    }

    @Override
    public byte[] serialize(Charset charset) {
        List<FieldInfo> fields = Fields.getInstance().getFields(getClass());

        int total = 0;
        List<byte[]> fieldBytes = new ArrayList<>();

        // 转换过程中填充属性值
        for (FieldInfo field : fields) {
            byte[] fieldByte = field.getBytes(this, charset);
            total += fieldByte.length;
            fieldBytes.add(fieldByte);

            if (field.getIndex() == 4) {
                filePathLength = fieldByte.length;
            } else if (field.getIndex() == 5) {
                metaDataLength = fieldByte.length;
            }
        }

        // 重新填充
        fieldBytes.set(0, fields.get(0).getBytes(this, charset));
        fieldBytes.set(1, fields.get(1).getBytes(this, charset));


        byte[] parametersByte = new byte[total];
        int offset = 0;
        for (byte[] fieldByte : fieldBytes) {
            System.arraycopy(fieldByte, 0, parametersByte, offset, fieldByte.length);
            offset += fieldByte.length;
        }

        Header header = generateHeader(parametersByte.length);
        byte[] headerByte = Codec.encodeToBytes(header, charset);

        return mergeHeaderAndParameters(headerByte, parametersByte);
    }

    @Override
    protected Header generateHeader(int parameterLength) {
        return new Header(parameterLength, type);
    }

    public long getFilePathLength() {
        return filePathLength;
    }

    public void setFilePathLength(int filePathLength) {
        this.filePathLength = filePathLength;
    }

    public long getMetaDataLength() {
        return metaDataLength;
    }

    public void setMetaDataLength(int metaDataLength) {
        this.metaDataLength = metaDataLength;
    }

    public byte getOpFlag() {
        return opFlag;
    }

    public void setOpFlag(byte opFlag) {
        this.opFlag = opFlag;
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

    public Set<MetaData> getMetaDataSet() {
        return Collections.unmodifiableSet(metaDataSet);
    }

    public void setMetaDataSet(Set<MetaData> metaDataSet) {
        this.metaDataSet = new HashSet<>(metaDataSet);
    }
}
