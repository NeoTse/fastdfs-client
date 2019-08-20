package me.johntse.fastdfs.conn;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * 连接概念，表示一个网络连接，比如tcp，udp等
 *
 * @author johntse
 * @since 0.1.0
 */
public interface Connection extends Closeable {

    /**
     * 测试连接是否已经关闭.
     *
     * @return 如果连接已关闭则返回true.
     */
    boolean isClosed();

    /**
     * 测试连接是否可用.
     *
     * @return 如果可用则返回true.
     */
    boolean isConnected();

    /**
     * 获取连接上的输出流，用于获取消息.
     *
     * @return 返回输出流
     * @throws IOException 如果连接异常，则throw this
     */
    OutputStream getOutputStream() throws IOException;

    /**
     * 获取连接上的输入流，用于发送消息.
     *
     * @return 返回输入流
     * @throws IOException 如果连接异常，则throw this
     */
    InputStream getInputStream() throws IOException;

    /**
     * 获取连接上传输内容的编码方式.
     *
     * @return 内容编码方式
     */
    Charset getCharset();
}
