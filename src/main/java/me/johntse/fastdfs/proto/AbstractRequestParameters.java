package me.johntse.fastdfs.proto;

import me.johntse.fastdfs.proto.base.Codec;
import me.johntse.fastdfs.util.ByteUtils;

import java.nio.charset.Charset;

/**
 * 抽象请求参数.
 *
 * @author johntse
 * @since 0.1.0
 */
public abstract class AbstractRequestParameters implements Parameters {
    protected byte type;

    protected AbstractRequestParameters(byte type) {
        this.type = type;
    }

    @Override
    public byte[] serialize(Charset charset) {
        byte[] parametersByte = null;
        if (isOnlyHeader()) {
            parametersByte = ByteUtils.EMPTY_BYTES;
        } else {
            parametersByte = Codec.encodeToBytes(this, charset);
        }


        Header header = generateHeader(parametersByte.length);
        byte[] headerByte = header.serialize(charset);

        return mergeHeaderAndParameters(headerByte, parametersByte);
    }

    protected final byte[] mergeHeaderAndParameters(byte[] header, byte[] parameters) {
        int offset = 0;
        byte[] result = new byte[header.length + parameters.length];
        System.arraycopy(header, 0, result, offset, header.length);
        offset += header.length;

        System.arraycopy(parameters, 0, result, offset, parameters.length);
        offset += parameters.length;

        assert result.length == offset;

        return result;
    }

    /**
     * 生成请求头，并填充负载参数长度.
     *
     * @param parameterLength 负载参数长度
     * @return 符合要求的请求头
     */
    protected abstract Header generateHeader(int parameterLength);

    /**
     * 是否只包含请求头，没有额外的请求参数.
     *
     * @return true表示仅包含请求头，否则含有额外请求参数
     */
    protected boolean isOnlyHeader() {
        return false;
    }
}
