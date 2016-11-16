package cn.edu.cqjtu.emailapp.util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by JohnNiang on 2016/11/15.
 */

public class NetworkUtil {
    private NetworkUtil() {

    }

    /**
     * 判断当前网络是否可用
     *
     * @return true，表示可用，否则不可用
     */
    public static boolean isNetworkAvalable(Activity activity) {
        Context context = activity.getApplicationContext();
        // 获取手机所有连接管理对象(包括wifi，net等连接的管理)
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        }
        NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
        if (networkInfos != null || networkInfos.length > 0) {
            for (int i = 0; i < networkInfos.length; i++) {
                System.out.println(i+"===状态==="+networkInfos[i].getState());
                System.out.println(i+"===类型==="+networkInfos[i].getType());
                if(networkInfos[i].getState() == NetworkInfo.State.CONNECTED){
                    return true;
                }
            }
        }
        return false;
    }
}
