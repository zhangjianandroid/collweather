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
public class Province extends DataSupport {
    private int id;
    private String provinceName;
    private int provinceCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }



}
