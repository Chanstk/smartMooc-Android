package com.example.lenovo.smartMooc;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.example.lenovo.definedWidget.changeColorIconWithText;
import com.example.lenovo.fragment.fragment_my;
import com.example.lenovo.fragment.fragment_videoList;
import com.example.lenovo.login_signin.login;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private String tabs[] = {"课程", "我的"};//fragment标签
    private changeColorIconWithText to_Video, to_My;//底部自定义部件
    private TextView title;
    private android.support.v7.app.ActionBar actionBar;
    private DrawerLayout drawerLayout;//抽屉菜单
    private TextView math, allcourse, language, computer;//左上角列表目录表示文字
    private com.example.lenovo.fragment.fragment_my fragment_my;//个人中心fragment
    private boolean menuIsOpen, pos;//pos=true为显示课程列表fragment，false 为个人中心fragment，menuispoen判断抽屉菜单是否打开
    private fragment_videoList fragment_videoList;//课程列表fragment
    private AVUser user;//后端云服务user类
    private static final int REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        actionBar = getSupportActionBar();
        menuIsOpen = false;
        pos = true;
        user = AVUser.getCurrentUser();//获取当前磁盘缓存用户
        init();
    }

    private void init() {
        //初始化fragment
        fragment_my = new fragment_my();
        fragment_videoList = new fragment_videoList();
        addFragment(fragment_my, tabs[1]);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.hide(fragment_my).commit();
        addFragment(fragment_videoList, tabs[0]);

        actionBar.setTitle("全部课程");
        //底部部件初始化
        to_Video = (changeColorIconWithText) findViewById(R.id.toCourse);
        to_My = (changeColorIconWithText) findViewById(R.id.toMy);
        to_Video.setOnClickListener(this);
        to_My.setOnClickListener(this);
        to_Video.setIconAlpha(1.0f);
       /* switchFragmentSupport(R.id.fragment,tabs[0]);*/
        to_My.setIconAlpha(0f);
        title = (TextView) findViewById(R.id.title);
        //侧滑菜单
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        //抽屉菜单初始化
        math = (TextView) findViewById(R.id.math);
        math.setOnClickListener(this);
        allcourse = (TextView) findViewById(R.id.allCouse);
        allcourse.setOnClickListener(this);
        language = (TextView) findViewById(R.id.language);
        language.setOnClickListener(this);
        computer = (TextView) findViewById(R.id.computer);
        computer.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        fragment_videoList.listView.setSelection(0);
        switch (v.getId()) {
            //点击课程列表
            case R.id.toCourse:
                actionBar.setTitle("全部课程");
                to_Video.setIconAlpha(1.0f);
                pos = true;
                invalidateOptionsMenu();//actionbar右部标签变换
                tranFragment(fragment_my, fragment_videoList);//改变fragment
                to_My.setIconAlpha(0f);
                break;
            //点击个人中心
            case R.id.toMy:
                to_My.setIconAlpha(1.0f);
                pos = false;
                invalidateOptionsMenu();
                tranFragment(fragment_videoList,fragment_my);
                actionBar.setTitle("我的课程");
                to_Video.setIconAlpha(0f);
                break;
            case R.id.allCouse:
                drawerLayout.closeDrawer(GravityCompat.END);
                fragment_videoList.changeList("allcourse");//改变课程列表内容（细分列表）
                actionBar.setTitle("全部课程");
                menuIsOpen = false;
                break;
            case R.id.math:
                drawerLayout.closeDrawer(GravityCompat.END);
                fragment_videoList.changeList("math");
                actionBar.setTitle("数学");
                menuIsOpen = false;
                break;
            case R.id.language:
                drawerLayout.closeDrawer(GravityCompat.END);
                fragment_videoList.changeList("language");
                actionBar.setTitle("编程语言");
                menuIsOpen = false;
                break;
            case R.id.computer:
                drawerLayout.closeDrawer(GravityCompat.END);
                fragment_videoList.changeList("computer");
                actionBar.setTitle("计算机原理");
                menuIsOpen = false;
                break;
        }
    }
    public void addFragment(Fragment fragment ,String tag){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(R.id.fragment, fragment, tag);
        ft.commit();
    }
    public void tranFragment(Fragment from, Fragment to){
        if(from.isHidden())
            return;
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.hide(from).show(to).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        if (pos == true) {
            menu.findItem(R.id.moreCourse).setVisible(true);
            menu.findItem(R.id.quit).setVisible(false);
        }
        else {
            menu.findItem(R.id.moreCourse).setVisible(false);
            menu.findItem(R.id.quit).setVisible(true);
        }
        //如果磁盘有缓存用户，actionbar右部文字显示退出登录，否则显示登陆
        if(user==null)
            menu.findItem(R.id.quit).setTitle("登录");
        else
            menu.findItem(R.id.quit).setTitle("退出登录");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.moreCourse:
                if (menuIsOpen == false) {
                    drawerLayout.openDrawer(GravityCompat.END);
                    menuIsOpen = true;
                } else {
                    drawerLayout.closeDrawer(GravityCompat.END);
                    menuIsOpen = false;
                }
                break;
            case R.id.quit:
                //如果本地有缓存用户，则按钮为退出登录，否则打开登陆界面
                if(user!=null){
                AlertDialog.Builder multiDia = new AlertDialog.Builder(this);
                multiDia.setTitle("确定退出登录？");
                multiDia.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                    }
                });
                multiDia.setNegativeButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        AVUser.logOut();
                        user = AVUser.getCurrentUser();
                        invalidateOptionsMenu();
                        fragment_my.onJudge();
                    }
                });
                multiDia.create().show();
                 }else{
                    Intent intent = new Intent(this, login.class);
                    startActivityForResult(intent, REQUEST);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST) {
            if(resultCode == -1) {
                //登陆成功后刷新个人fragment界面
                user = AVUser.getCurrentUser();
                invalidateOptionsMenu();
                fragment_my.name.setText(user.getString("nicheng"));
                fragment_my.init();
            }
        }
    }

    @Override
    protected void onDestroy() {
        Log.i("onDestroy","destroyed");
        super.onDestroy();
    }
}
