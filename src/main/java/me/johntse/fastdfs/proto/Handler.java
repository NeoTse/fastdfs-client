package me.johntse.fastdfs.proto;

import me.johntse.fastdfs.conn.Connection;

/**
 * 命令接口，用于描述fastDFS通信协议处理.
 */
public interface Handler {
    /**
     * 在特定连接上执行该协议指令.
     *
     * @param connection 指定的网络连接
     */
    void handle(Connection connection);
}
