package com.example.lenovo.smartMooc;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class faceTraining extends Activity implements SurfaceHolder.Callback,Camera.PreviewCallback {
    private boolean isPreview = false;
    private static final String tag="yan";
    private SurfaceView mPreviewSV = null; //预览SurfaceView
    private SurfaceHolder mySurfaceHolder = null;
    private Camera myCamera = null;
    private Camera.AutoFocusCallback myAutoFocusCallback = null;
    private TextView textView;
    private static  final int DETECTING = 1;
    private ImageView image;
    private Button button;
    SimpleDateFormat df = new SimpleDateFormat("ss:SSS");//时间测试显示
    private int count= 0;
    private int threadCount = 0;
    private int frame=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.face_training);
        //初始化SurfaceView
        mPreviewSV = (SurfaceView)findViewById(R.id.previewSV);
        mySurfaceHolder = mPreviewSV.getHolder();
        mySurfaceHolder.setFormat(PixelFormat.TRANSLUCENT);//translucent半透明 transparent透明
        mySurfaceHolder.addCallback(this);
        mySurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        textView = (TextView) findViewById(R.id.text);
        image = (ImageView) findViewById(R.id.image);
        //自动聚焦变量回调
        myAutoFocusCallback = new Camera.AutoFocusCallback() {
            public void onAutoFocus(boolean success, Camera camera) {
                // TODO Auto-generated method stub
                if(success)//success表示对焦成功
                {
                    Log.i(tag, "myAutoFocusCallback: success...");
                    //myCamera.setOneShotPreviewCallback(null);
                }
                else
                {
                    //未对焦成功
                    Log.i(tag, "myAutoFocusCallback: 失败了...");

                }
            }
        };
        button = (Button) findViewById(R.id.start);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myCamera.setPreviewCallback(faceTraining.this);
            }
        });
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
       /*if(null != mTask){
            switch(mTask.getStatus()){
                case RUNNING:
                    return;
                case PENDING:
                    mTask.cancel(false);
                    break;
                case FINISHED:
                    break;
            }
        }
        mTask = new backTask(data);
        mTask.execute((Void) null);*/
        Log.i("onPreviewFrameIsCalled", "" + frame++);
        if(count>50){
            myCamera.setPreviewCallback(null);
            AlertDialog.Builder multiDia = new AlertDialog.Builder(this);
            multiDia.setTitle("识别成功！点击返回");
            multiDia.setNegativeButton("确定", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    finish();
                }
            });
            multiDia.create().show();
        }else{
            new threadTask(data).start();
            threadCount++;
        }
    }
    private class threadTask extends Thread{
        private byte[] mData;
        threadTask(byte[] data){
            this.mData = data;
            Log.i("NumberOfThread",""+threadCount);
        }
        @Override
        public void run() {
            super.run();
            Camera.Size size = myCamera.getParameters().getPreviewSize(); //获取预览大小
            final int w = size.width;  //宽度
            final int h = size.height;
            final YuvImage image = new YuvImage(mData, ImageFormat.NV21, w, h, null);
            ByteArrayOutputStream os = new ByteArrayOutputStream(mData.length);
            image.compressToJpeg(new Rect(0, 0, w, h), 80, os);
            byte[] tmp = os.toByteArray();
            Bitmap bmp = BitmapFactory.decodeByteArray(tmp, 0, tmp.length);
            count++;
            Log.i("NumberOfFrame",""+count);
            //自己定义的实时分析预览帧视频的算法

            threadCount--;
        }
    }

    private Handler mUIHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DETECTING:
                    break;
            }
        }
    };
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        myCamera = Camera.open(1);
        try {
            myCamera.setPreviewDisplay(mySurfaceHolder);
            Log.i(tag, "SurfaceHolder.Callback: surfaceCreated!");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            if(null != myCamera){
                myCamera.release();
                myCamera = null;
            }
            e.printStackTrace();
        }
    }
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        initCamera();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.i(tag, "SurfaceHolder.Callback：Surface Destroyed");
        if(null != myCamera)
        {
            myCamera.setPreviewCallback(null);
            myCamera.stopPreview();
            isPreview = false;
            myCamera.release();
            myCamera = null;
        }
    }

    //初始化相机
    public void initCamera(){
        if(isPreview){
            myCamera.stopPreview();
        }
        if(null != myCamera){
            Camera.Parameters myParam = myCamera.getParameters();
            Point point = new Point();
            getWindowManager().getDefaultDisplay().getSize(point);
            myParam.setPreviewSize(point.y-200, point.x);//设置分辨率参数

            myCamera.setDisplayOrientation(90);
            myParam.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            myCamera.setParameters(myParam);
            myCamera.startPreview();
            myCamera.autoFocus(myAutoFocusCallback);
            isPreview = true;
        }
    }
    @Override
    public void onBackPressed()
    //无意中按返回键时要释放内存
    {
        // TODO Auto-generated method stub
        super.onBackPressed();
        faceTraining.this.finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.home:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
