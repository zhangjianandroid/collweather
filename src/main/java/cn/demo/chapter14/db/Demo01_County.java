package cn.demo.chapter14.db;

import org.litepal.crud.DataSupport;

/**
 * 项目名称：2018_AndroidStudio_ FirstLineOfCode_No.2
 * 类描述：
 * 创建人：ZJ
 * 创建时间：2019/5/14 11:04
 * 修改时间：2019/5/14
 * 修改备注：
 *
 * @version 1.0
 */
public class Demo01_County extends DataSupport {
    private int id;
    private String countyName;
    private String weatherId;
    private  int cityId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
}
