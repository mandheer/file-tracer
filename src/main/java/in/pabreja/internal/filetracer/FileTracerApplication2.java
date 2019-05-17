package in.pabreja.internal.filetracer;

import in.pabreja.internal.filetracer.browse.FileProcessor;
import in.pabreja.internal.filetracer.browse.FileProcessorFactory;
import in.pabreja.internal.filetracer.directory.DirProcessorFactory;
import in.pabreja.internal.filetracer.directory.DirectoryProcessor;
import in.pabreja.internal.filetracer.domain.FileType;
import in.pabreja.internal.filetracer.domain.MtdDomain;
import in.pabreja.internal.filetracer.property.PropertyLoaderSingleton;
import in.pabreja.internal.filetracer.reader.FileReaderStrategyRun;
import in.pabreja.internal.filetracer.reader.FileStrategyExecutionManager;
import in.pabreja.internal.filetracer.reader.FileStrategyFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Stream;

public class FileTracerApplication2 {
    final static PropertyLoaderSingleton prop = PropertyLoaderSingleton.getInstance();

    public static void main(String[] args) {
        PropertyLoaderSingleton properties = PropertyLoaderSingleton.getInstance();
        FileTracerApplication2 application = new FileTracerApplication2();
        application.processFile(Paths.get(prop.getProperty("directory.location")));

    }

    private int getQueueCapacity() {
        return Integer.parseInt(PropertyLoaderSingleton.getInstance().getProperty("queue.size"));
    }

    private void processDmtdSmtdDir(Path pathToDir) {

    }

    private void processFile(Path pathToDir) {
        int defaultQSize = 100;
        final ExecutorService executorService = Executors.newFixedThreadPool(2);
        Queue<Path> paths = new LinkedBlockingQueue<>(prop.getIntProperty("queue.size", defaultQSize));
        CountDownLatch latch = new CountDownLatch(2);

        executorService.execute(new FileReaderStrategyRun(FileStrategyFactory.getFileStrategy(FileType.TEXT), paths, latch));
        executorService.execute(new FileReaderStrategyRun(FileStrategyFactory.getFileStrategy(FileType.CSV), paths, latch));

        try (Stream<Path> filePathStream = Files.walk(pathToDir)) {

            filePathStream.filter(filePath ->
                filePath.toString().endsWith("." + FileType.TEXT.getVal()) || filePath.toString().endsWith("." + FileType.CSV.getVal())
            )
                    .forEach(filePath -> {
                        if (Files.isRegularFile(filePath)) {
                            while (paths.size() == prop.getIntProperty("queue.size", defaultQSize)) {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            paths.add(filePath);
                        }
                    });


        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        final ArrayList<MtdDomain> mtdDomains = new ArrayList<>();
        try (Stream<Path> filePathStream = Files.walk(pathToDir)) {
            filePathStream.filter(filePath ->
                 filePath.toString().endsWith("." + FileType.MTD.getVal())
            ).forEach(
                    filePath -> {
                        try {
                            Files.readAllLines(filePath).forEach(line -> {
                                mtdDomains.add(new MtdDomain(line));
                            });
                        } catch (IOException e){
                            e.printStackTrace();
                        }
                    }
            );
            DirectoryProcessor processor = DirProcessorFactory.buildFileChainProcessor(new FileType[]{FileType.DMTD,FileType.SMTD});
            processor.process(pathToDir,mtdDomains);

        } catch (IOException e){
            e.printStackTrace();
        }



        executorService.shutdown();
    }

    private void processDir(Path pathToDir) {
        if (!Files.isDirectory(pathToDir)) {
            throw new IllegalArgumentException("No directory is passed");
        }
        getDirProcessor().process(pathToDir);
    }

    private FileProcessor getDirProcessor() {
        return FileProcessorFactory.buildFileChainProcessor(new FileType[]{FileType.DMTD, FileType.SMTD});
    }
}
