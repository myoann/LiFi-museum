package lifi.lifi_museum;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.luciom.opticallbs.SmartLightHandlerAbs;
import com.luciom.opticallbs.SmartLightRunnable;

public class TestLifi extends AppCompatActivity {

    private SmartLightRunnable smartLight = null;
    private Thread lifiThread = null;
    private SmartLightHandler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_lifi);
        mHandler = new SmartLightHandler((TextView)findViewById(R.id.id_filteredTxtView),
                (TextView)findViewById(R.id.msgTxtView));
        smartLight = new SmartLightRunnable(mHandler, getApplicationContext());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
    @Override
    public void onResume() {
        super.onResume();
        if(!smartLight.isRecording()) {
            lifiThread = new Thread(smartLight);
            lifiThread.start();
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        if(smartLight.isRecording()) {
            lifiThread.interrupt();
            lifiThread = null;
        }
    }

}
