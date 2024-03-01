package com.csharp.interop;

import android.util.Log;

import java.util.Date;

public class HelloJNI {
    static {
        System.loadLibrary("hello-jni");
    }
    public static native String stringFromJNI(String str);

    private final static String TAG = "HelloJNI";

    public static void callByCSharp(){
        Log.d(TAG,"callByCSharp time:"+new Date());
    };
    public static String callByCSharp(String par){
        String msg = "java response: '" + par + "' at " + new Date();
        Log.d(TAG, msg);
        return msg;
    };

}
