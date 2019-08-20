package me.johntse.fastdfs.proto;

import me.johntse.fastdfs.proto.base.Codec;
import me.johntse.fastdfs.proto.base.Field;
import me.johntse.fastdfs.util.ByteUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * fastDFS通信协议头定义.
 *
 * @author johntse
 * @since 0.1.0
 */
public class Header implements Parameters {
    /**
     * 报文头长，固定为10.
     * 报文荷载内容长度+报文类型+状态位
     */
    public static final int HEAD_LENGTH = ProtocolConstants.FDFS_PROTO_PKG_LEN_SIZE + 2;

    /**
     * 报文荷载内容长度.
     */
    @Field(index = 0, size = 8)
    private long contentLength = 0;

    /**
     * 协议类型.
     *
     * @see ProtocolConstants
     */
    @Field(index = 1, size = 1)
    private byte type = 0;

    /**
     * 状态位.
     *
     * @see ProtocolConstants
     */
    @Field(index = 2, size = 1)
    private byte status = 0;

    public Header() {
    }

    public Header(byte type) {
        this.type = type;
    }

    /**
     * 根据给定的内容长度、协议类型和状态构造协议头.
     *
     * @param contentLength 报文荷载内容长度
     * @param type          协议类型
     * @param status        状态位，请求时忽略
     */
    public Header(long contentLength, byte type, byte status) {
        this.contentLength = contentLength;
        this.type = type;
        this.status = status;
    }

    /**
     * 根据给定的内容长度和协议类型构造协议头.
     * 一般用于构造请求头
     *
     * @param contentLength 报文荷载内容长度
     * @param type          协议类型
     */
    public Header(long contentLength, byte type) {
        this.contentLength = contentLength;
        this.type = type;
    }

    /**
     * 从流中读取数据反序列化生成报文头数据.
     *
     * @param in 读取的流
     * @return 序列化成功的报文头
     * @throws IOException 读取流发生IO异常
     */
    public static Header instanceOf(InputStream in) throws IOException {
        byte[] header = new byte[HEAD_LENGTH];
        int count = 0;
        if ((count = in.read(header)) != HEAD_LENGTH) {
            throw new IOException("recv package size " + count + " != " + HEAD_LENGTH);
        }

        long contentLength = ByteUtils.bytesToLong(header, 0);
        byte type = header[ProtocolConstants.PROTO_HEADER_CMD_INDEX];
        byte status = header[ProtocolConstants.PROTO_HEADER_STATUS_INDEX];

        return new Header(contentLength, type, status);
    }

    public long getContentLength() {
        return contentLength;
    }

    public byte getType() {
        return type;
    }

    public byte getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Header [contentLength = " + contentLength + ", type = " + type
                + ", status = " + status + "]";
    }

    @Override
    public byte[] serialize(Charset charset) {
        return Codec.encodeToBytes(this, charset);
    }
}
