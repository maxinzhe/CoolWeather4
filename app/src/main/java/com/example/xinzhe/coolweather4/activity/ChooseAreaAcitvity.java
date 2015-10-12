package com.example.xinzhe.coolweather4.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.xinzhe.coolweather4.R;
import com.example.xinzhe.coolweather4.db.CoolWeatherDB;
import com.example.xinzhe.coolweather4.model.City;
import com.example.xinzhe.coolweather4.model.County;
import com.example.xinzhe.coolweather4.model.Province;
import com.example.xinzhe.coolweather4.util.HttpRequestListener;
import com.example.xinzhe.coolweather4.util.HttpUtil;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Created by Xinzhe on 2015/10/11.
 */
public class ChooseAreaAcitvity extends Activity {

    private ProgressDialog progressDialog;
    private   int currentLevel=0;
    private final static int LEVEL_PROVINCE=0;
    private final static int LEVEL_CITY=1;
    private final static int LEVEL_COUNTY=2;

    private CoolWeatherDB db=CoolWeatherDB.getInstance(this);
    private List<Province> provinceList=new ArrayList<Province>();
    private List<City> cityList=new ArrayList<City>();
    private List<County> countyList =new ArrayList<County>();

    private Province selectedProvince=new Province();
    private City selectedCity=new City();
    private County selectedCounty=new County();

    private List<String> dataList=new ArrayList<String>();
    private ArrayAdapter<String> arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_area);

       // db= CoolWeatherDB.getInstance(this);

        arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
        ListView listView=(ListView)findViewById(R.id.listView);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(currentLevel==LEVEL_PROVINCE){
                    //加载省级的数据，获取选中的省份，然后查询城市
                    selectedProvince=provinceList.get(position);
                    queryCities();
                }else if(currentLevel==LEVEL_CITY){
                    //加载市级数据，获取选中城市，然后查询县级
                    selectedCity=cityList.get(position);
                    queryCounties();
                }else if(currentLevel==LEVEL_COUNTY){

                }
            }
        });
        //如果没有按键事件，则直接加载省份列表
        queryProvince();
    }
    private void queryProvince(){
        //查询数据库填充provinceList，若没有则fromServer
        provinceList=db.loadProvinces();
        if(provinceList!=null&&provinceList.size()>0){
            dataList.clear();
            for(Province p:provinceList){
                dataList.add(p.getProvinceName());
            }
            arrayAdapter.notifyDataSetChanged();
            currentLevel=LEVEL_PROVINCE;
        }else{
            queryFromServer(null,"province");
        }
    }
    private void queryCities(){
        cityList=db.loadCities(selectedProvince.getId());
        if (cityList!=null&&cityList.size()>0){
            dataList.clear();
            for(City c:cityList){
                dataList.add(c.getCityName());
            }
            arrayAdapter.notifyDataSetChanged();
            currentLevel=LEVEL_CITY;
        }else{
            queryFromServer(selectedProvince.getProvinceCode(),"city");
        }

    }
    private void queryCounties(){
        countyList=db.loadCounty(selectedCity.getId());
        if(cityList!=null&&cityList.size()>0){
            dataList.clear();
            for (County c:countyList){
                dataList.add(c.getCountyName());
            }
            arrayAdapter.notifyDataSetChanged();
            currentLevel=LEVEL_COUNTY;
        }else{
            queryFromServer(selectedCity.getCityCode(),"city");
        }
    }
    private void queryFromServer(String code, final String type){
        startProgressDialog();
        String address;

        if(TextUtils.isEmpty(code)){//是空的证明是查询province
            address="www.weather.com.cn/data/list3/city.xml";
        }else{//是省或者市
            address="www.weather.com.cn/data/list3/city" +
                    code+
                    ".xml";
        }
        HttpUtil httpUtil=new HttpUtil();

        httpUtil.sendHttpRequest(address, new HttpRequestListener() {
            boolean result;
            @Override
            public void onFinish(String response) {
                if("province".equals(type)){
                    result=db.handleProvinceResponse(response);
                    //queryProvince();//改变UI的操作要在主线程
                }else if("city".equals(type)){
                    result=db.handleCityResponse(response);
                    //queryCities();
                }else if("county".equals(type)){
                    result=db.handleCityResponse(response);
                    //queryCounties();
                }
                if(result){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if("province".equals(type)){
                               //result=db.handleProvinceResponse(response);
                                queryProvince();//改变UI的操作要在主线程
                            }else if("city".equals(type)){
                                // result=db.handleCityResponse(response);
                                queryCities();
                            }else if("county".equals(type)){
                                // result=db.handleCityResponse(response);
                                queryCounties();
                            }
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(ChooseAreaAcitvity.this,"加载失败",Toast.LENGTH_SHORT);
                    }
                });
            }
        });


    }
    private void startProgressDialog(){
        if(progressDialog==null)
            progressDialog=new ProgressDialog(this);
            progressDialog.setMessage("正在加载");
            progressDialog.setCanceledOnTouchOutside(false);
    }
    private void closeProgressDialog(){
        if(progressDialog!=null){
            progressDialog.dismiss();//
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if(currentLevel==LEVEL_COUNTY){
            queryCities();
        }else if(currentLevel==LEVEL_CITY){
            queryProvince();
        }else{
            finish();
        }
    }
}//看看在不在