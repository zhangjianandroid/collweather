package cn.demo.chapter14.gson;

import com.google.gson.annotations.SerializedName;

/**
 * 项目名称：2018_AndroidStudio_ FirstLineOfCode_No.2
 * 类描述： 生活建议类
 * 创建人：ZJ
 * 创建时间：2019/5/20 9:01
 * 修改时间：2019/5/20
 * 修改备注：
 *
 * @version 1.0
 */
public class Suggestion {
//    舒适度类，因为运动中有多个参数，所以要创建类中类
    @SerializedName("comf")
    public Comfort comfort;
//    洗车指数类，因为运动中有多个参数，所以要创建类中类
    @SerializedName("cw")
    public CarWash carWash;
//    运动类，因为运动中有多个参数，所以要创建类中类
    public Sport sport;

    public class Comfort{
        @SerializedName("txt")
        public String info;
    }

    public class CarWash{
        @SerializedName("txt")
        public String info;
    }

    public class Sport{
        @SerializedName("txt")
        public String info;
    }
}
