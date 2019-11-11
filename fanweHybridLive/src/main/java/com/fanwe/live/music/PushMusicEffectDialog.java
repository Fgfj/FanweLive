package com.fanwe.live.music;

import android.app.Activity;
import android.view.View;
import android.widget.SeekBar;

import com.fanwe.lib.cache.SDDisk;
import com.fanwe.live.R;
import com.fanwe.live.dialog.LiveBaseDialog;
import com.fanwe.live.view.VerticalSeekBar;

/**
 * 推流直播的时候音效设置窗口
 */
public class PushMusicEffectDialog extends LiveBaseDialog
{
    private View iv_reset;
    private View iv_close;
    private VerticalSeekBar sb_music;
    private VerticalSeekBar sb_mic;

    private MusicEffectConfig mConfig;

    private MusicEffectCallback mMusicEffectCallback;

    public PushMusicEffectDialog(Activity activity)
    {
        super(activity);
        setContentView(R.layout.dialog_push_music_effect);
        setCanceledOnTouchOutside(true);
        paddings(0);

        iv_reset = findViewById(R.id.iv_reset);
        iv_close = findViewById(R.id.iv_close);
        sb_music = (VerticalSeekBar) findViewById(R.id.sb_music);
        sb_mic = (VerticalSeekBar) findViewById(R.id.sb_mic);

        iv_reset.setOnClickListener(this);
        iv_close.setOnClickListener(this);

        mConfig = SDDisk.open().getSerializable(MusicEffectConfig.class);
        if (mConfig == null)
        {
            mConfig = new MusicEffectConfig();
        }

        sb_music.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                mConfig.setMusicVolume(progress);
                notifyMusicEffectCallback();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
            }
        });

        sb_mic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                mConfig.setMicVolume(progress);
                notifyMusicEffectCallback();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
            }
        });
        updateViewState();
    }

    public void setMusicEffectCallback(MusicEffectCallback musicEffectCallback)
    {
        mMusicEffectCallback = musicEffectCallback;
    }

    private void updateViewState()
    {
        sb_music.setProgress(mConfig.getMusicVolume());
        sb_mic.setProgress(mConfig.getMicVolume());
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        if (v == iv_close)
        {
            dismiss();
        } else if (v == iv_reset)
        {
            mConfig = new MusicEffectConfig();
            updateViewState();
        }
    }

    @Override
    protected void onStop()
    {
        SDDisk.open().putSerializable(mConfig);
        super.onStop();
    }

    private void notifyMusicEffectCallback()
    {
        if (mMusicEffectCallback != null)
        {
            mMusicEffectCallback.onMusicEffect(mConfig);
        }
    }

    public interface MusicEffectCallback
    {
        void onMusicEffect(MusicEffectConfig config);
    }
}
