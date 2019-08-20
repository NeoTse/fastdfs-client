package me.johntse.fastdfs.proto.tracker.internal;

import me.johntse.fastdfs.proto.AbstractRequestParameters;
import me.johntse.fastdfs.proto.Header;
import me.johntse.fastdfs.proto.ProtocolConstants;

/**
 * 查询分组信息所需要的参数.
 *
 * @author johntse
 * @since 0.1.0
 */
public class ListGroupsParameters extends AbstractRequestParameters {
    public ListGroupsParameters() {
        super(ProtocolConstants.TRACKER_PROTO_CMD_SERVER_LIST_GROUP);
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
