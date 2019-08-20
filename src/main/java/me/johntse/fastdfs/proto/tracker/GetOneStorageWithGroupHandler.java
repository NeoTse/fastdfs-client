package me.johntse.fastdfs.proto.tracker;

import me.johntse.fastdfs.proto.AbstractHandler;
import me.johntse.fastdfs.proto.FastDfsRequest;
import me.johntse.fastdfs.proto.FastDfsResponse;
import me.johntse.fastdfs.proto.Request;
import me.johntse.fastdfs.proto.Response;
import me.johntse.fastdfs.proto.base.Codec;
import me.johntse.fastdfs.proto.struct.StorageNode;
import me.johntse.fastdfs.proto.tracker.internal.GetOneStorageWithGroupParameters;

import java.nio.charset.Charset;

/**
 * 在指定的Group中可用的Storage中随机选取一个.
 *
 * @author johntse
 * @since 0.1.0
 */
public class GetOneStorageWithGroupHandler extends AbstractHandler<StorageNode> {
    /**
     * 使用指定的参数对象构造一个查询器.
     *
     * @param parameters 参数对象
     * @param request    查询器使用的Request对象
     * @param response   查询器使用的Response对象
     */
    public GetOneStorageWithGroupHandler(GetOneStorageWithGroupParameters parameters,
                                         Request request, Response response) {
        super(parameters, request, response);
    }

    /**
     * 使用指定的参数对象构造一个查询器.
     *
     * @param parameters 参数对象
     */
    public GetOneStorageWithGroupHandler(GetOneStorageWithGroupParameters parameters) {
        this(parameters, new FastDfsRequest(), new FastDfsResponse());
    }

    /**
     * 使用指定的分组名称构造一个查询器.
     *
     * @param groupName 指定的分组名称
     */
    public GetOneStorageWithGroupHandler(String groupName) {
        this(new GetOneStorageWithGroupParameters(groupName));
    }

    @Override
    protected StorageNode getResult(byte[] body, Charset charset) {
        if (body == null) {
            return null;
        }

        StorageNode result;

        try {
            result = Codec.decodeToInstance(body, StorageNode.class, charset);
        } catch (IllegalAccessException | InstantiationException e) {
            result = null;
        }

        return result;
    }

    @Override
    public String getName() {
        return "QueryOneStorageWithGroup";
    }
}
