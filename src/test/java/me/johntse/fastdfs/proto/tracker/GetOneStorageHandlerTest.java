package me.johntse.fastdfs.proto.tracker;

import me.johntse.fastdfs.proto.HandlerTest;
import me.johntse.fastdfs.proto.struct.StorageNode;
import me.johntse.fastdfs.proto.tracker.internal.GetOneStorageParameters;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Storage查询器测试用例.
 *
 * @author johntse
 * @since 0.1.0
 */
public class GetOneStorageHandlerTest extends HandlerTest<StorageNode> {

    @Before
    public void setUp() throws Exception {
        GetOneStorageHandler handler = new GetOneStorageHandler(new GetOneStorageParameters());
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