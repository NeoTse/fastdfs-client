package me.johntse.fastdfs.proto.base;

import me.johntse.fastdfs.proto.ProtocolConstants;
import me.johntse.fastdfs.proto.struct.MetaData;
import me.johntse.fastdfs.util.ByteUtils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 内部字段信息测试用例.
 *
 * @author johntse
 * @since 0.1.0
 */
public class FieldInfoTest {
    private MockTest testObj;
    private MetaTest metaTestObj;
    private Charset utf8;

    @Before
    public void setUp() throws Exception {
        testObj = new MockTest("FieldInfo Test", 1492475518,
                80, (byte) 1, true, "Let's test it!!");
        metaTestObj = new MetaTest();
        metaTestObj.metaDataSet.add(new MetaData("group", "China"));
        metaTestObj.metaDataSet.add(new MetaData("author", "John Tse"));
        metaTestObj.metaDataSet.add(new MetaData("date", "2017-4-18"));
        metaTestObj.metaDataSet.add(new MetaData("location"));

        utf8 = StandardCharsets.UTF_8;
    }

    @Test
    public void getBytes() throws Exception {
        int offset = 0;
        FieldInfo name = new FieldInfo(testObj.getClass().getDeclaredField("name"), offset);
        byte[] nameByteRaw = new byte[ProtocolConstants.FDFS_GROUP_NAME_MAX_LEN];
        byte[] raw = testObj.name.getBytes(utf8);
        System.arraycopy(raw, 0, nameByteRaw, 0, raw.length);
        Assert.assertArrayEquals(nameByteRaw, name.getBytes(testObj, utf8));
        Assert.assertEquals(ProtocolConstants.FDFS_GROUP_NAME_MAX_LEN, name.getSize());
        Assert.assertEquals(0, name.getIndex());
        Assert.assertEquals(0, name.getOffset());
        Assert.assertEquals(FieldType.FIXED, name.getType());
        offset += name.getSize();

        FieldInfo timestamp = new FieldInfo(testObj.getClass().getDeclaredField("timestamp"), offset);
        Assert.assertArrayEquals(ByteUtils.longToBytes(testObj.timestamp), timestamp.getBytes(testObj, utf8));
        Assert.assertEquals(1, timestamp.getIndex());
        Assert.assertEquals(8, timestamp.getSize());
        Assert.assertEquals(offset, timestamp.getOffset());
        Assert.assertEquals(FieldType.FIXED, timestamp.getType());
        offset += timestamp.getSize();

        FieldInfo port = new FieldInfo(testObj.getClass().getDeclaredField("port"), offset);
        Assert.assertArrayEquals(ByteUtils.intToBytes(testObj.port), port.getBytes(testObj, utf8));
        Assert.assertEquals(2, port.getIndex());
        Assert.assertEquals(4, port.getSize());
        Assert.assertEquals(offset, port.getOffset());
        Assert.assertEquals(FieldType.FIXED, port.getType());
        offset += port.getSize();

        FieldInfo index = new FieldInfo(testObj.getClass().getDeclaredField("index"), offset);
        byte[] indexByte = new byte[1];
        indexByte[0] = testObj.index;
        Assert.assertArrayEquals(indexByte, index.getBytes(testObj, utf8));
        Assert.assertEquals(3, index.getIndex());
        Assert.assertEquals(1, index.getSize());
        Assert.assertEquals(offset, index.getOffset());
        Assert.assertEquals(FieldType.FIXED, index.getType());
        offset += index.getSize();

        FieldInfo isOk = new FieldInfo(testObj.getClass().getDeclaredField("isOk"), offset);
        byte[] isOkByte = new byte[1];
        isOkByte[0] = testObj.isOk ? (byte) 1 : 0;
        Assert.assertArrayEquals(isOkByte, isOk.getBytes(testObj, utf8));
        Assert.assertEquals(4, isOk.getIndex());
        Assert.assertEquals(1, isOk.getSize());
        Assert.assertEquals(offset, isOk.getOffset());
        Assert.assertEquals(FieldType.FIXED, isOk.getType());
        offset += isOk.getSize();

        FieldInfo date = new FieldInfo(testObj.getClass().getDeclaredField("date"), offset);
        Assert.assertArrayEquals(ByteUtils.longToBytes(testObj.date.getTime() / 1000),
                date.getBytes(testObj, utf8));
        Assert.assertEquals(5, date.getIndex());
        Assert.assertEquals(8, date.getSize());
        Assert.assertEquals(offset, date.getOffset());
        Assert.assertEquals(FieldType.FIXED, date.getType());
        offset += date.getSize();

        FieldInfo allIsMine = new FieldInfo(testObj.getClass().getDeclaredField("allIsMine"), offset);
        byte[] allIsMineRaw = testObj.allIsMine.getBytes(utf8);
        Assert.assertArrayEquals(allIsMineRaw, allIsMine.getBytes(testObj, utf8));
        Assert.assertEquals(6, allIsMine.getIndex());
        Assert.assertEquals(0, allIsMine.getSize());
        Assert.assertEquals(offset, allIsMine.getOffset());
        Assert.assertEquals(FieldType.VARIABLE, allIsMine.getType());

        FieldInfo meta = new FieldInfo(metaTestObj.getClass().getDeclaredField("metaDataSet"), 0);
        byte[] metaByte = Codec.encodeMetaToBytes(metaTestObj.metaDataSet, utf8);
        Assert.assertArrayEquals(metaByte, meta.getBytes(metaTestObj, utf8));
        Assert.assertEquals(0, meta.getIndex());
        Assert.assertEquals(0, meta.getSize());
        Assert.assertEquals(0, meta.getOffset());
        Assert.assertEquals(FieldType.META, meta.getType());

        VariableWithMax normal = new VariableWithMax("172.16.15.1");
        FieldInfo varNormal = new FieldInfo(normal.getClass().getDeclaredField("ipAddress"), 0);
        byte[] varNormalByte = normal.ipAddress.getBytes(utf8);
        Assert.assertArrayEquals(varNormalByte, varNormal.getBytes(normal, utf8));
        Assert.assertEquals(ProtocolConstants.FDFS_IPADDR_SIZE - 1, varNormal.getSize());

        VariableWithMax min = new VariableWithMax("");
        FieldInfo varMin = new FieldInfo(normal.getClass().getDeclaredField("ipAddress"), 0);
        byte[] varMinByte = min.ipAddress.getBytes(utf8);
        Assert.assertArrayEquals(varMinByte, varMin.getBytes(min, utf8));
        Assert.assertEquals(ProtocolConstants.FDFS_IPADDR_SIZE - 1, varMin.getSize());

        VariableWithMax overflow = new VariableWithMax("172.161.155.11111");
        FieldInfo varOverFlow = new FieldInfo(overflow.getClass().getDeclaredField("ipAddress"), 0);
        byte[] varOverFlowRaw = overflow.ipAddress.getBytes(utf8);
        byte[] varOverFlowByte = new byte[varOverFlow.getSize()];
        System.arraycopy(varOverFlowRaw, 0, varOverFlowByte, 0, varOverFlow.getSize());
        Assert.assertArrayEquals(varOverFlowByte, varOverFlow.getBytes(overflow, utf8));
        Assert.assertEquals(ProtocolConstants.FDFS_IPADDR_SIZE - 1, varOverFlow.getSize());
    }

    @Test
    public void getValue() throws Exception {
        int offset = 0;
        byte[] content;
        FieldInfo name = new FieldInfo(testObj.getClass().getDeclaredField("name"), offset);
        byte[] nameByte = name.getBytes(testObj, utf8);
        offset += name.getSize();

        FieldInfo timestamp = new FieldInfo(testObj.getClass().getDeclaredField("timestamp"), offset);
        byte[] timestampByte = timestamp.getBytes(testObj, utf8);
        offset += timestamp.getSize();

        FieldInfo port = new FieldInfo(testObj.getClass().getDeclaredField("port"), offset);
        byte[] portByte = port.getBytes(testObj, utf8);
        offset += port.getSize();

        FieldInfo index = new FieldInfo(testObj.getClass().getDeclaredField("index"), offset);
        offset += index.getSize();

        FieldInfo isOk = new FieldInfo(testObj.getClass().getDeclaredField("isOk"), offset);
        offset += isOk.getSize();

        FieldInfo date = new FieldInfo(testObj.getClass().getDeclaredField("date"), offset);
        byte[] dateByte = date.getBytes(testObj, utf8);
        offset += date.getSize();

        FieldInfo allIsMine = new FieldInfo(testObj.getClass().getDeclaredField("allIsMine"), offset);
        byte[] allIsMineByte = allIsMine.getBytes(testObj, utf8);
        offset += allIsMineByte.length;

        content = new byte[offset];
        offset = 0;
        System.arraycopy(nameByte, 0, content, offset, name.getSize());
        offset += name.getSize();
        System.arraycopy(timestampByte, 0, content, offset, timestamp.getSize());
        offset += timestamp.getSize();
        System.arraycopy(portByte, 0, content, offset, port.getSize());
        offset += port.getSize();
        content[offset] = testObj.index;
        offset += index.getSize();
        content[offset] = testObj.isOk ? (byte) 1 : 0;
        offset += isOk.getSize();
        System.arraycopy(dateByte, 0, content, offset, date.getSize());
        offset += date.getSize();
        System.arraycopy(allIsMineByte, 0, content, offset, allIsMineByte.length);
        offset += allIsMineByte.length;

        Assert.assertEquals(offset, content.length);

        Assert.assertEquals(testObj.name, name.getValue(content, utf8));
        Assert.assertEquals(testObj.timestamp, timestamp.getValue(content, utf8));
        Assert.assertEquals(testObj.port, port.getValue(content, utf8));
        Assert.assertEquals(testObj.index, index.getValue(content, utf8));
        Assert.assertEquals(testObj.isOk, isOk.getValue(content, utf8));
        Assert.assertEquals(testObj.date.getTime() / 1000,
                ((Date) date.getValue(content, utf8)).getTime() / 1000);
        Assert.assertEquals(testObj.allIsMine, allIsMine.getValue(content, utf8));


        VariableWithMax normal = new VariableWithMax("172.16.15.1");
        FieldInfo varNormal = new FieldInfo(normal.getClass().getDeclaredField("ipAddress"), 0);
        byte[] varNormalByte = normal.ipAddress.getBytes(utf8);
        Assert.assertEquals(normal.ipAddress, varNormal.getValue(varNormalByte, utf8));
        Assert.assertEquals(ProtocolConstants.FDFS_IPADDR_SIZE - 1, varNormal.getSize());

        VariableWithMax min = new VariableWithMax("");
        FieldInfo varMin = new FieldInfo(normal.getClass().getDeclaredField("ipAddress"), 0);
        byte[] varMinByte = min.ipAddress.getBytes(utf8);
        Assert.assertEquals(min.ipAddress, varMin.getValue(varMinByte, utf8));
        Assert.assertEquals(ProtocolConstants.FDFS_IPADDR_SIZE - 1, varMin.getSize());

        VariableWithMax overflow = new VariableWithMax("172.161.155.11111");
        FieldInfo varOverFlow = new FieldInfo(overflow.getClass().getDeclaredField("ipAddress"), 0);
        byte[] varOverFlowRaw = overflow.ipAddress.getBytes(utf8);
        byte[] varOverFlowByte = new byte[varOverFlow.getSize()];
        System.arraycopy(varOverFlowRaw, 0, varOverFlowByte, 0, varOverFlow.getSize());
        Assert.assertEquals(overflow.ipAddress.substring(0, ProtocolConstants.FDFS_IPADDR_SIZE - 1),
                varOverFlow.getValue(varOverFlowByte, utf8));
        Assert.assertEquals(ProtocolConstants.FDFS_IPADDR_SIZE - 1, varOverFlow.getSize());
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

        @Field(index = 5, size = 8)
        Date date;

        @Field(index = 6, type = FieldType.VARIABLE)
        String allIsMine;

        MockTest() {

        }

        MockTest(String name, long timestamp, int port, byte index,
                 boolean isOk, String allIsMine) {
            this.name = name;
            this.timestamp = timestamp;
            this.port = port;
            this.index = index;
            this.isOk = isOk;
            this.date = new Date();
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
                    + port + ", index = " + index + ", isOk = " + isOk + ", date = " + date
                    + ", allIsMine = " + allIsMine + " ]";
        }
    }

    static class MetaTest {
        @Field(index = 0, type = FieldType.META)
        Set<MetaData> metaDataSet;

        MetaTest() {
            metaDataSet = new HashSet<>();
        }

        @Override
        public String toString() {
            return metaDataSet.toString();
        }
    }

    static class VariableWithMax {
        @Field(index = 0, size = ProtocolConstants.FDFS_IPADDR_SIZE - 1, type = FieldType.VARIABLE)
        private String ipAddress;

        public VariableWithMax() {
        }

        public VariableWithMax(String ipAddress) {
            this.ipAddress = ipAddress;
        }
    }
}