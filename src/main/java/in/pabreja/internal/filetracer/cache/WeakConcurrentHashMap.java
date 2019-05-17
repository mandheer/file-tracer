package in.pabreja.internal.filetracer.cache;

import in.pabreja.internal.filetracer.browse.FileProcessor;
import in.pabreja.internal.filetracer.browse.FileProcessorFactory;
import in.pabreja.internal.filetracer.domain.FileType;
import in.pabreja.internal.filetracer.property.PropertyLoaderSingleton;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class WeakConcurrentHashMap<V> extends ConcurrentHashMap<Path, V> {

    private static final long serialVersionUID = 1L;

    private Map<Path, Long> timeMap = new ConcurrentHashMap<Path, Long>();
    private long expiryInMillis = Long.parseUnsignedLong(PropertyLoaderSingleton.getInstance().getProperty("cache.timeout"));
    private long checkFrequency = Long.parseUnsignedLong(PropertyLoaderSingleton.getInstance().getProperty("cache.clear.frequency"));

    public WeakConcurrentHashMap() {
        initialize();
    }

    void initialize() {
        if(checkFrequency > expiryInMillis){
            checkFrequency = expiryInMillis >> 2;
        }
        Thread t = new CleanerThread();
        t.setDaemon(true);
        t.start();
    }

    @Override
    public V put(Path key, V value) {
        timeMap.put(key, System.currentTimeMillis());
        return super.put(key, value);
    }

    @Override
    public void putAll(Map<? extends Path, ? extends V> m) {
        for (Path key : m.keySet()) {
            put(key, m.get(key));
        }
    }

    @Override
    public V putIfAbsent(Path key, V value) {
        if (!containsKey(key))
            return put(key, value);
        else
            return get(key);
    }

    class CleanerThread extends Thread {
        @Override
        public void run() {
            System.out.println("Initiating Cleaner Thread..");
            while (true) {

                try {
                    cleanMap();
                    Thread.sleep(checkFrequency);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e ){
                    e.printStackTrace();
                }
            }
        }

        private void cleanMap() throws IOException {
            long currentTime = System.currentTimeMillis();
            final FileProcessor processor = FileProcessorFactory.buildFileChainProcessor(new FileType[]{FileType.TEXT,FileType.CSV});
            for (Path key : timeMap.keySet()) {
                if (currentTime > (timeMap.get(key) + expiryInMillis)) {
                    V value = remove(key);
                    timeMap.remove(key);
                }
                if(Files.getLastModifiedTime(key).toMillis() + expiryInMillis > currentTime){
                    processor.process(key);
                }
            }
        }

    }
}
