package me.johntse.fastdfs.proto;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Handler测试抽象.
 */
public abstract class HandlerTest<T> {
    protected ConnectionMock connection;
    protected AbstractHandler<T> handler;
    protected ServerMock server;

    protected void init(AbstractHandler<T> handler) {
        this.handler = handler;
        this.server = new ServerMock();
        this.connection = new ConnectionMock(new ByteArrayOutputStream(), server);
    }

    public void close() throws IOException {
        connection.close();
    }
}
