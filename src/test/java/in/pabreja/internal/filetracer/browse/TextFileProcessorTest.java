package in.pabreja.internal.filetracer.browse;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TextFileProcessorTest {

    @Test
    void getFileExt() {
        FileProcessor fp = new TextFileProcessor();
        Assertions.assertEquals(".txt",fp.getFileExt());
    }

    @Test
    void getOutFileExt() {
        FileProcessor fp = new TextFileProcessor();
        Assertions.assertEquals(".mtd",fp.getOutFileExt());
    }

}