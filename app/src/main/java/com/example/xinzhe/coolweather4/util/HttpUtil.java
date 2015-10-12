package com.example.xinzhe.coolweather4.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Xinzhe on 2015/10/12.
 */
public class HttpUtil {
    public void sendHttpRequest(final String  address, final HttpRequestListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection httpURLConnection=null;
                try{
                    StringBuilder resoponse=new StringBuilder();
                    String temp;
                    URL url=new URL(address);
                    httpURLConnection=(HttpURLConnection)url.openConnection();
                    InputStream is=httpURLConnection.getInputStream();
                    BufferedReader br=new BufferedReader(new InputStreamReader(is));
                    while((temp=br.readLine())!=null){
                        resoponse.append(temp);
                    }
                    if (listener!=null){
                        listener.onFinish(resoponse.toString());
                    }
                }catch(Exception e){
                    if (listener!=null){
                        listener.onError(e);
                    }
                }finally {
                    if (httpURLConnection!=null){
                        httpURLConnection.disconnect();
                    }
                }

            }
        }).start();

    }
}
