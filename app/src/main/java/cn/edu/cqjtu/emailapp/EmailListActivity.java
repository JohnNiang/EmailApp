package cn.edu.cqjtu.emailapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import cn.edu.cqjtu.emailapp.adapter.EmailListAdapter;
import cn.edu.cqjtu.emailapp.entity.EmailReceiver;
import cn.edu.cqjtu.emailapp.exception.ReceiveEmailFailedException;
import cn.edu.cqjtu.emailapp.exception.ResponseException;
import cn.edu.cqjtu.emailapp.exception.ServerConnectFailedException;
import cn.edu.cqjtu.emailapp.exception.StreamCloseException;
import cn.edu.cqjtu.emailapp.util.NetworkUtil;

public class EmailListActivity extends AppCompatActivity implements EmailListAdapter.OnClickCheckButtonListener {

    public static final String ACTION_NAME = "cn.edu.cqjtu.emailapp.EmailListActivity";

    private TextView tvState;

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        tvState = (TextView) findViewById(R.id.tvState);
        tvState.setText("加载中...");
        this.setTitle("邮件列表");

        // 获取Intent数据
        Intent intent = getIntent();
        final String username = intent.getStringExtra("username");
        final String password = intent.getStringExtra("password");

        // 设置可返回键
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);

        // 设置悬浮按钮的点击事件
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkNetwork()) {
                    Intent intent = new Intent(SendEmailActivity.ACTION_NAME);
                    intent.putExtra("username", username);
                    intent.putExtra("password", password);
                    startActivity(intent);
                } else {
                    Snackbar.make(view, "当前无网络连接", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                }
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.email_list);

        tvState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvState.setText("获取数据中...");
                if (checkNetwork()) {
                    // 利用异步操作进行获取数据
                    new LoadEmailInfoTask(username, password).execute();
                }
            }
        });
        checkNetwork();
        // 利用异步操作进行获取数据
        new LoadEmailInfoTask(username, password).execute();
    }

    public boolean checkNetwork() {
        boolean flag = NetworkUtil.isNetworkAvalable(this);
        if (!flag) {
            // 如果网络不可用
            Snackbar.make(tvState, "当前网络不可用", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            tvState.setText("当前网络不可用,请设置网络后点击刷新");
        }
        return flag;
    }

    @Override
    public void startEmailContentAty(int no, String content) {
        Intent intent = new Intent(EmailContentActivity.ACTION_NAME);
        intent.putExtra("email_no", no);
        intent.putExtra("email_content", content);
        startActivity(intent);
    }

    /**
     * 加载邮件信息的异步任务
     */
    private class LoadEmailInfoTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUsername;

        private final String mPassword;

        public LoadEmailInfoTask(String username, String password) {
            mUsername = username;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return false;
            }
            boolean flag = false;
            // TODO: 2016/11/14 完成接收邮件的操作
            EmailReceiver receiver = new EmailReceiver();
            receiver.setUserName(mUsername);
            receiver.setPassWord(mPassword);
            try {
                receiver.receive();
                flag = true;
            } catch (ServerConnectFailedException e) {
                e.printStackTrace();
            } catch (StreamCloseException e) {
                e.printStackTrace();
            } catch (ResponseException e) {
                e.printStackTrace();
            } catch (ReceiveEmailFailedException e) {
                e.printStackTrace();
            }
            return flag;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean && EmailReceiver.msgContent != null) {
                // 如果操作完成并且正常，则显示正常的内容
                tvState.setText("");
                tvState.setEnabled(false);
                EmailListAdapter adapter = new EmailListAdapter(EmailReceiver.msgContent, EmailListActivity.this);
                mRecyclerView.setAdapter(adapter);
            } else {
                // 如果操作失败，则进行提示
                tvState.setText("加载失败!点击刷新");
            }
            super.onPostExecute(aBoolean);
        }
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
