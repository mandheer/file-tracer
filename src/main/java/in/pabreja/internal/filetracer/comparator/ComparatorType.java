package in.pabreja.internal.filetracer.comparator;

public enum ComparatorType {
    WORD("word"),
    vowel("vowel");

    private String extType;

    ComparatorType(String extType){
        this.extType = extType;
    }

    public String getVal(){
        return this.extType;
    }

    @Override
    public String toString() {
        return "FileType{" +
                "extType='" + extType + '\'' +
                '}';
    }

    public static ComparatorType fromString(String text) {
        for (ComparatorType b : ComparatorType.values()) {
            if (b.extType.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }
}
