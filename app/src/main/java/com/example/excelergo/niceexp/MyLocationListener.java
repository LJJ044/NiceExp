package com.example.excelergo.niceexp;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;

public class MyLocationListener extends BDAbstractLocationListener {
    @Override
    public void onReceiveLocation(BDLocation location) {
        //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果

        String addr = location.getAddrStr();    //获取详细地址信息
        String country = location.getCountry();    //获取国家
        String province = location.getProvince();    //获取省份
        String city = location.getCity();    //获取城市
        String district = location.getDistrict();    //获取区县
        String street = location.getStreet();    //获取街道信息
        String adcode = location.getAdCode();    //获取adcode
        String town = location.getTown();    //获取乡镇信息
        if(city!=null) {
            MainActivity.tv_location.setText(city);
            Fragment4.city2=city.substring(0,city.length()-1);
            Fragment4.tv_location4.setText("位置：" + addr);
        }else {
            MainActivity.locationService.start();
            MainActivity.mLocationClient.start();
        }

    }
}
