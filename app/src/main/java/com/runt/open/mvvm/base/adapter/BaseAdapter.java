package com.runt.open.mvvm.base.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

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
public abstract class BaseAdapter<DATA, VB extends ViewBinding> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected List<DATA> dataList = new ArrayList<>();
    protected OnItemClickListener<DATA> onItemClickListener;
    public boolean showNull;
    public float defaultMarginBottom,lastMarginBottom;

    public interface  OnItemClickListener<DATA>{
        void onItemClick(int position,DATA data);
    }

    public class ViewBindHolder extends RecyclerView.ViewHolder  {
        ViewBinding binding;
        public ViewBindHolder(ViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void setOnItemClickListener(OnItemClickListener<DATA> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setData(List<DATA> list){
        if(dataList != list) {
            dataList.clear();
            if (list != null) {
                dataList.addAll(list);
            }
        }
        notifyDataSetChanged();
    }

    public void addData(DATA data){
        if(data != null){
            dataList.add(data);
        }
        notifyDataSetChanged();
    }

    public void addData(List<DATA> list){
        if (list != null && list.size() > 0) {
            this.dataList.addAll(list);
        }
        notifyDataSetChanged();
    }

    public List<DATA> getData() {
        return dataList;
    }

    @NonNull
    @Override
    public ViewBindHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == 1 ){
            // get genericity "B"
            try {
                Class<VB> entityClass = (Class<VB>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
                /*for(Method method: entityClass.getMethods()){
                    StringBuilder sb = new StringBuilder();
                    for(Class type : method.getParameterTypes()){
                        sb.append(type.getSimpleName()+",");
                    }
                    Log.e("BaseAdapter",String.format("method:%s,return:%s,param:%s",method.getName(),method.getReturnType().getSimpleName(),sb.toString()));
                }*/
                Method method = entityClass.getMethod("inflate", LayoutInflater.class,ViewGroup.class,boolean.class);//get method from name "inflate";
                VB vBind = (VB) method.invoke(entityClass,LayoutInflater.from(parent.getContext()),parent,false);//execute method to create a objct of viewbind;
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
        return new ViewBindHolder( LayoutNullBinding.inflate( LayoutInflater.from(parent.getContext()), parent, false ) );
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewBindHolder bindHolder = (ViewBindHolder) holder;
        if(getItemViewType(position) == 0 || bindHolder.binding instanceof LayoutNullBinding){
            onBindEmptyView((LayoutNullBinding) bindHolder.binding);
        }else{
            if (onItemClickListener != null) {
                bindHolder.binding.getRoot().setOnClickListener(view -> {
                    if(onItemClickListener != null){
                        onItemClickListener.onItemClick(position,getItem(position));
                    }
                });
            }
            onBindView((VB) bindHolder.binding, position, getItem(position));
            setBottomMargin(bindHolder,position);
        }
    }

    /**
     * 设置最后一个底部间隔
     * @param holder
     * @param position
     */
    protected void setBottomMargin(ViewBindHolder holder, int position){
        setBottomMargin(holder,position,lastMarginBottom);
    }

    /**
     * 设置最后一个底部间隔
     * @param holder
     * @param position  位置
     * @param dp        间距
     */
    protected void setBottomMargin(RecyclerView.ViewHolder holder, int position,float dp){
        setBottomMargin(holder,position,dp,defaultMarginBottom);
    }

    protected void setBottomMargin(RecyclerView.ViewHolder holder, int position, float dp, float defaultDp){
        ViewGroup.MarginLayoutParams params1 = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
        if(position == dataList.size() -1){
            params1.setMargins(params1.leftMargin, params1.topMargin, params1.rightMargin, DeviceUtil.convertDpToPixel(dp,holder.itemView.getContext()));
        }else{
            params1.setMargins(params1.leftMargin, params1.topMargin, params1.rightMargin, DeviceUtil.convertDpToPixel(defaultDp,holder.itemView.getContext()));
        }
    }


    /**
     * 空数据支持
     * @return
     */
    @Override
    public int getItemCount() {
        return (showNull && dataList.size() == 0 )? 1 : dataList.size();
    }

    /**
     * 当下标为0，数据集合为0 返回0（意味当前应显示空数据视图））
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return  (showNull && position == 0 && dataList.size() == 0)? 0 : 1;
    }

    public DATA getItem(int position){
        if(position >= dataList.size()){
            return null;
        }else {
            return dataList.get(position);
        }
    }

    protected abstract void onBindView(VB binding, int position, DATA data);

    /**
     * 空数据显示
     */
    protected void onBindEmptyView(LayoutNullBinding emptyBinding){
        Log.e("baseAdapter"," emptyBinding:"+emptyBinding);

    }
}
