package me.johntse.fastdfs.exception;

/**
 * FDFS异常类.
 *
 * @author johntse
 * @since 0.1.0
 */
abstract class FdfsException extends RuntimeException {
    private static final long serialVersionUID = -1732991315024220449L;

    FdfsException(String message) {
        super(message);
    }

    FdfsException(String message, Throwable cause) {
        super(message, cause);
    }
}
