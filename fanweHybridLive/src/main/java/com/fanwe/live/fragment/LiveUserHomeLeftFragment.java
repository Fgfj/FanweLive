package com.fanwe.live.fragment;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.fanwe.library.customview.SDGridLinearLayout;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.activity.LiveMySelfContActivity;
import com.fanwe.live.adapter.LiveUserHomeLeftAdapter;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.utils.GlideUtil;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 作者 E-mail:
 * @version 创建时间：2016-6-11 下午3:30:11 类说明
 */
public class LiveUserHomeLeftFragment extends LiveUserHomeBaseFragment
{
    public static final String TAG = "LiveUserHomeLeftFragment";

    @ViewInject(R.id.gll_info)
    private SDGridLinearLayout gll_info;

    private LiveUserHomeLeftAdapter adapter;

    @Override
    protected int onCreateContentView()
    {
        return R.layout.frag_user_home_left;
    }

    @Override
    protected void init()
    {
        super.init();
        bindData();
    }

    private void bindData()
    {
        if (app_user_homeActModel == null)
        {
            return;
        }

        UserModel user = app_user_homeActModel.getUser();

        if (user != null)
        {
            ArrayList<ItemUserModel> list = new ArrayList<LiveUserHomeLeftFragment.ItemUserModel>();
            Map<String, String> map = user.getItem();

            if (map != null && map.size() > 0)
            {
                for (String key : map.keySet())
                {
                    ItemUserModel item = new ItemUserModel();
                    item.key = key;
                    item.value = map.get(key);
                    list.add(item);
                }
            }

            adapter = new LiveUserHomeLeftAdapter(list, getActivity());
            gll_info.setColNumber(1);
            gll_info.setAdapter(adapter);
        }
    }

    public static class ItemUserModel
    {
        private String key;
        private String value;

        public String getKey()
        {
            return key;
        }

        public void setKey(String key)
        {
            this.key = key;
        }

        public String getValue()
        {
            return value;
        }

        public void setValue(String value)
        {
            this.value = value;
        }
    }
}
