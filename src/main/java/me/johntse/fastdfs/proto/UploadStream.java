package me.johntse.fastdfs.proto;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * 文件上传.
 *
 * @author johntse
 * @since 0.1.0
 */
public class UploadStream implements CallBack {
    private InputStream fileStream;
    private long fileSize = 0;

    public UploadStream(InputStream fileStream, long fileSize) {
        this.fileStream = fileStream;
        this.fileSize = fileSize;
    }

    @Override
    public void send(OutputStream out) throws IOException {
        long remainBytes = fileSize;
        byte[] buff = new byte[256 * 1024];
        int bytes;

        while (remainBytes > 0) {
            if ((bytes = fileStream.read(buff, 0,
                    remainBytes > buff.length ? buff.length : (int) remainBytes)) < 0) {
                throw new IOException("the end of the stream has been reached. not match the expected size ");
            }

            out.write(buff, 0, bytes);
            remainBytes -= bytes;
        }
    }

    @Override
    public int receive(InputStream in, Charset charset, long exceptLen) throws IOException {
        throw new UnsupportedOperationException("UploadStream can't receive.");
    }
}
