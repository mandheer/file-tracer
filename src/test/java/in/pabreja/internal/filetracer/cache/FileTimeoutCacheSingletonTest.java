package in.pabreja.internal.filetracer.cache;

import in.pabreja.internal.filetracer.domain.MtdDomain;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class FileTimeoutCacheSingletonTest {

    @Test
    void getInstance() {
        assertNotNull(FileTimeoutCacheSingleton.getInstance());
    }

    @Test
    void getCheckMapValue() {
        MtdDomain mtdDomain = new MtdDomain(0,0,0,Paths.get("dummy1"));
        FileTimeoutCacheSingleton.getInstance().put(Paths.get("dummy1"),mtdDomain);

        assertNotNull(FileTimeoutCacheSingleton.getInstance().get(Paths.get("dummy1")));
        assertEquals(mtdDomain,FileTimeoutCacheSingleton.getInstance().get(Paths.get("dummy1")));
    }
}