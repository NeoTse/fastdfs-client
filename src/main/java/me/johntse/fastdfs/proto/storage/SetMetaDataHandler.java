package me.johntse.fastdfs.proto.storage;

import me.johntse.fastdfs.proto.AbstractHandler;
import me.johntse.fastdfs.proto.FastDfsRequest;
import me.johntse.fastdfs.proto.FastDfsResponse;
import me.johntse.fastdfs.proto.ProtocolConstants;
import me.johntse.fastdfs.proto.Request;
import me.johntse.fastdfs.proto.Response;
import me.johntse.fastdfs.proto.storage.internal.SetMetaDataParameters;
import me.johntse.fastdfs.proto.struct.MetaData;

import java.nio.charset.Charset;
import java.util.Set;

/**
 * 文件元数据设置器.
 *
 * @author johntse
 * @since 0.1.0
 */
public class SetMetaDataHandler extends AbstractHandler<Void> {

    /**
     * 使用指定的参数对象构建一个文件元数据设置器.
     *
     * @param parameters 指定的参数对象
     * @param request    文件元数据设置器使用的Request对象
     * @param response   文件元数据设置器使用的Response对象
     */
    public SetMetaDataHandler(SetMetaDataParameters parameters,
                              Request request, Response response) {
        super(parameters, request, response);
    }

    /**
     * 通过给定的各个参数构建一个文件元数据设置器.
     *
     * @param groupName   设置的文件所在分组名
     * @param filePath    设置文件的存储路径
     * @param metaDataSet 需要设置的元数据
     * @param opFlag      操作类型
     * @param request     文件元数据设置器使用的Request对象
     * @param response    文件元数据设置器使用的Response对象
     */
    public SetMetaDataHandler(String groupName, String filePath, Set<MetaData> metaDataSet,
                              ProtocolConstants.OperationFlag opFlag,
                              Request request, Response response) {
        this(new SetMetaDataParameters(groupName, filePath, metaDataSet, opFlag),
                request, response);
    }

    /**
     * 通过给定的各个参数构建一个文件元数据设置器.
     *
     * @param groupName   设置的文件所在分组名
     * @param filePath    设置文件的存储路径
     * @param metaDataSet 需要设置的元数据
     * @param opFlag      操作类型
     */
    public SetMetaDataHandler(String groupName, String filePath, Set<MetaData> metaDataSet,
                              ProtocolConstants.OperationFlag opFlag) {
        this(new SetMetaDataParameters(groupName, filePath, metaDataSet, opFlag),
                new FastDfsRequest(), new FastDfsResponse());
    }

    @Override
    protected Void getResult(byte[] body, Charset charset) {
        return null;
    }

    @Override
    public String getName() {
        return "SetMetaData";
    }
}
