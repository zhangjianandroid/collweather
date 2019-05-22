package cn.demo.chapter14.gson;

import com.google.gson.annotations.SerializedName;

/**
 * 项目名称：2018_AndroidStudio_ FirstLineOfCode_No.2
 * 类描述： 单天天气 的实体类
 * 创建人：ZJ
 * 创建时间：2019/5/20 9:05
 * 修改时间：2019/5/20
 * 修改备注：
 *
 * @version 1.0
 */
public class Forecast {
    public String date;

    @SerializedName("tmp")
    public Temperature temperature;

    @SerializedName("cond")
    public More more;

    public class Temperature{

        public String max;

        public String min;
    }

    public class More{
        @SerializedName("txt_d")
        public String info;
    }
}
