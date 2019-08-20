package me.johntse.fastdfs.proto;

import me.johntse.fastdfs.conn.Connection;
import me.johntse.fastdfs.exception.FdfsIOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * 抽象命令，定义了常见操作.
 *
 * @author johntse
 * @since 0.1.0
 */
public abstract class AbstractHandler<T> implements Handler {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractHandler.class);

    protected Request request;
    protected Response response;

    protected Parameters parameters;

    protected volatile T result;

    protected AbstractHandler(Parameters parameters, Request request, Response response) {
        this.parameters = parameters;
        this.request = request;
        this.response = response;
    }

    @Override
    public void handle(Connection connection) {
        doRequest(connection);
        doResponse(connection);
    }

    /**
     * 对返回的结果进行反序列化.
     *
     * @param charset 反序列化编码方式
     * @return 反序列化成功后，返回给定的类的对象
     */
    public T getResult(Charset charset) {
        if (result != null) {
            return result;
        }

        synchronized (this) {
            if (result == null) {
                result = getResult(response.getBody(), charset);
            }

            return result;
        }
    }

    /**
     * 对返回的结果按照指定的编码方式进行反序列化.
     *
     * @param body    返回的原始结果
     * @param charset 编码方式
     * @return 反序列化成功后，返回给定的类的对象
     */
    protected abstract T getResult(byte[] body, Charset charset);

    protected void doRequest(Connection connection) {
        try {
            request.write(parameters.serialize(connection.getCharset()),
                    connection.getOutputStream());
        } catch (IOException e) {
            LOGGER.error("Request to connection {} error: {}", connection, e);
            throw new FdfsIOException("Socket io exception occurred while request.", e);
        }
    }

    protected void doResponse(Connection connection) {
        try {
            response.read(connection.getInputStream(), connection.getCharset());
        } catch (IOException e) {
            LOGGER.error("Response from connection {} error: {}", connection, e);
            throw new FdfsIOException("Socket io exception occurred while receive content.", e);
        }
    }


    /**
     * 获取可读性的命名名称.
     *
     * @return 命名名称
     */
    public abstract String getName();
}
