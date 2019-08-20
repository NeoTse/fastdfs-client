package me.johntse.fastdfs.conn;

import me.johntse.fastdfs.exception.FdfsConnectException;
import me.johntse.fastdfs.proto.ProtocolConstants;
import me.johntse.fastdfs.util.ByteUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * 提供TCP连接功能.
 *
 * @author johntse
 * @since 0.1.0
 */
public class TcpConnection implements Connection {
    private static final Logger LOGGER = LoggerFactory.getLogger(TcpConnection.class);

    private Socket socket;
    private Charset charset;

    /**
     * 创建一个TCP连接.
     *
     * @param address     连接的服务器地址
     * @param soTimeout   SOCKET读取阻塞超时时长，单位milliseconds
     * @param connTimeout 连接服务器超时时长，单位milliseconds
     * @param charset     传输内容编码方式
     */
    public TcpConnection(InetSocketAddress address,
                         int soTimeout, int connTimeout, Charset charset) {
        try {
            socket = new Socket();
            socket.setSoTimeout(soTimeout);
            socket.connect(address, connTimeout);

            LOGGER.debug("connect to {} soTimeout={} connectTimeout={}",
                    address, soTimeout, connTimeout);
            this.charset = charset;
        } catch (IOException e) {
            throw new FdfsConnectException("Server Address: " + address, e);
        }
    }

    @Override
    public boolean isClosed() {
        return socket.isClosed();
    }

    @Override
    public boolean isConnected() {
        LOGGER.debug("check connection {}", toString());
        byte[] data = generateCommandHeader(ProtocolConstants.FDFS_PROTO_CMD_ACTIVE_TEST);

        try {
            socket.getOutputStream().write(data);

            // 注意这里复用了data数组
            return socket.getInputStream().read(data) == data.length && checkStatusOk(data);

        } catch (IOException e) {
            LOGGER.error("connection exception: {}", e);
            return false;
        }
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return socket.getOutputStream();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return socket.getInputStream();
    }

    @Override
    public Charset getCharset() {
        return charset;
    }

    @Override
    public void close() throws IOException {
        LOGGER.debug("disconnect from {}", toString());
        byte[] data = generateCommandHeader(ProtocolConstants.FDFS_PROTO_CMD_QUIT);

        try {
            socket.getOutputStream().write(data);
        } catch (IOException e) {
            LOGGER.error("close connection exception: {}", e);
            throw e;
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    // ignored
                }
            }
        }
    }

    @Override
    public String toString() {
        return String.format("%s Charset: %s", socket.toString(), charset.toString());
    }

    private byte[] generateCommandHeader(byte cmd) {
        byte[] header = new byte[ProtocolConstants.FDFS_PROTO_PKG_LEN_SIZE + 2];
        Arrays.fill(header, (byte) 0);

        byte[] packageLen = ByteUtils.longToBytes(0);
        System.arraycopy(packageLen, 0, header, 0, packageLen.length);
        header[ProtocolConstants.PROTO_HEADER_CMD_INDEX] = cmd;
        header[ProtocolConstants.PROTO_HEADER_STATUS_INDEX] = (byte) 0;

        return header;
    }

    private boolean checkStatusOk(byte[] resp) {
        return resp != null && resp.length == ProtocolConstants.FDFS_PROTO_PKG_LEN_SIZE + 2
                && resp[ProtocolConstants.PROTO_HEADER_STATUS_INDEX] == 0;
    }
}
