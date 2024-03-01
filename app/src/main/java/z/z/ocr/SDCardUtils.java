package z.z.ocr;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SDCardUtils {

    private static final String TAG = SDCardUtils.class.getSimpleName();
    private static String[] PIC_PATH = {
            "/sdcard/Pictures/Screenshots/",
            "/sdcard/Download/Browser/",
            "/sdcard/DCIM/Camera/",
    };
    public static void ScanSDCardFile(Context ctx) {


        for (String s : PIC_PATH) {
            String folderPath = s;
            Log.d(TAG, "ScanSDCardFile: filepath :"+s);
            File folder = new File(folderPath);

            if (folder.exists() && folder.isDirectory()) {
                // 调用遍历方法
                traverseFolder(folder);
            } else {
                Log.w(TAG, "ScanSDCardFile: Invalid folder path.");
            }
        }
    }

    private static void readFile(File file) {
        if (file.exists()) {
            try {
                Log.d(TAG, "readFile: "+file.getAbsolutePath());
//                // 读取文件内容
//                BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
//                StringBuilder stringBuilder = new StringBuilder();
//                String line;
//
//                while ((line = bufferedReader.readLine()) != null) {
//                    stringBuilder.append(line).append("\n");
//                }
//
//                // 在这里可以使用文件内容进行相应的操作
//                String fileContent = stringBuilder.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void traverseFolder(File folder) {
        File[] files = folder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // 如果是文件夹，递归遍历
                    traverseFolder(file);
                } else {
                    // 如果是文件，输出文件路径
                    Log.i(TAG, "traverseFolder: File: " + file.getAbsolutePath());
                    readFile(file);
                }
            }
        }
    }


//    private List<LayoutElementParcelable> listImages() {
//        final String[] projection = {MediaStore.Images.Media.DATA};
//        return listMediaCommon(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null);
//    }
//    private @Nullable List<LayoutElementParcelable> listMediaCommon(
//            Uri contentUri, @NonNull String[] projection, @Nullable String selection) {
//        final Context context = this.context.get();
//
//        if (context == null) {
//            cancel(true);
//            return null;
//        }
//
//        Cursor cursor =
//                context.getContentResolver().query(contentUri, projection, selection, null, null);
//
//        ArrayList<LayoutElementParcelable> retval = new ArrayList<>();
//        if (cursor == null) return retval;
//        else if (cursor.getCount() > 0 && cursor.moveToFirst()) {
//            do {
//                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA));
//                HybridFileParcelable strings = RootHelper.generateBaseFile(new File(path), showHiddenFiles);
//                if (strings != null) {
//                    LayoutElementParcelable parcelable = createListParcelables(strings);
//                    if (parcelable != null) retval.add(parcelable);
//                }
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//        return retval;
//    }
//    private @Nullable LayoutElementParcelable createListParcelables(HybridFileParcelable baseFile) {
//        if (dataUtils.isFileHidden(baseFile.getPath())) {
//            return null;
//        }
//
//        final MainFragment mainFragment = this.mainFragmentReference.get();
//        final Context context = this.context.get();
//
//        if (mainFragment == null || context == null) {
//            cancel(true);
//            return null;
//        }
//
//        String size = "";
//        long longSize = 0;
//
//        if (!baseFile.isDirectory()) {
//            if (baseFile.getSize() != -1) {
//                try {
//                    longSize = baseFile.getSize();
//                    size = Formatter.formatFileSize(context, longSize);
//                } catch (NumberFormatException e) {
//                    LOG.warn("failed to create list parcelables", e);
//                }
//            }
//        }
//
//        LayoutElementParcelable layoutElement =
//                new LayoutElementParcelable(
//                        context,
//                        baseFile.getName(context),
//                        baseFile.getPath(),
//                        baseFile.getPermission(),
//                        baseFile.getLink(),
//                        size,
//                        longSize,
//                        false,
//                        baseFile.getDate() + "",
//                        baseFile.isDirectory(),
//                        showThumbs,
//                        baseFile.getMode());
//        return layoutElement;
//    }

    public interface OnPhotoListener{
        public  void onFinish(List<MediaBean> list);
    }
    /**
     * 读取手机中所有图片信息
     */
    public static void  getAllPhotoInfo(Context ctx,OnPhotoListener cb) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<MediaBean> allPhotosTemp = new ArrayList<>();//所有照片
                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                String[] projImage = {MediaStore.Images.Media._ID
                        , MediaStore.Images.Media.DATA
                        , MediaStore.Images.Media.SIZE
                        , MediaStore.Images.Media.DISPLAY_NAME};
                String[] selectionArgs = null;
                String selection = null;
//                String[] selectionArgs = {"image/jpeg", "image/png"};
//                String selection = MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?";
                Cursor mCursor = ctx.getContentResolver().query(mImageUri,
                        projImage,
                        selection,
                        selectionArgs,
                        MediaStore.Images.Media.DATE_MODIFIED+" desc");

                if(mCursor!=null){
                    while (mCursor.moveToNext()) {
                        // 获取图片的路径
                        @SuppressLint("Range") String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                        @SuppressLint("Range") int size = mCursor.getInt(mCursor.getColumnIndex(MediaStore.Images.Media.SIZE))/1024;
                        @SuppressLint("Range") String displayName = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
                        // 获取该图片的父路径名
                        String dirPath = new File(path).getParentFile().getAbsolutePath();
                        //存储对应关系
                        allPhotosTemp.add(new MediaBean(MediaBean.Type.Image, path, size, displayName));
                    }
                    mCursor.close();
                }
                if (cb!=null){
                    cb.onFinish(allPhotosTemp);
                }
            }
        }).start();
    }
}
