package com.example.xinzhe.coolweather4.util;

/**
 * Created by Xinzhe on 2015/10/12.
 */
public interface HttpRequestListener {
    void  onFinish(String response);
    void onError(Exception e);
}
