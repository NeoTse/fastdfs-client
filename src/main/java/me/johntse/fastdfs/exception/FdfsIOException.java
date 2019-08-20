package me.johntse.fastdfs.exception;

/**
 * fastDFS输入输出异常.
 *
 * @author johntse
 * @since 0.1.0
 */
public class FdfsIOException extends FdfsException {
    private static final long serialVersionUID = -5224172942266225829L;

    public FdfsIOException(String message) {
        super(message);
    }

    public FdfsIOException(String message, Throwable cause) {
        super(message, cause);
    }
}
