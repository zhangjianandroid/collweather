package cn.demo.chapter14.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cn.demo.chapter14.R;

/**
 *项目名称：WeatherActivity.java
 *类描述： 将 省市县级别的碎片 添加到 Activity活动中去
 *创建人：ZJ
 *创建时间：2019/5/17 16:45
 *修改时间：ZJ
 *修改备注：
 *@version
 */
public class WeatherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_activity);
    }
}
