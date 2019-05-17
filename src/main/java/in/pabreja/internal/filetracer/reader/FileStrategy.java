package in.pabreja.internal.filetracer.reader;

import in.pabreja.internal.filetracer.domain.MtdDomain;

import java.nio.file.Path;

public interface FileStrategy {
    public MtdDomain apply(Path filePath);
    String getFileExt();
    String getOutFileExt();
}
