package com.kocapplication.pixeleye.kockocapp.write.course;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.main.BaseActivityWithoutNav;
import com.kocapplication.pixeleye.kockocapp.util.EnterListener;

/**
 * Created by Han_ on 2016-06-29.
 */
public class CourseTitleActivity extends BaseActivityWithoutNav {
    private EditText courseTitleInput;
    private Button confirm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
        actionBarTitleSet("코스", Color.WHITE);

        container.setLayoutResource(R.layout.activity_course_title);
        View containView = container.inflate();

        courseTitleInput = (EditText) containView.findViewById(R.id.course_title);
        confirm = (Button) containView.findViewById(R.id.confirm);

        courseTitleInput.setOnEditorActionListener(new EditTextEnterListener());
        confirm.setOnClickListener(new ButtonListener());
    }

    private class EditTextEnterListener extends EnterListener {
        @Override
        public void onEnter() {
            super.onEnter();
            confirm.performClick();
        }
    }

    private class ButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String courseTitle = courseTitleInput.getText().toString();

            if (courseTitle.isEmpty()) {
                Snackbar.make(courseTitleInput, "코스 제목을 입력해주세요.", Snackbar.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(CourseTitleActivity.this, CourseWriteActivity.class);
            intent.putExtra("COURSE_TITLE", courseTitle);
            startActivity(intent);
            finish();
        }
    }

}
