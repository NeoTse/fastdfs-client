package me.johntse.fastdfs.util;

import org.junit.Assert;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * ByteUtils Test Case.
 *
 * @author johntse
 * @since 0.1.0
 */
public class ByteUtilsTest {
    @Test
    public void longToBytes() throws Exception {
        ByteBuffer byteBuffer = ByteBuffer.allocate(Long.SIZE / Byte.SIZE);
        byteBuffer.putLong(0);
        Assert.assertArrayEquals(byteBuffer.array(), ByteUtils.longToBytes(0));
        byteBuffer.clear();

        byteBuffer.putLong(-1);
        Assert.assertArrayEquals(byteBuffer.array(), ByteUtils.longToBytes(-1));
        byteBuffer.clear();

        byteBuffer.putLong(0x123456);
        Assert.assertArrayEquals(byteBuffer.array(), ByteUtils.longToBytes(0x123456));
        byteBuffer.clear();

        byteBuffer.putLong(Long.MAX_VALUE);
        Assert.assertArrayEquals(byteBuffer.array(), ByteUtils.longToBytes(Long.MAX_VALUE));
        byteBuffer.clear();

        byteBuffer.putLong(Long.MIN_VALUE);
        Assert.assertArrayEquals(byteBuffer.array(), ByteUtils.longToBytes(Long.MIN_VALUE));
        byteBuffer.clear();
    }

    @Test
    public void bytesToLong() throws Exception {
        ByteBuffer byteBuffer = ByteBuffer.allocate(Long.SIZE / Byte.SIZE);
        byteBuffer.putLong(0);
        byte[] result = byteBuffer.array();
        Assert.assertEquals(0, ByteUtils.bytesToLong(result, 0));
        byteBuffer.clear();

        byteBuffer.putLong(-1);
        result = byteBuffer.array();
        Assert.assertEquals(-1, ByteUtils.bytesToLong(result, 0));
        byteBuffer.clear();

        byteBuffer.putLong(0x123456);
        result = byteBuffer.array();
        Assert.assertEquals(0x123456, ByteUtils.bytesToLong(result, 1));
        byteBuffer.clear();
    }

    @Test
    public void intToBytes() throws Exception {
        ByteBuffer byteBuffer = ByteBuffer.allocate(Integer.SIZE / Byte.SIZE);
        byteBuffer.putInt(0);
        Assert.assertArrayEquals(byteBuffer.array(), ByteUtils.intToBytes(0));
        byteBuffer.clear();

        byteBuffer.putInt(-1);
        Assert.assertArrayEquals(byteBuffer.array(), ByteUtils.intToBytes(-1));
        byteBuffer.clear();

        byteBuffer.putInt(0x12345);
        Assert.assertArrayEquals(byteBuffer.array(), ByteUtils.intToBytes(0x12345));
        byteBuffer.clear();

        byteBuffer.putInt(Integer.MAX_VALUE);
        Assert.assertArrayEquals(byteBuffer.array(), ByteUtils.intToBytes(Integer.MAX_VALUE));
        byteBuffer.clear();

        byteBuffer.putInt(Integer.MIN_VALUE);
        Assert.assertArrayEquals(byteBuffer.array(), ByteUtils.intToBytes(Integer.MIN_VALUE));
        byteBuffer.clear();
    }

    @Test
    public void bytesToInt() throws Exception {
        ByteBuffer byteBuffer = ByteBuffer.allocate(Integer.SIZE / Byte.SIZE);
        byteBuffer.putInt(0);
        byte[] result = byteBuffer.array();
        Assert.assertEquals(0, ByteUtils.bytesToInt(result, 0));
        byteBuffer.clear();

        byteBuffer.putInt(-1);
        result = byteBuffer.array();
        Assert.assertEquals(-1, ByteUtils.bytesToInt(result, 0));
        byteBuffer.clear();

        byteBuffer.putInt(0x123456);
        result = byteBuffer.array();
        Assert.assertEquals(0x123456, ByteUtils.bytesToInt(result, 1));
        byteBuffer.clear();
    }

    @Test
    public void stringToBytes() throws Exception {
        String s = "你好";
        Charset utf8 = Charset.forName("UTF-8");
        Assert.assertArrayEquals(s.getBytes(utf8), ByteUtils.stringToBytes(s, utf8));

        s = "";
        Assert.assertArrayEquals(ByteUtils.EMPTY_BYTES, ByteUtils.stringToBytes(s, utf8));
    }

    @Test
    public void stringToBytes1() throws Exception {
        String s = "你好";
        Charset utf8 = Charset.forName("UTF-8");
        byte[] result = s.getBytes(utf8);
        byte[] portion = new byte[result.length - 3];
        System.arraycopy(result, 0, portion, 0, portion.length);

        Assert.assertArrayEquals(portion, ByteUtils.stringToBytes(s, portion.length, utf8));
    }

}