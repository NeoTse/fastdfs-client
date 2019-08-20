package me.johntse.fastdfs.proto.tracker;

import me.johntse.fastdfs.proto.HandlerTest;
import me.johntse.fastdfs.proto.struct.StorageNode;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Storage查询器测试用例（带组名）.
 */
public class GetOneStorageWithGroupHandlerTest extends HandlerTest<StorageNode> {
    @Before
    public void setUp() throws Exception {
        GetOneStorageWithGroupHandler handler =
                new GetOneStorageWithGroupHandler("组C");
        init(handler);
    }

    @Test
    public void getResult() throws Exception {
        handler.handle(connection);

        Assert.assertEquals(server.getStorageNode(), handler.getResult(connection.getCharset()));
    }

    @After
    public void tearDown() throws Exception {
        close();
    }
}