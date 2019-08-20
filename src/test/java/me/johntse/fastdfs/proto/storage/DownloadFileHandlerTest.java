package me.johntse.fastdfs.proto.storage;

import me.johntse.fastdfs.proto.CallBack;
import me.johntse.fastdfs.proto.DownloadStream;
import me.johntse.fastdfs.proto.FastDfsRequest;
import me.johntse.fastdfs.proto.FastDfsResponse;
import me.johntse.fastdfs.proto.HandlerTest;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;

/**
 * 文件下载器测试用例.
 *
 * @author johntse
 * @since 0.1.0
 */
public class DownloadFileHandlerTest extends HandlerTest<Void> {
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() throws Exception {
        outputStream = new ByteArrayOutputStream();
        CallBack callBack = new DownloadStream(outputStream);
        DownloadFileHandler handler = new DownloadFileHandler(0, 0, "F",
                "download/file/path", new FastDfsRequest(), new FastDfsResponse(callBack));

        init(handler);
    }

    @After
    public void tearDown() throws Exception {
        connection.close();
        outputStream.close();
    }

    @Test
    public void getResult() throws Exception {
        handler.handle(connection);

        Assert.assertEquals(server.getDownloadFileContent(),
                new String(outputStream.toByteArray(), connection.getCharset()));
    }

}