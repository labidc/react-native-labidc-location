package com.react.labidc.location.platform;

import android.content.Context;
import android.provider.SyncStateContract;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.react.labidc.location.RNReactNativeLabidcLocationModule;

/**
 * 高德地图回调监听
 */
public class AMapLocationListenerImpl implements AMapLocationListener {

    /**
     * 当前上下文
     */
    private Context context;

    /**
     * React 上下文Java模块
     */
    private RNReactNativeLabidcLocationModule reactContextBaseJavaModule;

    /**
     * 构造函数
     * @param context
     * @param reactContextBaseJavaModule 自定义RN模块对象，为了传入处理方法
     */
    public AMapLocationListenerImpl(Context context, RNReactNativeLabidcLocationModule reactContextBaseJavaModule) {
        this.context = context;
        this.reactContextBaseJavaModule = reactContextBaseJavaModule;
    }

    /**
     * 地理位置变化监听函数
     * @param aMapLocation
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {

        if (aMapLocation != null) {

            if (aMapLocation.getErrorCode() == 0) {
                String adderss = "发送到前端。。。纬度："+aMapLocation.getLatitude()+",经度："+aMapLocation.getLongitude()+",地址信息："+aMapLocation.getAddress().toString();

                // Toast.makeText( this.context,adderss , Toast.LENGTH_SHORT).show();

                Log.e("LC",  adderss  );
                this.reactContextBaseJavaModule.sendEvent("location",aMapLocation.getLatitude() + "," + aMapLocation.getLongitude());

            }else {

                String error = "定位出现错误:错误代码："
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo();

                Log.e("LC",  error);
                this.reactContextBaseJavaModule.sendEvent("location","");

            }
        }
    }
}

