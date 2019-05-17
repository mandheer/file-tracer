package in.pabreja.internal.filetracer.writer;

import in.pabreja.internal.filetracer.domain.FileType;
import in.pabreja.internal.filetracer.domain.MtdDomain;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileWriter {
    public static void writeToFileSystem(Path filePath, String string){
        try{
            BufferedWriter writer = Files.newBufferedWriter(filePath);
            writer.write(string);
            writer.flush();
            writer.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void writeList(Path filePath, List<MtdDomain> list){
        try{
            final BufferedWriter writer = Files.newBufferedWriter(filePath);
            for(MtdDomain m:list){
                writer.write(m.getJson());
            }
            writer.flush();
            writer.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void writeStringList(Path filePath, List<String> list){
        try{
            final BufferedWriter writer = Files.newBufferedWriter(filePath);
            for(String m:list){
                writer.write(m);
            }
            writer.flush();
            writer.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
