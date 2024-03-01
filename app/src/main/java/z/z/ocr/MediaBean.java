package z.z.ocr;

import android.util.Log;

public class MediaBean {

    private static final String TAG = MediaBean.class.getSimpleName();
    public final Type type;
    public final String path;
    public final int size;
    public final String displayName;

    public MediaBean(Type image, String path, int size, String displayName) {
        this.type = image;
        this.path = path;
        this.size = size;
        this.displayName = displayName;
        Log.d(TAG, "MediaBean: name:"+displayName+" Path:"+path);
    }

    public static enum Type {
        Image, VIDEO
    };
}
