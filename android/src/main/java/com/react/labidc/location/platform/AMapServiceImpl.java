package com.react.labidc.location.platform;

import android.content.Context;
import android.widget.Toast;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.react.labidc.location.RNReactNativeLabidcLocationModule;
import com.react.labidc.location.tool.NetWorkUtil;

/**
 * 高德地图
 */
public class AMapServiceImpl implements IMapService {

    /**
     * 当前上下文
     */
    private  Context context;

    /**
     * AMapLocationClient类对象
     */
    private  AMapLocationClient mLocationClient = null;

    /**
     * 声明定位自定义回调监听器
     */
    private  AMapLocationListener mLocationListener = null;

    /**
     * AMapLocationClientOption定位模式设置对象
     */
    private  AMapLocationClientOption mLocationOption = null;

    /**
     * 定位刷新频率 默认10秒
     */
    private int interval = 10;

    /**
     * 时间单位刻度
     */
    private final int timeUnit = 1000;

    /**
     * RN交互模块对象
     */
    private  RNReactNativeLabidcLocationModule reactNativeLabidcLocationModule;

    /**
     * RN交互对象
     * @param reactNativeLabidcLocationModule
     */
    public AMapServiceImpl(RNReactNativeLabidcLocationModule reactNativeLabidcLocationModule)
    {
        this.reactNativeLabidcLocationModule = reactNativeLabidcLocationModule;
        //this.interval = interval;
    }

    @Override
    public  void start(int interval, Context context) {

        if(interval> 0){
            this.interval = interval;
        }
        this.context = context;

        /**
         * 判断网络是否打开
         */
        if (NetWorkUtil.isNetAvailable(this.context) == false) {
            Toast.makeText(this.context, "请打开网络连接", Toast.LENGTH_SHORT).show();
            return;
        } else {

            this.mLocationClient = new AMapLocationClient(this.context);

            //初始化AMapLocationClientOption对象
            this.mLocationOption = new AMapLocationClientOption();


            this.mLocationListener = new AMapLocationListenerImpl(this.context, reactNativeLabidcLocationModule);

            //设置定位场景，目前支持三种场景（签到、出行、运动，默认无场景）
            this.mLocationOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.Transport);
            if(null !=  this.mLocationClient){
                this.mLocationOption.setOnceLocation(false);
                // GPS 优先
                this.mLocationOption.setGpsFirst(true);
                //回调时间间隔
                this.mLocationOption.setInterval(this.timeUnit * this.interval);
                //this.mLocationOption.setDeviceModeDistanceFilter()
                //this.mLocationOption.setLastLocationLifeCycle()
                //是否返回地址
                this.mLocationOption.setNeedAddress(true);

                this.mLocationClient.setLocationOption(this.mLocationOption);


                //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
                //MainActivity.this.mLocationClient.stopLocation();
                //启动定位
                this.mLocationClient.startLocation();
            }

            //设置定位回调监听
            this.mLocationClient.setLocationListener(this.mLocationListener);

        }

    }

}
