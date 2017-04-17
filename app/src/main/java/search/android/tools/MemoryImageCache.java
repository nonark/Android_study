package search.android.tools;

import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

/**
 * Created by nhnent on 2017. 4. 11..
 */

public class MemoryImageCache {

    private static int MAX_BITMAP_COUNT = 1000;
    private static LruCache<String, Bitmap> lruCache = new LruCache<>(MAX_BITMAP_COUNT);

    public enum DUPLICATE {
        OVERLAB,
        SKIP;
    }

    public static void addBitmap(String key, Bitmap bitmap, DUPLICATE type) {

        if(key == null || bitmap == null) {
            return;
        }

        synchronized (lruCache) {
            if(type == DUPLICATE.SKIP && lruCache.get(key) != null) {
                return;
            }

            if(lruCache.size() + 1 > lruCache.maxSize()) {
                clear();
            }

            lruCache.put(key, bitmap);
        }
    }

    public static Bitmap getBitmap(String key) {
        synchronized (lruCache) {
            if(key == null || lruCache.get(key) == null) {
                return null;
            }

            return lruCache.get(key);
        }
    }

    public static synchronized void clear() {
        lruCache.evictAll();
    }
}
