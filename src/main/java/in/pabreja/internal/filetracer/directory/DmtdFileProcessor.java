package in.pabreja.internal.filetracer.directory;

import in.pabreja.internal.filetracer.browse.FileProcessor;
import in.pabreja.internal.filetracer.domain.FileType;
import in.pabreja.internal.filetracer.domain.MtdDomain;
import in.pabreja.internal.filetracer.writer.FileWriter;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class DmtdFileProcessor implements DirectoryProcessor {

    private DirectoryProcessor nextInChain;

    @Override
    public void process(Path path, List<MtdDomain> domains) {
        FileWriter.writeList(Paths.get(path.toString() +"\\"+ path.getFileName().toString() + "."+ getOutFileExt()),domains);
        if(nextInChain != null){
            nextInChain.process(path,domains);
        }
    }

    @Override
    public String getOutFileExt() {
        return "."+ FileType.DMTD.getVal();
    }

    @Override
    public void setNext(DirectoryProcessor next) {
        nextInChain = next;
    }

}
