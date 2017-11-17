package che.bwie.com.taobaogouwuchedemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import che.bwie.com.taobaogouwuchedemo.Adapter.ShopAdapter;
import che.bwie.com.taobaogouwuchedemo.Bean.ShopBean;


public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.third_recyclerview)
    RecyclerView thirdRecyclerview;
    @InjectView(R.id.third_allselect)
    TextView thirdAllselect;
    @InjectView(R.id.third_totalprice)
    TextView thirdTotalprice;
    @InjectView(R.id.third_totalnum)
    TextView thirdTotalnum;
    @InjectView(R.id.third_submit)
    TextView thirdSubmit;
    @InjectView(R.id.third_pay_linear)
    LinearLayout thirdPayLinear;
    private LinearLayoutManager manager;
    private ShopAdapter adapter;
    private List<ShopBean.OrderDataBean.CartlistBean> mAllOrderList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        getData();
        // 非选中
        thirdAllselect.setTag(1);
        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        adapter = new ShopAdapter(this);
       thirdRecyclerview.setLayoutManager(manager);
        thirdRecyclerview.setAdapter(adapter);
        adapter.add(mAllOrderList);
        //获取数据

       adapter.setCheckBoxListener(new ShopAdapter.CheckBoxListener() {
            @Override
            public void check(int position, int count, boolean check,List<ShopBean.OrderDataBean.CartlistBean> list) {


                sum(list);
            }
        });

        adapter.setCustomViewListener(new ShopAdapter.CustomViewListener() {
            @Override
            public void click(int count,List<ShopBean.OrderDataBean.CartlistBean> list) {
                sum(list);
            }
        });

        adapter.setDelListener(new ShopAdapter.DelListener() {
            @Override
            public void del(int position,List<ShopBean.OrderDataBean.CartlistBean> list) {
                sum(list);
            }
        });

    }
    float price = 0;
    int count;

    /**
     * 计算总价
     * @param mAllOrderList
     */
    private void sum(List<ShopBean.OrderDataBean.CartlistBean> mAllOrderList) {
        price = 0;
        count = 0;

        boolean allCheck = true ;
        for (ShopBean.OrderDataBean.CartlistBean bean : mAllOrderList) {
            if (bean.isCheck()) {
                //得到总价
                price += bean.getPrice() * bean.getCount();
                //得到商品个数
                count += bean.getCount();
            }else {
                // 只要有一个商品未选中，全选按钮 应该设置成 为选中
                allCheck = false;
            }
        }

        thirdTotalprice.setText("总价: " + price);
        thirdTotalnum.setText("共" + count + "件商品");
 if(allCheck){
            thirdAllselect.setTag(2);
            //thirdAllselect.setBackgroundResource(R.drawable.shopcart_selected);
        }else {
            thirdAllselect.setTag(1);
           // thirdAllselect.setBackgroundResource(R.drawable.shopcart_unselected);
        }

    }

    private void getData() {
        //模拟网络请求数据
        try {
            //打开文件资源
            InputStream inputStream = getAssets().open("shop.json");
            //字节流转成字符串
            String data = convertStreamToString(inputStream, "utf-8");
            Gson gson = new Gson();
            Log.i("data","data");
            ShopBean shopBean = gson.fromJson(data, ShopBean.class);


            for (int i = 0; i < shopBean.getOrderData().size(); i++) {
                int length = shopBean.getOrderData().get(i).getCartlist().size();
                for (int j = 0; j < length; j++) {
                    mAllOrderList.add(shopBean.getOrderData().get(i).getCartlist().get(j));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String convertStreamToString(InputStream inputStream, String charset) {
        try {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder stringBuilder = new StringBuilder();
        String string = null;

            while ((string=bufferedReader.readLine())!=null){
                stringBuilder.append(string);
            }

           bufferedReader.close();
            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    boolean select = false ;
    @OnClick(R.id.third_allselect)
    public void onClick() {
        //全选按钮 点击事件

        int tag = (Integer) thirdAllselect.getTag() ;


        if(tag ==1){
            thirdAllselect.setTag(2);
            select = true;

        } else {
            thirdAllselect.setTag(1);
            select = false;
        }
        for (ShopBean.OrderDataBean.CartlistBean bean : mAllOrderList) {
            bean.setCheck(select);
        }
        adapter.notifyDataSetChanged();

        sum(adapter.getList());





    }


}
