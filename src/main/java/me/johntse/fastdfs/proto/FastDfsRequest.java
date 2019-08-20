package me.johntse.fastdfs.proto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;

/**
 * fastDFS通信协议请求.
 *
 * @author johntse
 * @since 0.1.0
 */
public class FastDfsRequest implements Request {
    private static final Logger LOGGER = LoggerFactory.getLogger(Request.class);

    /**
     * 请求回调函数，可用于序列化具体荷载内容.
     */
    private CallBack callBack;

    public FastDfsRequest() {

    }

    public FastDfsRequest(CallBack callBack) {
        this.callBack = callBack;
    }

    public CallBack getCallBack() {
        return callBack;
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public void write(byte[] content, OutputStream out) throws IOException {
        out.write(content);

        // 如果有请求内容，写入请求内容
        if (callBack != null) {
            LOGGER.debug("Request callback: {}", callBack);
            callBack.send(out);
        }
    }
}
