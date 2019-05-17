package in.pabreja.internal.filetracer.domain;

public enum FileType {
    TEXT("txt"),
    CSV("csv"),
    MTD("mtd"),
    DMTD("dmtd"),
    SMTD("smtd");

    private String extType;

    FileType(String extType){
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
}
