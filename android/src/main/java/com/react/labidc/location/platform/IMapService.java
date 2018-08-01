package com.react.labidc.location.platform;

import android.content.Context;

import com.react.labidc.location.RNReactNativeLabidcLocationModule;

/**
 * 地图服务
 */
public interface IMapService {
    /**
     * 启动定位服务,
     * @param interval 定位时间
     * @param context 当前上下文
     */
    void start(int interval, Context context);
}
