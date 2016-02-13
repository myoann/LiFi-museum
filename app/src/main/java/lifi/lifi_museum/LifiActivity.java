package lifi.lifi_museum;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.luciom.opticallbs.SmartLightRunnable;

public class LifiActivity extends AppCompatActivity {

    private SmartLightRunnable smartLight = null;
    private Thread lifiThread = null;
    private SmartLightHandler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lifi);
        mHandler = new SmartLightHandler((TextView)findViewById(R.id.id_filteredTxtView),
                (TextView)findViewById(R.id.msgTxtView));
        smartLight = new SmartLightRunnable(mHandler, getApplicationContext());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Add the Up Action
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
