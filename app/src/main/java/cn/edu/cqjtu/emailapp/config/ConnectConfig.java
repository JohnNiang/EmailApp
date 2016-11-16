package cn.edu.cqjtu.emailapp.config;

/**
 * Created by JohnNiang on 2016/11/12.
 */

public class ConnectConfig {

    public static String sSmtpServer = "smtp.163.com";

    public static int sSmtpPort = 25;

    public static String sPop3Server = "pop3.163.com";

    public static int sPop3Port = 110;

    //    static {
    //        // TODO: 2016/11/12 这里进行初始化数据
    //        Properties prop = new Properties();
    //        try {
    //            InputStream in = ObjectInputStream.GetField.class.getClassLoader().getResourceAsStream("config.txt");
    //            // 加载配置文件
    //            // String fileName = "asserts/config.txt";
    //            String fileName = "/assets/config.txt";
    //            prop.load(in);
    //            sSmtpServer = prop.getProperty("smtp_server");
    //            sSmtpPort = Integer.parseInt(prop.getProperty("smtp_port"));
    //            sPop3Server = prop.getProperty("pop3_server");
    //            sPop3Port = Integer.parseInt(prop.getProperty("pop3_port"));
    //        } catch (Exception e) {
    //            throw new RuntimeException("配置配置文件出错"+e.getMessage());
    //        }
    //    }

    private ConnectConfig() {
    }
}
