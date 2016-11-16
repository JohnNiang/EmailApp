package cn.edu.cqjtu.emailapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class EmailContentActivity extends AppCompatActivity {

    public static final String ACTION_NAME = "cn.edu.cqjtu.emailapp.EmailContentActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_content);
        TextView tvShowEmailContent = (TextView) findViewById(R.id.tvShowEmailContent);

        // 设置可返回按钮
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);

        // 获取数据
        Intent intent = getIntent();
        int no = intent.getIntExtra("email_no",0);
        String content = intent.getStringExtra("email_content");

        // 设置相关内容
        this.setTitle("第"+no+"封邮件");
        tvShowEmailContent.setText(content);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
