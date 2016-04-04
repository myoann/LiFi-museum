package lifi.lifi_museum.nfc;


import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import java.io.Console;
import java.util.ArrayList;

import lifi.lifi_museum.R;
import lifi.lifi_museum.activity.DetailsActivity;

public class NFCWriter extends AppCompatActivity {

    public NfcAdapter nfcAdapter;
    private TextView id_filtered, message;
    private EditText idNFC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_writer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        Button btnGoToWriter = (Button) findViewById(R.id.btn_send_id);
        idNFC   = (EditText)findViewById(R.id.nouvelIdTagNFC);
        btnGoToWriter.setOnClickListener(sendID);

    }
    View.OnClickListener sendID = new
            View.OnClickListener() {
                public void onClick(View v) {
                    // TODO : Récupérer id et envoyer au Tag NFC
                    //final Intent redirectIntent = new Intent(NFCWriter.this, NFCWriter.class);
                    //NFCWriter.this.startActivity(redirectIntent);
                    Log.v("Nouveau Tag NFC ", idNFC.getText().toString());

                }
            };

    String getTextData(byte[] payload) {
        String str = new String(payload);
        /*String texteCode = ((payload[0] & amp; 0deux00) == 0) ' "UTF-8" : "UTF-16";
        int langageCodeTaille = payload[0] &amp; 0077;
        return new String(payload, langageCodeTaille + 1, payload.length - langageCodeTaille - 1, texteCode);*/
       /* String texteCode = ((payload[0] &amp; 0deux00) == 0) ' "UTF-8" : "UTF-16";
        int langageCodeTaille = payload[0] &amp; 0077;*/
        return str.substring(3);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        this.nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
    }

    public void onPause() {
        super.onPause();
        this.nfcAdapter.disableForegroundDispatch(this);
    }


    public void onNewIntent(Intent intent) {
        Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        //do something with tagFromIntent
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        ArrayList<NdefMessage> messages = new ArrayList<NdefMessage>();
        for(int i=0 ; i< rawMsgs.length;i++){
            messages.add((NdefMessage) rawMsgs[i]);
        }
        int j = 0;
        for(NdefMessage m : messages){
            NdefRecord record = m.getRecords()[j];
            byte[] id = record.getId();
            short tnf = record.getTnf();
            byte[] type = record.getType();
            System.out.println("ici -->"+record);
            String message = getTextData(record.getPayload());
            System.out.println("message--->"+message);
            if (message.length() > 0) {
                final Intent redirectIntent = new Intent(this, DetailsActivity.class);
                redirectIntent.putExtra("id_LIFI", message);
                this.startActivity(redirectIntent);
            }
            j++;
        }

        /*defMessage[] messages;
        if (rawMsgs != null) {
            msgs = new NdefMessage[rawMsgs.length];
            for (int i = 0; i < rawMsgs.length; i++) {
                messages[i] = (NdefMessage) rawMsgs[i];
            }
            System.out.println("toto->" + rawMsgs);
        }*/
    }

    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onStop() {
        super.onStop();

    }
}
