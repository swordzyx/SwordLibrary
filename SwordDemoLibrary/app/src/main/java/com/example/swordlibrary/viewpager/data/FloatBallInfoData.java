package com.example.swordlibrary.viewpager.data;

import org.json.JSONObject;

public class FloatBallInfoData {
  static final String KEY_RED_POINT = "red_point";
  static final String KEY_IDENTIFY = "identify";
  final WindowInfo windowInfo;
  
  
  public FloatBallInfoData(JSONObject jsonObject) {
    windowInfo = new WindowInfo();
    if (jsonObject == null) {
      return;
    }

    windowInfo.redPoint = jsonObject.optInt(KEY_RED_POINT, -1);
    windowInfo.identify = jsonObject.optInt(KEY_IDENTIFY, -1);
  }
  
  class WindowInfo {
    private int redPoint;
    private int identify;
  }
}
