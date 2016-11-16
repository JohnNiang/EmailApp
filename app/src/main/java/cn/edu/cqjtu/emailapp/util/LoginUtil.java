package cn.edu.cqjtu.emailapp.util;

import cn.edu.cqjtu.emailapp.entity.EmailSender;
import cn.edu.cqjtu.emailapp.exception.ResponseException;
import cn.edu.cqjtu.emailapp.exception.SendEmailFailedException;
import cn.edu.cqjtu.emailapp.exception.ServerConnectFailedException;
import cn.edu.cqjtu.emailapp.exception.StreamCloseException;

/**
 * Created by JohnNiang on 2016/11/14.
 */

public class LoginUtil {

    private LoginUtil(){

    }

    /**
     * 验证登陆是否成功
     * @param email 邮箱
     * @param password 密码
     * @return true，登陆成功，否则登录失败
     */
    public static boolean loginVarify(String email,String password){
        boolean flag = false;
        EmailSender sender = new EmailSender();
        int indexOfAt = email.indexOf("@");
        String username = email.substring(0,indexOfAt);
        System.out.println(username);

        sender.setUserName(username);
        sender.setPassWord(password);
        try {
            sender.userVerify();
            flag = true;
        } catch (SendEmailFailedException e) {
            e.printStackTrace();
        } catch (ResponseException e) {
            e.printStackTrace();
        } catch (StreamCloseException e) {
            flag = true;
            e.printStackTrace();
        } catch (ServerConnectFailedException e) {
            e.printStackTrace();
        }
        return flag;
    }

}
