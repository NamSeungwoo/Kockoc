package com.kocapplication.pixeleye.kockocapp.detail;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.kocapplication.pixeleye.kockocapp.R;
import com.kocapplication.pixeleye.kockocapp.main.BaseActivity;
import com.kocapplication.pixeleye.kockocapp.util.BasicValue;
import com.kocapplication.pixeleye.kockocapp.util.JspConn;

/**
 * Created by hp on 2016-06-20.
 */
public class DetailActivity extends AppCompatActivity {
    final static String TAG = "DetailActivity";
    DetailFragment detailFragment;

    private EditText comment_et;
    private Button commentSend_btn;
    private Button courseCopy_btn;
    private Button scrap_btn;
    private ImageButton back_btn;

    private int boardNo;
    private int courseNo;
    private int board_userNo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setTitle("상세 보기");

        getIntentValue();
        init();
        getFragmentManager().beginTransaction().replace(R.id.container,detailFragment).commit();
    }

    protected void init(){
        detailFragment = new DetailFragment(boardNo,courseNo);

        comment_et = (EditText) findViewById(R.id.edit_comment);
        commentSend_btn = (Button) findViewById(R.id.btn_send_comment);
        courseCopy_btn = (Button)findViewById(R.id.btn_detail_course_copy);
        scrap_btn = (Button)findViewById(R.id.btn_detail_interest);
        back_btn = (ImageButton)findViewById(R.id.btn_detail_back);

        commentSend_btn.setOnClickListener(new CommentSendListener());
        courseCopy_btn.setOnClickListener(new CourseCopyListener());
        scrap_btn.setOnClickListener(new ScrapListener());
        back_btn.setOnClickListener(new BackListener());
    }
    private void getIntentValue(){
        Intent intent = getIntent();
        boardNo = intent.getIntExtra("boardNo",0);
        courseNo = intent.getIntExtra("courseNo",0);
        board_userNo = intent.getIntExtra("board_userNo",0);
    }

    /**
     * CommentSendListener
     * 댓글 입력
     */
    private class CommentSendListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String commentString = comment_et.getText().toString();

            JspConn.WriteComment(commentString, boardNo, BasicValue.getInstance().getUserNo());
            JspConn.pushGcm(commentString+"|"+boardNo+"&"+courseNo, board_userNo); //gcm
            detailFragment.addComment();
            softKeyboardHide(comment_et);
        }
    }
    /**
     * CourseCopyListener
     * 코스 복사
     */
    private class CourseCopyListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
//            int index;
//            ArrayList<Courses> courseArr = (JsonParser.readCourseToCopy(JspConn.readCourseAll()));
//            for (index = 0; index < courseArr.size(); index++) {
//                if (courseArr.get(index).courseNo == mCourseNo) {
//                    break;
//                }
//            }
//            Courses courses = new Courses();
//            courses.courseArr = courseArr.get(index).courseArr;
//            courses.courseNo = courseArr.get(index).courseNo;
//            Intent intent = new Intent(getApplicationContext(), GetTitleActivity.class);
//            intent.putExtra("courses",courses);
//            intent.putExtra("courseArr",courseArr);
//            startActivity(intent);
//            finish();
        }
    }
    /**
     * CourseCopyListener
     * 관심글 등록
     */
    private class ScrapListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
//            JspConn.addScrap(mBoardNo);
//            Toast.makeText(v.getContext(), "관심글 등록되었습니다.", Toast.LENGTH_SHORT).show();
        }
    }
    private class BackListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            finish();
        }
    }

    /**
     * onCreateOptionsMenu
     * 글 작성자면 삭제,수정 타인이면 신고
     * @param menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO: 2016-06-21 userNo값 받기
//        if (Integer.parseInt(mDetailPageData.getUserNo()) == BasicValue.mUserNo)
//            getMenuInflater().inflate(R.menu.menu_detail_page, menu);
//        else
//            getMenuInflater().inflate(R.menu.menu_detail_page_report, menu);
//
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if(id == R.id.menu_delete)
//            openDeleteDialog();
//        else if(id == R.id.menu_edit)
//             TODO: 2016-06-21 수정 기능 구현 필요함
//                editBoard();
//             TODO: 2016-06-21 신고 기능 구현 필요함
//        else if (id == R.id.menu_report)
//            Toast.makeText(this, "너 고소", Toast.LENGTH_SHORT).show();

        return super.onOptionsItemSelected(item);
    }
    private void openDeleteDialog() {

        new AlertDialog.Builder(this)
                .setTitle(R.string.menu_delete_title)
                .setMessage(R.string.menu_delete_message)
                .setPositiveButton(R.string.menu_delete_ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialoginterface, int i) {
//                                JspConn.delete(mDetailPageArr.get(vp_detail_page.getCurrentItem()).getBoardNo(),BasicValue.mUserNo);
                                finish();
                            }
                        })
                .setNegativeButton(R.string.menu_delete_no,
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialoginterface, int i) {}
                        })
                .show();
    }
    //키보드 숨김
    protected void softKeyboardHide(EditText editText){
        InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mInputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        editText.setText("");
    }
}