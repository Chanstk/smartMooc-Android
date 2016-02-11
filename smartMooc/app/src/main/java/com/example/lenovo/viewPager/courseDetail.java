package com.example.lenovo.viewPager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lenovo.smartMooc.R;

/**
 * Created by lenovo on 2015/11/5.
 */
public class courseDetail extends Fragment {
    private View view;
    private TextView teacher, detail_teacher, detail_course, detail_title;
    private String url;
    private Bundle bundle;

    public courseDetail() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.course_detail, container, false);
        bundle = getArguments();
        teacher = (TextView) view.findViewById(R.id.teacher);
        detail_course = (TextView) view.findViewById(R.id.detail_course);
        detail_teacher = (TextView) view.findViewById(R.id.detail_teacher);
        detail_title = (TextView) view.findViewById(R.id.detail_title);

        teacher.setText(bundle.getString("teacher"));
        detail_title.setText(bundle.getString("title"));
        detail_course.setText(bundle.getString("detail_course"));
        detail_teacher.setText(bundle.getString("detail_teacher"));
        url = bundle.getString("url");
        return view;
    }
}
