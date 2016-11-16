package cn.edu.cqjtu.emailapp.entity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import cn.edu.cqjtu.emailapp.dao.IReceive;
import cn.edu.cqjtu.emailapp.dto.Dto;
import cn.edu.cqjtu.emailapp.exception.ReceiveEmailFailedException;
import cn.edu.cqjtu.emailapp.exception.ResponseException;
import cn.edu.cqjtu.emailapp.exception.ServerConnectFailedException;
import cn.edu.cqjtu.emailapp.exception.StreamCloseException;

/**
 * 邮件接收
 * Created by JohnNiang on 2016/11/12.
 */

public class EmailReceiver extends Email implements IReceive {

    private int message_size;

    public  static Map<Integer, Integer> msgBaseInfo;

    public static Map<Integer, String> msgContent;

    /**
     * 接收邮件
     *
     * @throws ServerConnectFailedException
     * @throws StreamCloseException
     * @throws ResponseException
     * @throws ReceiveEmailFailedException
     */
    @Override
    public void receive() throws ServerConnectFailedException, StreamCloseException, ResponseException, ReceiveEmailFailedException {
        if (userName == null) {
            throw new RuntimeException("用户名不能为空");
        }
        if (passWord == null) {
            throw new RuntimeException("密码不能为空");
        }
        try {
            if (mSocket == null) {
                mSocket = new Socket(Dto.sPop3Server, Dto.sPop3Port);
                mSocket.setSoTimeout(3000);
            }
            in = mSocket.getInputStream();
            out = mSocket.getOutputStream();
        } catch (IOException e) {
            throw new ServerConnectFailedException();
        }
        reader = new BufferedReader(new InputStreamReader(in));
        writer = new PrintWriter(out, true);

        showAPop3Response();
        println("USER " + userName);
        showAPop3Response();
        println("PASS " + passWord);
        showAPop3Response();
        println("STAT");
        showAPop3Response();
        println("LIST");
        getMessageBaseInfo();
        message_size = msgBaseInfo.size();
        for (int i = 1; i <= message_size; i++) {
            println("RETR " + i);
            getMessageContent(i);
        }
        println("QUIT");
        showAPop3Response();
        try {
            close();
        } catch (StreamCloseException e) {
            throw new StreamCloseException();
        }
    }

    /**
     * 接收Pop3服务器的一行回复
     */
    public String showAPop3Response() throws ResponseException, ReceiveEmailFailedException {
        String line = null;
        try {
            line = reader.readLine();
            if (line.startsWith("-ERR")) {
                throw new ReceiveEmailFailedException();
            }
            System.out.println(line);
        } catch (IOException e) {
            throw new ResponseException();
        }
        return line;
    }


    /**
     * 当请求LIST时，进行调用
     */
    private void getMessageBaseInfo() throws ReceiveEmailFailedException, ResponseException {
        String response = showAPop3Response();
        if (msgBaseInfo == null) {
            msgBaseInfo = new HashMap<>();
        }
        while (response != null && !response.equals(".")) {
            response = showAPop3Response();
            String[] responses = response.split(" ");
            if (responses.length == 2) {
                try {
                    int no = Integer.parseInt(responses[0]);
                    int size = Integer.parseInt(responses[1]);
                    msgBaseInfo.put(no, size);
                } catch (NumberFormatException nfe) {
                    throw new RuntimeException("Email的序号和长度转换成整型出错!" + nfe.getMessage());
                }
            }
        }
    }

    /**
     * 根据索引获取邮件内容
     * @param no 索引
     * @throws ReceiveEmailFailedException
     * @throws ResponseException
     */
    private void getMessageContent(int no) throws ReceiveEmailFailedException, ResponseException {
        String response = showAPop3Response();
        if (msgContent == null) {
            msgContent = new HashMap<>();
        }
        StringBuilder sb = new StringBuilder();
        while (response != null && !response.equals(".")) {
            response = showAPop3Response();
            if (!response.equals(".")) {
                sb.append(response);
            }
        }
        msgContent.put(no, sb.toString());
    }

    /**
     * 根据索引获取邮件内容
     *
     * @param no 索引
     */
    public String getMessage(int no) {
        return msgContent.get(no);
    }

    public int getMessageSize() {
        return message_size;
    }
}
