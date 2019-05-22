package cn.demo.chapter14.gson;

import com.google.gson.annotations.SerializedName;

/**
 * 项目名称：2018_AndroidStudio_ FirstLineOfCode_No.2
 * 类描述：基础类
 * 创建人：ZJ
 * 创建时间：2019/5/20 8:53
 * 修改时间：2019/5/20
 * 修改备注：
 *
 * @version 1.0
 */
public class Basic {
    /***
     * 注意：由于JSON中一些字段可能不太适合直接作为Java字段
     * 来命名，因此这里使用 @SerializedName 注解的方式让 JSON字
     * 段和 Java字段之间建立映射关系
     */
//    城市名称
    @SerializedName("city")
    public String cityName;

//    城市的ID
    @SerializedName("id")
    public String weatherId;

//  更新的时间类，因为basic中中update多个参数，所以要定义成类
    public Update update;

    public class Update{
//        更新的时间
        @SerializedName("loc")
        public String updateTime;
    }
}
