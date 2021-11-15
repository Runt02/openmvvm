package com.runt.open.mvvm.base.adapter;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.runt.open.mvvm.base.activities.BaseActivity;
import com.runt.open.mvvm.databinding.LayoutNullBinding;
import com.runt.open.mvvm.util.DeviceUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2021/10/27 0027.
 *  T  数据类型
 *  V 适配器视图
 */
public abstract class BaseAdapter<B extends ViewBinding,T> extends RecyclerView.Adapter {

    protected List<T> mData = new ArrayList<>();

    protected Drawable nullDrawable;
    protected String nullTxt="暂无数据";
    protected String TAG = "BaseAdapter";
    protected BaseActivity activity;

    public BaseAdapter(){
    }

    public BaseAdapter(@NonNull List<T> data){
        mData = data;
    }

    public List<T> getData() {
        return mData;
    }

    public void setData(@NonNull List<T> data){
        mData = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == 1 ){
            // get genericity "B"
            Class<B> entityClass = (Class<B>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
            try {
               /* for(Method method: entityClass.getMethods()){
                    StringBuilder sb = new StringBuilder();
                    for(Class type : method.getParameterTypes()){
                        sb.append(type.getSimpleName()+",");
                    }
                    Log.e(TAG,String.format("method:%s,return:%s,param:%s",method.getName(),method.getReturnType().getSimpleName(),sb.toString()));
                }*/
                Method method = entityClass.getMethod("inflate", LayoutInflater.class,ViewGroup.class,boolean.class);//get method from name "inflate";
                B vBind = (B) method.invoke(entityClass,LayoutInflater.from(parent.getContext()),parent,false);//execute method to create a objct of viewbind;
                return new ViewBindHolder(vBind);
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                Throwable t = e.getTargetException();// 获取目标异常
                t.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new NullViewHolder( LayoutNullBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //MyLog.i(TAG,"onBindViewHolder position:"+position+" "+mData.size()+" "+getItemViewType(position));
        if(activity == null){
            activity = (BaseActivity) holder.itemView.getContext();
        }
        if(getItemViewType(position)==0){
            bindView((NullViewHolder) holder);
        }else {
            bindView((ViewBindHolder) holder, mData.size() == 0 ? null : mData.get(position), position);
        }
    }

    /**
     * 设置最后一个底部间隔
     * @param holder
     * @param position
     */
    protected void setBottomMargin(ViewBindHolder holder, int position){
        setBottomMargin(holder,position,23);
    }

    /**
     * 设置最后一个底部间隔
     * @param holder
     * @param position  位置
     * @param dp        间距
     */
    protected void setBottomMargin(RecyclerView.ViewHolder holder, int position,int dp){
        setBottomMargin(holder,position,dp,0);
    }
    protected void setBottomMargin(RecyclerView.ViewHolder holder, int position, int dp, int defaultDp){
        ViewGroup.MarginLayoutParams params1 = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
        if(position == mData.size() -1){
            params1.setMargins(params1.leftMargin, params1.topMargin, params1.rightMargin, DeviceUtil.convertDpToPixel(dp,holder.itemView.getContext()));
        }else{
            params1.setMargins(params1.leftMargin, params1.topMargin, params1.rightMargin, DeviceUtil.convertDpToPixel(defaultDp,holder.itemView.getContext()));
        }
    }
    protected abstract void bindView(ViewBindHolder holder,T data,int position);


    protected void bindView(NullViewHolder holder){

    }

    @Override
    public int getItemCount() {
        //默认显示空视图，若不显示空视图则重写该方法，返回mData.size()
        return mData == null || mData.size() == 0 ?1:mData.size();
    }


    @Override
    public int getItemViewType(int position) {
        //当下标为0，数据集合为0 返回0（意味当前应显示空数据视图））
        //MyLog.i(TAG,"getItemViewType position:"+position+" mdata:"+mData.size()+" "+(position ==0 && mData.size()==0));
        return position == 0 && mData.size()==0?0:1;
    }

    public class ViewBindHolder extends RecyclerView.ViewHolder{
        public B binding;
        public ViewBindHolder( B binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
    /**
     * 空数据显示
     * Created by Administrator on 2021/10/28 0028.
     */
    public class NullViewHolder extends RecyclerView.ViewHolder {
        LayoutNullBinding binding;

        public NullViewHolder(LayoutNullBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
