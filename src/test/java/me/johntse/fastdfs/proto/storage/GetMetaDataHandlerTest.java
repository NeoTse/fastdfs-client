package me.johntse.fastdfs.proto.storage;

import me.johntse.fastdfs.proto.HandlerTest;
import me.johntse.fastdfs.proto.struct.MetaData;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

/**
 * 元数据查看器测试用例.
 *
 * @author johntse
 * @since 0.1.0
 */
public class GetMetaDataHandlerTest extends HandlerTest<Set<MetaData>> {
    private String filePath;

    @Before
    public void setUp() throws Exception {
        filePath = "file/path/serialize/meta/data";
        GetMetaDataHandler handler = new GetMetaDataHandler("A", filePath);
        init(handler);
    }

    @Test
    public void getResult() throws Exception {
        handler.handle(connection);

        Assert.assertEquals(server.getFilePath(), filePath);
        Assert.assertEquals(server.getMetaDataSet(), handler.getResult(connection.getCharset()));
    }

    @After
    public void tearDown() throws Exception {
        close();
    }
}