package com.fanwe.auction.activity;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fanwe.auction.model.LiveLnvitationAwardModel;
import com.fanwe.hybrid.activity.BaseTitleActivity;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.live.R;
import com.fanwe.live.activity.LiveLnvitationAwardActivity;
import com.fanwe.live.common.CommonInterface;

public class ActivityInviteFriend extends BaseTitleActivity  {



    private TextView
            tv_invite_code,
            tv_extension_brief,
            tv_invite_link;
    private ImageView iv_qr_code;
    private Button bt_copy_link, bt_save_code;

    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friend);

        init();
    }

    private void init(){
        initView();
        initTitle();
        initData();
    }

    private void initTitle() {
        mTitle.setMiddleTextTop("推广链接");
    }

    private void initView(){

        tv_invite_code = (TextView) findViewById(R.id.tv_extension_code);
//        tv_invite_link=$(R.id.tv_invite_link);
        tv_extension_brief = (TextView) findViewById(R.id.tv_extension_brief);
        iv_qr_code = (ImageView) findViewById(R.id.iv_qr_code);
        bt_copy_link = (Button) findViewById(R.id.bt_copy_link);
        bt_save_code = (Button) findViewById(R.id.bt_save_code);
    }

    private void initData(){
        CommonInterface.requestLnvitationAward(new AppRequestCallback<LiveLnvitationAwardModel>() {
            @Override
            protected void onSuccess(SDResponse sdResponse) {
                if(actModel.isOk()){
                    bitmap = EncodingUtils.createQRCode(actModel.getAndroid_down_url(), iv_qr_code.getWidth(), iv_qr_code.getHeight());
                    iv_qr_code.setImageBitmap(bitmap);
                    if(actModel==null)
                        return;
                        tv_extension_brief.setText(actModel.getDec());
                        tv_invite_code.setText(actModel.getInvitation_code());
                        bt_copy_link.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                            // 将文本内容放到系统剪贴板里。
                            cm.setPrimaryClip(ClipData.newPlainText(null, actModel.getAndroid_down_url()));
                            Toast.makeText(ActivityInviteFriend.this, "链接已复制,赶快分享给好友,领取奖励吧！", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

    }

    /**
     * Display setting dialog.
     */
//    public void showSettingDialog(Context context, final List<String> permissions) {
//        List<String> permissionNames = Permission.transformText(context, permissions);
//        new AlertDialog.Builder(context)
//                .setCancelable(false)
//                .setTitle("温馨提示")
//                .setMessage("请开启读写手机存储权限，否则功能无法使用")
//                .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        setPermission();
//                    }
//                })
//                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                })
//                .show();
//    }
//
//    private void setPermission() {
//        AndPermission.with(this)
//                .runtime()
//                .setting()
//                .onComeback(new Setting.Action() {
//                    @Override
//                    public void onAction() {
//                        savePicture(createViewBitmap(findViewById(R.id.ll_save)));
//                    }
//                })
//                .start();
//    }
//
//    private void savePicture(Bitmap bitmap) {
//        //保存图片的路径，手机内置内存卡
//        /** 首先默认个文件保存路径 */
//        final String SAVE_PIC_PATH = Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED) ? Environment.getExternalStorageDirectory().getAbsolutePath() : "/mnt/sdcard";//保存到SD卡
//        final String SAVE_REAL_PATH = SAVE_PIC_PATH + "/ceshi/";//保存的确切位置
//        File foder = new File(SAVE_REAL_PATH);
//        if (!foder.exists()) {
//            //创建文件夹
//            foder.mkdirs();
//        }
//        File file = new File(SAVE_REAL_PATH, System.currentTimeMillis() + ".png");
//        if (file.exists()) {
//            file.delete();
//        }
//        try {
//            file.createNewFile();
//            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));
//            bitmap.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
//            outputStream.flush();
//            outputStream.close();
//            //发广播告诉相册有图片需要更新，这样可以在图册下看到保存的图片了
//            Intent intent=new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//            Uri uri=Uri.fromFile(file);
//            intent.setData(uri);
//            sendBroadcast(intent);
//
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Toast.makeText(ActivityInviteFriend.this, "二维码保存成功", Toast.LENGTH_SHORT).show();
//                }
//            });
//        } catch (IOException e) {
//            e.printStackTrace();
//            Toast.makeText(ActivityInviteFriend.this, "二维码保存失败", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    public static Bitmap createViewBitmap(View v) {
//        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
//                Bitmap.Config.RGB_565);
//        Canvas canvas = new Canvas(bitmap);
//        v.draw(canvas);
//        return bitmap;
//    }
}
