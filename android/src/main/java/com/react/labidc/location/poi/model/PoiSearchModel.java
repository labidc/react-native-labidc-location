package com.react.labidc.location.poi.model;

import lombok.Data;

/**
 * 搜索实体
 */
@Data
public class PoiSearchModel {

    /**
     * 查询关键字
     */
    private String keyWord;

    /**
     * 搜索类型，该字段和keyWord两选一
     * 不设置POI的类别，默认返回“餐饮服务”、“商务住宅”、“生活服务”这三种类别的POI，
     * 下方提供了POI分类码表，请按照列表内容设置希望检索的POI类型。
     * （建议使用POI类型的代码进行检索）
     * 查看地址
     * https://lbs.amap.com/api/webservice/download
     */
    private String poiType;

    /**
     * 城市编码
     * 关键字未设置城市信息（默认为全国搜索）时，
     * 如果涉及多个城市数据返回，仅会返回建议城市，
     * 请根据APP需求，选取城市进行搜索。
     * 查看地址
     * https://lbs.amap.com/api/webservice/download
     */
    private String cityCode;


    /**
     * 纬度
     */
    private Double latitude;

    /**
     * 经度
     */
    private Double longitude;

    /**
     * 半径大小
     */
    private Integer bound;


    /**
     * 每页大小
     */
    private Integer pageSize;


    /**
     * 当前页码
     */
    private Integer currentPage;
}
