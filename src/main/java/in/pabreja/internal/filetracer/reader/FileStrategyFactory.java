package in.pabreja.internal.filetracer.reader;

import in.pabreja.internal.filetracer.domain.FileType;
import in.pabreja.internal.filetracer.domain.MtdDomain;

import java.nio.file.Path;

public class FileStrategyFactory {
    private static final FileStrategy csv = new CsvFileOperation();
    private static final FileStrategy text = new TextFileOperation();

    public static FileStrategy getFileStrategy(FileType type){
        switch (type) {
            case TEXT:
                return text;
            case CSV:
                return csv;
            default:
                throw new IllegalArgumentException("type provided not available");
        }
    }

}
