package com.tencent.demo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sphinxs.autombstone.AutombstoneActivity;
import com.sphinxs.autombstone.annotation.SafeUiData;
import com.tencent.demo.entity.Student;
import com.tencent.demo.entity.Teacher;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AutombstoneActivity {

    private TextView mTxt;
    private Button mBtn;

    @SafeUiData
    public String mStrTestValue;
    @SafeUiData
    public int mIntTestValue;
    @SafeUiData
    public Student mStudent;
    @SafeUiData
    public ArrayList<String> mStrList;
    @SafeUiData
    public ArrayList<Student> mStudentList;
    @SafeUiData
    public HashMap<String, Student> mMap;
    @SafeUiData
    public Teacher mTeacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTxt = findViewById(R.id.textview);
        mBtn = findViewById(R.id.button);

        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStrList = new ArrayList<>();
                mStrList.add("word");

                mStudent = new Student();
                mStudent.age = 12;
                mStudent.name = "sphinx";
                mStrTestValue = "hello";
                mIntTestValue = 1;

                mStudentList = new ArrayList<>();
                Student sx = new Student();
                sx.age = 123;
                sx.name = "fff";
                mStudentList.add(sx);

                mMap = new HashMap<>();
                Student sx2 = new Student();
                sx2.age = 333;
                sx2.name = "ggg";
                mMap.put("sss", sx2);

                mTxt.setText(mStrTestValue + mStudent.name + mStudent.age + mIntTestValue);

                mTeacher = new Teacher();
                mTeacher.name = "teacher";
                mTeacher.level = 1333;
            }
        });

        if (savedInstanceState != null) {
            mTxt.setText(mStrTestValue + mStudent.name + mStudent.age + mIntTestValue);
        }
    }

}
