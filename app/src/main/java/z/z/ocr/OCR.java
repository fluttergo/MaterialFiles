package z.z.ocr;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;

import com.csharp.interop.HelloJNI;
import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.util.List;

import kotlin.jvm.JvmStatic;


public class OCR {
    private static final String TAG = OCR.class.getSimpleName();
    private static volatile boolean isRunning = false;
    private static TessBaseAPI tessApi = null;
    @JvmStatic
    public static void orc(Context context) {
        if (isRunning) {
            Log.d(TAG, "ignore , tess is  isRunning " );
            return;
        }
        if (null==tessApi)tessApi = initTess(context);

        isRunning = true;
        SDCardUtils.getAllPhotoInfo(context, new SDCardUtils.OnPhotoListener() {
            @Override
            public void onFinish(List<MediaBean> list) {
                Log.d(TAG, "File size:" + list.size());
                for (MediaBean mediaBean : list) {
                    File imageFile = new File(mediaBean.path);
                    Log.d(TAG, "getHOCRText image  : " + imageFile.getAbsolutePath());
                    if (!imageFile.exists()) {
                        Log.w(TAG, "File not exist" + imageFile.getAbsolutePath());
                        continue;
                    }
                    tessApi.setImage(imageFile);
                    String text = tessApi.getUTF8Text();
                    Log.d(TAG, "getHOCRText content: " + text);
                }
                isRunning = false;
            }
        });
        String productName = android.os.Build.PRODUCT;
        String gpu = android.opengl.GLES20.glGetString(android.opengl.GLES20.GL_RENDERER);
        String helloWorld = "productName:" + productName + " gpu:" + gpu;
        Log.d(TAG, "stringFromJNI: " + helloWorld);
        HelloJNI.stringFromJNI(helloWorld);
    }

    @NonNull
    private static TessBaseAPI initTess(Context context) {
        Assets.extractAssets(context);
        final TessBaseAPI tessApi = new TessBaseAPI(progressValues -> {
            Log.d(TAG, "Progress: " + progressValues.getPercent() + " %");
        });
        String dataPath = Assets.getTessDataPath(context);
        tessApi.init(dataPath, Config.TESS_LANG_CHI_SIM, Config.TESS_ENGINE);
        return tessApi;
    }
}
