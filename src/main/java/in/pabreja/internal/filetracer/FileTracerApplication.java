package in.pabreja.internal.filetracer;

import in.pabreja.internal.filetracer.browse.FileProcessor;
import in.pabreja.internal.filetracer.browse.FileProcessorFactory;
import in.pabreja.internal.filetracer.domain.FileType;
import in.pabreja.internal.filetracer.property.PropertyLoaderSingleton;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class FileTracerApplication {

    final FileProcessor fileProcessor = FileProcessorFactory.buildFileChainProcessor(new FileType[]{FileType.TEXT,FileType.CSV});
    final FileProcessor dirProcessor = FileProcessorFactory.buildFileChainProcessor(new FileType[]{FileType.DMTD,FileType.SMTD});

    public static void main(String[] args) {
        PropertyLoaderSingleton properties = PropertyLoaderSingleton.getInstance();
        String rootDirectory = properties.getProperty("directory.location");

        FileTracerApplication application = new FileTracerApplication();
        application.processFile(Paths.get(rootDirectory));

    }

    private void processFile(Path pathToDir){
        try (Stream<Path> filePathStream = Files.walk(pathToDir)) {
            filePathStream
                    .forEach(filePath -> {
                        if (Files.isDirectory(filePath)){
                            //processFile(filePath);
                            processDir(filePath);
                        } else if (Files.isRegularFile(filePath)){
                            fileProcessor.process(filePath);
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processDir(Path pathToDir){
        if(!Files.isDirectory(pathToDir)){
            throw new IllegalArgumentException("No directory is passed");
        }
        dirProcessor.process(pathToDir);
    }
}
