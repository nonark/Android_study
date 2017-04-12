package search.android.tools;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.LruCache;

import search.android.aos_search.R;

/**
 * Created by nhnent on 2017. 4. 11..
 */

public class MemoryImageCache {

    private static int MAX_BITMAP_COUNT = 1000;
    private static LruCache<String, Bitmap> lruCache = new LruCache<>(MAX_BITMAP_COUNT);
    private static Bitmap defaultImage = BitmapFactory.decodeResource(Resources.getSystem(), R.mipmap.ic_launcher_round);

    public enum DUPLICATE {
        OVERLAB,
        SKIP;
    }

    public static synchronized void addBitmap(String key, Bitmap bitmap, DUPLICATE type) {
        if(key == null || bitmap == null) {
            return;
        }

        if(type == DUPLICATE.SKIP && lruCache.get(key) != null) {
            return;
        }

        if(lruCache.size() + 1 > lruCache.maxSize()) {
            clear();
        }

        lruCache.put(key, bitmap);
    }

    public static synchronized Bitmap getBitmap(String key) {

        if(key == null || lruCache.get(key) == null) {
            return BitmapFactory.decodeResource(Resources.getSystem(), R.mipmap.ic_launcher_round);
//            return defaultImage;
        }

        return lruCache.get(key);
    }

    public static synchronized void clear() {
        lruCache.evictAll();
    }
}
