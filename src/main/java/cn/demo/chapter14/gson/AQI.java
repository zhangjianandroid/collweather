package cn.demo.chapter14.gson;

/**
 * 项目名称：2018_AndroidStudio_ FirstLineOfCode_No.2
 * 类描述： 空气质量类
 * 创建人：ZJ
 * 创建时间：2019/5/20 8:58
 * 修改时间：2019/5/20
 * 修改备注：
 *
 * @version 1.0
 */
public class AQI {
    public  AQICity city;

//    城市的空气质量类，因为城市中有多个空气质量参数，如：aqi值, pm25值
    public class AQICity{

          public String aqi;

          public String pm25;
    }
}
