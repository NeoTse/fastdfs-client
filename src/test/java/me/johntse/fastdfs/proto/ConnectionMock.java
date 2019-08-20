package me.johntse.fastdfs.proto;

import me.johntse.fastdfs.conn.Connection;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * Connection Mock，用于本地测试.
 *
 * @author johntse
 * @since 0.1.0
 */
public class ConnectionMock implements Connection {
    public final static Charset UTF8 = Charset.forName("utf8");

    private ByteArrayOutputStream outputStream;
    private InputStream inputStream;
    private ServerMock server;

    private boolean isClosed = false;

    public ConnectionMock(ByteArrayOutputStream outputStream, ServerMock server) {
        this.outputStream = outputStream;

        this.server = server;
    }

    @Override
    public boolean isClosed() {
        return isClosed;
    }

    @Override
    public boolean isConnected() {
        return true;
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return outputStream;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        inputStream = server.response(outputStream, UTF8);
        return inputStream;
    }

    @Override
    public Charset getCharset() {
        return UTF8;
    }

    @Override
    public void close() throws IOException {
        if (outputStream != null) {
            outputStream.close();
        }

        if (inputStream != null) {
            inputStream.close();
        }

        isClosed = true;
    }
}
