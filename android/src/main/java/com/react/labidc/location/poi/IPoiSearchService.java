package com.react.labidc.location.poi;

import android.content.Context;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.poisearch.PoiResult;
import com.react.labidc.location.poi.model.PoiSearchModel;

/**
 * 先实现高德，以后扩展百度
 */
public interface IPoiSearchService<T> {

    T searchByKeyWord(Context context, PoiSearchModel poiSearchModel) throws AMapException;

}
