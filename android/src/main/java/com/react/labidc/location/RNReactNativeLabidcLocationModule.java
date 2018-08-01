
package com.react.labidc.location;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.Nullable;
import android.widget.Toast;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.amap.api.services.poisearch.PoiResult;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.react.labidc.location.platform.AMapServiceImpl;
import com.react.labidc.location.platform.IMapService;
import com.react.labidc.location.poi.AMapPoiSearchImpl;
import com.react.labidc.location.poi.IPoiSearchService;
import com.react.labidc.location.poi.model.PoiSearchModel;
import lombok.extern.java.Log;

/**
 * 位置服务
 */
@Log
public class RNReactNativeLabidcLocationModule extends ReactContextBaseJavaModule {



  /**
   * 当前上下文
   */
  private  ReactApplicationContext reactContext;

  /**
   * 静态对象
   */
  private IMapService iMapService = null;

  /**
   * 间隔时间，默认10秒
   */
  //public static Integer INTERVAL = 10;

  /**
   * 模块构造函数
   * @param reactContext
   */
  public RNReactNativeLabidcLocationModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;

    try {
      ApplicationInfo appInfo = this.reactContext.getPackageManager()
              .getApplicationInfo(this.reactContext.getPackageName(),
                      PackageManager.GET_META_DATA);

      //INTERVAL = appInfo.metaData.getInt("labidc.location.interval", INTERVAL);

    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
    //, INTERVAL
    this.iMapService = new AMapServiceImpl(this);
  }

  /**
   * 主动给RN前端js发送消息
   * @param eventName
   * @param data
   */
  public void sendEvent(String eventName,
                        @Nullable Object data) {
    this.reactContext
            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
            .emit(eventName, data);
  }

  /**
   * 模块名称
   * @return
   */
  @Override
  public String getName() {
    return "LabidcLocation";
  }


  /**
   * 启动定时推送到RN
   */
  @ReactMethod
  public void start(int interval) {

     /* LocationManager locManger = (LocationManager) getCurrentActivity().getSystemService(Context.LOCATION_SERVICE);
      if(locManger.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
          *//*提示用户打开允许获得地理位置信息选项*//*
         // Toast.makeText(getCurrentActivity(), "您未打开GPS定位，请打开GPS定位", Toast.LENGTH_SHORT).show();
      }*/

      this.iMapService.start(interval, getCurrentActivity());

  }


  /**
   * poi查询
   * @param jsonModel
   * @param callBack
   */
  @ReactMethod
  public void poiSearch(String jsonModel, Callback callBack) {
     /* LocationManager locManger = (LocationManager) getCurrentActivity().getSystemService(Context.LOCATION_SERVICE);
      if(locManger.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
          *//*提示用户打开允许获得地理位置信息选项*//*
         // Toast.makeText(getCurrentActivity(), "您未打开GPS定位，请打开GPS定位", Toast.LENGTH_SHORT).show();
      }
*/
      try {
          PoiSearchModel poiSearchModel = JSON.parseObject(jsonModel, new TypeReference<PoiSearchModel>() {
          });

          IPoiSearchService iPoiSearchService = new AMapPoiSearchImpl();
          PoiResult poiResult = (PoiResult) iPoiSearchService.searchByKeyWord(getCurrentActivity(), poiSearchModel);
          log.warning("返回结果"+poiResult.getPageCount());

          log.warning("======================="+ JSON.toJSONString(poiResult));
          callBack.invoke(0, JSON.toJSONString(poiResult));
      } catch (Exception ex) {
          callBack.invoke(1, "出现异常");
          ex.printStackTrace();
      }


  }
}