package com.fanwe.live.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.IntentUtils;
import com.blankj.utilcode.util.SpanUtils;
import com.blankj.utilcode.util.Utils;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fanwe.live.R;
import com.fanwe.live.activity.LivePrivateChatActivity;
import com.fanwe.live.model.Contact;
import com.fanwe.live.model.RuleItemModel;
import com.fanwe.live.utils.ClipboardUtil;

import java.util.List;

/**
 * Created by Administrator on 2016/7/6.
 */
public class LiveRuleAdapter extends BaseMultiItemQuickAdapter<RuleItemModel, BaseViewHolder> {
    private Activity mActivity;

    public LiveRuleAdapter(List<RuleItemModel> data, Activity activity) {
        super(data);
        mActivity = activity;
        addItemType(RuleItemModel.TYPE_RESELLER, R.layout.item_live_recharge_diamonds_payment_rule_);
        addItemType(RuleItemModel.TYPE_RULE, R.layout.item_live_recharge_diamonds_payment_rule);
    }

    @Override
    protected void convert(@NonNull final BaseViewHolder helper, RuleItemModel item) {
        switch (helper.getItemViewType()) {
            case RuleItemModel.TYPE_RESELLER:
                helper.setText(R.id.tv_name, item.getName());
                helper.setText(R.id.tv_money, "余额" + item.getMoney());
                LinearLayout btnLayout = helper.getView(R.id.btn_layout);
                btnLayout.removeAllViews();
                for (final Contact contact : item.getContact()) {
                    View view = View.inflate(helper.itemView.getContext(), R.layout.item_recharge_rule_btn, null);
                    TextView btn = view.findViewById(R.id.btn);
                    btn.setText(contact.getName());
                    btn.setTag(item.getItemType());
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            SpanUtils spanUtils = new SpanUtils();
                            String btnText;
                            if (contact.getType() == 1) {
                                spanUtils.append("为避免不必要的损失，")
                                        .append("每次").setForegroundColor(Color.RED)
                                        .append("联系代理充值前请到充值页面")
                                        .append("确认代理是否显示在列表中，并拥有足够的余额。").setForegroundColor(Color.RED)
                                        .appendLine()
                                        .appendLine()
                                        .append("充值异常需要帮助请联系客服，平台将协助您挽回损失。");
                                btnText = "进入私信";
                            } else if (contact.getType() == 2) {
                                ClipboardUtil.copyText(contact.getId());
                                spanUtils.append("已复制代理QQ号：")
                                        .append(contact.getId()).setForegroundColor(Color.RED)
                                        .appendLine()
                                        .append("请打开QQ添加好友。")
                                        .appendLine()
                                        .appendLine()
                                        .append("为避免不必要的损失，")
                                        .append("每次").setForegroundColor(Color.RED)
                                        .append("联系代理充值前请到充值页面")
                                        .append("确认代理是否显示在列表中，并拥有足够的余额。").setForegroundColor(Color.RED)
                                        .appendLine()
                                        .appendLine()
                                        .append("充值异常需要帮助请联系客服，平台将协助您挽回损失。");
                                btnText = "打开QQ";
                            } else {
                                ClipboardUtil.copyText(contact.getId());
                                spanUtils.append("已复制代理微信号：")
                                        .append(contact.getId()).setForegroundColor(Color.RED)
                                        .appendLine()
                                        .append("请打开微信添加好友。")
                                        .appendLine()
                                        .appendLine()
                                        .append("为避免不必要的损失，")
                                        .append("每次").setForegroundColor(Color.RED)
                                        .append("联系代理充值前请到充值页面")
                                        .append("确认代理是否显示在列表中，并拥有足够的余额。").setForegroundColor(Color.RED)
                                        .appendLine()
                                        .appendLine()
                                        .append("充值异常需要帮助请联系客服，平台将协助您挽回损失。");
                                btnText = "打开微信";
                            }
                            new AlertDialog.Builder(mActivity)
                                    .setMessage(spanUtils.create())
                                    .setPositiveButton(btnText, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            if (contact.getType() == 1) {
                                                Intent intent = new Intent(Utils.getApp(), LivePrivateChatActivity.class);
                                                intent.putExtra(LivePrivateChatActivity.EXTRA_USER_ID, contact.getId());
//                                                intent.putExtra(LivePrivateChatActivity.EXTRA_USER_ID, "146299");
                                                ActivityUtils.startActivity(intent);
                                            } else if (contact.getType() == 2) {
                                                try {
                                                    String url = "mqqwpa://im/chat?chat_type=wpa&uin=" + contact.getId();
                                                    ActivityUtils.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                    try {
                                                        Intent intent = IntentUtils.getLaunchAppIntent("com.tencent.mobileqq");
                                                        ActivityUtils.startActivity(intent);
                                                    } catch (Exception ex) {
                                                        ex.printStackTrace();
                                                    }
                                                }
                                            } else {
                                                try {
                                                    Intent intent = IntentUtils.getLaunchAppIntent("com.tencent.mm");
                                                    ActivityUtils.startActivity(intent);
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                    })
                                    .show()
                                    .setCanceledOnTouchOutside(false);
                        }
                    });
                    switch (contact.getType()) {
                        case 1:
                            btn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_chat, 0, 0, 0);
                            break;
                        case 2:
                            btn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_qq, 0, 0, 0);
                            break;
                        case 3:
                            btn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_wx, 0, 0, 0);
                            break;
                    }
                    btnLayout.addView(view);
                }
                break;
            case RuleItemModel.TYPE_RULE:
                helper.setText(R.id.tv_diamonds, String.valueOf(item.getDiamonds()));
                helper.setText(R.id.tv_name, item.getName());
                helper.setText(R.id.tv_money, item.getMoney_name());
                break;
        }

    }

}
