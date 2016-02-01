package com.example.lenovo.facedetector;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnPreparedListener;
import io.vov.vitamio.utils.Log;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.text.Layout;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.example.lenovo.LocUntil.LocUtil;
import com.example.lenovo.screenBean.ScreenBean;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class LocalPlay extends Activity implements View.OnClickListener {
    private String path = Environment.getExternalStorageDirectory()
            + "/Movies/周杰伦-爱你没差-十二新作.mp4";
    private int mVolume = -1;
    private int mMaxVolume;
    private float mBrightness = -1f;
    private int finNum = 0;
    private VideoView mVideoView;
    private ScreenBean screenBean;
    private GestureDetector gestDetector;
    private boolean controller_isOpen;
    private Button controller_Pause;
    private LinearLayout player_Controller;
    private SeekBar seekBar;
    private static final int SEEKBAR = 1;
    private android.os.Handler seekBarHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SEEKBAR:
                    seekBar.setProgress(0);
                    break;
            }
        }
    };
    final Message msg = seekBarHandler.obtainMessage(SEEKBAR);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIntent();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_local_play);
        if (!io.vov.vitamio.LibsChecker.checkVitamioLibs(this))
            return;
        init();
        if (savedInstanceState != null
                && savedInstanceState.getInt("currentposition") != 0) {
            mVideoView.seekTo(savedInstanceState.getInt("currentposition"));
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(500);
                        seekBarHandler.sendMessage(msg);
                    } catch (InterruptedException e) {
                        return;
                    }
                }
            }
        });
    }

    // 屏幕方向改变不刷新播放进度
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // 什么都不用写
        } else {
            // 什么都不用写
        }
    }


    private void init() {
        // TODO Auto-generated method stub
        controller_isOpen = true;
        mVideoView = (VideoView) findViewById(R.id.local_player);
        player_Controller = (LinearLayout) findViewById(R.id.controller);
        controller_Pause = (Button) player_Controller.findViewById(R.id.player_pause);
        controller_Pause.setOnClickListener(this);
        mMaxVolume = LocUtil.getMaxVolume(this);
        gestDetector = new GestureDetector(this, new SingleGestureListener());
        screenBean = LocUtil.getScreenPix(this);
        if (path == "")
            return;
        else {
            mVideoView.setVideoPath(path);/*
            mVideoView.setMediaController(new MediaController(this))*/
            ;
            mVideoView.requestFocus();
            mVideoView.setOnPreparedListener(new OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    // TODO Auto-generated method stub
                    mp.setPlaybackSpeed(1.0f);
                }
            });
        }
        //播放条初始化
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setMax(1000);
        seekBar.setProgress(0);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                long newposition = (mVideoView.getDuration() * progress) / 1000;
                mVideoView.seekTo(newposition);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    private class SingleGestureListener implements
            android.view.GestureDetector.OnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            // TODO Auto-generated method stu
            controller_toggle();
            return false;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            // TODO Auto-generated method stub

        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            // TODO Auto-generated method stub
            if (finNum == 2) {
                return false;
            }
            float moldX = e1.getX();
            float moldY = e1.getY();
            float y = e2.getY();
            if (moldX > screenBean.getsWidth() * 7 / 10.0)
                changeVolum((moldY - y) / screenBean.getsHeight());
            else if (moldX < screenBean.getsWidth() * 3.0 / 10.0)
                changeBrightness((moldY - y) / screenBean.getsHeight());
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {
            // TODO Auto-generated method stub

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            // TODO Auto-generated method stub
            return false;
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        finNum = event.getPointerCount();
        if (finNum == 1) {
            gestDetector.onTouchEvent(event);
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                    endGesture();
            }
        }
        return super.onTouchEvent(event);
    }

    private void endGesture() {
        // TODO Auto-generated method stub
        mVolume = -1;
        mBrightness = -1;
    }

    private void changeBrightness(float percent) {
        // TODO Auto-generated method stub
        if (mBrightness < 0) {
            mBrightness = getWindow().getAttributes().screenBrightness;
            if (mBrightness <= 0.00f)
                mBrightness = 0.50f;
            if (mBrightness < 0.01f)
                mBrightness = 0.01f;
        }
        WindowManager.LayoutParams lpa = getWindow().getAttributes();
        lpa.screenBrightness = mBrightness + percent;
        if (lpa.screenBrightness > 1.0f)
            lpa.screenBrightness = 1.0f;
        else if (lpa.screenBrightness < 0.01f)
            lpa.screenBrightness = 0.01f;
        getWindow().setAttributes(lpa);
    }

    private void changeVolum(float percent) {
        // TODO Auto-generated method stub
        if (mVolume == -1) {
            mVolume = LocUtil.getCurVolume(this);
            if (mVolume < 0)
                mVolume = 0;
        }
        int index = (int) (percent * mMaxVolume) + mVolume;
        if (index > mMaxVolume)
            index = mMaxVolume;
        else if (index < 0) {
            index = 0;
        }
        LocUtil.setCurVolume(this, index);
    }

    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        outState.putInt("currentposition", (int) mVideoView.getCurrentPosition() + 1);
        super.onSaveInstanceState(outState);
    }

    public void controller_toggle() {
        if (controller_isOpen == true) {
            controller_isOpen = false;
            player_Controller.setVisibility(View.GONE);
        } else {
            player_Controller.setVisibility(View.VISIBLE);
            controller_isOpen = true;
        }
    }

    public void play_toggle() {
        if (mVideoView.isPlaying()) {
            mVideoView.pause();
        } else {
            mVideoView.start();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.player_pause:
                play_toggle();
                break;
        }
    }
}
