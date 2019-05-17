package in.pabreja.internal.filetracer.domain;

import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MtdDomain implements Serializable {

    private int wordsCount;
    private int vowelCount;
    private int splCharCount;
    private Path path;

    public MtdDomain(int wordsCount, int vowelCount, int splCharCount, Path path) {
        this.wordsCount = wordsCount;
        this.vowelCount = vowelCount;
        this.splCharCount = splCharCount;
        this.path = path;
    }

    public MtdDomain(String json){
        this.setJson(json);
    }


    public String getJson() {
        return "{" +
                "\"path\":\"" + path +
                "\", \"wordsCount\":\"" + wordsCount +
                "\", \"vowelCount\":\"" + vowelCount +
                "\", \"splCharCount\":\"" + splCharCount +
                "\"}";
    }

    private void setJson(String str){
        for(String s:str.substring(1,str.length()-1).split(",")){
            String[] arr = s.split("\":\"");
            if(arr != null && arr[0].equals("\"path")){
                this.path = Paths.get(arr[1].substring(0,arr[1].length()-1));
            }
            if(arr != null && arr[0].equals(" \"wordsCount")){
                this.wordsCount = Integer.parseInt(arr[1].substring(0,arr[1].length()-1));
            }
            if(arr != null && arr[0].equals(" \"vowelCount")){
                this.vowelCount = Integer.parseInt(arr[1].substring(0,arr[1].length()-1));
            }
            if(arr != null && arr[0].equals(" \"splCharCount")){
                this.splCharCount = Integer.parseInt(arr[1].substring(0,arr[1].length()-1));
            }
        }
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public int getWordsCount() {
        return wordsCount;
    }

    public void setWordsCount(int wordsCount) {
        this.wordsCount = wordsCount;
    }

    public int getVowelCount() {
        return vowelCount;
    }

    public void setVowelCount(int vowelCount) {
        this.vowelCount = vowelCount;
    }

    public int getSplCharCount() {
        return splCharCount;
    }

    public void setSplCharCount(int splCharCount) {
        this.splCharCount = splCharCount;
    }
}
