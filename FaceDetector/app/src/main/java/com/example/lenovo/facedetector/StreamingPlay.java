package com.example.lenovo.facedetector;

/*
 * Copyright (C) 2013 yixia.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.MediaController;

import io.vov.vitamio.utils.Log;


public class StreamingPlay extends Activity implements MediaPlayer.OnErrorListener,MediaPlayer.OnPreparedListener{

    private android.widget.VideoView videoView;
    private String path;
    private Intent intent;
    private MediaController controller;
    private ProgressDialog progressDialog;
    private Uri url;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_streaming_play);
        intent = this.getIntent();
        init();
    }
    private void init(){
        path =intent.getStringExtra("url");
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("正在加载中...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        try {
            url = Uri.parse(path);
        }catch (Exception e){
            progressDialog.dismiss();
            AlertDialog.Builder multiDia = new AlertDialog.Builder(this);
            multiDia.setCancelable(false);
            multiDia.setTitle("视频无法播放");
            multiDia.setPositiveButton("退出", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    finish();
                }
            });
            multiDia.show();
        }
        videoView = (android.widget.VideoView) findViewById(R.id.streamingPlay);
        controller = new MediaController(this);
        controller.setMediaPlayer(videoView);
        controller.setKeepScreenOn(true);
        videoView.setVideoURI(url);
        videoView.setMediaController(controller);
        videoView.requestFocus();
        videoView.setOnErrorListener(this);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        finish();
        return true;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        videoView.start();
        progressDialog.cancel();
    }
}
