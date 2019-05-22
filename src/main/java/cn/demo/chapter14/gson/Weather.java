package cn.demo.chapter14.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 项目名称：2018_AndroidStudio_ FirstLineOfCode_No.2
 * 类描述：
 * 创建人：ZJ
 * 创建时间：2019/5/20 9:36
 * 修改时间：2019/5/20
 * 修改备注：
 *
 * @version 1.0
 */
public class Weather {
//    因为天气中还包含一项 status数据，成功返回OK值，失败则会返回具体的原因
    public String status;

    public Basic basic;

    public AQI aqi;

    public Now now;

    public Suggestion suggestion;

    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;
}
