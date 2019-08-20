package me.johntse.fastdfs.exception;

import me.johntse.fastdfs.proto.ProtocolConstants;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * fastdfs服务端返回的错误异常，可通过协议错误码生成
 *
 * @author johntse
 * @since 0.1.0
 */
public class FdfsServerException extends FdfsException {

    /**
     * 错误码转换表.
     */
    private static final Map<Integer, String> CODE_MESSAGE_MAPPING;

    static {
        Map<Integer, String> mapping = new HashMap<Integer, String>();
        mapping.put((int) ProtocolConstants.ERR_NO_ENOENT, "Can't found the node or file.");
        mapping.put((int) ProtocolConstants.ERR_NO_EIO, "Server IO exception.");
        mapping.put((int) ProtocolConstants.ERR_NO_EINVAL, "Invalid arguments");
        mapping.put((int) ProtocolConstants.ERR_NO_EBUSY, "Server busy.");
        mapping.put((int) ProtocolConstants.ERR_NO_ENOSPC, "No enough space.");
        mapping.put((int) ProtocolConstants.ERR_NO_CONNREFUSED, "Server refused.");
        mapping.put((int) ProtocolConstants.ERR_NO_EALREADY, "File already exist.");
        CODE_MESSAGE_MAPPING = Collections.unmodifiableMap(mapping);
    }


    private FdfsServerException(String message) {
        super(message);

    }

    /**
     * 根据error code生成相应的异常.
     *
     * @param errorCode 具体的error code
     * @return error code对应的异常类，
     */
    public static FdfsServerException valueOf(int errorCode) {
        String message = CODE_MESSAGE_MAPPING.get(errorCode);
        if (message == null) {
            message = "Unknown";
        }
        message = "Error Code：" + errorCode + ", Message：" + message;

        return new FdfsServerException(message);
    }

}
