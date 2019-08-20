package me.johntse.fastdfs.proto.base;

/**
 * 字段类型.
 *
 * @author johntse
 * @since 0.1.0
 */
public enum FieldType {
    FIXED, // 固定宽度
    VARIABLE, // 可变宽度。如果同时设定了size长度，则表示在指定范围内可变
    IGNORE,  // 忽略该字段，仅在解码时生效
    META // 文件元数据
}
