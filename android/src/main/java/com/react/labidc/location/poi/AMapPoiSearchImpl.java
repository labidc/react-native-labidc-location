package com.react.labidc.location.poi;

import android.content.Context;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.react.labidc.location.poi.handle.AMapPoiSearchHandle;
import com.react.labidc.location.poi.model.PoiSearchModel;

/**
 * 高德poi查询实现
 */
public class AMapPoiSearchImpl implements IPoiSearchService<PoiResult> {

    /**
     * 查询关键字
     * @param context
     * @param poiSearchModel
     * @return
     * @throws AMapException
     */
    @Override
    public PoiResult searchByKeyWord(Context context, PoiSearchModel poiSearchModel) throws AMapException {
        PoiSearch.Query query = new PoiSearch.Query(
                poiSearchModel.getKeyWord(),
                poiSearchModel.getPoiType(),
                poiSearchModel.getCityCode());
        query.setPageSize(poiSearchModel.getPageSize());// 设置每页最多返回多少条poiitem
        query.setPageNum(poiSearchModel.getCurrentPage());//设置查询页码
        PoiSearch poiSearch = new PoiSearch(context, query);



        if( (poiSearchModel.getLatitude() != null && poiSearchModel.getLatitude() >0) &&
                (poiSearchModel.getLongitude() != null && poiSearchModel.getLongitude() >0) &&
                (poiSearchModel.getBound() != null && poiSearchModel.getBound() >0)) {
            poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(poiSearchModel.getLatitude(),
                    poiSearchModel.getLongitude()), poiSearchModel.getBound()));
        }

        // 异步返回监听返回
        // poiSearch.setOnPoiSearchListener(new AMapPoiSearchHandle());
        return poiSearch.searchPOI();
    }


}
