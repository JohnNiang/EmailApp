package cn.edu.cqjtu.emailapp.entity;


import android.util.Base64;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import cn.edu.cqjtu.emailapp.dao.ISend;
import cn.edu.cqjtu.emailapp.dto.Dto;
import cn.edu.cqjtu.emailapp.exception.ResponseException;
import cn.edu.cqjtu.emailapp.exception.SendEmailFailedException;
import cn.edu.cqjtu.emailapp.exception.ServerConnectFailedException;
import cn.edu.cqjtu.emailapp.exception.StreamCloseException;


/**
 * 邮件发送
 * Created by JohnNiang on 2016/11/12.
 */

public class EmailSender extends Email implements ISend {

    private List<String> toMails;

    private String fromMail;

    private String subject;

    private String content;

    @Override
    public void send() throws ServerConnectFailedException, StreamCloseException, SendEmailFailedException, ResponseException {
        if (userName == null) {
            throw new RuntimeException("用户名不能为空");
        }
        if (passWord == null) {
            throw new RuntimeException("密码不能为空");
        }
        if (fromMail == null) {
            throw new RuntimeException("发送方不能为空");
        }
        if (toMails == null || toMails.size() == 0) {
            throw new RuntimeException("接收方不能为空");
        }
        if (content == null) {
            throw new RuntimeException("内容不能为空");
        }

        try {
            if (mSocket == null) {
                mSocket = new Socket(Dto.sSmtpServer, Dto.sSmtpPort);
                mSocket.setSoTimeout(3000);
            }
            in = mSocket.getInputStream();
            out = mSocket.getOutputStream();
        } catch (IOException ie) {
            throw new ServerConnectFailedException();
        }
        reader = new BufferedReader(new InputStreamReader(in));
        writer = new PrintWriter(out, true);

        showSmtpResponse();
        println("HELO " + Dto.sSmtpServer);
        showSmtpResponse();
        println("AUTH LOGIN");
        showSmtpResponse();
        println(Base64.encodeToString(userName.getBytes(), Base64.NO_WRAP));
        showSmtpResponse();
        println(Base64.encodeToString(passWord.getBytes(), Base64.NO_WRAP));
        showSmtpResponse();
        println("MAIL FROM:<" + fromMail + ">");
        showSmtpResponse();
        for (String mail : toMails) {
            println("RCPT TO: <" + mail + ">");
            showSmtpResponse();
        }
        println("DATA");
        if (subject != null) {
            println("Subject:" + subject);
        }
        println("From:" + fromMail);
        println("To:");
        for (String mail : toMails) {
            print(mail + ";");
        }
        println();
        println();
        println(content);
        println(".");
        showSmtpResponse();
        println("QUIT");
        showSmtpResponse();
        close();
    }

    public void userVerify() throws SendEmailFailedException, ResponseException, StreamCloseException, ServerConnectFailedException {
        if (userName == null) {
            throw new RuntimeException("用户名不能为空");
        }
        if (passWord == null) {
            throw new RuntimeException("密码不能为空");
        }

        try {
            if (mSocket == null) {
                mSocket = new Socket(Dto.sSmtpServer, Dto.sSmtpPort);
                mSocket.setSoTimeout(3000);
            }
            in = mSocket.getInputStream();
            out = mSocket.getOutputStream();
        } catch (IOException ie) {
            throw new ServerConnectFailedException();
        }
        reader = new BufferedReader(new InputStreamReader(in));
        writer = new PrintWriter(out, true);

        showSmtpResponse();
        println("HELO " + Dto.sSmtpServer);
        showSmtpResponse();
        println("AUTH LOGIN");
        showSmtpResponse();
        println(Base64.encodeToString(userName.getBytes(), Base64.NO_WRAP));
        showSmtpResponse();
        println(Base64.encodeToString(passWord.getBytes(), Base64.NO_WRAP));
        showSmtpResponse();
        println("QUIT");
        showSmtpResponse();
        close();
    }

    /**
     * 接收Smtp服务器的回复
     *
     * @throws ResponseException        响应异常
     * @throws SendEmailFailedException 发送邮件失败异常
     */
    public void showSmtpResponse() throws ResponseException, SendEmailFailedException {
        try {
            String line = reader.readLine();
            String number = line.substring(0, 3);
            int num = -1;
            try {
                num = Integer.parseInt(number);
            } catch (NumberFormatException nfe) {
                throw new RuntimeException(number + "转换为整型失败");
            }
            if (num == -1) {
                throw new ResponseException();
            } else if (num >= 400) {
                System.err.println(line);
                throw new SendEmailFailedException();
            }
            System.out.println(line);
        } catch (IOException e) {
            throw new ResponseException();
        }
    }

    /**
     * 添加接收方邮件
     *
     * @param mail 接收方邮件
     */
    public void addToMails(String mail) {
        if (toMails == null) {
            toMails = new ArrayList<>();
        }
        toMails.add(mail);
    }


    public String getFromMail() {
        return fromMail;
    }

    public void setFromMail(String fromMail) {
        this.fromMail = fromMail;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
