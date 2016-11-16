package cn.edu.cqjtu.emailapp.dao;

import cn.edu.cqjtu.emailapp.exception.ResponseException;
import cn.edu.cqjtu.emailapp.exception.SendEmailFailedException;
import cn.edu.cqjtu.emailapp.exception.ServerConnectFailedException;
import cn.edu.cqjtu.emailapp.exception.StreamCloseException;

/**
 * 发送接口
 * Created by JohnNiang on 2016/11/13.
 */

public interface ISend {
    void send()  throws ServerConnectFailedException, StreamCloseException, SendEmailFailedException, ResponseException;
}
