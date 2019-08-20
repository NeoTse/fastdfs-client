package me.johntse.fastdfs.proto.storage;

import me.johntse.fastdfs.proto.HandlerTest;
import me.johntse.fastdfs.proto.ProtocolConstants;
import me.johntse.fastdfs.proto.struct.MetaData;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * 设置元数据测试用例.
 *
 * @author johntse
 * @since 0.1.0
 */
public class SetMetaDataHandlerTest extends HandlerTest<Void> {
    private Set<MetaData> metaDataSet;
    private String filePath;

    @Before
    public void setUp() throws Exception {
        filePath = "file/path/set/meta/data";
        metaDataSet = new HashSet<>();
        metaDataSet.add(new MetaData("say", "hello"));
        metaDataSet.add(new MetaData("name", "I'm your tester."));

        SetMetaDataHandler handler = new SetMetaDataHandler("A", filePath, metaDataSet,
                ProtocolConstants.OperationFlag.STORAGE_SET_METADATA_FLAG_OVERWRITE);

        init(handler);
    }

    @Test
    public void getResult() throws Exception {
        handler.handle(connection);

        Assert.assertEquals(filePath, server.getFilePath());
        Assert.assertEquals(metaDataSet, server.getMetaDataSet());
    }

    @After
    public void tearDown() throws Exception {
        close();
    }
}