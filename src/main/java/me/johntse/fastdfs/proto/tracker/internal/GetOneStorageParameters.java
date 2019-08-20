package me.johntse.fastdfs.proto.tracker.internal;

import me.johntse.fastdfs.proto.AbstractRequestParameters;
import me.johntse.fastdfs.proto.Header;
import me.johntse.fastdfs.proto.ProtocolConstants;

/**
 * 随机获取可用Storage参数对象.
 *
 * @author johntse
 * @since 0.1.0
 */
public class GetOneStorageParameters extends AbstractRequestParameters {
    public GetOneStorageParameters() {
        super(ProtocolConstants.TRACKER_PROTO_CMD_SERVICE_QUERY_STORE_WITHOUT_GROUP_ONE);
    }

    @Override
    protected Header generateHeader(int parameterLength) {
        return new Header(type);
    }

    @Override
    protected boolean isOnlyHeader() {
        return true;
    }
}
