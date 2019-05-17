package in.pabreja.internal.filetracer.browse;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CsvFileProcessorTest {

    @Test
    void getFileExt() {
        FileProcessor fp = new CsvFileProcessor();
        Assertions.assertEquals(".csv",fp.getFileExt());
    }

    @Test
    void getOutFileExt() {
        FileProcessor fp = new CsvFileProcessor();
        Assertions.assertEquals(".mtd",fp.getOutFileExt());
    }

}