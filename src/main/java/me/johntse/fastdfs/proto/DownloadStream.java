package me.johntse.fastdfs.proto;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * 下载回调.
 *
 * @author johntse
 * @since 0.1.0
 */
public class DownloadStream implements CallBack {
    private OutputStream out;

    public DownloadStream(OutputStream out) {
        this.out = out;
    }

    @Override
    public void send(OutputStream out) throws IOException {
        throw new UnsupportedOperationException("DownloadStream can't send.");
    }

    @Override
    public int receive(InputStream in, Charset charset, long exceptLen) throws IOException {
        long remainBytes = exceptLen;
        byte[] buff = new byte[256 * 1024];
        int bytes;

        while (remainBytes > 0) {
            if ((bytes = in.read(buff, 0,
                    remainBytes > buff.length ? buff.length : (int) remainBytes)) < 0) {
                throw new IOException("the end of the stream has been reached. not match the expected size ");
            }

            out.write(buff, 0, bytes);
            remainBytes -= bytes;
        }

        return (int) (exceptLen - remainBytes);
    }
}
