package in.pabreja.internal.filetracer.directory;

import in.pabreja.internal.filetracer.domain.MtdDomain;

import java.nio.file.Path;
import java.util.List;

public interface DirectoryProcessor {
    void process(Path path, List<MtdDomain> domains);
    void setNext(DirectoryProcessor next);
    String getOutFileExt();
}
