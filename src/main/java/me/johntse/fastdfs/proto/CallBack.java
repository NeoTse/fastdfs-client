package me.johntse.fastdfs.proto;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * 回调接口，用于上传、下载和解析等任务.
 *
 * @author johntse
 * @since 0.1.0
 */
public interface CallBack {
    /**
     * 发送数据到指定流.
     *
     * @param out 数据需要发送的流
     * @throws IOException 流写入时可能会发生
     */
    void send(OutputStream out) throws IOException;

    /**
     * 从指定流中读取数据.
     *
     * @param in        需要读取数据的流
     * @param charset   读取数据的编码方式
     * @param exceptLen 预计读取数据长度
     * @return 表示接受到的byte数，-1表示无数据可以读取
     * @throws IOException 流读取时可能会发生
     */
    int receive(InputStream in, Charset charset, long exceptLen) throws IOException;
}
