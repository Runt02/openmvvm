package com.runt.open.mvvm.ui.main.home;

import com.runt.open.mvvm.base.fragments.LoadPageFragment;
import com.runt.open.mvvm.databinding.RefreshRecyclerBinding;

import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends LoadPageFragment<RefreshRecyclerBinding,HomeViewModel,MsgAdapter,Message> {


    @Override
    protected Map requestParams() {
        return new HashMap();
    }

}