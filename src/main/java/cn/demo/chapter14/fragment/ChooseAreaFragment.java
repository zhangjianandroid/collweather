package cn.demo.chapter14.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.demo.chapter14.R;
import cn.demo.chapter14.activity.MainActivity;
import cn.demo.chapter14.activity.WeatherActivity;
import cn.demo.chapter14.db.City;
import cn.demo.chapter14.db.County;
import cn.demo.chapter14.db.Province;
import cn.demo.chapter14.gson.Weather;
import cn.demo.chapter14.utils.HttpUtils;
import cn.demo.chapter14.utils.Utility;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 项目名称：2018_AndroidStudio_ FirstLineOfCode_No.2
 * 类描述：     省市县的 碎片
 * 创建人：ZJ
 * 创建时间：2019/5/15 18:14
 * 修改时间：2019/5/15
 * 修改备注：
 *
 * @version 1.0
 */
public class ChooseAreaFragment extends Fragment {
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;
    //    进度条
    private ProgressDialog progressDialog;

    private TextView c14_title_text;
    private Button c14_back_button;
    private ListView c14_list_view;

//  创建 ArrayAdapter适配器
    private ArrayAdapter adapter;
//    创建 List集合对象
    ArrayList dataList = new ArrayList<>();

//    省列表
    private List<Province> provinceList;
//    市列表
    private List<City> cityList;
//    县列表
    private List<County> countyList;
//    选中的省
    private Province selectedProvince;
//    选中的市
    private City selectedCity;
//    选中的县
    private County selectedCounty;
//    当前选中的级别
    private int currentLevel;


    private DrawerLayout c14_drawer_layout;
    /***
     * 当碎片创建视图（加载布局）的时候调用。
     * @param inflater 填充哪个布局文件
     * @param container 视图组
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//      !!! 1.0 填充布局
        View view = inflater.inflate(R.layout.choose_area, container, false);
//       !!! 2.0 调用 初始化 choose_area.xml文件中的控件，切记这里
//        使用的 view是choose_area返回的 view对象。
        c14_title_text = (TextView) view.findViewById(R.id.c14_title_text);
        c14_back_button = (Button) view.findViewById(R.id.c14_back_button);
        c14_list_view = (ListView) view.findViewById(R.id.c14_list_view);

//       !!! 3.0 创建 ArrayAdapter适配器
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, dataList);
//       !!! 4.0 ArrayAdapter适配器设置给 listview
        c14_list_view.setAdapter(adapter);
//       !!! 5.0 返回 choose_area.xml文件的view对象
        return view;
    }

    /***
     * 确保与碎片相关联的活动一定已经创建完毕的时候调用。
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        1.0 给 listview设置点击事件，当点击某个省的时候，会进入 ListView的 onItemClik方法。
//        这个时候要根据当前的级别来判断是去调用 市级 还是 县级。
        c14_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                如果当前是 PROVINCE省级别，就查询 position位置上的这个省级别下的所有 市级。
                if (currentLevel == LEVEL_PROVINCE){
//                    获取每个省级别的 position位置
                    selectedProvince = provinceList.get(position);
//                    查询当前 position省级下的 所有市级
                    queryCites();
//                    如果当前 是 CITY市级别，就查询 position位置上的这个省级别下的所有 县级。
                }else if (currentLevel == LEVEL_CITY){
//                    获取到每个市级的 position位置
                    selectedCity = cityList.get(position);
//                    查询当前 position市级下的 所有县级
                    queryCounties();
                }
//                如果当前级别是：COUNTY县城级别，就启动天气界面进行具体的天气查询
                else if(currentLevel == LEVEL_COUNTY){
//                    先从集合中获取每个县城具体的 id
                    String weatherId = countyList.get(position).getWeatherId();
                    /***
                     * 设置判断当前如果时 MainActivity界面的时候，就跳转到 WeatherActivity界面
                     */
                    if (getActivity() instanceof MainActivity){
//                    再通过意图跳转的方式显示具体的 县城城市的天气
                        Intent intent = new Intent(getActivity(), WeatherActivity.class);
//                    跳转时将该 weather_id具体的县城ID 作为参数传递到 WeatherActivity界面
                        intent.putExtra("weather_id", weatherId);
//                    进行跳转
                        startActivity(intent);
//                    成功跳转后，销毁当前活动。
                        getActivity().finish();
                    /***
                     * 如果当前是 WeatherActivity界面，只需要重新加载该界面，重新获取天气信息即可
                     */
                    }else if(getActivity() instanceof WeatherActivity){
                        WeatherActivity activity = (WeatherActivity) getActivity();
//                        切记：下面的 c14_drawer_layout，c14_swipe_refresh，requestWeather()修饰符
//                        必须公共的，才能在该类中被调用。
                        activity.c14_drawer_layout.closeDrawers();
                        activity.c14_swipe_refresh.setRefreshing(true);
                        activity.requestWeather(weatherId);
                    }

                }
            }
        });
//        返回 按钮的点击事件
       c14_back_button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
//                  如果当前 返回的按钮位于 县级别的窗口，点击返回按钮后，就会返回 市级别的列表
                  if (currentLevel == LEVEL_COUNTY){
//                      就查询 市级
                        queryCites();
//                   如果当前 返回的按钮位于 市级别的窗口，那么点击返回按钮后，就会返回 省级别的列表
                   }else if (currentLevel == LEVEL_CITY){
                       queryProvinces();
                   }
           }
       });
//       首先调用该方法后第一步就是先 加载所有的 省级别列表数据
        queryProvinces();
    }


    /***
     * 查询 全国所有的省
     * 先从数据库中查询，如果没有查询到再去服务器上进行查询
     */
    private void queryProvinces() {
//        设置标题
        c14_title_text.setText("中国");
//        设置 返回按钮，为隐藏状态
        c14_back_button.setVisibility(View.GONE);
//        先从数据库中进行读取
//        利用 DataSupport 的 API 的findAll()方法进行查询，传入的参数是自定义JavaBean类的字节码对象。
        provinceList = DataSupport.findAll(Province.class);
//        如果 provinceList集合长度大于0，就表示数据库中有数据
        if (provinceList.size() > 0){
//            先清空一下 data集合
            dataList.clear();
//            对 省级数据进行遍历
            for (Province province : provinceList){
//                将遍历到的每个省级名称都添加到 dataList集合中去
                dataList.add(province.getProvinceName());
            }
//            刷新界面，通知数据发生了变化
            adapter.notifyDataSetChanged();
//            设置listview默认选中项，0表示第一个
            c14_list_view.setSelection(0);
//            切换当前状态为 省级状态
            currentLevel = LEVEL_PROVINCE;
//            如果数据库中查询不到，就联网从服务器上查询
        }else {
//            服务器上的具体地址
            String address = "http://guolin.tech/api/china";
//            具体查询的操作
            queryFromServer(address, "province");
        }
    }

    /***
     * 查询 市级
     * 查询选中的省中的所有的市级，优先从数据中查询，如果查询不到再从服务器中查询
     */
    private void queryCites() {
//        标题 设置为被点击的省
        c14_title_text.setText(selectedProvince.getProvinceName());
        c14_back_button.setVisibility(View.VISIBLE);
 //        先从数据库中进行读取
//         利用 DataSupport 的 API 的where方法进行条件查询。
//              参数1：要查询的id, 参数2：自定义JavaBean类的字节码对象。
        cityList = DataSupport.where("provinceid = ?"
                , String.valueOf(selectedProvince.getId())).find(City.class);
        if (cityList.size() > 0){
            dataList.clear();
            for (City city: cityList){
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            c14_list_view.setSelection(0);
            currentLevel = LEVEL_CITY;
        }else {
//            通过选中的市，来获得到该市级别的id
            int provinceCode = selectedProvince.getProvinceCode();
            String address = "http://guolin.tech/api/china/" + provinceCode;
            queryFromServer(address, "city");
        }
    }

    /**
     * 查询 县级
     */
    private void queryCounties() {
        c14_title_text.setText(selectedCity.getCityName());
        c14_back_button.setVisibility(View.VISIBLE);
        countyList = DataSupport.where("cityid = ?",
                String.valueOf(selectedCity.getId())).find(County.class);
        if (countyList.size() > 0){
            dataList.clear();
            for (County county : countyList){
                dataList.add(county.getCountyName());
            }
            adapter.notifyDataSetChanged();
            c14_list_view.setSelection(0);
            currentLevel = LEVEL_COUNTY;
        }else {
            int provinceCode = selectedProvince.getProvinceCode();
            int cityCode = selectedCity.getCityCode();
            String address = "http://guolin.tech/api/china/" + provinceCode +"/" + cityCode;
            queryFromServer(address, "county");
        }
    }


    /***
     * 根据传入到的网络地址 和 类型，从服务器上查询省市县数据
     * @param address 服务器上的地址
     * @param type 具体的类型：是省级，还是市级，还是县级。
     */
    private void queryFromServer(String address, final String type) {
//        利用HttpUtils工具类，向服务器发送请求
        HttpUtils.sendOkHttpRequest(address, new Callback() {
//            服务器访问失败的时候调用
            @Override
            public void onFailure(Call call, IOException e) {
//                Android不允许在子线程中去做更新UI的操作，所以要通
//	              过 runOnUiThread方法，将线程切换到主线程去更新UI
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getActivity().getApplication(), "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            /***
             *  服务器访问成功的时候调用
             *【响应的数据会回调 onResponse()方法中】
             * @param call
             * @param response
             * @throws IOException
             */
            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                服务器访问成功后，得到返回的具体数据
                String responseText = response.body().string();
//                定义一个 初始值的布尔值为false，因为
                boolean result = false;
//                如果当前是 province省级别的类型
                if ("province".equals(type)){
//                    因为只有这个方法返回 true的时候才能查询到 服务器返回的 省级数据
                    result = Utility.handleProvinceResponse(responseText);
                }else if("city".equals(type)){
//                    因为只有这个方法返回 true的时候才能查询到 服务器返回的 市级数据
                    result = Utility.handleCityResponse(responseText,
                            selectedProvince.getId());
                }else if("county".equals(type)){
//                    因为只有这个方法返回 true的时候才能查询到 服务器返回的 县级数据
                    result = Utility.handleCountyResponse(responseText,
                            selectedCity.getId());
                }
                if (result){
//                    因为子线程不能刷新UI，所以要切换到主线程去做刷新UI的操作。
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if ("province".equals(type)){
                                queryProvinces();
                            }else if("city".equals(type)){
                                queryCites();
                            }else if("county".equals(type)){
                                queryCounties();
                            }
                        }
                    });
                }
            }
        });
    }

    /***
     * 显示 进度条对话框
     */
    private void showProgressDialog(){
//        如果当前对话框 不存在
        if (progressDialog == null){
//            创建 对话框对象
            progressDialog = new ProgressDialog(getActivity());
//            设置 对话框信息
            progressDialog.setMessage("正在加载。。。");
//            设置当触摸到窗口外时是否取消此对话框,如果设置为真，则对话框设置为可取消已经设置。
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    /***
     * 关闭 进度条对话框
     */
    private void closeProgressDialog() {
//        如果当前对话框 存在，就关闭对话框
        if (progressDialog != null){
            progressDialog.dismiss();
        }
    }


}
