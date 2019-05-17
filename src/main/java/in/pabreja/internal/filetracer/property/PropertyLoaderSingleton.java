package in.pabreja.internal.filetracer.property;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.locks.ReentrantLock;

public class PropertyLoaderSingleton {

    private static PropertyLoaderSingleton _INSTANCE;

    private static Properties properties;

    private PropertyLoaderSingleton(){
    }

    public static PropertyLoaderSingleton getInstance(){
        if(_INSTANCE==null){
            synchronized(PropertyLoaderSingleton.class){
                if(_INSTANCE == null){
                    _INSTANCE = new PropertyLoaderSingleton();
                    loadProperties("filetracer.properties");
                }
            }
        }
        return _INSTANCE;
    }

    private static void loadProperties(String resourceFileName) {
        properties = new Properties();
        try(InputStream inputStream = PropertyLoaderSingleton.class.getClassLoader().getResourceAsStream(resourceFileName)){
            properties.load(inputStream);
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public String getProperty(String p){
       return properties.getProperty(p);
    }

    public int getIntProperty(String p,int defaultVal){
        try{
            return Integer.parseInt(this.getProperty(p));
        } catch (NumberFormatException nfe){
            nfe.printStackTrace();
        }
        return defaultVal;
    }
     public Object get(Object key){
        return properties.get(key);
     }
}
