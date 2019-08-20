package me.johntse.fastdfs.conn;

import org.apache.commons.pool2.BaseKeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * TCP连接池创建工厂.
 *
 * @author johntse
 * @since 0.1.0
 */
public class TcpConnectionPoolFactory extends BaseKeyedPooledObjectFactory<InetSocketAddress, Connection> {
    /**
     * SOCKET读取阻塞超时时长，单位milliseconds.
     */
    private final int soTimeout;

    /**
     * 连接服务器超时时长，单位milliseconds.
     */
    private final int connectTimeout;

    /**
     * 传输内容编码方式.
     */
    private final Charset charset;

    /**
     * 工厂构造方法.
     *
     * @param soTimeout      SOCKET读取阻塞超时时长，单位milliseconds.
     * @param connectTimeout 连接服务器超时时长，单位milliseconds.
     * @param charset        传输内容编码方式.
     */
    public TcpConnectionPoolFactory(int soTimeout, int connectTimeout, Charset charset) {
        if (soTimeout < 0 || connectTimeout < 0) {
            throw new IllegalArgumentException("time can't be set negative.");
        }
        this.soTimeout = soTimeout;
        this.connectTimeout = connectTimeout;
        this.charset = charset;
    }

    @Override
    public Connection create(InetSocketAddress address) throws Exception {
        return new TcpConnection(address, soTimeout, connectTimeout, charset);
    }

    @Override
    public PooledObject<Connection> wrap(Connection connection) {
        return new DefaultPooledObject<>(connection);
    }

    @Override
    public void destroyObject(InetSocketAddress key, PooledObject<Connection> pooledObject) throws Exception {
        pooledObject.getObject().close();
    }

    @Override
    public boolean validateObject(InetSocketAddress key, PooledObject<Connection> pooledObject) {
        return pooledObject.getObject().isConnected();
    }
}
