package me.johntse.fastdfs.proto.storage;

import me.johntse.fastdfs.proto.CallBack;
import me.johntse.fastdfs.proto.ConnectionMock;
import me.johntse.fastdfs.proto.FastDfsRequest;
import me.johntse.fastdfs.proto.FastDfsResponse;
import me.johntse.fastdfs.proto.HandlerTest;
import me.johntse.fastdfs.proto.UploadStream;
import me.johntse.fastdfs.proto.struct.StoragePath;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * 上传文件测试用例.
 *
 * @author johntse
 * @since 0.1.0
 */
public class UploadFileHandlerTest extends HandlerTest<StoragePath> {
    private InputStream inputStream;
    private String fileContent;

    @Before
    public void setUp() throws Exception {
        fileContent = "Upload a file!! Hi, It's me :).";
        byte[] fileContentBytes = fileContent.getBytes(ConnectionMock.UTF8);
        inputStream = new ByteArrayInputStream(fileContentBytes);
        CallBack callBack = new UploadStream(inputStream, fileContentBytes.length);

        UploadFileHandler handler =
                new UploadFileHandler((byte) 0, fileContentBytes.length, ".txt", false,
                        new FastDfsRequest(callBack), new FastDfsResponse());
        init(handler);
    }

    @After
    public void tearDown() throws Exception {
        inputStream.close();
        connection.close();
    }

    @Test
    public void getResult() throws Exception {
        handler.handle(connection);

        Assert.assertEquals(fileContent, server.getFileContent());
        Assert.assertEquals(server.getStoragePath(), handler.getResult(connection.getCharset()));
    }

}