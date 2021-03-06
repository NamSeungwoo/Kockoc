package com.kocapplication.pixeleye.kockocapp.write.continuousWrite;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.detail.DetailPageData;
import com.kocapplication.pixeleye.kockocapp.main.BaseActivityWithoutNav;
import com.kocapplication.pixeleye.kockocapp.main.MainActivity;
import com.kocapplication.pixeleye.kockocapp.model.Courses;
import com.kocapplication.pixeleye.kockocapp.util.BasicValue;
import com.kocapplication.pixeleye.kockocapp.util.JsonParser;
import com.kocapplication.pixeleye.kockocapp.util.JspConn;
import com.kocapplication.pixeleye.kockocapp.write.course.CourseWriteRecyclerAdapter;
import com.kocapplication.pixeleye.kockocapp.write.newWrite.NewWriteActivity;

/**
 * Created by hp on 2016-07-05.
 */
public class CourseSelectActivity extends BaseActivityWithoutNav {
    private final static String TAG = "CourseSelectActivity";

    public final static int CONTINUOUS_FLAG = 387;
    public final static int DEFAULT_FLAG = 88771;
    private int flag;
    int boardNo = 0;

    private View containView;
    private RecyclerView recyclerView;
    private CourseWriteRecyclerAdapter adapter;
    private Courses courses;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        actionBarTitleSet("코스 선택", Color.WHITE);

        container.setLayoutResource(R.layout.activity_course_select);
        containView = container.inflate();

        getComponent();
    }

    private void getComponent() {
        //코스 데이터 받아옴
        flag = getIntent().getIntExtra("FLAG", DEFAULT_FLAG);
        courses = (Courses) getIntent().getSerializableExtra("courses");

        recyclerView = (RecyclerView) containView.findViewById(R.id.recycler_layout_course_select);
        adapter = new CourseWriteRecyclerAdapter(courses.getCourses(), this, new ContinuousItemClickListener(),"CourseSelect");
        recyclerView.setAdapter(adapter);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        manager.scrollToPosition(0);
        recyclerView.setLayoutManager(manager);

        recyclerView.setHasFixedSize(true);
    }

    private class ContinuousItemClickListener implements View.OnClickListener {
        Intent intent = new Intent(CourseSelectActivity.this, NewWriteActivity.class);
        @Override
        public void onClick(View v) {
            int position = recyclerView.getChildLayoutPosition(v);

            try {
                boardNo = Integer.parseInt(JspConn.getBoardNoForEdit(courses.getCourseNo(),courses.getCourses().get(position).getTitle()));
            } catch (NumberFormatException e) {e.printStackTrace();}
            Log.e(TAG,"boardNo :"+boardNo);
            if (boardNo >0){ // 글이 있으면 이어쓰기 수정
                DetailPageData detailPageData;
                detailPageData = JsonParser.detailPageLoad(JspConn.loadDetailPage(String.valueOf(boardNo)));
                intent.putExtra("DATA",detailPageData);
                intent_newWrite(position,NewWriteActivity.CONTINUOUS_EDIT_FLAG);
            } else{ // 글이 없다면
                intent_newWrite(position,NewWriteActivity.CONTINUOUS_FLAG);
            }
        }
        void intent_newWrite(int position, int flag){

            intent.putExtra("FLAG", flag);
            intent.putExtra("COURSE_NO", courses.getCourseNo());
            intent.putExtra("COURSE_PO", (position + 1));
            intent.putExtra("COURSE_NAME",courses.getCourses().get(position).getTitle());
            Toast.makeText(CourseSelectActivity.this, ""+courses.getCourses().get(position).getTitle()+"선택", Toast.LENGTH_SHORT).show();
            startActivityForResult(intent, MainActivity.CONTINUOUS_WRITE_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case MainActivity.CONTINUOUS_WRITE_REQUEST_CODE:
                try {
                    Intent intent = new Intent();
                    intent.putExtra("boardNo",boardNo);
                    intent.putExtra("courseNo", courses.getCourseNo());
                    intent.putExtra("board_userNo", BasicValue.getInstance().getUserNo());
                    setResult(MainActivity.CONTINUOUS_WRITE_REQUEST_CODE, intent);
                    finish();
                } catch (NullPointerException e) {Log.d(TAG,"onActivityResult null");}
                break;
        }
    }
}
