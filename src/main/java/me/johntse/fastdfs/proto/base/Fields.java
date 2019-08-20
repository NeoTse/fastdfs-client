package me.johntse.fastdfs.proto.base;

import me.johntse.fastdfs.util.ByteUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 具有<code>@Field</code>注解的类中各个属性情况.
 *
 * @author johntse
 * @since 0.1.0
 */
public final class Fields {
    /**
     * 缓存，避免频繁反射影响性能.
     */
    private static final Map<String, FieldCollection> cache = new HashMap<>();

    private Fields() {
    }

    public static Fields getInstance() {
        return SingletonHolder.FIELDS;
    }

    /**
     * 获取指定Fields大小之和.
     *
     * @param fields 指定Fields
     * @return Fields大小之和
     */
    public static int sizeOf(List<FieldInfo> fields) {
        int size = 0;
        for (FieldInfo field : fields) {
            size += field.getSize();
        }

        return size;
    }

    /**
     * 根据指定Class信息获取其中的Field信息.
     * 如果存在动态字段，Field信息中有些信息并未填充，比如字段长度，准确的偏移量
     *
     * @param clazz 指定Class
     * @return Field信息
     */
    public List<FieldInfo> getFields(Class<?> clazz) {
        return getCollection(clazz).getFields();
    }

    /**
     * 根据指定Class信息获取其中的Field信息.
     * 并根据具体内容，对未确认字段进行填充.
     *
     * @param clazz   指定Class
     * @param content 具体内容
     * @return 填充后的Field信息
     */
    public List<FieldInfo> getFields(Class<?> clazz, byte[] content) {
        FieldCollection collection = getCollection(clazz);

        List<FieldInfo> notInitWithContent = collection.getFields();
        if (!collection.isHasVariableSizeField()) {
            return notInitWithContent;
        }

        int offset = 0;
        List<FieldInfo> result = new ArrayList<>();
        for (FieldInfo fieldInfo : notInitWithContent) {
            FieldInfo initInfo;
            int size;
            // 仅对不存在最大长度限制的可变长字段进行处理
            if (fieldInfo.getType() == FieldType.VARIABLE && fieldInfo.getSize() <= 0) {
                if (fieldInfo.getRefOffset() >= 0) {
                    // 内容中存在另外字段存储了可变长字段的具体长度
                    if (fieldInfo.getRefSize() == Integer.SIZE / Byte.SIZE) {
                        size = ByteUtils.bytesToInt(content, fieldInfo.getRefOffset());
                    } else if (fieldInfo.getRefSize() == Long.SIZE / Byte.SIZE) {
                        size = (int) ByteUtils.bytesToLong(content, fieldInfo.getRefOffset());
                    } else {
                        throw new IllegalArgumentException("unknown reference size.");
                    }

                } else {
                    // 否则，取剩下的全部
                    size = content.length - fieldInfo.getOffset();
                }
            } else {
                size = fieldInfo.getSize();
            }

            // 一旦中间的可变长字段更新后，其后字段的偏移量必须同步更新
            if (offset != fieldInfo.getOffset() || size != fieldInfo.getSize()) {
                initInfo = new FieldInfo(fieldInfo.getField(), fieldInfo.getIndex(), size,
                        offset, fieldInfo.getRefOffset(), fieldInfo.getRefSize(), fieldInfo.getType());
            } else {
                initInfo = fieldInfo;
            }

            offset += size;

            result.add(initInfo);
        }

        return Collections.unmodifiableList(result);
    }

    /**
     * 获取给定的Class的属性集合.
     * 采用double check方式
     *
     * @param clazz 给定Class
     * @return 属性集合
     */
    public FieldCollection getCollection(Class<?> clazz) {
        FieldCollection collection = cache.get(clazz.getName());

        if (collection == null) {
            synchronized (Fields.cache) {
                collection = cache.get(clazz.getName());

                if (collection == null) {
                    collection = new FieldCollection(clazz);
                    cache.put(collection.getClassName(), collection);
                }
            }
        }

        return collection;
    }

    /**
     * 懒汉式单列.
     */
    private static class SingletonHolder {
        private static final Fields FIELDS = new Fields();
    }

    /**
     * 属性集合.
     */
    public static class FieldCollection {
        /**
         * 对应的类名.
         */
        private String className;

        /**
         * 类中各个具有<code>@Field</code>注解属性.
         */
        private List<FieldInfo> fields;

        /**
         * 是否含有不定长的属性.
         */
        private boolean hasVariableSizeField;

        /**
         * 所有定长属性总长度.
         */
        private int totalFixedFieldSize;

        /**
         * 根据给定的Class生成对应的Fields对象.
         *
         * @param clazz 给定的class
         */
        public FieldCollection(Class<?> clazz) {
            if (clazz == null) {
                throw new IllegalArgumentException("Fields: class can't be null.");
            }

            hasVariableSizeField = false;
            totalFixedFieldSize = 0;

            className = clazz.getName();
            fields = Collections.unmodifiableList(getFieldsFrom(clazz));
        }

        private static void validateFields(List<FieldInfo> fields) {
            if (fields.isEmpty()) {
                throw new IllegalArgumentException("There is no any field.");
            }

            int index = 0;
            int size = fields.size();
            for (FieldInfo field : fields) {
                if (field.getIndex() != index) {
                    throw new IllegalArgumentException("Illegal field index. Excepted "
                            + index + ", but " + field.getIndex());
                }

                if (index < size - 1 && field.getType() == FieldType.VARIABLE
                        && field.getRefOffset() < 0) {
                    throw new IllegalArgumentException("Variable field isn't locate end "
                            + "or hasn't referenced fixed field/");
                }

                ++index;
            }

        }

        public int getTotalFixedFieldSize() {
            return totalFixedFieldSize;
        }

        public boolean isHasVariableSizeField() {
            return hasVariableSizeField;
        }

        public List<FieldInfo> getFields() {
            return fields;
        }

        public String getClassName() {
            return className;
        }

        private List<FieldInfo> getFieldsFrom(Class<?> clazz) {
            java.lang.reflect.Field[] fields = clazz.getDeclaredFields();
            List<FieldInfo> result = new ArrayList<>();

            int offset = 0;

            for (int i = 0; i < fields.length; i++) {
                java.lang.reflect.Field field = fields[i];

                field.setAccessible(true);
                if (field.isAnnotationPresent(Field.class)) {
                    Field f = field.getAnnotation(Field.class);
                    int refIndex = f.fixedByIndex();
                    int refOffset = -1;
                    int refSize = 0;
                    if (refIndex >= 0) {
                        if (result.size() <= refIndex) {
                            throw new IllegalArgumentException(
                                    String.format("%s field referenced index (%d) doesn't exist.",
                                            field.getName(), refIndex));
                        }

                        FieldInfo ref = result.get(refIndex);

                        if (ref.getType() != FieldType.FIXED
                                && ref.getField().getType() != int.class) {
                            throw new IllegalArgumentException(
                                    String.format("The referenced field(%s) by %s isn't a "
                                                    + "FIXED INTEGER field.",
                                            ref.getField().getName(), field.getName()));
                        }

                        refOffset = ref.getOffset();
                        refSize = ref.getSize();

                        if (refSize != 4 && refSize != 8) {
                            throw new IllegalArgumentException(
                                    String.format("The referenced field's(%s) length must be 4 or 8.",
                                            ref.getField().getName()));
                        }
                    }

                    FieldInfo info = new FieldInfo(field, refOffset, refSize, offset);

                    if (info.getType() == FieldType.VARIABLE && !hasVariableSizeField) {
                        hasVariableSizeField = true;
                    } else if (info.getType() == FieldType.FIXED) {
                        offset += info.getSize();
                    }

                    result.add(info);
                }
            }

            validateFields(result);

            totalFixedFieldSize = offset;

            return result;
        }
    }


}
