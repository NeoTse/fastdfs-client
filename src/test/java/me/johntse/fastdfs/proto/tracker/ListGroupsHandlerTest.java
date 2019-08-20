package me.johntse.fastdfs.proto.tracker;

import me.johntse.fastdfs.proto.HandlerTest;
import me.johntse.fastdfs.proto.struct.GroupState;
import me.johntse.fastdfs.proto.tracker.internal.ListGroupsParameters;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * 分组信息查询器测试用例.
 *
 * @author johntse
 * @since 0.1.0
 */
public class ListGroupsHandlerTest extends HandlerTest<List<GroupState>> {
    @Before
    public void setUp() throws Exception {
        ListGroupsHandler handler = new ListGroupsHandler(new ListGroupsParameters());
        init(handler);
    }

    @Test
    public void getResult() throws Exception {
        handler.handle(connection);

        Assert.assertEquals(server.getGroupStates(), handler.getResult(connection.getCharset()));
    }

    @After
    public void tearDown() throws Exception {
        close();
    }
}