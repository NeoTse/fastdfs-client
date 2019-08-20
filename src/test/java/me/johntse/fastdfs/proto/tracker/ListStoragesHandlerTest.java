package me.johntse.fastdfs.proto.tracker;

import me.johntse.fastdfs.proto.HandlerTest;
import me.johntse.fastdfs.proto.struct.StorageState;
import me.johntse.fastdfs.proto.tracker.internal.ListStoragesParameters;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Storage信息查询器测试用例.
 * 不带IP地址
 *
 * @author johntse
 * @since 0.1.0
 */
public class ListStoragesHandlerTest extends HandlerTest<List<StorageState>> {
    @Before
    public void setUp() throws Exception {
        ListStoragesHandler handler = new ListStoragesHandler(new ListStoragesParameters("分组F"));

        init(handler);
    }

    @Test
    public void getResult() throws Exception {
        handler.handle(connection);

        Assert.assertEquals(server.getStorageStates(), handler.getResult(connection.getCharset()));
    }

    @After
    public void tearDown() throws Exception {
        close();
    }

}