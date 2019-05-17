package in.pabreja.internal.filetracer.browse;

import in.pabreja.internal.filetracer.directory.DmtdFileProcessor;
import in.pabreja.internal.filetracer.directory.SmtdFileProcessor;
import in.pabreja.internal.filetracer.domain.FileType;

public class FileProcessorFactory {

    private static FileProcessor text = new TextFileProcessor();
    private static FileProcessor csv = new CsvFileProcessor();
    private static FileProcessor mtd = new MtdFileProcessor();



    public static FileProcessor getFileProcessor(FileType type) {
        switch (type) {
            case TEXT:
                return text;
            case CSV:
                return csv;
            case MTD:
                return mtd;
            default:
                throw new IllegalArgumentException("type provided not available");
        }
    }

    public static FileProcessor buildFileChainProcessor(FileType[] types){
        FileProcessor fp = null;
        FileProcessor next = null;
        FileProcessor f  = null;
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
