package me.johntse.fastdfs.proto.base;

import me.johntse.fastdfs.proto.struct.MetaData;
import me.johntse.fastdfs.util.ByteUtils;

import java.nio.charset.Charset;
import java.util.Date;
import java.util.Set;

/**
 * 字段信息，用于类反射.
 *
 * @author johntse
 * @since 0.1.0
 */
public class FieldInfo {
    private final java.lang.reflect.Field field;
    private final int index;
    private final int size;
    private final int offset;
    private final int refOffset;
    private final int refSize;
    private FieldType type;

    /**
     * 通过给定Field构建.
     *
     * @param field     指定的Field
     * @param refOffset 引用的Field的偏移量，关于引用{@link Field}
     * @param refSize   引用的Field的大小
     * @param offset    该Field对应的偏移量
     */
    public FieldInfo(java.lang.reflect.Field field, int refOffset, int refSize, int offset) {
        Field f = field.getAnnotation(Field.class);
        this.field = field;
        this.index = f.index();
        this.size = f.size();
        this.type = f.type();
        this.refOffset = refOffset;
        this.refSize = refSize;
        this.offset = offset;
    }

    public FieldInfo(java.lang.reflect.Field field, int offset) {
        this(field, -1, 0, offset);
    }

    /**
     * 通过给定Field构建.
     *
     * @param field     指定的Field
     * @param index     索引
     * @param size      大小
     * @param offset    偏移量
     * @param refOffset 引用的Field的偏移量，关于引用{@link Field}
     * @param refSize   引用的Field的大小
     * @param type      类型
     */
    public FieldInfo(java.lang.reflect.Field field, int index, int size,
                     int offset, int refOffset, int refSize, FieldType type) {
        this.field = field;
        this.index = index;
        this.size = size;
        this.offset = offset;
        this.refOffset = refOffset;
        this.refSize = refSize;
        this.type = type;
    }

    private static Object getValueByFieldName(Object obj, String fieldName) {
        try {
            java.lang.reflect.Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public java.lang.reflect.Field getField() {
        return field;
    }

    public int getIndex() {
        return index;
    }

    public int getSize() {
        return size;
    }

    public int getOffset() {
        return offset;
    }

    public int getRefOffset() {
        return refOffset;
    }

    public int getRefSize() {
        return refSize;
    }

    public FieldType getType() {
        return type;
    }

    /**
     * 根据Field注解信息对Content按照指定编码方式获取对应的值内容.
     *
     * @param content 原始内容
     * @param charset 指定编码
     * @return 对应的内容
     */
    public Object getValue(byte[] content, Charset charset) {
        Class<?> clazz = field.getType();

        if (clazz == String.class) {
            int size;
            // 可变长字段
            if (type == FieldType.VARIABLE) {
                size = sizeOfVariableField(content);
            } else {
                size = getSize();
            }

            return new String(content, offset, size, charset).trim();
        } else if (type == FieldType.META) {
            int size = sizeOfVariableField(content);

            return Codec.decodeMetaToInstance(content, offset, size, charset);
        } else if (clazz == Date.class) {
            return new Date(ByteUtils.bytesToLong(content, offset) * 1000);
        } else if (long.class == clazz) {
            return ByteUtils.bytesToLong(content, offset);
        } else if (int.class == clazz) {
            return ByteUtils.bytesToInt(content, offset);
        } else if (byte.class == clazz) {
            return content[offset];
        } else if (boolean.class == clazz) {
            return content[offset] != 0;
        } else {
            throw new RuntimeException("Unknown type.");
        }
    }

    /**
     * 将指定对象中该Field的值按照特定编码方式进行序列化.
     *
     * @param obj     指定的对象实例
     * @param charset 编码方式
     * @return 序列化结果
     */
    @SuppressWarnings("unchecked")
    public byte[] getBytes(Object obj, Charset charset) {
        Object value = getValueByFieldName(obj, field.getName());

        Class<?> clazz = field.getType();
        if (type == FieldType.VARIABLE) {
            byte[] result = ByteUtils.stringToBytes((String) value, charset);
            // 存在最大长度限制
            if (result.length > size && size > 0) {
                byte[] replace = new byte[size];
                System.arraycopy(result, 0, replace, 0, size);
                return replace;
            } else {
                return result;
            }
        } else if (type == FieldType.META) {
            return Codec.encodeMetaToBytes((Set<MetaData>) value, charset);
        } else if (clazz == String.class) {
            return ByteUtils.stringToBytes((String) value, size, charset);
        } else if (clazz == Date.class) {
            return ByteUtils.longToBytes(((Date) value).getTime() / 1000);
        } else if (clazz == long.class) {
            return ByteUtils.longToBytes((long) value);
        } else if (clazz == int.class) {
            return ByteUtils.intToBytes((int) value);
        } else if (clazz == byte.class) {
            byte[] result = new byte[1];
            result[0] = (byte) value;
            return result;
        } else if (clazz == boolean.class) {
            byte[] result = new byte[1];
            result[0] = (boolean) value ? (byte) 1 : 0;
            return result;
        } else {
            throw new RuntimeException("Unknown type.");
        }
    }

    private int sizeOfVariableField(byte[] content) {
        int size = getSize();
        if (refOffset >= 0) {
            // 可变长字段存在引用字段来定义其长度
            if (refSize == Long.SIZE / Byte.SIZE) {
                size = (int) ByteUtils.bytesToLong(content, refOffset);
            } else if (refSize == Integer.SIZE / Byte.SIZE) {
                size = ByteUtils.bytesToInt(content, refOffset);
            } else {
                throw new IllegalArgumentException("unknown reference size.");
            }
        } else if (size > 0) {
            // 可变长有最大长度限制
            size = content.length - offset > size ? size : content.length - offset;
        } else {
            // 纯粹可变长
            size = content.length - offset;
        }

        return size;
    }

    @Override
    public String toString() {
        return "FieldMateData [field=" + field.getName() + ", index=" + index + ", size=" + size
                + ", offset=" + offset + ", RefOffset=" + refOffset + "]";
    }
}
