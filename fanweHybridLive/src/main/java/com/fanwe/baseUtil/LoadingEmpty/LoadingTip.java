package com.fanwe.baseUtil.LoadingEmpty;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fanwe.live.R;


/**
 * joe:加载页面内嵌提示
 * on 2018.07.20
 */
public class LoadingTip extends LinearLayout {

    private ImageView img_tip_logo;
    private ProgressBar progress;
    private TextView tv_tips;
    private LinearLayout ll_empty;
    private String errorMsg;
    private onReloadListener onReloadListener;


    public LoadingTip(Context context) {
        super(context);
        initView(context);
    }

    public LoadingTip(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public LoadingTip(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LoadingTip(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    //分为服务器失败，网络加载失败、数据为空、加载中、完成四种状态
    public static enum LoadStatus {
        sereverError,error, empty, loading,finish
    }

    private void initView(Context context) {
        View.inflate(context, R.layout.dialog_loading_tip, this);
        img_tip_logo = (ImageView) findViewById(R.id.img_tip_logo);
        progress = (ProgressBar) findViewById(R.id.progress);
        tv_tips = (TextView) findViewById(R.id.tv_tips);
        ll_empty = (LinearLayout) findViewById(R.id.ll_empty);
        //重新尝试
        ll_empty.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onReloadListener!=null){
                    onReloadListener.reload();
                }
            }
        });
        setVisibility(View.GONE);
    }

    public void setTips(String tips){
        if(tv_tips!=null){
            tv_tips.setText(tips);
        }
    }

    /**
     * 根据状态显示不同的提示
     * @param loadStatus
     */
    public void setLoadingTip(LoadStatus loadStatus){
        switch (loadStatus){
            case empty:
                setVisibility(View.VISIBLE);
                img_tip_logo.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
                tv_tips.setText(getContext().getText(R.string.empty).toString());
                img_tip_logo.setImageResource(R.drawable.loading_empty);
                break;
            case sereverError:
                setVisibility(View.VISIBLE);
                img_tip_logo.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
                if (TextUtils.isEmpty(errorMsg)){
                    tv_tips.setText(getContext().getText(R.string.net_error).toString());
                }else {
                    tv_tips.setText(errorMsg);
                }
                img_tip_logo.setImageResource(R.drawable.loading_empty);
                break;
            case error:
                setVisibility(View.VISIBLE);
                img_tip_logo.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
                if (TextUtils.isEmpty(errorMsg)){
                    tv_tips.setText(getContext().getText(R.string.net_error).toString());
                }else {
                    tv_tips.setText(errorMsg);
                }
                img_tip_logo.setImageResource(R.drawable.loading_empty);
                break;
            case loading:
                setVisibility(View.VISIBLE);
                img_tip_logo.setVisibility(View.GONE);
                progress.setVisibility(View.VISIBLE);
                tv_tips.setText(getContext().getText(R.string.loading).toString());
                break;
            case finish:
                setVisibility(View.GONE);
                break;
        }
    }


    public void setOnReloadListener(onReloadListener onReloadListener){
        this.onReloadListener=onReloadListener;
    }
    /**
     * 重新尝试接口
     */
    public interface onReloadListener{
        void reload();
    }
}

