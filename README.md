
# 使用方法，
## 版本说明： 
### 高德 没有IDFA的基础库V1.5.3,3DMap_Lib_V6.3.0,Search_Lib_V6.1.1
### IOS 10以上
### 安卓 开发插件：gradle:3.1.3  包管理：gradle-4.4-all.zip


## 第一步安装包

`$ npm install react-native-labidc-location --save`

### link 自动加入安卓和IOS工程

`$ react-native link react-native-labidc-location`

## 安卓配置
1.  AndroidManifest.xml 配置文件 在 application 节点内添加，android:value="" 请填写高德的应用key。 
```javascript 
<meta-data android:name="com.amap.api.v2.apikey" android:value=""/>
<service android:name="com.amap.api.location.APSService"></service>
```



## 安卓RN代码使用
```javascript
import { DeviceEventEmitter} from 'react-native';
import LabidcLocation from 'react-native-labidc-location';
// poi 搜索
 let json = {};
    json.keyWord ="重庆小吃"; 
    json.poiType = ""; // 查看 https://lbs.amap.com/api/webservice/download 规则
    json.cityCode = "023";// 城市编码 https://lbs.amap.com/api/webservice/download 规则
    json.pageSize = 10;// 每页行数
    json.currentPage = 1; //当前页码

    json.latitude = 0; //该三个字段必须全部填写才会生效，Double类型 选填
    json.longitude = 0; // Double类型 选填
    json.bound = 100; // 选填


       // statusCode 返回0 表示正确，
    LabidcLocation.poiSearch(JSON.stringify(json), (statusCode, statusJson) => {
        console.warn(statusCode);
        console.warn(typeof (statusCode));
        console.warn(statusJson);
	});


    // 启动定位，参数表示多少秒推送一次到location事件
    LabidcLocation.start(10);
    // 设置监听location事件
    DeviceEventEmitter.addListener('location', (events) => {
        console.warn(events)
	});
	
	
```


## IOS 配置
1. 用Xcode打开工程文件，在左边资源管理器，找到Libraries展开->找到RNReactNativeLabidcLocation 工程 展开->找到Framework展开
->找到AMapLocationKit.framework 
2. 将AMapLocationKit.framework 拖到你的项目根目录下。
3. 在您的项目根目录下添加一个 Property List 文件类型的文件，取名config.plist,  然后在该文件，Root节点下添加一个
AMapKey 节点，数据类型String,值就是你在高德上申请的应用key
4. IOS 还需要单独配置一些权限，见该地址
  https://lbs.amap.com/api/ios-location-sdk/guide/get-location/backgroundlocation
 从 “第 4 步，配置后台定位” 开始看，其他都不用关心

## IOS RN代码使用
```javascript
import { NativeEventEmitter} from 'react-native';
import LabidcLocation from 'react-native-labidc-location';
// poi 搜索
 let json = {};
  json.keyWord = "重庆小吃";
  json.poiType = ""; // 查看 https://lbs.amap.com/api/webservice/download 规则
  json.cityCode = "023"; // 城市编码 https://lbs.amap.com/api/webservice/download 规则
   
   // statusCode 返回0 表示正确，
  LabidcLocation.poiSearch(JSON.stringify(json), (statusCode, statusJson) => {
    console.warn(statusCode);
    console.warn(typeof(statusCode));
    console.warn(statusJson);
  });

  // 启动与监听location 事件
  const labidcLocationEmitter = new NativeEventEmitter(LabidcLocation);
  const subscription = labidcLocationEmitter.addListener(
    'location',
    (reminder) => {
      console.warn(reminder.location)
    }
  );
   // IOS 启动和安卓不一样，这里填写的参数10，代表移动距离超过10米之后才发送数据到location 事件
   // statusCode 返回0 表示正确，
   LabidcLocation.start(10, (statusCode, statusJson) => {
      console.warn(statusCode);
      console.warn(typeof (statusCode));
      console.warn(statusJson);
  });

    //  别忘了取消订阅，通常在componentWillUnmount生命周期方法中实现。 
   subscription.remove();
```
