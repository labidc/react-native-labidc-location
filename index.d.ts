declare module "react-native-labidc-location" {
  export default class LabidcLocation {
    static start(interval: Number): void;
    static poiSearch(jsonModel: String, callBack: Function): void;
  }
}
