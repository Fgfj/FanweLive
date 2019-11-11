package com.fanwe.live.view;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.widget.VideoView;

public class FullscreenVideoView extends VideoView {
    public FullscreenVideoView(Context context) {
        this(context, null);
    }

    public FullscreenVideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FullscreenVideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
                mediaPlayer.setLooping(true);
            }
        });
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getDefaultSize(0, widthMeasureSpec);
        int height = getDefaultSize(0, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }
}
