package me.johntse.fastdfs.proto.tracker;

import me.johntse.fastdfs.proto.FastDfsRequest;
import me.johntse.fastdfs.proto.FastDfsResponse;
import me.johntse.fastdfs.proto.HandlerTest;
import me.johntse.fastdfs.proto.struct.StorageNodeLite;
import me.johntse.fastdfs.proto.tracker.internal.FetchOneStorageParameters;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Storage选择器测试用例.
 *
 * @author johntse
 * @since 0.1.0
 */
public class FetchOneStorageHandlerTest extends HandlerTest<StorageNodeLite> {

    @Before
    public void setUp() throws Exception {
        FetchOneStorageParameters parameters = new FetchOneStorageParameters("分组A",
                "M00/00/2F/oYYBAFjsQlmAdOnhAAEVHPW8HPU517.jpg");

        FetchOneStorageHandler handler =
                new FetchOneStorageHandler(parameters, new FastDfsRequest(), new FastDfsResponse());
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