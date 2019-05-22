package cn.demo.chapter14.gson;

import com.google.gson.annotations.SerializedName;

/**
 * 项目名称：2018_AndroidStudio_ FirstLineOfCode_No.2
 * 类描述：天气状态类
 * 创建人：ZJ
 * 创建时间：2019/5/20 9:00
 * 修改时间：2019/5/20
 * 修改备注：
 *
 * @version 1.0
 */
public class Now {
    @SerializedName("tmp")
//    温度
    public String temperature;
//    更多
    @SerializedName("cond")
    public More more;

//    信息
    public class More {
        @SerializedName("txt")
        public String info;
    }
}
