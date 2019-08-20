package me.johntse.fastdfs.proto;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 请求接口.
 *
 * @author johntse
 * @since 0.1.0
 */
public interface Request {
    /**
     * 写入（序列化）请求内容，构造请求.
     *
     * @param content 请求内容
     * @param out     写入的流
     * @throws IOException 网络异常
     */
    void write(byte[] content, OutputStream out) throws IOException;
}
