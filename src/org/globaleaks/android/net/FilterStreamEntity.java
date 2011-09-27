package org.globaleaks.android.net;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.entity.InputStreamEntity;

public class FilterStreamEntity extends InputStreamEntity {

    private final ProgressListener listener;

    public FilterStreamEntity(InputStream stream, long length, ProgressListener pl) {
        super(stream, length);
        listener = pl;
    }

    @Override
    public void writeTo(final OutputStream outstream) throws IOException {
        super.writeTo(new CountingOutputStream(outstream, listener));
    }

    public static class CountingOutputStream extends FilterOutputStream {

        private final ProgressListener listener;
        private long transferred;

        public CountingOutputStream(final OutputStream out, final ProgressListener listener) {
            super(out);
            this.listener = listener;
            transferred = 0;
        }

        public void write(byte[] b, int off, int len) throws IOException {
            out.write(b, off, len);
            transferred += len;
            listener.transferred(transferred);
        }

        public void write(int b) throws IOException {
            out.write(b);
            transferred++;
            listener.transferred(transferred);
        }
    }

}
