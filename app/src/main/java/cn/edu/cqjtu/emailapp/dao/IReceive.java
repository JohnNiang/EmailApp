package cn.edu.cqjtu.emailapp.dao;

import cn.edu.cqjtu.emailapp.exception.ReceiveEmailFailedException;
import cn.edu.cqjtu.emailapp.exception.ResponseException;
import cn.edu.cqjtu.emailapp.exception.ServerConnectFailedException;
import cn.edu.cqjtu.emailapp.exception.StreamCloseException;

/**
 * 接收接口
 * Created by JohnNiang on 2016/11/13.
 */

public interface IReceive {
    void receive() throws ServerConnectFailedException, StreamCloseException, ResponseException, ReceiveEmailFailedException;
}
