package me.johntse.fastdfs.proto.tracker;

import me.johntse.fastdfs.proto.AbstractHandler;
import me.johntse.fastdfs.proto.FastDfsRequest;
import me.johntse.fastdfs.proto.FastDfsResponse;
import me.johntse.fastdfs.proto.Request;
import me.johntse.fastdfs.proto.Response;
import me.johntse.fastdfs.proto.base.Codec;
import me.johntse.fastdfs.proto.struct.StorageNodeLite;
import me.johntse.fastdfs.proto.tracker.internal.FetchOneStorageParameters;

import java.nio.charset.Charset;

/**
 * Storage选择器，根据文件路径选择一个Storage.
 * 具体选取策略参见tracker.conf#download_server 配置项
 *
 * @author johntse
 * @since 0.1.0
 */
public class FetchOneStorageHandler extends AbstractHandler<StorageNodeLite> {
    /**
     * 使用指定的参数对象构造一个Storage选择器.
     *
     * @param parameters 参数对象
     * @param request    选择器使用的Request对象
     * @param response   选择器使用的Response对象
     */
    public FetchOneStorageHandler(FetchOneStorageParameters parameters,
                                  Request request, Response response) {
        super(parameters, request, response);
    }

    /**
     * 使用指定的参数构造一个Storage选择器.
     *
     * @param groupName 文件所在分组名称
     * @param filePath  文件路径
     * @param request   选择器使用的Request对象
     * @param response  选择器使用的Response对象
     */
    public FetchOneStorageHandler(String groupName, String filePath,
                                  Request request, Response response) {
        this(new FetchOneStorageParameters(groupName, filePath), request, response);
    }

    /**
     * 使用指定的参数构造一个Storage选择器.
     * 使用默认的Request和Response对象
     *
     * @param groupName 文件所在分组名称
     * @param filePath  文件路径
     */
    public FetchOneStorageHandler(String groupName, String filePath) {
        this(new FetchOneStorageParameters(groupName, filePath),
                new FastDfsRequest(), new FastDfsResponse());
    }

    @Override
    protected StorageNodeLite getResult(byte[] body, Charset charset) {
        if (body == null) {
            return null;
        }

        StorageNodeLite result;

        try {
            result = Codec.decodeToInstance(body, StorageNodeLite.class, charset);
        } catch (IllegalAccessException | InstantiationException e) {
            result = null;
        }

        return result;
    }

    @Override
    public String getName() {
        return "FetchOneStorage";
    }
}
