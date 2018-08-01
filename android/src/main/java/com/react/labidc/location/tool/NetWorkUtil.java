package com.react.labidc.location.tool;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 网络检查工具
 */
public class NetWorkUtil {

    /**
     * 判断网络是否可用
     * @param context
     * @return
     */
    public static boolean isNetAvailable(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netWorkInfo = connManager.getActiveNetworkInfo();
        if(netWorkInfo==null || !netWorkInfo.isAvailable()){
            return false;
        }

        return true;
    }

    /**
     * 判断GPS是否可用
     * @param context
     * @return
     */
    public static boolean isGPSAvailable(Context context) {
        LocationManager locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean net = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if(gps || net){
            return true;
        }
        return false;
    }
}
