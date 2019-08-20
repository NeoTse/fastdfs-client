package me.johntse.fastdfs.proto.base;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于标记fastDFS通信协议解析过程中需要序列化和反序列化的字段.
 *
 * @author johntse
 * @since 0.1.0
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Field {
    /**
     * 序列化/反序列化过程中对应的索引号(从0开始)
     *
     * @return 获取索引号
     */
    int index() default 0;

    /**
     * 序列化/反序列化中在最终数据中占用段的长度，即多少个byte。
     * 设置为-1，表示长度不确定，需要根据具体数据确定
     *
     * @return 占用空间
     */
    int size() default 0;

    /**
     * 字段类型，默认为固定宽度。长度由字段类型或者size()中指定
     *
     * @return 字段类型
     */
    FieldType type() default FieldType.FIXED;

    /**
     * 长度由其它指定的Field表示.
     * 其中指定的Field只能向前引用，默认为-1，表示不指定。
     *
     * @return 指定Field的index
     */
    int fixedByIndex() default -1;
}
