
#import <React/RCTBridgeModule.h>
#import <React/RCTEventEmitter.h>


#import <Foundation/Foundation.h>
#import <React/RCTLog.h>
#import <AMapFoundationKit/AMapFoundationKit.h>
#import <AMapSearchKit/AMapSearchKit.h>
#import "MJExtension.h"
#import <AMapLocationKit/AMapLocationKit.h>
#import <React/RCTEventEmitter.h>
// 高德后台定位说明
// https://lbs.amap.com/api/ios-location-sdk/guide/get-location/backgroundlocation


@interface LabidcLocation : RCTEventEmitter <RCTBridgeModule, AMapSearchDelegate, AMapLocationManagerDelegate>
{
    AMapSearchAPI *search;
    RCTResponseSenderBlock callBack;
    AMapLocationManager *locationManager;
}
@property (strong, nonatomic) AMapSearchAPI *search;
@property (strong, nonatomic) RCTResponseSenderBlock callBack;
@property (strong, nonatomic) AMapLocationManager *locationManager;
@property (nonatomic, assign) BOOL locatingWithReGeocode;
// 获取高德key
- (NSString *) getAMapKey;
- (NSString *) poiSearch:(NSDictionary *) jsonDic;
- (void)onPOISearchDone:(AMapPOISearchBaseRequest *)request response:(AMapPOISearchResponse *)response;
- (void)AMapSearchRequest:(id)request didFailWithError:(NSError *)error;
- (void)amapLocationManager:(AMapLocationManager *)manager didUpdateLocation:(CLLocation *)location reGeocode:(AMapLocationReGeocode *)reGeocode;
@end
  
