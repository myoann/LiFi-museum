package lifi.lifi_museum;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.luciom.opticallbs.*;

/**
 * Created by Yoann on 1/27/2016.
 */
public class SmartLightHandler extends SmartLightHandlerAbs {
    private TextView id_filtered, message;
    private LifiActivity lifi_activity;
    public SmartLightHandler(TextView id_filtered, TextView message, LifiActivity lifi_activity) {
        super();
        this.id_filtered = id_filtered;
        this.message = message;
        this.lifi_activity = lifi_activity;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        String id_filtered_data = "";
        String message_data = "";
        if(msg.what==MsgWhat.NEW_MESSAGE.value) {
            if(msg.obj instanceof LiFiMessage) {
                LiFiMessage lifiMsg = (LiFiMessage)msg.obj;
                SimpleDateFormat sdf = new SimpleDateFormat("[HH'h'mm'm'ss.SSS]\n",
                        Locale.getDefault());
                message_data = sdf.format(Calendar.getInstance().getTime())+"Last ID=";
                for(int i=0; i<lifiMsg.getUID().length; i++) {
                    message_data += String.format("%02X", lifiMsg.getUID()[i]);
                }
                message_data += System.getProperty("line.separator")+"Last DATA=";
                for(int i=0; i<lifiMsg.getUserData().length; i++) {
                    message_data += String.format("%02X", lifiMsg.getUserData()[i]);
                }
                switch(lifiMsg.getUserDataStatus()) {
                    case NO_DATA:
                        message_data += " NO_DATA";
                        break;
                    case DATA_ERROR:
                        message_data += " DATA_ERROR";
                        break;
                    case CRC_NOK:
                        message_data += " CRC_NOK";
                        break;
                    case NO_CRC:
                        message_data += " (NO_CRC)";
                        break;
                    case CRC_OK:
                        message_data += " (CRC_OK)";
                        break;
                }
            }
        }
        else if(msg.what==MsgWhat.FILTERED_UID.value) {
            if(msg.obj instanceof byte[]) {
                id_filtered_data = "";
                byte[] filtered_id = (byte[])msg.obj;
                for(int i=0; i<filtered_id.length; i++) {
                    id_filtered_data += String.format("%02X", filtered_id[i]);
                }
            }
        }
        else if(msg.what==MsgWhat.DEAD.value) {
            id_filtered_data = "RECHERCHE EN COURS ...";
            message_data = ".";
        }
        else if(msg.what==MsgWhat.START_LBS.value) {
            id_filtered_data = "LIFI ACTIVÉ";
            message_data = ".";
        }
        else if(msg.what==MsgWhat.STOP_LBS.value) {
            id_filtered_data = "LIFI ARRÊTÉ";
            message_data = ".";
        }
        if(!id_filtered_data.equals("")) {
            id_filtered.setText(id_filtered_data);
            if(!id_filtered_data.equals("LIFI ACTIVÉ") && !id_filtered_data.equals("LIFI ARRÊTÉ") && !id_filtered_data.equals("RECHERCHE EN COURS ...")) {
                final Intent redirectIntent = new Intent(lifi_activity, DetailsActivity.class);
                redirectIntent.putExtra("id_LIFI", id_filtered_data);
                lifi_activity.startActivity(redirectIntent);
            }
        }
        if(!message_data.equals("")) {
            message.setText(message_data);
        }
    }
}