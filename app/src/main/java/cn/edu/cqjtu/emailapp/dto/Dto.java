package cn.edu.cqjtu.emailapp.dto;

import cn.edu.cqjtu.emailapp.config.ConnectConfig;

/**
 * Created by JohnNiang on 2016/11/12.
 */

public class Dto {

    public static final Dto dto = new Dto();

    public final static String sSmtpServer = ConnectConfig.sSmtpServer;

    public final static int sSmtpPort = ConnectConfig.sSmtpPort;

    public final static String sPop3Server = ConnectConfig.sPop3Server;

    public final static int sPop3Port = ConnectConfig.sPop3Port;

    private Dto() {
    }
}
