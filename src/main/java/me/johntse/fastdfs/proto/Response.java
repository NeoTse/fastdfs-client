package me.johntse.fastdfs.proto;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * 响应接口.
 *
 * @author johntse
 * @since 0.1.0
 */
public interface Response {

    /**
     * 读取（反序列化）响应信息.
     *
     * @param in      读取的流
     * @param charset 读取信息的编码方式
     * @throws IOException 网络读取错误
     */
    void read(InputStream in, Charset charset) throws IOException;

    /**
     * 获取反序列化的响应头.
     *
     * @return 如果反序列化成功或者响应正常，返回反序列化后响应头；否则，返回<code>null</code>
     */
    Header getHeader();

    /**
     * 获取未解析的响应体.
     *
     * @return 如果存在响应体且响应正常，返回原始响应体内容；否则返回<code>null</code>
     */
    byte[] getBody();
}
