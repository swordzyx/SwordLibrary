package com.example.swordlibrary.viewpager;


import androidx.annotation.NonNull;

import com.example.swordlibrary.viewpager.data.FloatBallInfoData;
import com.sword.Encryption;
import com.sword.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class FloatBallData {
  public static final String TAG = "FloatBallData";
  final String[] urls;
  final String[] titleStringArray;
  FloatBallInfoData floatBallInfoData;

  public static final FloatBallData floatBallData = new FloatBallData();

  private FloatBallData() {
    titleStringArray = new String[]{"账户", "客服", "实名", "切换"};
    String feedBackUrl = "a";
    String accountUrl = "http://192.168.18.82:12009/my?Yz0lN0IlMjJvcGVuX2lkJTIyJTNBJTIyNDQ0NzA3MzY3ODgwMjI4ODY0JTIyJTJDJTIyY2hhbm5lbF9pZCUyMiUzQSUyMm0xMDAxJTIyJTJDJTIyc3ViX2NoYW5uZWxfaWQlMjIlM0ElMjJtMjAyMjA3MjIwMiUyMiUyQyUyMnNlcnZlcl9pZCUyMiUzQSUyMjMxJTIyJTJDJTIycm9sZV9pZCUyMiUzQSUyMjExMjUxOTg4NTU1MjY1NTIzOSUyMiUyQyUyMnV1aWQlMjIlM0ElMjJmZmZmZmZmZi1lMTA1LTEyNjktMDAwMC0wMDAwNGY3MGFmNjglMjIlMkMlMjJhcHBfaWQlMjIlM0ElMjIzMCUyMiUyQyUyMnJvbGVfbmFtZSUyMiUzQSUyMiVFNSVBRSU4NyVFNiU5NiU4NyVFOCU4QiU5MSVFNSVCOCU4NiUyMiUyQyUyMnZpcCUyMiUzQSUyMjElMjIlMkMlMjJkZXZpY2UlMjIlM0ElMjIxJTIyJTJDJTIyYXBrVmVyc2lvbkRlc2MlMjIlM0ElMjIyLjUuMiUyMiUyQyUyMm9zVmVyc2lvbiUyMiUzQSUyMjEyJTIyJTJDJTIyaXAlMjIlM0ElMjIxOTIuMTY4LjIwLjEyNyUyMiUyQyUyMmdhbWVfaWQlMjIlM0ElMjIzMCUyMiUyQyUyMnpvbmVfaWQlMjIlM0ElMjIwOSUyMiU3RCZzPWQyMWEwOTMwNDBjOGE1YWM5MGUyOTU4ODcxYzljMWFiJnQ9MTY3MTQzMjc2Mg==";
    urls = new String[]{accountUrl, feedBackUrl, "file:///android_asset/webview.html", "about:blank"};
    
    initData();
  }


  public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
  private void initData() {
    OkHttpClient client = new OkHttpClient.Builder()
        .build();

    JSONObject jsonObject = new JSONObject();
    try {
      jsonObject.put("app_id", "3");
      jsonObject.put("time", System.currentTimeMillis() + "");
      jsonObject.put("os", "1");
      jsonObject.put("open_id", "441169649535549440");
      jsonObject.put("channel_id", "m1002");
      jsonObject.put("sub_channel_id", "1");
      jsonObject.put("server_id", "123");
      jsonObject.put("role_id", "kdkkdkdk");
      jsonObject.put("token", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOi8vMTAuMTkuNS4xODY6MTMwMDAvYXBpL3Rva2VuUmVmcmVzaCIsImlhdCI6MTYwOTM3OTQ5MiwiZXhwIjoxNjA5ODk4MzQyLCJuYmYiOjE2MDkzNzk5NDIsImp0aSI6IjZNNTJGU01xVEt3eFNaSkMiLCJzdWIiOjExMjg1LCJwcnYiOiJjOGVlMWZjODllNzc1ZWM0YzczODY2N2U1YmUxN2E1OTBiNmQ0MGZjIn0.ymT_1Wr8i3hPMhQQqW4mjiHdPIlWiEJilTi_bD636CI");
      jsonObject.put("sign", Encryption.md5(jsonObject.toString()));
    } catch (JSONException e) {
      e.printStackTrace();
    }


    Request request = new Request.Builder()
        .url("http://192.168.18.66:92/api/getInfoById")
        .post(RequestBody.create(jsonObject.toString(), JSON))
        .build();
    
    client.newCall(request).enqueue(new Callback() {
      @Override
      public void onFailure(@NonNull Call call, @NonNull IOException e) {
        LogUtil.error(TAG, "getInfoById 请求失败");
      }

      @Override
      public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
        ResponseBody responseBody = response.body();
        if (responseBody != null) {
          String bodyString = responseBody.string();
          LogUtil.debug(TAG, bodyString);

          try {
            JSONObject jsonObject = new JSONObject(bodyString);
            if (jsonObject.optInt("status") == 0) {
              floatBallInfoData = new FloatBallInfoData(jsonObject.optJSONObject("data"));
            }
          } catch (JSONException e) {
            e.printStackTrace();
          }
        } else {
          LogUtil.error(TAG, "getInfoById 响应内容为空");
        }
      }
    });
  }

}
