package cn.demo.chapter14.utils;

import android.text.TextUtils;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.demo.chapter14.db.City;
import cn.demo.chapter14.db.County;
import cn.demo.chapter14.db.Province;
import cn.demo.chapter14.gson.Weather;

/**
 * 项目名称：2018_AndroidStudio_ FirstLineOfCode_No.2
 * 类描述： 解析json数据类
 *              * 分别解析 省级，市级，县级 的JSON数据
 * 创建人：ZJ
 * 创建时间：2019/5/15 17:38
 * 修改时间：2019/5/15
 * 修改备注：
 *
 * @version 1.0
 */
public class Utility {
    /**
     * 解析和处理服务器返回的 省级数据
     *   具体说明：
     *      因为服务器中定义的是一个json数组，因此这里将服务器返回来的数据传入到
     *      一个JSONArray对象中，然后遍历这个JSONArray，从中取出每个元素都是一个
     *      JSONObject对象，而每个JSONObject对象中有包含id, name, version 这些数
     *      据，然后再使用 getString()方法将这些数据取出
     * @param response
     * @return
     */
    public static boolean handleProvinceResponse(String response){
//        1.0 判断要解析的 数组数据是否为空，如果不为空，就进行解析
        if ( ! TextUtils.isEmpty(response)) {
            try {
//            2.0 因为服务器返回来的是一个 json数组，所以要创建 一个 JSONArray数组对象，如这种格式：[{name:'哈哈',age:24},{name:'王五',age:26}]
                JSONArray allProvinces = new JSONArray(response);
//            3.0 遍了整个 JSONArray数组对象，从而拿到每个数组具体的 jsonObject对象，如这种格式：{name:'哈哈', age:34}
                for (int i = 0; i < allProvinces.length(); i++) {
//                4.0 通过 allProvinces .getJSONObject(i)方式获取到该数组对象中的每个jsonObject对象。
//                  （该对象可能是：一个数组/一个对象/一个字符串。而这里是 jsonObject对象）
                    JSONObject province_jsonObject = allProvinces.getJSONObject(i);
//                5.0 因为要将从服务器中返回来的数据 存储到 自定义Java Bean对象中，所以要 new出这个
//                    Demo01_Province实例化对象。
                    Province province = new Province();
//                6.0 通过 province_jsonObject 每个jsonObject对象，拿到每个对象所对应的 具体字段(参数：name)。
//                6.1 然后将从服务器中解析出来的 这个对象的具体name字段的值 设置给 Province 对象。
                    province.setProvinceName(province_jsonObject.getString("name"));
//                7.0 通过 provinceObject 每个jsonObject对象，拿到每个对象所对应的 具体字段(参数：id)。
//                7.1 然后将从服务器中解析出来的 这个对象的具体name字段的值 设置给 Province 对象。
                    province.setProvinceCode(province_jsonObject.getInt("id"));
//                8.0 然后在将获取到的每个字段 都保存到 province对象中，从而为在数据库中生成做准备。
                    province.save();
                }
                return true;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
           return false;
    }

    /***
     * 解析和处理服务器返回的 市级数据
     * @param response
     * @param provinceId
     * @return
     */
    public static boolean handleCityResponse(String response, int provinceId){
        if ( ! TextUtils.isEmpty(response)){
            try {
                JSONArray allCites = new JSONArray(response);
                for (int i = 0; i < allCites.length(); i++){
                    JSONObject citesObject = allCites.getJSONObject(i);
                    City city = new City();
                    city.setCityName(citesObject.getString("name"));
                    city.setCityCode(citesObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return false;
    }

    /***
     * 解析和处理服务器返回的 县级数据
     * @param response
     * @param cityId
     * @return
     */
    public static boolean handleCountyResponse(String response, int cityId){
        if ( ! TextUtils.isEmpty(response)){
            try {
                JSONArray allCountys = new JSONArray(response);
                for (int i=0; i<allCountys.length(); i++){
                    JSONObject countysObject = allCountys.getJSONObject(i);
                    County county = new County();
                    county.setCountyName(countysObject.getString("name"));
                    county.setWeatherId(countysObject.getString("weather_id"));
                    county.setCityId(cityId);
                    county.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /***
     * 用于 解析天气的JSON数据 的方法
     *      将返回来的JSON数据解析成Wather实体类
     * @param response
     * @return
     */
    public static Weather handleWeatherResponse(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather");
            String weatherContent = jsonArray.getJSONObject(0).toString();
            Weather weather = new Gson().fromJson(weatherContent, Weather.class);
            return weather;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
