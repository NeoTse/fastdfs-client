package me.johntse.fastdfs.proto.base;

import me.johntse.fastdfs.proto.ProtocolConstants;
import me.johntse.fastdfs.proto.struct.MetaData;
import me.johntse.fastdfs.util.ByteUtils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 内容编解码器.
 *
 * @author johntse
 * @since 0.1.0
 */
public final class Codec {
    /**
     * 将byte数组<code>content</code>中内容按照指定编码方式<code>charset</code>解码为指定类型<code>clazz</code>的对象实例.
     *
     * @param content 报文负载内容
     * @param clazz   需要转换的指定类型的class
     * @param charset 转换中需要的编码方式
     * @param <T>     具体类型
     * @return 解码成功，返回指定类型的对象实例
     * @throws IllegalAccessException @see Class.newInstance()
     * @throws InstantiationException @see Class.newInstance()
     */
    public static <T> T decodeToInstance(byte[] content, Class<T> clazz, Charset charset)
            throws IllegalAccessException, InstantiationException {
        List<FieldInfo> fields = Fields.getInstance().getFields(clazz, content);

        T obj = clazz.newInstance();

        for (FieldInfo field : fields) {
            // IGNORE类型在解码是忽略
            if (field.getType() != FieldType.IGNORE) {
                field.getField().set(obj, field.getValue(content, charset));
            }
        }

        return obj;
    }

    /**
     * 将byte数组<code>content</code>中内容按照指定编码方式<code>charset</code>解码为指定类型<code>clazz</code>的多个对象实例.
     * 不支持带有动态字段的对象
     *
     * @param content 报文负载内容
     * @param clazz   需要转换的指定类型的class
     * @param charset 转换中需要的编码方式
     * @param <T>     具体类型
     * @return 解码成功，返回指定类型的多个对象实例。否则返回空列表
     * @throws IllegalAccessException @see Class.newInstance()
     * @throws InstantiationException @see Class.newInstance()
     */
    public static <T> List<T> decodeToInstances(byte[] content, Class<T> clazz, Charset charset)
            throws IllegalAccessException, InstantiationException {
        List<T> result = new ArrayList<>();

        Fields.FieldCollection collection = Fields.getInstance().getCollection(clazz);
        int totalFixedFieldSize = collection.getTotalFixedFieldSize();

        if (content.length % totalFixedFieldSize != 0 || collection.isHasVariableSizeField()) {
            return result;
        }

        int count = content.length / totalFixedFieldSize;
        int offset = 0;

        for (int i = 0; i < count; i++) {
            byte[] one = new byte[totalFixedFieldSize];
            System.arraycopy(content, offset, one, 0, totalFixedFieldSize);
            result.add(decodeToInstance(one, clazz, charset));
            offset += totalFixedFieldSize;
        }

        return result;
    }

    /**
     * 将指定对象实例<code>obj</code>按照指定编码<code>charset</code>编码为byte数组.
     *
     * @param obj     需要转换的对象实例
     * @param charset 指定的编码方式
     * @return 成功编码后，返回指定byte数组
     */
    public static byte[] encodeToBytes(Object obj, Charset charset) {
        Fields.FieldCollection collection = Fields.getInstance().getCollection(obj.getClass());
        List<FieldInfo> fields = collection.getFields();

        try (ByteArrayOutputStream byteArray = new ByteArrayOutputStream(collection.getTotalFixedFieldSize());
             DataOutputStream outputStream = new DataOutputStream(byteArray)) {
            for (FieldInfo field : fields) {
                outputStream.write(field.getBytes(obj, charset));
            }

            return byteArray.toByteArray();
        } catch (IOException e) {
            return ByteUtils.EMPTY_BYTES;
        }
    }

    /**
     * 将指定的元数据按照指定的编码方式序列化.
     *
     * @param dataSet 指定的元数据
     * @param charset 指定的编码方式
     * @return 序列化结果，失败返回空数组
     */
    public static byte[] encodeMetaToBytes(Set<MetaData> dataSet, Charset charset) {
        if (dataSet == null || dataSet.isEmpty()) {
            return ByteUtils.EMPTY_BYTES;
        }

        StringBuilder sb = new StringBuilder(32 * dataSet.size());
        for (MetaData data : dataSet) {
            sb.append(data.getName()).append(ProtocolConstants.FDFS_FIELD_SEPERATOR);
            if (data.getValue() != null) {
                sb.append(data.getValue());
            }

            sb.append(ProtocolConstants.FDFS_RECORD_SEPERATOR);
        }

        sb.delete(sb.length() - ProtocolConstants.FDFS_RECORD_SEPERATOR.length(), sb.length());

        return sb.toString().getBytes(charset);
    }

    /**
     * 将指定内容按照指定编码反序列化为元数据存储对象.
     *
     * @param content 指定内容
     * @param charset 指定编码
     * @return 反序列化结果，失败返回空结果
     */
    public static Set<MetaData> decodeMetaToInstance(byte[] content, Charset charset) {
        return decodeMetaToInstance(content, 0, content.length, charset);
    }

    /**
     * 将指定内容按照指定编码反序列化为元数据存储对象.
     *
     * @param content 指定内容
     * @param offset  内容数组偏移量
     * @param len     反序列化内容的长度
     * @param charset 指定编码
     * @return 反序列化结果，失败返回空结果
     */
    public static Set<MetaData> decodeMetaToInstance(byte[] content, int offset,
                                                     int len, Charset charset) {
        Set<MetaData> result = new HashSet<>();

        if (content == null || content.length <= 0
                || offset >= content.length || offset + len > content.length) {
            return result;
        }

        String all = new String(content, offset, len, charset);
        String[] items = all.split(ProtocolConstants.FDFS_RECORD_SEPERATOR);

        for (String item : items) {
            String[] fields = item.split(ProtocolConstants.FDFS_FIELD_SEPERATOR, 2);
            MetaData meta = new MetaData(fields[0]);
            if (fields.length == 2 && !fields[1].isEmpty()) {
                meta.setValue(fields[1]);
            }

            result.add(meta);
        }

        return result;
    }
}
