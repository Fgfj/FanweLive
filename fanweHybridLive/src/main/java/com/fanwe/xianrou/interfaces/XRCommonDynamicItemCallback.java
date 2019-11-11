package com.fanwe.xianrou.interfaces;

import android.view.View;

/**
 * @包名 com.fanwe.xianrou.common
 * @描述
 * @作者 Su
 * @创建时间 2017/3/19 8:58
 **/
public interface XRCommonDynamicItemCallback<E> extends XRCommonOnMoreClickCallback<E>
{
    void onUserHeadClick(View view, E itemEntity, int position);

    void onFavoriteClick(View view, E itemEntity, int position);
}
