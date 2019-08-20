package me.johntse.fastdfs.util;

import java.nio.charset.Charset;

/**
 * byte数值相关操作结合.这里使用位移操作
 *
 * @author johntse
 * @since 0.1.0
 */
public final class ByteUtils {
    public static final byte[] EMPTY_BYTES = new byte[0];

    /**
     * 将long型数字转换为byte数组，注意这里转换后的结果是Big Endian，即网络序.
     *
     * @param nl 待转换的long数字
     * @return 成功转换后的网络序byte数组
     */
    public static byte[] longToBytes(long nl) {
        byte[] bytes;

        bytes = new byte[8];
        bytes[0] = (byte) ((nl >> 56) & 0xFF);
        bytes[1] = (byte) ((nl >> 48) & 0xFF);
        bytes[2] = (byte) ((nl >> 40) & 0xFF);
        bytes[3] = (byte) ((nl >> 32) & 0xFF);
        bytes[4] = (byte) ((nl >> 24) & 0xFF);
        bytes[5] = (byte) ((nl >> 16) & 0xFF);
        bytes[6] = (byte) ((nl >> 8) & 0xFF);
        bytes[7] = (byte) (nl & 0xFF);

        return bytes;
    }

    /**
     * 将Big Endian的byte数组中指定偏移量的byte转换为long型数字.
     *
     * @param bs     待转换的byte数组
     * @param offset 转换所需byte的偏移量
     * @return 成功转换后的long数字
     */
    public static long bytesToLong(byte[] bs, int offset) {
        if (bs.length - offset < 8) {
            offset = 0;
        }

        return (((long) (bs[offset] >= 0 ? bs[offset] : 256 + bs[offset])) << 56)
                | (((long) (bs[offset + 1] >= 0 ? bs[offset + 1] : 256 + bs[offset + 1])) << 48)
                | (((long) (bs[offset + 2] >= 0 ? bs[offset + 2] : 256 + bs[offset + 2])) << 40)
                | (((long) (bs[offset + 3] >= 0 ? bs[offset + 3] : 256 + bs[offset + 3])) << 32)
                | (((long) (bs[offset + 4] >= 0 ? bs[offset + 4] : 256 + bs[offset + 4])) << 24)
                | (((long) (bs[offset + 5] >= 0 ? bs[offset + 5] : 256 + bs[offset + 5])) << 16)
                | (((long) (bs[offset + 6] >= 0 ? bs[offset + 6] : 256 + bs[offset + 6])) << 8)
                | (bs[offset + 7] >= 0 ? bs[offset + 7] : 256 + bs[offset + 7]);
    }

    /**
     * 将integer型数字转换为byte数组，注意这里转换后的结果是Big Endian，即网络序.
     *
     * @param number 待转换的integer数字
     * @return 成功转换后的网络序byte数组
     */
    public static byte[] intToBytes(int number) {
        byte[] bytes;

        bytes = new byte[4];
        bytes[0] = (byte) ((number >> 24) & 0xFF);
        bytes[1] = (byte) ((number >> 16) & 0xFF);
        bytes[2] = (byte) ((number >> 8) & 0xFF);
        bytes[3] = (byte) (number & 0xFF);

        return bytes;
    }

    /**
     * 将Big Endian的byte数组中指定偏移量的byte转换为integer型数字.
     *
     * @param bytes  待转换的byte数组
     * @param offset 转换所需byte的偏移量
     * @return 成功转换后的integer数字
     */
    public static int bytesToInt(byte[] bytes, int offset) {
        if (bytes.length - offset < 4) {
            offset = 0;
        }

        return ((bytes[offset] >= 0 ? bytes[offset] : 256 + bytes[offset]) << 24)
                | ((bytes[offset + 1] >= 0 ? bytes[offset + 1] : 256 + bytes[offset + 1]) << 16)
                | ((bytes[offset + 2] >= 0 ? bytes[offset + 2] : 256 + bytes[offset + 2]) << 8)
                | (bytes[offset + 3] >= 0 ? bytes[offset + 3] : 256 + bytes[offset + 3]);
    }

    /**
     * 将字符串按照指定编码方式编码并转换为byte数组.
     *
     * @param str     待转换的字符串
     * @param charset 字符串转换编码方式
     * @return 按照指定编码生成的byte数组
     */
    public static byte[] stringToBytes(String str, Charset charset) {
        if (str == null || str.isEmpty()) {
            return EMPTY_BYTES;
        } else {
            return str.getBytes(charset);
        }
    }

    /**
     * 将字符串中指定长度的子字符串按照指定编码方式编码并转换为byte数组.
     *
     * @param str     待转换的字符串
     * @param len     子字符串长度
     * @param charset 字符串转换编码方式
     * @return 照指定编码生成的byte数组
     */
    public static byte[] stringToBytes(String str, int len, Charset charset) {
        if (str == null || str.isEmpty()) {
            if (len > 0) {
                return new byte[len];
            } else {
                return EMPTY_BYTES;
            }
        } else {
            byte[] max = new byte[len];

            byte[] raw = str.getBytes(charset);
            int trunked = len;
            if (raw.length <= len) {
                trunked = raw.length;
            }

            System.arraycopy(raw, 0, max, 0, trunked);
            return max;
        }
    }
}
