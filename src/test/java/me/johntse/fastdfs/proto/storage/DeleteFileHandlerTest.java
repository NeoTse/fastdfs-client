package me.johntse.fastdfs.proto.storage;

import me.johntse.fastdfs.proto.HandlerTest;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * 删除文件测试用例.
 *
 * @author johntse
 * @since 0.1.0
 */
public class DeleteFileHandlerTest extends HandlerTest<Void> {
    private String filePath;

    @Before
    public void setUp() throws Exception {
        filePath = "test/file/path";
        DeleteFileHandler handler = new DeleteFileHandler("A", filePath);

        init(handler);
    }

    @Test
    public void getResult() throws Exception {
        handler.handle(connection);

        Assert.assertTrue(server.isFileDelete());
        Assert.assertEquals(filePath, server.getFilePath());
    }

    @After
    public void tearDown() throws Exception {
        close();
    }
}