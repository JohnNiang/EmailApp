package cn.edu.cqjtu.emailapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.edu.cqjtu.emailapp.entity.EmailSender;
import cn.edu.cqjtu.emailapp.exception.ResponseException;
import cn.edu.cqjtu.emailapp.exception.SendEmailFailedException;
import cn.edu.cqjtu.emailapp.exception.ServerConnectFailedException;
import cn.edu.cqjtu.emailapp.exception.StreamCloseException;
import cn.edu.cqjtu.emailapp.util.NetworkUtil;

public class SendEmailActivity extends AppCompatActivity {

    public static final String ACTION_NAME = "cn.edu.cqjtu.emailapp.SendEmailActivity";

    private EditText etReceiverEmail;
    private EditText etSubject;
    private EditText etContent;
    private TextView tvShowProgress;

    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_email);

        etReceiverEmail = (EditText) findViewById(R.id.etReceiverEmail);
        etSubject = (EditText) findViewById(R.id.etSubject);
        etContent = (EditText) findViewById(R.id.etContent);
        tvShowProgress = (TextView) findViewById(R.id.tvShowProgress);
        tvShowProgress.setText("当前网络部不可用");
        tvShowProgress.setVisibility(checkNetwork() ? View.GONE : View.VISIBLE);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");

        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.menu_send_email, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 发送邮件的异步任务
     */
    private class SendEmailTask extends AsyncTask<Void, Void, Boolean> {

        private EmailSender mSender;

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean flag = false;
            try {
                mSender.send();
                flag = true;
            } catch (ServerConnectFailedException e) {
                e.printStackTrace();
            } catch (StreamCloseException e) {
                e.printStackTrace();
            } catch (SendEmailFailedException e) {
                e.printStackTrace();
            } catch (ResponseException e) {
                e.printStackTrace();
            }
            return flag;
        }

        @Override
        protected void onPreExecute() {
            tvShowProgress.setVisibility(View.VISIBLE);
            tvShowProgress.setText("发送中...");
            if (mSender == null) {
                mSender = new EmailSender();
            }
            mSender.setUserName(username);
            mSender.setPassWord(password);
            mSender.setContent(etContent.getText().toString());
            mSender.setSubject(etSubject.getText().toString());
            mSender.setFromMail(username + "@163.com");
            mSender.addToMails(etReceiverEmail.getText().toString());
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean) {
                // 如果发送成功
                tvShowProgress.setText("发送成功");
                Toast.makeText(SendEmailActivity.this, "发送成功", Toast.LENGTH_LONG).show();
                finish();
            } else {
                tvShowProgress.setText("发送失败");
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                tvShowProgress.setVisibility(View.GONE);
                Toast.makeText(SendEmailActivity.this, "发送失败", Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean checkNetwork() {
        boolean flag = NetworkUtil.isNetworkAvalable(this);
        if (!flag) {
            Toast.makeText(this, "当前网络不可用", Toast.LENGTH_LONG).show();
        }
        return flag;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        } else if (id == R.id.sendEmail) {
            attempSend();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 检查输入的内容是否为有效的邮件的格式
     *
     * @param email 收入的邮件
     * @return true，是合法的邮件，否则不合法
     */
    private boolean isEmailValid(String email) {
        // 检查邮件的正确性的正则表达式
        String regex = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * 尝试发送邮件
     */
    private void attempSend() {
        etReceiverEmail.setError(null);
        etSubject.setError(null);
        etContent.setError(null);

        boolean cancel = false;
        View focusView = null;

        String email = etReceiverEmail.getText().toString().trim();
        String content = etContent.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            etReceiverEmail.setError("接受者不能为空");
            focusView = etReceiverEmail;
            cancel = true;
        }
        if (!isEmailValid(email)) {
            etReceiverEmail.setError("邮箱格式错误");
            focusView = etReceiverEmail;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            etContent.setError("内容不能为空");
            focusView = etContent;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        }else{
            if (NetworkUtil.isNetworkAvalable(this)) {
                // 如果网络可用则进行发送操作
                new SendEmailTask().execute();
            }
        }
    }
}
