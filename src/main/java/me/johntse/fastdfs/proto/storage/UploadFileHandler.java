package me.johntse.fastdfs.proto.storage;

import me.johntse.fastdfs.proto.AbstractHandler;
import me.johntse.fastdfs.proto.Request;
import me.johntse.fastdfs.proto.Response;
import me.johntse.fastdfs.proto.base.Codec;
import me.johntse.fastdfs.proto.storage.internal.UploadFileParameters;
import me.johntse.fastdfs.proto.struct.StoragePath;

import java.nio.charset.Charset;

/**
 * 上传文件处理器.
 *
 * @author johntse
 * @since 0.1.0
 */
public class UploadFileHandler extends AbstractHandler<StoragePath> {

    /**
     * 使用指定的参数对象构建一个上传文件处理器.
     *
     * @param parameters 指定的参数对象
     * @param request    文件上传处理器使用的Request对象
     * @param response   文件上传处理器使用的Response对象
     */
    public UploadFileHandler(UploadFileParameters parameters, Request request, Response response) {
        super(parameters, request, response);
    }

    /**
     * 通过给定的各个参数构建一个上传文件处理器.
     *
     * @param storageIndex 上传文件存储的storage节点
     * @param fileSize     上传文件大小
     * @param fileExtName  上传文件扩展名
     * @param isAppendFile 是否为可追加文件
     * @param request      文件处理器使用的Request对象
     * @param response     文件处理器使用的Response对象
     */
    public UploadFileHandler(byte storageIndex, long fileSize,
                             String fileExtName, boolean isAppendFile,
                             Request request, Response response) {
        super(new UploadFileParameters(storageIndex, fileSize,
                fileExtName, isAppendFile), request, response);
    }

    @Override
    protected StoragePath getResult(byte[] body, Charset charset) {
        StoragePath result;
        if (body == null) {
            return null;
        }

        try {
            result = Codec.decodeToInstance(body, StoragePath.class, charset);
            // generate full path
            result.getFullPath();
        } catch (IllegalAccessException | InstantiationException e) {
            result = null;
        }

        return result;
    }

    @Override
    public String getName() {
        return "UploadFile";
    }
}
