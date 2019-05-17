package in.pabreja.internal.filetracer.reader;

import in.pabreja.internal.filetracer.cache.FileTimeoutCacheSingleton;
import in.pabreja.internal.filetracer.domain.FileType;
import in.pabreja.internal.filetracer.domain.MtdDomain;
import in.pabreja.internal.filetracer.property.PropertyLoaderSingleton;
import in.pabreja.internal.filetracer.writer.FileWriter;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantLock;

public class FileReaderStrategyRun implements Runnable {

    private final int tWaitTime = PropertyLoaderSingleton.getInstance().getIntProperty("thread.waittime", 100);
    private final long totalTimetoLive = PropertyLoaderSingleton.getInstance().getIntProperty("thread.livetime", 0);
    private final CountDownLatch latch;

    private final FileStrategy fileStrategy;
    private final Queue<Path> pathQueue;

    public FileReaderStrategyRun(FileStrategy fileStrategy, Queue<Path> pathQueue, CountDownLatch latch) {
        this.fileStrategy = fileStrategy;
        this.pathQueue = pathQueue;
        this.latch = latch;

    }

    @Override
    public void run() {
        Thread.currentThread().setName(fileStrategy.getFileExt() + "Processor Thread");
        long startTime = System.currentTimeMillis();
        if (pathQueue == null) {
            throw new NullPointerException("please initialise queue");
        }
        while (true) {

            if (pathQueue.isEmpty()) {
                try {
                    Thread.sleep(tWaitTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                if (pathQueue.peek()!= null && pathQueue.peek().toString().endsWith(fileStrategy.getFileExt())) {
                    Path path = pathQueue.poll();
                    if (FileTimeoutCacheSingleton.getInstance().get(path) == null) {
                        MtdDomain domain = this.fileStrategy.apply(path);
                        FileTimeoutCacheSingleton.getInstance().put(path, domain);
                        FileWriter.writeToFileSystem(Paths.get(path.toString()
                                .substring(0, path.toString().lastIndexOf(fileStrategy.getFileExt())) +
                                        fileStrategy.getOutFileExt())
                                , domain.getJson());
                    }
                }
            }
            if (totalTimetoLive != 0l && System.currentTimeMillis() > totalTimetoLive + startTime) {
                break;
            }
        }
        latch.countDown();
    }
}
