package info.pixelmon.repack.ninja.leaping.configurate.util;

import java.util.concurrent.ConcurrentMap;

@FunctionalInterface
public interface MapFactory {
   ConcurrentMap create();
}
