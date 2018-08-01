
#import "LabidcLocation.h"

@implementation LabidcLocation
@synthesize search;
@synthesize callBack;
@synthesize locationManager;
bool hasListeners;

// 大于或等于IOS10的版本
- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}

RCT_EXPORT_MODULE()

// 导出方法, RN 通过这里和ios原生交互
RCT_EXPORT_METHOD(poiSearch:(NSString *)jsonModel callback: (RCTResponseSenderBlock)callback) {
    
    if (jsonModel == nil) {
        NSLog(@"请求数据不能为空");
        callback(@[@0, @"json数据不能为空"]);
        return;
    }
    
    NSData *jsonData = [jsonModel dataUsingEncoding:NSUTF8StringEncoding];
    NSError *error;
    NSDictionary *jsonDic = [NSJSONSerialization JSONObjectWithData:jsonData
                                                            options:NSJSONReadingMutableContainers
                                                              error:&error];
    if (error) {
        NSLog(@"出现错误:%@",error);
        callback(@[@1, @"json转换出现错误"]);
        return;
    }
    
    self.callBack = callback;
    // 调用方法执行
    [self poiSearch:jsonDic];
    // 调用js 回调方法
    // callback(@[@0, @"请求成功"]);
}

// 启动后台定位
// 需要设置对应权限，才能使用 https://lbs.amap.com/api/ios-location-sdk/guide/get-location/backgroundlocation
RCT_EXPORT_METHOD(start:(int)interval callback:(RCTResponseSenderBlock)callback ){
    if( self.locationManager == nil) {
        
        NSLog(@"====================================开始启动定位");
        @try {
            self.callBack = callback;
            NSString * aMapKey = [self getAMapKey];
            [AMapServices sharedServices].apiKey = aMapKey;
            self.locatingWithReGeocode = YES;
            self.locationManager = [[AMapLocationManager alloc] init];
            self.locationManager.delegate = self;
            // 设置定位最小更新距离方法如下，单位米。当两次定位距离满足设置的最小更新距离时，SDK会返回符合要求的定位结果。
            self.locationManager.distanceFilter=interval;
            
            //iOS 9（不包含iOS 9） 之前设置允许后台定位参数，保持不会被系统挂起
            [self.locationManager setPausesLocationUpdatesAutomatically:NO];
            
            //iOS 9（包含iOS 9）之后新特性：将允许出现这种场景，同一app中多个locationmanager：一些只能在前台定位，另一些可在后台定位，并可随时禁止其后台定位。
            if ([[[UIDevice currentDevice] systemVersion] floatValue] >= 9) {
                self.locationManager.allowsBackgroundLocationUpdates = YES;
            }
            //开始持续定位
            [self.locationManager startUpdatingLocation];
            self.callBack(@[@0, @"调用成功"]);
        }
        @catch (NSException *exception) {
            NSLog(@"%@", exception);
            self.callBack(@[@3, exception]);
        }
        
    }
}


// 获取高德key
-(NSString *) getAMapKey
{
    NSString *plistPath = [[NSBundle mainBundle] pathForResource:@"config" ofType:@"plist"];
    NSDictionary *labidcDic = [[NSDictionary alloc] initWithContentsOfFile:plistPath];
    NSString * aMapKey = labidcDic[@"AMapKey"];
    return aMapKey;
}

// 实际业务查询
- (NSString *) poiSearch:(NSDictionary *) jsonDic
{
    NSString * aMapKey = [self getAMapKey];
    [AMapServices sharedServices].apiKey = aMapKey;
    self.search = [[AMapSearchAPI alloc] init];
    self.search.delegate = self;
    
    AMapPOIKeywordsSearchRequest *request = [[AMapPOIKeywordsSearchRequest alloc] init];
    request.keywords            = jsonDic[@"keyWord"];
    request.types                = jsonDic[@"poiType"];
    request.city               = jsonDic[@"cityCode"];
    request.requireExtension    = YES;
    
    /*  搜索SDK 3.2.0 中新增加的功能，只搜索本城市的POI。*/
    request.cityLimit           = YES;
    request.requireSubPOIs      = YES;
    [self.search AMapPOIKeywordsSearch:request];
    NSLog(@"已经发送请求");
    NSLog(@"获取到的数据keyWord：%@",request.keywords);
    NSLog(@"获取到的数据city：%@",request.city);
    NSLog(@"获取到的数据types：%@",request.types);
    return @"";
}

// 因为直接使用了 AMapPOISearchBaseRequest 序列化，所以需要在 AMapPOISearchBaseRequest 对象上加入
/* POI 搜索回调. */
- (void)onPOISearchDone:(AMapPOISearchBaseRequest *)request response:(AMapPOISearchResponse *)response
{
    if (response.pois.count == 0)
    {
        self.callBack(@[@1,@"没有数据"]);
        return;
    }
    NSArray *dictArray = [AMapPOI mj_keyValuesArrayWithObjectArray: response.pois];
    NSLog(@"得到的数据%@", dictArray);
    self.callBack(@[@0,dictArray]);
}
// 查询接口发生错误
- (void)AMapSearchRequest:(id)request didFailWithError:(NSError *)error {
   
    if(error.code == 1008) {
       self.callBack(@[@1,@"高德key验证失败"]);
    }else{
       self.callBack(@[@2,@"其他错误"]);
    }
}
// 定位接口
- (void)amapLocationManager:(AMapLocationManager *)manager didUpdateLocation:(CLLocation *)location reGeocode:(AMapLocationReGeocode *)reGeocode
{
    
    if (hasListeners) {
        NSLog(@"location:{lat:%f; lon:%f; accuracy:%f}", location.coordinate.latitude, location.coordinate.longitude, location.horizontalAccuracy);
        NSString *retValue =  [NSString stringWithFormat:@" %f,%f",location.coordinate.latitude, location.coordinate.longitude];
        
        NSLog(@"================地理位置字符串%@", retValue);
        [self sendEventWithName:@"location" body:@{@"location":retValue}];
    }else
    {
         NSLog(@"================没有监听者");
    }
   
}

// 实现 RCTEventEmitter 类的方法
- (NSArray<NSString *> *)supportedEvents
{
    return @[@"location"];
}

// Will be called when this module's first listener is added.
-(void)startObserving {
    hasListeners = YES;
    // Set up any upstream listeners or background tasks as necessary
}

// Will be called when this module's last listener is removed, or on dealloc.
-(void)stopObserving {
    hasListeners = NO;
    // Remove upstream listeners, stop unnecessary background tasks
}

@end
  
