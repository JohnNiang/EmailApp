package cn.edu.cqjtu.emailapp.entity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import cn.edu.cqjtu.emailapp.exception.StreamCloseException;

/**
 * Created by JohnNiang on 2016/11/13.
 */

public abstract class Email {
    protected String userName;

    protected String passWord;

    protected Socket mSocket;

    protected InputStream in = null;

    protected OutputStream out = null;

    protected BufferedReader reader;

    protected PrintWriter writer;


    /**
     * 关闭所有的流以及连接
     *
     * @throws StreamCloseException
     */
    protected void close() throws StreamCloseException {
        try {
            if (reader != null) {
                reader.close();
            }
            if (writer != null) {
                writer.close();
            }
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            if (mSocket != null) {
                mSocket.close();
            }
            reader = null;
            writer = null;
            in = null;
            out = null;
            mSocket = null;
        } catch (IOException e) {
            throw new StreamCloseException();
        }
    }

    /**
     * 向服务器发送命令(自动换行)
     *
     * @param msg 命令内容
     */
    protected void println(String msg) {
        System.out.println(msg);
        writer.println(msg);
    }

    /**
     * 向服务器发送命令
     */
    protected void println() {
        System.out.println();
        writer.println();
    }

    /**
     * 向服务器发送命令(不换行)
     *
     * @param msg 需要发送的命令
     */
    protected void print(String msg) {
        System.out.print(msg);
        writer.print(msg);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }
}
