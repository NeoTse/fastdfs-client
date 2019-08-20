package me.johntse.fastdfs.exception;

/**
 * 无法连接异常.
 *
 * @author johntse
 * @since 0.1.0
 */
public class FdfsConnectException extends FdfsException {
    private static final long serialVersionUID = 1440553916098488800L;

    public FdfsConnectException(String message) {
        super(message);
    }

    public FdfsConnectException(String message, Throwable cause) {
        super(message, cause);
    }
}
