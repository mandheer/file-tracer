package in.pabreja.internal.filetracer.directory;

import in.pabreja.internal.filetracer.browse.CsvFileProcessor;
import in.pabreja.internal.filetracer.browse.FileProcessor;
import in.pabreja.internal.filetracer.browse.MtdFileProcessor;
import in.pabreja.internal.filetracer.browse.TextFileProcessor;
import in.pabreja.internal.filetracer.domain.FileType;

public class DirProcessorFactory {

    private static DirectoryProcessor dmtd = new DmtdFileProcessor();
    private static DirectoryProcessor smtd = new SmtdFileProcessor();



    public static DirectoryProcessor getFileProcessor(FileType type) {
        switch (type) {
            case DMTD:
                return dmtd;
            case SMTD:
                return smtd;
            default:
                throw new IllegalArgumentException("dir type provided not available");
        }
    }

    public static DirectoryProcessor buildFileChainProcessor(FileType[] types){
        DirectoryProcessor fp = null;
        DirectoryProcessor next = null;
        DirectoryProcessor f  = null;
        for (FileType type:types){
            if(fp == null){
                fp = getFileProcessor(type);
            } else if(next == null && fp != null){
                next = getFileProcessor(type);
                fp.setNext(next);
            }
            else {
                f = getFileProcessor(type);
                next.setNext(f);
                next = f;
            }
        }
        return fp;
    }
}
