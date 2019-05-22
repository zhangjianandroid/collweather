package cn.demo.chapter14.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.IOException;

import cn.demo.chapter14.R;
import cn.demo.chapter14.gson.Forecast;
import cn.demo.chapter14.gson.Weather;
import cn.demo.chapter14.service.AutoUpdateService;
import cn.demo.chapter14.utils.HttpUtils;
import cn.demo.chapter14.utils.Utility;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity  {

    private TextView c14_title_city;
    private TextView c14_title_update_time;
    private TextView c14_degree_text;
    private TextView c14_weather_info_text;
    private LinearLayout c14_forecast_layout;
    private TextView c14_aqi_text;
    private TextView c14_pm25_text;
    private TextView c14_comfort_text;
    private TextView c14_car_wash_text;
    private TextView c14_sport_text;
    private ScrollView c14_weather_layout;
//    设置这个属性的修饰符为public，方便其他类使用该参数
    public SwipeRefreshLayout c14_swipe_refresh;

    private Button c14_nav_button;
//    设置这个属性的修饰符为public，方便其他类使用该参数
    public DrawerLayout c14_drawer_layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        initView();

        /**
         * 1.0 给切换城市的按钮 设置一个点击事件，当点击该事件的时候就打开左侧菜单
         */
        c14_nav_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c14_drawer_layout.openDrawer(GravityCompat.START);
            }
        });
        /***
         * &&& 1.0 添加下拉刷新功能，让其手动的更新天气
         */
//        &&& 1.0 设置 下拉刷新进度条的颜色，和主题的颜色一致
        c14_swipe_refresh.setColorSchemeResources(R.color.colorPrimary);


        /***
         * 先从 SharedPreference中将数据读取出来，第一次是肯定没有的，所以要先从
         * 从Intent中取出天气的ID。
         */
//        1.0 先获取到 SharedPreferences对象
        SharedPreferences prfs = PreferenceManager.getDefaultSharedPreferences(this);
//        2.0 再从 SharedPreferences对象中取出 缓存数据
        String weatherString = prfs.getString("weather", null);
//       &&& 2.0 设置一个ID，用于记录 被点击后具体县城城市天气的id
        final String weatherById;
//        2.1 如果 SharedPreferences对象中的 缓存数据不为空
        if (weatherString != null) {
//            2.2 就 直接拿到Utility中的 handleWeatherResponse("")方法 进行解析天气的JSON数据
            Weather weather = Utility.handleWeatherResponse(weatherString);
//            &&& 3.0 拿到天气的id, 用于从缓存对象中取出天气信息
            weatherById = weather.basic.weatherId;
//            2.3 将 天气读取出来，进行显示
            showWeatherInfo(weather);
            /***
             * 从Intent中取出天气的ID
             */
        } else {
//            &&& 4.0 拿到MainActivity意图跳转过来携带的数据
            weatherById = getIntent().getStringExtra("weather_id");
//            3.0 如果 SharedPreferences对象中没有缓存数据，就通过被跳转的 意图的方式拿到该天气的ID
            String weather_id = getIntent().getStringExtra("weather_id");
//            3.1 请求数据的时候，先将 ScrollView先隐藏起来，不然空数据的界面看起来很奇怪。
            c14_weather_layout.setVisibility(View.INVISIBLE);
//            3.2 联网请求 天气
            requestWeather(weather_id);
        }

//       &&& 给swipeRefreshlayout设置下拉监听器，当触发了下拉刷新操作的时候，就去回调 onRefresh方法。
//          【如果没有缓存就从服务器中查询】
        c14_swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//              因为缓存对象中没有，所以从 MainActivity意图跳转过来带的某个城市的id
                requestWeather(weatherById);
            }
        });
    }

    /***
     * 联网从服务器中查询最新的天气
     * 设置这个【方法】的修饰符为public，方便其他类使用该【方法】
     * @param weather_id
     */
    public void requestWeather(String weather_id) {
//                http://guolin.tech/api/weather?cityid=CN101190401&key=a748a94afe364109ae8254c6d0d7e3ee
        String weatherUrl = "http://guolin.tech/api/weather?cityid=" + weather_id + "&key=a748a94afe364109ae8254c6d0d7e3ee";
//        使用 Okhttp网络框架 发送网络请求，服务器会将相应城市的天气信息以JSON格式进行返回。
        HttpUtils.sendOkHttpRequest(weatherUrl, new Callback() {
            /***
             * 读取 服务器返回的 天气信息的json格式的 数据【失败】后的，回调方法。
             * @param call
             * @param e
             */
            @Override
            public void onFailure(Call call, IOException e) {
//                由子线程切换到主线程当中
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "从网络中 获取天气信息失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            /***
             * 读取 服务器返回的 天气信息的json格式的 数据【成功】后的，回调方法。
             * @param call
             * @param response
             * @throws IOException
             */
            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                接收从服务器中返回来的数据
                final String responseText = response.body().string();
//                将从服务器中返回来的数据 进行天气的解析。
//                先调用 下面的方法将返回的json数据转换成Weather对象。
                final Weather weather = Utility.handleWeatherResponse(responseText);
//                再将当前线程切换到主线程中去。
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        如果服务器返回来的status状态是ok，就表示请求天气成功。
                        if (weather != null && "ok".equals(weather.status)) {
//                            此时将返回来的数据，先存储到 SharedPreferences缓存对象中
                            SharedPreferences.Editor editor = PreferenceManager.
                                    getDefaultSharedPreferences(WeatherActivity.this)
                                    .edit();
                            editor.putString("weather", responseText);
                            editor.apply();
//                            并调用下面的方法将内容显示再界面上。
                            showWeatherInfo(weather);
                        } else {
                            Toast.makeText(WeatherActivity.this, "从 缓存对象中获取数据失败", Toast.LENGTH_SHORT).show();
                        }
//                        联网成功查询后，传入false，表示刷新事件结束，并隐藏刷新进度条。
                        c14_swipe_refresh.setRefreshing(false);
                    }
                });
            }
        });
    }

    /***
     * 显示天气信息的 方法（从缓存对象中进行查询）
     * @param weather
     */
    public void showWeatherInfo(Weather weather) {
//        从 Weather对象中获取数据，然后显示到相应的控件上。
//          城市的名字
            String cityName = weather.basic.cityName;
//        更新天气的时间，通过split进行截取
            String updateTime = weather.basic.update.updateTime.split(" ")[1];
//        温度
            String degree = weather.now.temperature + "°C";
//        天气信息
            String weatherInfo = weather.now.more.info;
            c14_title_city.setText(cityName);
            c14_title_update_time.setText(updateTime);
            c14_degree_text.setText(degree);
            c14_weather_info_text.setText(weatherInfo);

            c14_forecast_layout.removeAllViews();
//        注意在未来天气预报的部分我们使用一个for循环来处理每天的天气信息，
//        在循环中动态加载 forecast_item布局并设置相应的数据，然后添加到父布局中去，
            for (Forecast forecast : weather.forecastList) {
                View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, c14_forecast_layout, false);
                TextView dataText = view.findViewById(R.id.c14_date_text);
                TextView infoText = view.findViewById(R.id.c14_info_text);
                TextView maxText = view.findViewById(R.id.c14_max_text);
                TextView minText = view.findViewById(R.id.min_text);

                dataText.setText(forecast.date);
                infoText.setText(forecast.more.info);
                maxText.setText(forecast.temperature.max);
                minText.setText(forecast.temperature.min);
                c14_forecast_layout.addView(view);
            }

            if (weather.aqi != null) {
                c14_aqi_text.setText(weather.aqi.city.aqi);
                c14_pm25_text.setText(weather.aqi.city.pm25);
            }

            String comfort = "舒适度：\n" + weather.suggestion.comfort.info;
            String carWash = "洗车指数：\n" + weather.suggestion.carWash.info;
            String sport = "运动指数：\n" + weather.suggestion.sport.info;
            c14_comfort_text.setText(comfort);
            c14_car_wash_text.setText(carWash);
            c14_sport_text.setText(sport);
//      以上设置完成后，再将ScrollView重新变成可见的。
            c14_weather_layout.setVisibility(View.VISIBLE);
        if (weather != null && "ok".equals(weather.status)){
            Intent intent = new Intent(this, AutoUpdateService.class);
            startService(intent);
        }else{
            Toast.makeText(this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
        }

    }


    /***
     * 初始化控件
     */
    private void initView() {
        c14_title_city = (TextView) findViewById(R.id.c14_title_city);
        c14_title_update_time = (TextView) findViewById(R.id.c14_title_update_time);
        c14_degree_text = (TextView) findViewById(R.id.c14_degree_text);
        c14_weather_info_text = (TextView) findViewById(R.id.c14_weather_info_text);
        c14_forecast_layout = (LinearLayout) findViewById(R.id.c14_forecast_layout);
        c14_aqi_text = (TextView) findViewById(R.id.c14_aqi_text);
        c14_pm25_text = (TextView) findViewById(R.id.c14_pm25_text);
        c14_comfort_text = (TextView) findViewById(R.id.c14_comfort_text);
        c14_car_wash_text = (TextView) findViewById(R.id.c14_car_wash_text);
        c14_sport_text = (TextView) findViewById(R.id.c14_sport_text);
        c14_weather_layout = (ScrollView) findViewById(R.id.c14_weather_layout);
        c14_swipe_refresh = (SwipeRefreshLayout) findViewById(R.id.c14_swipe_refresh);

        c14_nav_button = (Button) findViewById(R.id.c14_nav_button);
        c14_drawer_layout = (DrawerLayout) findViewById(R.id.c14_drawer_layout);

    }



}
