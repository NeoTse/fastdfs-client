package me.johntse.fastdfs.proto.tracker;

import me.johntse.fastdfs.proto.AbstractHandler;
import me.johntse.fastdfs.proto.FastDfsRequest;
import me.johntse.fastdfs.proto.FastDfsResponse;
import me.johntse.fastdfs.proto.Request;
import me.johntse.fastdfs.proto.Response;
import me.johntse.fastdfs.proto.base.Codec;
import me.johntse.fastdfs.proto.struct.GroupState;
import me.johntse.fastdfs.proto.tracker.internal.ListGroupsParameters;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * 查询Tracker服务器获取分组信息.
 *
 * @author johntse
 * @since 0.1.0
 */
public class ListGroupsHandler extends AbstractHandler<List<GroupState>> {
    /**
     * 使用指定的参数对象构造一个分组查询器.
     *
     * @param parameters 参数对象
     * @param request    分组查询器使用的Request对象
     * @param response   分组查询器使用的Response对象
     */
    public ListGroupsHandler(ListGroupsParameters parameters, Request request, Response response) {
        super(parameters, request, response);
    }

    /**
     * 使用指定的参数对象构造一个分组查询器.
     *
     * @param parameters 参数对象
     */
    public ListGroupsHandler(ListGroupsParameters parameters) {
        super(parameters, new FastDfsRequest(), new FastDfsResponse());
    }

    @Override
    protected List<GroupState> getResult(byte[] body, Charset charset) {
        List<GroupState> result = new ArrayList<>();
        if (body == null) {
            return result;
        }

        try {
            result = Codec.decodeToInstances(body, GroupState.class, charset);
        } catch (IllegalAccessException | InstantiationException e) {
            result = null;
        }

        return result;
    }

    @Override
    public String getName() {
        return "ListGroups";
    }
}
