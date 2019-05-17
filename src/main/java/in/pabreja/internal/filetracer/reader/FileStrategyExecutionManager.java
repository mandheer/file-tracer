package in.pabreja.internal.filetracer.reader;

import in.pabreja.internal.filetracer.cache.FileTimeoutCacheSingleton;
import in.pabreja.internal.filetracer.domain.FileType;
import in.pabreja.internal.filetracer.domain.MtdDomain;
import in.pabreja.internal.filetracer.writer.FileWriter;

import java.nio.file.Path;
import java.nio.file.Paths;

public class FileStrategyExecutionManager {

    public static MtdDomain executeFileStrategy(Path filePath){
        MtdDomain domain = null;
        Path writeFileName = null;
        if(FileTimeoutCacheSingleton.getInstance().get(filePath) == null) {
            if (filePath.toString().endsWith("." + FileType.TEXT.getVal())) {
                domain = FileStrategyFactory.getFileStrategy(FileType.TEXT).apply(filePath);
                writeFileName = Paths.get(filePath.toString()
                        .substring(0, filePath.toString().lastIndexOf(FileType.TEXT.getVal())) + FileType.MTD);
            } else if (filePath.toString().endsWith("." + FileType.CSV.getVal())) {
                domain = FileStrategyFactory.getFileStrategy(FileType.CSV).apply(filePath);
                writeFileName = Paths.get(filePath.toString()
                        .substring(0, filePath.toString().lastIndexOf(FileType.CSV.getVal())) + FileType.MTD);
            }
            if (domain != null && writeFileName != null) {
                FileTimeoutCacheSingleton.getInstance().put(filePath, domain);
                FileWriter.writeToFileSystem(writeFileName,domain.getJson());
            }
        }
        return domain;
    }
}
