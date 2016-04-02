package lifi.lifi_museum;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.VideoView;

public class VideoPlayer extends Activity implements MediaPlayer.OnCompletionListener,MediaPlayer.OnPreparedListener,View.OnTouchListener {

    private VideoView mVV;

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);

        setContentView(R.layout.videoplayer);

        String filePath = "";
        Bundle e = getIntent().getExtras();
        if (e!=null) {
            filePath = e.getString("filePath");
        }

        mVV = (VideoView)findViewById(R.id.myvideoview);
        mVV.setOnCompletionListener(this);
        mVV.setOnPreparedListener(this);
        mVV.setOnTouchListener(this);

        if (!playFileRes(filePath)) return;

        mVV.start();
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        String filePath = "";
        Bundle e = getIntent().getExtras();
        if (e != null) {
            filePath = e.getString("filePath");
        }
        playFileRes(filePath);
    }

    private boolean playFileRes(String filePath) {
        if (filePath=="") {
            stopPlaying();
            return false;
        } else {
            Log.d("video", filePath);
            mVV.setVideoPath(filePath);
            return true;
        }
    }

    public void stopPlaying() {
        mVV.stopPlayback();
        this.finish();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        finish();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        stopPlaying();
        return true;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.setLooping(true);
    }
}