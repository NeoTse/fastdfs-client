package me.johntse.fastdfs.proto;

import java.nio.charset.Charset;

/**
 * 参数接口.
 *
 * @author johntse
 * @since 0.1.0
 */
public interface Parameters {
    /**
     * 按照指定编码方式获取参数序列化后的byte数组
     *
     * @param charset 指定的编码方式
     * @return 序列化结果
     */
    byte[] serialize(Charset charset);
}
