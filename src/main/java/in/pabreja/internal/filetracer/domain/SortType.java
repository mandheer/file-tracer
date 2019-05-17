package in.pabreja.internal.filetracer.domain;

public enum SortType {
    WORD("word"),
    VOWEL("vowel");

    private String sortOrder;

    SortType(String sortOrder){
        this.sortOrder = sortOrder;
    }

    public String getVal() {
        return sortOrder;
    }

    @Override
    public String toString() {
        return "SortType{" +
                "sortOrder='" + sortOrder + '\'' +
                '}';
    }
}
