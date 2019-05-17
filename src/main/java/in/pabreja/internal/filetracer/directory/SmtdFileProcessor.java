package in.pabreja.internal.filetracer.directory;

import in.pabreja.internal.filetracer.comparator.ComparatorFactory;
import in.pabreja.internal.filetracer.comparator.ComparatorType;
import in.pabreja.internal.filetracer.domain.FileType;
import in.pabreja.internal.filetracer.domain.MtdDomain;
import in.pabreja.internal.filetracer.property.PropertyLoaderSingleton;
import in.pabreja.internal.filetracer.writer.FileWriter;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SmtdFileProcessor implements DirectoryProcessor {

    private DirectoryProcessor nextInChain;

    @Override
    public void process(Path path, List<MtdDomain> domains) {
        ComparatorType ct = ComparatorType.fromString(
                PropertyLoaderSingleton.getInstance().getProperty("sorting.type")
        );
        Comparator<MtdDomain> comparator = ComparatorFactory.getComparator(
                ct);
        FileWriter.writeStringList(Paths.get(
                path.toString() +"\\"+ path.getFileName().toString() + "."+ getOutFileExt()),
                domains
                        .stream()
                        .map(m ->
                            "{" + m.getPath() +"," + ct.getVal() + ":" +m.getWordsCount() +"}"
                        )
                        .collect(
                                Collectors.toList()
                        ));
        if(nextInChain != null){
            nextInChain.process(path,domains);
        }
    }

    @Override
    public String getOutFileExt() {
        return "."+ FileType.SMTD.getVal();
    }

    @Override
    public void setNext(DirectoryProcessor next) {
        nextInChain = next;
    }

}
