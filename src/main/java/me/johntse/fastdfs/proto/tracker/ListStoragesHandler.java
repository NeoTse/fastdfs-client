package me.johntse.fastdfs.proto.tracker;

import me.johntse.fastdfs.proto.AbstractHandler;
import me.johntse.fastdfs.proto.FastDfsRequest;
import me.johntse.fastdfs.proto.FastDfsResponse;
import me.johntse.fastdfs.proto.Request;
import me.johntse.fastdfs.proto.Response;
import me.johntse.fastdfs.proto.base.Codec;
import me.johntse.fastdfs.proto.struct.StorageState;
import me.johntse.fastdfs.proto.tracker.internal.ListStoragesParameters;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * 获取指定分组下的所有的Storage Server.
 *
 * @author johntse
 * @since 0.1.0
 */
public class ListStoragesHandler extends AbstractHandler<List<StorageState>> {
    /**
     * 使用指定的参数对象构造一个Storage查询器.
     *
     * @param parameters 参数对象
     * @param request    查询器使用的Request对象
     * @param response   查询器使用的Response对象
     */
    public ListStoragesHandler(ListStoragesParameters parameters,
                               Request request, Response response) {
        super(parameters, request, response);
    }

    /**
     * 使用指定的参数对象构造一个Storage查询器.
     *
     * @param parameters 参数对象
     */
    public ListStoragesHandler(ListStoragesParameters parameters) {
        super(parameters, new FastDfsRequest(), new FastDfsResponse());
    }

    @Override
    protected List<StorageState> getResult(byte[] body, Charset charset) {
        List<StorageState> result = new ArrayList<>();
        if (body == null) {
            return result;
        }

        try {
            result = Codec.decodeToInstances(body, StorageState.class, charset);
        } catch (IllegalAccessException | InstantiationException e) {
            result = null;
        }

        return result;
    }

    @Override
    public String getName() {
        return "ListStorages";
    }
}
