package me.johntse.fastdfs.proto.tracker;

import me.johntse.fastdfs.proto.HandlerTest;
import me.johntse.fastdfs.proto.struct.StorageNodeLite;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * 更新信息Storage选择器测试用例.
 *
 * @author johntse
 * @since 0.1.0
 */
public class GetUpdateStorageHandlerTest extends HandlerTest<StorageNodeLite> {
    @Before
    public void setUp() throws Exception {
        GetUpdateStorageHandler handler =
                new GetUpdateStorageHandler("组B", "M00/00/2F/oYYBAFjsQlmAdOnhAAEVHPW8HPU517.jpg");
        init(handler);
    }

    @Test
    public void getResult() throws Exception {
        handler.handle(connection);

        Assert.assertEquals(server.getStorageNodeLite(), handler.getResult(connection.getCharset()));
    }

    @After
    public void tearDown() throws Exception {
        close();
    }
}