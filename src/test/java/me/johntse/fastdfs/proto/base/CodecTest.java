package me.johntse.fastdfs.proto.base;

import me.johntse.fastdfs.proto.ProtocolConstants;
import me.johntse.fastdfs.proto.struct.MetaData;
import me.johntse.fastdfs.util.ByteUtils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 编解码器测试用例.
 *
 * @author johntse
 * @since 0.1.0
 */
public class CodecTest {

    private Charset utf8 = Charset.forName("utf8");
    private MockTest testObj;
    private byte[] exceptedBytes;
    private Set<MetaData> metaDataSet = new HashSet<>();

    @Before
    public void setUp() throws Exception {
        testObj = new MockTest("Codec测试", 1492475518,
                80, (byte) 1, true, "Let's test it!!");

        byte[] nameByte = testObj.name.getBytes(utf8);
        byte[] longByte = ByteUtils.longToBytes(testObj.timestamp);
        byte[] intByte = ByteUtils.intToBytes(testObj.port);
        byte[] allIsMineByte = testObj.allIsMine.getBytes(utf8);

        exceptedBytes = new byte[ProtocolConstants.FDFS_GROUP_NAME_MAX_LEN
                + longByte.length + intByte.length + 2 + allIsMineByte.length];
        int offset = 0;
        System.arraycopy(nameByte, 0, exceptedBytes, offset, nameByte.length);
        offset += ProtocolConstants.FDFS_GROUP_NAME_MAX_LEN;

        System.arraycopy(longByte, 0, exceptedBytes, offset, longByte.length);
        offset += longByte.length;

        System.arraycopy(intByte, 0, exceptedBytes, offset, intByte.length);
        offset += intByte.length;

        exceptedBytes[offset] = testObj.index;
        offset += 1;

        exceptedBytes[offset] = testObj.isOk ? (byte) 1 : 0;
        offset += 1;

        System.arraycopy(allIsMineByte, 0, exceptedBytes, offset, allIsMineByte.length);
        offset += allIsMineByte.length;

        assert offset == exceptedBytes.length;

        metaDataSet.add(new MetaData("group", "烽火"));
        metaDataSet.add(new MetaData("author", "John Tse"));
        metaDataSet.add(new MetaData("date", "2017-4-18"));
        metaDataSet.add(new MetaData("location"));
    }

    @Test
    public void decodeToInstance() throws Exception {
        MockTest fromBytes = Codec.decodeToInstance(exceptedBytes, MockTest.class, utf8);

        Assert.assertEquals(testObj, fromBytes);
    }

    @Test
    public void decodeToInstances() throws Exception {
        Assert.assertEquals(new ArrayList<>(), Codec.decodeToInstances(exceptedBytes, MockTest.class, utf8));

        int count = 3;
        int fixed = exceptedBytes.length - testObj.allIsMine.getBytes(utf8).length;
        byte[] multi = new byte[fixed * count];

        int offset = 0;

        for (int i = 0; i < count; i++) {
            System.arraycopy(exceptedBytes, 0, multi, offset, fixed);
            offset += fixed;
        }

        List<MockTestFixed> except = new ArrayList<>();
        except.add(new MockTestFixed("Codec测试", 1492475518, 80, (byte) 1, true));
        except.add(new MockTestFixed("Codec测试", 1492475518, 80, (byte) 1, true));
        except.add(new MockTestFixed("Codec测试", 1492475518, 80, (byte) 1, true));

        Assert.assertEquals(except, Codec.decodeToInstances(multi, MockTestFixed.class, utf8));
    }

    @Test
    public void encodeToBytes() throws Exception {
        byte[] result = Codec.encodeToBytes(testObj, utf8);
        int offset = 0;
        byte[] nameByte = new byte[ProtocolConstants.FDFS_GROUP_NAME_MAX_LEN];
        System.arraycopy(result, offset, nameByte, 0, ProtocolConstants.FDFS_GROUP_NAME_MAX_LEN);
        byte[] nameByteRaw = new byte[ProtocolConstants.FDFS_GROUP_NAME_MAX_LEN];
        byte[] raw = testObj.name.getBytes(utf8);
        System.arraycopy(raw, 0, nameByteRaw, 0, raw.length);
        Assert.assertArrayEquals(nameByteRaw, nameByte);
        offset += ProtocolConstants.FDFS_GROUP_NAME_MAX_LEN;

        byte[] longByte = new byte[8];
        System.arraycopy(result, offset, longByte, 0, 8);
        Assert.assertArrayEquals(ByteUtils.longToBytes(testObj.timestamp), longByte);
        offset += 8;

        byte[] intByte = new byte[4];
        System.arraycopy(result, offset, intByte, 0, 4);
        Assert.assertArrayEquals(ByteUtils.intToBytes(testObj.port), intByte);
        offset += 4;

        Assert.assertEquals(1, result[offset]);
        offset += 1;

        Assert.assertEquals(true, result[offset] != 0);
        offset += 1;

        byte[] allIsMineByteRaw = testObj.allIsMine.getBytes(utf8);
        byte[] allIsMineByte = new byte[allIsMineByteRaw.length];
        System.arraycopy(result, offset, allIsMineByte, 0, allIsMineByte.length);
        Assert.assertArrayEquals(allIsMineByteRaw, allIsMineByte);
        offset += allIsMineByte.length;

        Assert.assertEquals(result.length, offset);
    }

    @Test
    public void encodeAndDecodeMeta() throws Exception {
        Set<MetaData> gen = Codec.decodeMetaToInstance(Codec.encodeMetaToBytes(metaDataSet, utf8), utf8);

        Assert.assertEquals(metaDataSet, gen);
    }

    static class MockTest {
        @Field(index = 0, size = ProtocolConstants.FDFS_GROUP_NAME_MAX_LEN)
        String name;

        @Field(index = 1, size = 8)
        long timestamp;

        @Field(index = 2, size = 4)
        int port;

        @Field(index = 3, size = 1)
        byte index;

        @Field(index = 4, size = 1)
        boolean isOk;

        @Field(index = 5, type = FieldType.VARIABLE)
        String allIsMine;

        MockTest() {

        }

        MockTest(String name, long timestamp, int port, byte index, boolean isOk, String allIsMine) {
            this.name = name;
            this.timestamp = timestamp;
            this.port = port;
            this.index = index;
            this.isOk = isOk;
            this.allIsMine = allIsMine;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }

            if (obj != null && obj.getClass() == getClass()) {
                MockTest other = (MockTest) obj;

                return name != null && name.equals(other.name)
                        && timestamp == other.timestamp && port == other.port
                        && index == other.index && isOk == other.isOk
                        && allIsMine != null && allIsMine.equals(other.allIsMine);
            }

            return false;
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }

        @Override
        public String toString() {
            return "MockTest [ name = " + name + ", timestamp = " + timestamp + ", port = "
                    + port + ", index = " + index + ", isOk = " + isOk + ", allIsMine = " + allIsMine
                    + " ]";
        }
    }

    static class MockTestFixed {
        @Field(index = 0, size = ProtocolConstants.FDFS_GROUP_NAME_MAX_LEN)
        String name;

        @Field(index = 1, size = 8)
        long timestamp;

        @Field(index = 2, size = 4)
        int port;

        @Field(index = 3, size = 1)
        byte index;

        @Field(index = 4, size = 1)
        boolean isOk;

        MockTestFixed() {

        }

        public MockTestFixed(String name, long timestamp, int port, byte index, boolean isOk) {
            this.name = name;
            this.timestamp = timestamp;
            this.port = port;
            this.index = index;
            this.isOk = isOk;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }

            if (obj != null && obj.getClass() == getClass()) {
                MockTestFixed other = (MockTestFixed) obj;

                return name != null && name.equals(other.name)
                        && timestamp == other.timestamp && port == other.port
                        && index == other.index && isOk == other.isOk;
            }

            return false;
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }

        @Override
        public String toString() {
            return "MockTestFixed [ name = " + name + ", timestamp = " + timestamp + ", port = "
                    + port + ", index = " + index + ", isOk = " + isOk + " ]";
        }
    }
}