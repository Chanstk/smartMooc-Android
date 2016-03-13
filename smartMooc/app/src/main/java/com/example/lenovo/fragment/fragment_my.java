package com.example.lenovo.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.example.lenovo.bean.myCourseBean;
import com.example.lenovo.faceTraining.CameraActivity;
import com.example.lenovo.smartMooc.R;
import com.example.lenovo.smartMooc.faceTraining;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class fragment_my extends Fragment {
    private View view;
    private ListView myCourseListView;
    private List<myCourseBean> myCourseList = new ArrayList<myCourseBean>();
    private listAdapter adapter;
    public TextView name;
    private Intent faceDetect;
    public static final int REQUEST =1;
    public AVUser user =null;
    private Button faceDectector;
    private Intent intent;
    public fragment_my() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.my, container, false);
        name = (TextView) view.findViewById(R.id.touchToLogin);
        myCourseListView = (ListView) view.findViewById(R.id.myCourseListView);
        onJudge();
        return view;
    }
    //判断本地磁盘是否有用户缓存，如果没有就引导注册，如果有获取用户历史记录
    public void onJudge(){
        user = AVUser.getCurrentUser();
        if(user != null) {
            name.setText(user.getString("nicheng"));
            init();
        }else {
            name.setText("请先登录");
            myCourseListView.setAdapter(null);
        }
    }
    public void init(){
        faceDetect = new Intent();
        faceDetect.setClass(getActivity(),CameraActivity.class);
        faceDectector = (Button) view.findViewById(R.id.faceDectector);
        faceDectector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(faceDetect);
            }
        });
        adapter = new listAdapter();
        Drawable number = getResources().getDrawable(R.drawable.number);
        Drawable cccc = getResources().getDrawable(R.drawable.cccc);
        Drawable compl = getResources().getDrawable(R.drawable.compl);
        Drawable huibian = getResources().getDrawable(R.drawable.huibian);
        myCourseList.add(new myCourseBean("数字逻辑", number, "rtsp://218.204.223.237:554/live/1/0547424F573B085C/gsfp90ef4k0a6iap.sdp", "浙江工商大学", 40));
        myCourseList.add(new myCourseBean("编译原理", compl, "rtsp://218.204.223.237:554/live/1/0547424F573B085C/gsfp90ef4k0a6iap.sdp", "浙江工商大学", 30));
        myCourseList.add(new myCourseBean("汇编语言", huibian, "rtsp://218.204.223.237:554/live/1/0547424F573B085C/gsfp90ef4k0a6iap.sdp", "浙江工商大学", 10));
        myCourseList.add(new myCourseBean("面向对象程序设计", cccc, "rtsp://218.204.223.237:554/live/1/0547424F573B085C/gsfp90ef4k0a6iap.sdp", "浙江工商大学", 80));
        myCourseListView.setAdapter(adapter);
        name.setClickable(false);
    }
    protected class listAdapter extends BaseAdapter {

        public listAdapter() {
        }

        @Override
        public int getCount() {
            return myCourseList.size();
        }

        @Override
        public Object getItem(int position) {
            return myCourseList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            ViewHolder holder;
            if (view == null) {
                view = getActivity().getLayoutInflater().inflate(R.layout.mycourse_item, null);
                holder = new ViewHolder();
                holder.title = (TextView) view.findViewById(R.id.title_my);
                holder.provider = (TextView) view.findViewById(R.id.provider_my);
                holder.photo = (ImageView) view.findViewById(R.id.photo_my);
                holder. progressBar = (ProgressBar) view.findViewById(R.id.progress);
                view.setTag(holder);
            }else{
                holder = (ViewHolder) view.getTag();
            }
            myCourseBean bean = myCourseList.get(position);
            if(bean!=null) {
                holder.title.setText(bean.title);
                holder.provider.setText(bean.provider);
                holder.photo.setImageDrawable(bean.photo);
                holder.progressBar.setProgress(bean.progress);
            }
            return view;
        }
    }
    static class ViewHolder{
        TextView title,provider;
        ImageView photo;
        ProgressBar progressBar;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST) {
            if(resultCode == -1) {
                //登陆成功刷新个人fragment界面
                user = AVUser.getCurrentUser();
                name.setText(user.getString("nicheng"));
                init();
            }
        }
    }
}
