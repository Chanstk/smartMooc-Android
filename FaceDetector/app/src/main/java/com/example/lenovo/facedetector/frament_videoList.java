package com.example.lenovo.facedetector;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.example.lenovo.bean.videoBean;
import com.example.lenovo.definedWidget.ReflashListView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;


/**
 * A placeholder fragment containing a simple view.
 */
public class frament_videoList extends Fragment implements ReflashListView.reflashListener {
    private View view;
    private ReflashListView listView;
    private Drawable c, number, cccc, alg, java, compl, huibian;
    private List<videoBean> allcourse;
    private List<videoBean> math;
    private List<videoBean> language;
    private List<videoBean> computer;
    private listAdapter adapter;
    private String state = "null";
    private static final int MSG_INFO_LOADED = 1;
    private static final int IMAGE_INFO_LOADED = 2;
    private Intent intent;
    private int threadFlag;
    private Bitmap bitmap;
    //判断当前课程列表属于哪个分类
    public String getState() {
        String string = new String();
        if (state.equals("allcourse"))
            string = "全部课程";
        else if (state.equals("math"))
            string = "数学";
        else if (state.equals("computer"))
            string = "计算机原理";
        else if (state.equals("language"))
            string = "编程语言";
        return string;
    }
    public frament_videoList() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.video_list, container, false);
        intent = new Intent();
        intent.setClass(getActivity(), readyPlay.class);
        init();
        return view;
    }

    private void init() {
        c = getResources().getDrawable(R.drawable.photo);
        cccc = getResources().getDrawable(R.drawable.cccc);
        compl = getResources().getDrawable(R.drawable.compl);
        huibian = getResources().getDrawable(R.drawable.huibian);
        alg = getResources().getDrawable(R.drawable.alg);
        number = getResources().getDrawable(R.drawable.number);
        java = getResources().getDrawable(R.drawable.java);
        state = "allcourse";
        //listView初始化
        listView = (ReflashListView) view.findViewById(R.id.videoList);
        listView.setInterface(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                videoBean bean = (videoBean) parent.getAdapter().getItem(position);
                intent.putExtra("url", bean.url);
                intent.putExtra("title", bean.title);
                intent.putExtra("teacher", bean.teacher);
                intent.putExtra("detail_course", bean.detail_course);
                intent.putExtra("detail_teacher", bean.detail_teacher);
                startActivity(intent);
            }
        });
        //获取课程数据
        listView.reflesh();
        getInfo("getCourse");
    }

    private void getInfo(String order) {
        if (order.equals("getCourse")) {
            allcourse = new ArrayList<videoBean>();
            math = new ArrayList<videoBean>();
            language = new ArrayList<videoBean>();
            computer = new ArrayList<videoBean>();
            AVQuery<AVObject> query = AVQuery.getQuery("courseData");
            query.findInBackground(new FindCallback<AVObject>() {
                @Override
                public void done(List<AVObject> list, AVException e) {
                    if (e == null) {
                        for (int i = 0; i < list.size(); i++) {
                            videoBean bean = new videoBean();
                            bean.detail_course = list.get(i).getString("detail_course");
                            bean.title = list.get(i).getString("title");
                            bean.teacher = list.get(i).getString("teacher");
                            bean.provider = list.get(i).getString("provider");
                            bean.numbers = list.get(i).getInt("numbers");
                            bean.money = list.get(i).getInt("money");
                            bean.detail_teacher = list.get(i).getString("detail_teacher");
                            bean.category = list.get(i).getString("category");
                            if(bean.title.equals("数字逻辑"))
                                bean.photo = number;
                            else if (bean.title.equals("面向对象程序设计"))
                                bean.photo = cccc;
                            else if (bean.title.equals("线性代数"))
                                bean.photo = alg;
                            else if (bean.title.equals("Java程序设计"))
                                bean.photo = java;
                            else if (bean.title.equals("c语言程序设计"))
                                bean.photo = c;
                            else if (bean.title.equals("汇编语言"))
                                bean.photo = huibian;
                            else if (bean.title.equals("编译原理"))
                                bean.photo = compl;
                            if (bean.category.equals("math"))
                                math.add(bean);
                            else if (bean.category.equals("computer"))
                                computer.add(bean);
                            else if (bean.category.equals("language"))
                                language.add(bean);
                            allcourse.add(bean);
                        }
                        if (state.equals("allcourse"))
                            adapter = new listAdapter(allcourse);
                        else if (state.equals("math"))
                            adapter = new listAdapter(math);
                        else if (state.equals("computer"))
                            adapter = new listAdapter(computer);
                        else if (state.equals("language"))
                            adapter = new listAdapter(language);
                        //通知获取数据完毕
                        Message msg = mUIHandler.obtainMessage(MSG_INFO_LOADED);
                        mUIHandler.sendMessage(msg);
                    }
                }
            });
        }
    }

    private Handler mUIHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_INFO_LOADED:
                    //刷新listview
                    listView.setAdapter(adapter);
                    listView.reflashComplete();
                    break;
            }
        }
    };
    //刷新listview并获取数据
    @Override
    public void onReflash() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                listView.reflesh();
                getInfo("getCourse");
            }
        }, 1000);
    }

    protected class listAdapter extends BaseAdapter {
        private List<videoBean> list;

        public listAdapter(List<videoBean> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
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
                view = getActivity().getLayoutInflater().inflate(R.layout.video_item, null);
                holder = new ViewHolder();
                holder.image = (ImageView) view.findViewById(R.id.photo);
                holder.title = (TextView) view.findViewById(R.id.title);
                holder.provider = (TextView) view.findViewById(R.id.provider);
                holder.numbers = (TextView) view.findViewById(R.id.numbers);
                holder.money = (TextView) view.findViewById(R.id.money);
                holder.fire = (ImageView) view.findViewById(R.id.fire);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            videoBean bean = list.get(position);
            if (bean != null) {
                if (bean.photo == null)
                    holder.image.setImageDrawable(c);
                else
                    holder.image.setImageDrawable(bean.photo);
                holder.title.setText(bean.title);
                holder.provider.setText(bean.provider);
                holder.numbers.setText(bean.numbers + " 人在观看");
                if (bean.money == 0) {
                    holder.money.setTextColor(Color.BLUE);
                    holder.money.setText("免费");
                } else {
                    holder.money.setTextColor(Color.RED);
                    holder.money.setText(bean.money + " 元");
                }
                if (bean.numbers > 500) {
                    holder.fire = (ImageView) view.findViewById(R.id.fire);
                    holder.fire.setVisibility(View.VISIBLE);
                }
            }
            return view;
        }
    }

    static class ViewHolder {
        ImageView image, fire;
        TextView title, provider, numbers, money;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
    //改变课程列表分类
    public void changeList(String order) {
        if (order.equals("allcourse"))
            state = "allcourse";
        else if (order.equals("math"))
            state = "math";
        else if (order.equals("language"))
            state = "language";
        else if (order.equals("computer"))
            state = "computer";
        listView.reflesh();//刷新效果显示
        onReflash();
    }

    /*private String getContent(String url) throws Exception {
        StringBuilder sb = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpParams httpParams = client.getParams();

        //设置网络超时参数
        HttpConnectionParams.setConnectionTimeout(httpParams, 6000);
        HttpConnectionParams.setSoTimeout(httpParams, 6000);
        try {
            HttpResponse response = client.execute(new HttpGet(url));
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        entity.getContent(), "UTF-8"), 8192);
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                reader.close();
            } else
                return null;
        } catch (Exception e) {

        }
        return sb.toString();
    }*/

    /* class thread extends Thread {
         public void run() {
             allcourse = new ArrayList<videoBean>();
             try {
                 StringBuffer sb = new StringBuffer();
                 String body = getContent("http://192.168.199.119:4567/hello");
                 JSONArray array = new JSONArray(body);
                 for (int i = 0; i < array.length(); i++) {
                     JSONObject obj = array.getJSONObject(i);
                     videoBean tm = new videoBean();
                     tm.detail_course = obj.getString("detail_course");
                     tm.detail_teacher = obj.getString("detail_teacher");
                     tm.money = obj.getInt("money");
                     tm.numbers = obj.getInt("numbers");
                     String photo = obj.getString("photo");
                     if (photo.equals("c"))
                         tm.photo = c;
                     else if (photo.equals("cccc"))
                         tm.photo = cccc;
                     else if (photo.equals("java"))
                         tm.photo = java;
                     else if (photo.equals("huibian"))
                         tm.photo = huibian;
                     else if (photo.equals("compl"))
                         tm.photo = compl;
                     else if (photo.equals("alg"))
                         tm.photo = alg;
                     else if (photo.equals("number"))
                         tm.photo = number;
                     else tm.photo = null;
                     tm.provider = obj.getString("provider");
                     tm.teacher = obj.getString("teacher");
                     tm.title = obj.getString("title");
                     tm.url = obj.getString("url");
                     allcourse.add(tm);
                 }
                 state = "allcourse";
                 Message msg = mUIHandler.obtainMessage(MSG_INFO_LOADED);
                 mUIHandler.sendMessage(msg);
             } catch (Exception e) {
                 // TODO: handle exception

             }

         };
     }*/
    public static Bitmap getImage(String url) {
        URL myFileUrl = null;
        Bitmap bitmap = null;
        try {
            myFileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
