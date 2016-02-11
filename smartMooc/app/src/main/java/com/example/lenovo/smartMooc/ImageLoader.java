package com.example.lenovo.smartMooc;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.LruCache;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by chanst on 16-2-10.
 */
public class ImageLoader {
    private ImageView mImageView;
    private String mUrl;
    private LruCache<String,Bitmap> mCache;//图片缓存
    public ImageLoader(){
        //获取最大内存
        int maxMemory = (int)Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory/4;
        mCache = new LruCache<String,Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                //获取新缓存的大小
                return value.getByteCount();
            }
        };
    }
    //增加新的图片缓存
    public void addBitmapToCache(String url,Bitmap bitmap){
        if(getBitmapFromCache(url)==null){
            mCache.put(url,bitmap);
        }
    }
    //从缓存里面获取图片
    public Bitmap getBitmapFromCache(String url){
        return mCache.get(url);
    }
    public void showImage(ImageView imageView,final String url){
        mImageView = imageView;
        mUrl = url;
        new Thread(){
            @Override
            public void run() {
                super.run();
                //从缓存中取出图片
                Bitmap bitmap =getBitmapFromCache(url);
                if(bitmap == null) {
                    //如果缓存中没有图片，就下载
                    bitmap = getImage(url);
                    //将内存中没有图片加入到缓存里
                    if(bitmap!=null)
                        addBitmapToCache(url,bitmap);
                }
                Message message = Message.obtain();
                message.obj = bitmap;
                handler.sendMessage(message);
            }
        }.start();
    }
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(mImageView.getTag().equals(mUrl)) {
                mImageView.setImageBitmap((Bitmap) msg.obj);
            }
        }
    };
    public static Bitmap getImage(String urlString) {
        Bitmap bitmap;
        InputStream is;
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            is = new BufferedInputStream(connection.getInputStream());
            bitmap = BitmapFactory.decodeStream(is);
            return bitmap;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
