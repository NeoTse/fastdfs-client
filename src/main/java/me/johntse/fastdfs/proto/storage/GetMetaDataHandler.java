package me.johntse.fastdfs.proto.storage;

import me.johntse.fastdfs.proto.AbstractHandler;
import me.johntse.fastdfs.proto.FastDfsRequest;
import me.johntse.fastdfs.proto.FastDfsResponse;
import me.johntse.fastdfs.proto.Request;
import me.johntse.fastdfs.proto.Response;
import me.johntse.fastdfs.proto.base.Codec;
import me.johntse.fastdfs.proto.storage.internal.GetMetaDataParameters;
import me.johntse.fastdfs.proto.struct.MetaData;

import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;

/**
 * 元数据获取器.
 *
 * @author johntse
 * @since 0.1.0
 */
public class GetMetaDataHandler extends AbstractHandler<Set<MetaData>> {

    /**
     * 使用指定的参数对象构造一个元数据获取器.
     *
     * @param parameters 参数对象
     * @param request    元数据获取器使用的Request对象
     * @param response   元数据获取器使用的Response对象
     */
    public GetMetaDataHandler(GetMetaDataParameters parameters,
                              Request request, Response response) {
        super(parameters, request, response);
    }

    /**
     * 通过给定的各个参数构建一个元数据获取器.
     *
     * @param groupName 获取元数据对应文件所在的分组名称
     * @param filePath  元数据对应文件的路径
     * @param request   元数据获取器使用的Request对象
     * @param response  元数据获取器使用的Response对象
     */
    public GetMetaDataHandler(String groupName, String filePath,
                              Request request, Response response) {
        this(new GetMetaDataParameters(groupName, filePath), request, response);
    }

    /**
     * 通过给定的各个参数构建一个元数据获取器.
     *
     * @param groupName 获取元数据对应文件所在的分组名称
     * @param filePath  元数据对应文件的路径
     */
    public GetMetaDataHandler(String groupName, String filePath) {
        this(new GetMetaDataParameters(groupName, filePath),
                new FastDfsRequest(), new FastDfsResponse());
    }

    @Override
    protected Set<MetaData> getResult(byte[] body, Charset charset) {
        Set<MetaData> result = new HashSet<>();
        if (body == null) {
            return result;
        }

        result = Codec.decodeMetaToInstance(body, charset);

        return result;
    }

    @Override
    public String getName() {
        return "GetMetaData";
    }
}
