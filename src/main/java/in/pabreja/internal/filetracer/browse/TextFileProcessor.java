package in.pabreja.internal.filetracer.browse;

import in.pabreja.internal.filetracer.cache.FileTimeoutCacheSingleton;
import in.pabreja.internal.filetracer.domain.FileType;
import in.pabreja.internal.filetracer.domain.MtdDomain;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicInteger;

public class TextFileProcessor implements FileProcessor {

    private FileProcessor nextInChain;

    @Override
    public String getFileExt() {
        return "." + FileType.TEXT.getVal();
    }

    @Override
    public MtdDomain process(Path filePath) {
        MtdDomain domain = null;
        if (filePath.toString().endsWith(getFileExt()) && FileTimeoutCacheSingleton.getInstance().get(filePath) == null) {
            BufferedWriter writer = null;
            try {
                BasicFileAttributes fatr = Files.readAttributes(filePath,
                        BasicFileAttributes.class);

                final AtomicInteger wordCount = new AtomicInteger(0);
                final AtomicInteger vowelCount = new AtomicInteger(0);
                final AtomicInteger splCharCount = new AtomicInteger(0);
                Files.readAllLines(filePath).forEach((line) -> {
                    char[] chars = line.toCharArray();
                    for (char c : chars) {
                        if (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u') {
                            vowelCount.getAndIncrement();
                        } else if (c == '@' || c == '#' || c == '$' || c == '*') {
                            splCharCount.getAndIncrement();
                        }
                    }
                    for(String str:line.split("\\s+")){
                        if(str != null && str.length() != 0){
                            wordCount.getAndIncrement();
                        }
                    }
//                    wordCount.getAndAdd(line.split("\\s+").length);
                });
                domain = new MtdDomain(wordCount.get(), vowelCount.get(), splCharCount.get(), filePath);
                //adding to cache
                FileTimeoutCacheSingleton.getInstance().put(filePath,domain);

                final Path writeFileName = Paths.get(filePath.toString()
                        .substring(0, filePath.toString().lastIndexOf(getFileExt())) + getOutFileExt());
                writer = Files.newBufferedWriter(writeFileName);
                writer.write(domain.getJson());
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(nextInChain != null && domain==null){
           return nextInChain.process(filePath);
        }
        return domain;
    }

    public String getOutFileExt() {
        return "." + FileType.MTD.getVal();
    }

    @Override
    public void setNext(FileProcessor next) {
        nextInChain = next;
    }
}
