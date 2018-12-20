package com.apulbere.lagos.pipedstream;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import com.apulbere.lagos.pipedstream.ZipStream.Tuple;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Stream;

public class ForkInputStream<T> {
    private ExecutorService executorService;

    public ForkInputStream(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public List<Future<T>> fork(InputStream source, List<CallableWithInputStream<T>> consumers) throws Exception {
        try(var closeableCollection = generate(consumers)) {
            var futures = ZipStream.zip(consumers.stream(), closeableCollection.stream(), Tuple::new)
                    .map(this::submit)
                    .collect(toList());

            byte[] buffer = new byte[1024];
            int length;
            while ((length = source.read(buffer)) != -1) {
                for (var pos: closeableCollection.getCollection()) {
                    write(buffer, length, pos);
                }
            }
            return futures;
        }
    }

    private CloseableCollection<PipedOutputStream> generate(List<CallableWithInputStream<T>> consumers) {
        return Stream.generate(PipedOutputStream::new).limit(consumers.size()).collect(collectingAndThen(toList(), CloseableCollection::new));
    }

    private void write(byte[] buffer, int length, OutputStream outputStream) {
        try {
            outputStream.write(buffer, 0, length);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Future<T> submit(Tuple<CallableWithInputStream<T>, PipedOutputStream> tuple) {
        try {
            tuple.left.setInputStream(new PipedInputStream(tuple.right));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return executorService.submit(tuple.left);
    }
}
