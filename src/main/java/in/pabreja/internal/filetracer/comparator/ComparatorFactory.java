package in.pabreja.internal.filetracer.comparator;

import in.pabreja.internal.filetracer.domain.MtdDomain;

import java.util.Comparator;

public class ComparatorFactory {
    private static Comparator<MtdDomain> word = new MtdWordComparator();
    private static Comparator<MtdDomain> vowel = new MtdVowelComparator();

    public static Comparator getComparator(ComparatorType type){
        switch (type){
            case WORD: return word;
            case vowel: return vowel;
            default: throw new IllegalArgumentException("undefined comparator type provided");
        }
    }
}
