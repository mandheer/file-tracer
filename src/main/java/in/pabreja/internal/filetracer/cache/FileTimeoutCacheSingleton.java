package in.pabreja.internal.filetracer.cache;

import in.pabreja.internal.filetracer.domain.MtdDomain;

import java.nio.file.Path;
import java.util.Map;

public class FileTimeoutCacheSingleton {

    private static FileTimeoutCacheSingleton _INSTANCE;

    private static Map<Path, MtdDomain> weakConcurrentHashMap = new WeakConcurrentHashMap<>();

    private FileTimeoutCacheSingleton(){

    }

    public static FileTimeoutCacheSingleton getInstance() {
        if(_INSTANCE == null){
            synchronized (FileTimeoutCacheSingleton.class){
                if(_INSTANCE == null){
                    _INSTANCE = new FileTimeoutCacheSingleton();
                }
            }
        }
        return _INSTANCE;
    }

    public void put(Path key,MtdDomain value){
        weakConcurrentHashMap.put(key,value);
    }

    public MtdDomain get(Path key){
        return weakConcurrentHashMap.get(key);
    }

    public void clearCache(){
        weakConcurrentHashMap.clear();
    }

    public Map<Path, MtdDomain> getConcurrentHashMap(){
        return weakConcurrentHashMap;
    }
}
