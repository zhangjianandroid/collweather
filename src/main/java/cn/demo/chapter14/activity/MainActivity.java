package cn.demo.chapter14.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cn.demo.chapter14.R;

/**
 *项目名称：MainActivity.java
 *类描述： 将 省市县级别的碎片 添加到 Activity活动中去
 *创建人：ZJ
 *创建时间：2019/5/17 16:45
 *修改时间：ZJ
 *修改备注：
 *@version
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        加入一个缓存数据的判断
        SharedPreferences prfs = PreferenceManager.getDefaultSharedPreferences(this);
//        先从 prfs读取缓存数据，如果不为null,就说明之前已经请求过天气数
//        据了，没必要再让用户再次选择城市，而是直接跳转到 天气界面
        if (prfs.getString("weather", null) != null){
            Intent intent = new Intent(this, WeatherActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
