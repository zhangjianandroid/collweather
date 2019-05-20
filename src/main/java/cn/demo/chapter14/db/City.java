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
public class City extends DataSupport {
    private int id;
    private String cityName;
    private int cityCode;
    private int provinceId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }
}
