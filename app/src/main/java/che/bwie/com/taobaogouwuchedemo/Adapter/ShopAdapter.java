package che.bwie.com.taobaogouwuchedemo.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import che.bwie.com.taobaogouwuchedemo.Bean.ShopBean;
import che.bwie.com.taobaogouwuchedemo.CustomView;
import che.bwie.com.taobaogouwuchedemo.R;


/**
 * 此类的作用：
 *
 * @author: forever
 * @date: 2017/11/16 19:46
 */
public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.IViewHolder> {
    private Context context;
    private List<ShopBean.OrderDataBean.CartlistBean> list;

    public ShopAdapter(Context context) {
        this.context = context;
    }
    public void add(List<ShopBean.OrderDataBean.CartlistBean> list) {
        if (this.list == null) {
            this.list = new ArrayList<>();
        }
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public ShopAdapter.IViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.shop_adapter, null);

        return new IViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ShopAdapter.IViewHolder holder, final int position) {
        //防止checkbox 滑动 错乱
        holder.checkbox.setChecked(list.get(position).isCheck());
        holder.customviewid.setEditText(list.get(position).getCount());
       holder.danjia.setText(list.get(position).getPrice()+"");

        ImageLoader.getInstance().displayImage(list.get(position).getDefaultPic(),holder.shopface);
        holder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.get(position).setCheck(holder.checkbox.isChecked());
                notifyDataSetChanged();
                if(checkBoxListener != null){
                    checkBoxListener.check(position,holder.customviewid.getCurrentCount(),holder.checkbox.isChecked(),list);
                }
            }
        });


        /**
         * 加减监听
         */
        holder.customviewid.setListener(new CustomView.ClickListener() {
            @Override
            public void click(int count) {


                //更新数据源
                list.get(position).setCount(count);
                notifyDataSetChanged();

                if(listener != null){
                    listener.click(count,list);
                }


            }
        });

        /**
         * 删除点击事件
         */
        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                list.remove(position);
                notifyDataSetChanged();

                if(delListener != null){
                    delListener.del(position,list);
                }


            }
        });
    }


    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }




    static class IViewHolder extends RecyclerView.ViewHolder  {
        @InjectView(R.id.checkbox)
        CheckBox checkbox;
        @InjectView(R.id.shopface)
        ImageView shopface;
        @InjectView(R.id.danjia)
        TextView danjia;
        @InjectView(R.id.shop_btn_del)

        Button del ;
        @InjectView(R.id.customviewid)
        CustomView customviewid;


        IViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }
    public List<ShopBean.OrderDataBean.CartlistBean> getList(){
        return list;
    }

    CheckBoxListener checkBoxListener;

    /**
     * checkbox 点击事件
     * @param listener
     */
    public void setCheckBoxListener(CheckBoxListener listener){
        this.checkBoxListener = listener;
    }
    public interface CheckBoxListener {
        public void check(int position,int count, boolean check,List<ShopBean.OrderDataBean.CartlistBean> list);


    }

    CustomViewListener listener;

    /**
     * 加减号 点击事件
     * @param listener
     */
    public void setCustomViewListener(CustomViewListener listener){
        this.listener = listener;
    }
    public interface CustomViewListener {
        void click(int count,List<ShopBean.OrderDataBean.CartlistBean> list);
    }



    DelListener delListener ;
    /**
     * 加减号 删除按钮事件
     * @param listener
     */
    public void setDelListener(DelListener listener) {
        this.delListener = listener ;
    }
    public interface DelListener {
        public void del(int position,List<ShopBean.OrderDataBean.CartlistBean> list);
    }



}
