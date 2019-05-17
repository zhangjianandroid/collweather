package cn.demo.chapter14.utils;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * 项目名称：2018_AndroidStudio_ FirstLineOfCode_No.2
 * 类描述： 网络 连接服务器 交互类，这里使用 OkHttp网络框架
 * 创建人：ZJ
 * 创建时间：2019/5/15 17:33
 * 修改时间：2019/5/15
 * 修改备注：
 *
 * @version 1.0
 */
public class HttpUtils {
    public static void sendOkHttpRequest(String address, okhttp3.Callback callback){
//        1.0 创建 okhttp的实例
        OkHttpClient client = new OkHttpClient();
//        2.0 发送网络请求
        Request request = new Request.Builder().url(address).build();
//        3.0 并注册回调方法来处理服务器的响应
        client.newCall(request).enqueue(callback);
    }
}
