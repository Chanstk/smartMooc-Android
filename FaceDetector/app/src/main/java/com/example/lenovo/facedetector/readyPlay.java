package com.example.lenovo.facedetector;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.lenovo.viewPager.courseDetail;
import com.example.lenovo.viewPager.discussion;
import com.example.lenovo.viewPager.fragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class readyPlay extends AppCompatActivity {
    private ViewPager viewPager;
    private List<Fragment> fragList = new ArrayList<Fragment>();
    private List<String> titleList = new ArrayList<String>();
    private FragmentPagerAdapter adapter;
    private courseDetail courseDetail;
    private discussion discuss;
    private String title, detail_teacher, teacher, detail_course, url;
    private Intent intent, play;
    private ImageButton back;
    private LinearLayout touchToPlay;
    private View left,right;
    private LinearLayout leftTouch,rightTouch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ready_play);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        intent = getIntent();
        init();
    }

    private void init() {
        //获取intent传值
        title = intent.getStringExtra("title");
        detail_course = intent.getStringExtra("detail_course");
        detail_teacher = intent.getStringExtra("detail_teacher");
        teacher = intent.getStringExtra("teacher");
        url = intent.getStringExtra("url");
        //向fragment传值
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("detail_course", detail_course);
        bundle.putString("detail_teacher", detail_teacher);
        bundle.putString("teacher", teacher);
        bundle.putString("url", url);

        viewPager = (ViewPager) findViewById(R.id.viewPager);

        courseDetail = new courseDetail();
        discuss = new discussion();
        courseDetail.setArguments(bundle);

        play = new Intent();
        play.setClass(this, StreamingPlay.class);
        play.putExtra("url", url);
        touchToPlay = (LinearLayout) findViewById(R.id.touchToPlay);
        touchToPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(play);
            }
        });

        fragList.add(courseDetail);
        fragList.add(discuss);
        titleList.add("课程详情");
        titleList.add("课程讨论");
        adapter = new fragmentPagerAdapter(getSupportFragmentManager(), fragList, titleList);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    left.setBackgroundColor(Color.parseColor("#0066B3"));
                    right.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
                if (position == 1) {
                    left.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    right.setBackgroundColor(Color.parseColor("#0066B3"));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        left = findViewById(R.id.left);
        right = findViewById(R.id.right);
        right.setBackgroundColor(Color.parseColor("#FFFFFF"));
        left.setBackgroundColor(Color.parseColor("#0066B3"));
        //点击左边viewpager转向左边，点右转右
        leftTouch = (LinearLayout) findViewById(R.id.leftTouch);
        leftTouch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
                left.setBackgroundColor(Color.parseColor("#0066B3"));
                right.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
        });
        rightTouch = (LinearLayout)findViewById(R.id.rightTouch);
        rightTouch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
                left.setBackgroundColor(Color.parseColor("#FFFFFF"));
                right.setBackgroundColor(Color.parseColor("#0066B3"));
            }
        });
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
