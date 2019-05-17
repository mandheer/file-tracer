package in.pabreja.internal.filetracer.browse;

import in.pabreja.internal.filetracer.cache.FileTimeoutCacheSingleton;
import in.pabreja.internal.filetracer.domain.FileType;
import in.pabreja.internal.filetracer.domain.MtdDomain;

import java.nio.file.Path;

public class MtdFileProcessor implements FileProcessor {

    private FileProcessor nextInChain;


    @Override
    public String getFileExt() {
        return "."+ FileType.MTD.getVal();
    }

    @Override
    public MtdDomain process(Path filePath) {
        MtdDomain domain = null;

        if(nextInChain != null && domain==null){
            return nextInChain.process(filePath);
        }
        return domain;

    }

    @Override
    public String getOutFileExt() {
        return "."+ FileType.MTD.getVal();
    }

    @Override
    public void setNext(FileProcessor next) {
        nextInChain = next;
    }
}
