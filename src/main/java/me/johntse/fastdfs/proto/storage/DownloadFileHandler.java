package me.johntse.fastdfs.proto.storage;

import me.johntse.fastdfs.proto.AbstractHandler;
import me.johntse.fastdfs.proto.Request;
import me.johntse.fastdfs.proto.Response;
import me.johntse.fastdfs.proto.storage.internal.DownloadFileParameters;

import java.nio.charset.Charset;

/**
 * 文件下载处理器.
 *
 * @author johntse
 * @since 0.1.0
 */
public class DownloadFileHandler extends AbstractHandler<Void> {
    /**
     * 使用指定的参数对象构造一个文件下载处理器.
     *
     * @param parameters 文件下载参数对象
     * @param request    文件下载处理器使用的Request对象
     * @param response   文件下载处理器使用的Response对象
     */
    public DownloadFileHandler(DownloadFileParameters parameters,
                               Request request, Response response) {
        super(parameters, request, response);
    }

    /**
     * 通过给定的文件下载参数构造一个文件下载处理器.
     *
     * @param fileOffset 下载文件偏移
     * @param fileSize   下载文件大小
     * @param groupName  下载文件所在group名称
     * @param filePath   下载文件路径
     * @param request    文件下载处理器使用的Request对象
     * @param response   文件下载处理器使用的Response对象
     */
    public DownloadFileHandler(long fileOffset, long fileSize, String groupName,
                               String filePath, Request request, Response response) {
        super(new DownloadFileParameters(fileOffset, fileSize, groupName, filePath),
                request, response);
    }

    @Override
    protected Void getResult(byte[] body, Charset charset) {
        return null;
    }

    @Override
    public String getName() {
        return "DownloadFile";
    }
}
