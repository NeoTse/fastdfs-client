package me.johntse.fastdfs.proto.storage;

import me.johntse.fastdfs.proto.AbstractHandler;
import me.johntse.fastdfs.proto.FastDfsRequest;
import me.johntse.fastdfs.proto.FastDfsResponse;
import me.johntse.fastdfs.proto.Request;
import me.johntse.fastdfs.proto.Response;
import me.johntse.fastdfs.proto.storage.internal.DeleteFileParameters;

import java.nio.charset.Charset;

/**
 * 删除文件处理器.
 *
 * @author johntse
 * @since 0.1.0
 */
public class DeleteFileHandler extends AbstractHandler<Void> {
    /**
     * 使用指定的参数对象构建一个删除文件处理器.
     *
     * @param parameters 指定的参数对象
     * @param request    删除处理器使用的Request对象
     * @param response   删除处理器使用的Response对象
     */
    public DeleteFileHandler(DeleteFileParameters parameters, Request request, Response response) {
        super(parameters, request, response);
    }

    /**
     * 通过给定的各个参数构建一个删除文件处理器.
     *
     * @param groupName 删除文件所在的分组名称
     * @param filePath  删除文件路径
     * @param request   删除处理器使用的Request对象
     * @param response  删除处理器使用的Response对象
     */
    public DeleteFileHandler(String groupName, String filePath,
                             Request request, Response response) {
        this(new DeleteFileParameters(groupName, filePath), request, response);
    }

    /**
     * 通过给定的各个参数构建一个删除文件处理器.
     *
     * @param groupName 删除文件所在的分组名称
     * @param filePath  删除文件路径
     */
    public DeleteFileHandler(String groupName, String filePath) {
        this(new DeleteFileParameters(groupName, filePath),
                new FastDfsRequest(), new FastDfsResponse());
    }

    @Override
    protected Void getResult(byte[] body, Charset charset) {
        return null;
    }

    @Override
    public String getName() {
        return "DeleteFile";
    }
}
