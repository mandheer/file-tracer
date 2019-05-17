package in.pabreja.internal.filetracer.reader;

import in.pabreja.internal.filetracer.browse.FileProcessor;
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

public class TextFileOperation implements FileStrategy {
    @Override
    public String getFileExt() {
        return "." + FileType.TEXT.getVal();
    }
    @Override
    public String getOutFileExt() {
        return "." + FileType.MTD.getVal();
    }

    @Override
    public MtdDomain apply(Path filePath) {
        return process(filePath);
    }

    private MtdDomain process(Path filePath) {
        if (filePath.toString().endsWith(getFileExt()) && FileTimeoutCacheSingleton.getInstance().get(filePath) == null) {
            try {
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
                });
                return new MtdDomain(wordCount.get(), vowelCount.get(), splCharCount.get(), filePath);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
