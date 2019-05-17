package in.pabreja.internal.filetracer.browse;

import in.pabreja.internal.filetracer.domain.MtdDomain;

import java.nio.file.Path;
import java.util.function.Function;

public interface FileProcessor{

    String getFileExt();

    MtdDomain process(Path path);

    String getOutFileExt();

    void setNext(FileProcessor next);

}
