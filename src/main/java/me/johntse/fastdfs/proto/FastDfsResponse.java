package me.johntse.fastdfs.proto;

import me.johntse.fastdfs.exception.FdfsIOException;
import me.johntse.fastdfs.exception.FdfsServerException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * fastDFS通信协议请求.
 *
 * @author johntse
 * @since 0.1.0
 */
public class FastDfsResponse implements Response {
    private static final Logger LOGGER = LoggerFactory.getLogger(Response.class);

    private Header header;
    private byte[] body;

    private CallBack callBack;

    public FastDfsResponse() {

    }

    public FastDfsResponse(CallBack callBack) {
        this.callBack = callBack;
    }

    public CallBack getCallBack() {
        return callBack;
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public void read(InputStream in, Charset charset) throws IOException {
        header = Header.instanceOf(in);
        validateResponseHeader();

        if (callBack != null) {
            callBack.receive(in, charset, header.getContentLength());
            body = null;
        } else {
            body = receiveBody(in, (int) header.getContentLength());
        }
    }


    @Override
    public Header getHeader() {
        return header;
    }

    @Override
    public byte[] getBody() {
        // 由于每次连接都会生成FastDfsResponse对象，因此出于性能考虑，直接暴露内部状态
        return body;
    }

    private byte[] receiveBody(InputStream in, int exceptLen) throws IOException {
        byte[] body = new byte[exceptLen];
        int total = 0;
        int remain = exceptLen;
        int received;

        while (total < exceptLen) {
            if ((received = in.read(body, total, remain)) < 0) {
                break;
            }

            total += received;
            remain -= received;
        }

        if (total != exceptLen) {
            LOGGER.error("received body size {} != {}", total, exceptLen);
            throw new IOException("received body size " + total + " != " + exceptLen);
        }

        return body;
    }

    /**
     * 对Response进行合法性校验.
     *
     * @throws FdfsIOException     响应包本身有问题
     * @throws FdfsServerException 相应包没有问题，但返回状态码非0，表示服务处理有问题
     */
    private void validateResponseHeader() {
        if (ProtocolConstants.FDFS_PROTO_CMD_RESP != header.getType()) {
            String message = "Receive cmd: " + header.getType()
                    + " is not correct, expect cmd: " + ProtocolConstants.FDFS_PROTO_CMD_RESP;
            LOGGER.error(message);
            throw new FdfsIOException(message);
        }

        if (header.getStatus() != 0) {
            LOGGER.error("status code != 0, errno: " + header.getStatus());
            throw FdfsServerException.valueOf(header.getStatus());
        }

        if (header.getContentLength() < 0) {
            String message = "Receive content length: "
                    + header.getContentLength() + " < 0!";
            LOGGER.error(message);
            throw new FdfsIOException(message);
        }
    }
}
