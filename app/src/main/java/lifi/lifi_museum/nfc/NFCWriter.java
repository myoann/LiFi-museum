package lifi.lifi_museum.nfc;


import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import java.io.Console;
import java.io.IOException;
import java.util.ArrayList;

import lifi.lifi_museum.R;
import lifi.lifi_museum.activity.DetailsActivity;

public class NFCWriter extends AppCompatActivity {

    public NfcAdapter nfcAdapter;
    private TextView id_filtered, message;
    private Spinner idNFCSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_writer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        idNFCSpinner = (Spinner)findViewById(R.id.listIDOeuvre);

        String[] idOeuvres={"00100292","00100295","00100296"};
        ArrayAdapter<String> dataAdapterNFCSpinner = new ArrayAdapter<String>(this, R.layout.spinner_layout,idOeuvres);
        dataAdapterNFCSpinner.setDropDownViewResource(R.layout.spinner_layout);
        idNFCSpinner.setAdapter(dataAdapterNFCSpinner);
    }


    String getTextData(byte[] payload) {
        String str = new String(payload);
        /*String texteCode = ((payload[0] & amp; 0deux00) == 0) ' "UTF-8" : "UTF-16";
        int langageCodeTaille = payload[0] &amp; 0077;
        return new String(payload, langageCodeTaille + 1, payload.length - langageCodeTaille - 1, texteCode);*/
       /* String texteCode = ((payload[0] &amp; 0deux00) == 0) ' "UTF-8" : "UTF-16";
        int langageCodeTaille = payload[0] &amp; 0077;*/
        return str.substring(3);
    }

    protected void onResume() {
        super.onResume();
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0,new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        IntentFilter[] intentFiltersArray = new IntentFilter[] {ndef};

        String[][] techListsArray = new String[][] {new String[] {Ndef.class.getName()}};

        this.nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFiltersArray, techListsArray);
    }

    public void onPause() {
        super.onPause();
        this.nfcAdapter.disableForegroundDispatch(this);
    }



    @Override
    protected void onNewIntent(Intent intent) {
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        final Ndef ndef = Ndef.get(tag);
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    ndef.connect();
                    try {
                        if(idNFCSpinner.getSelectedItem().toString().length()==8){

                            NdefMessage message = new NdefMessage(NdefRecord.createApplicationRecord(idNFCSpinner.getSelectedItem().toString()));
                            ndef.writeNdefMessage(message);
                        }
                    }
                    catch(FormatException e) {
                        Log.e("NFC", e.getMessage(), e);
                    }
                    ndef.close();
                }
                catch(IOException e) {
                    Log.e("NFC", e.getMessage(), e);
                }
            }
        }).start();
        if(idNFCSpinner.getSelectedItem().toString().length()==8){
            Toast.makeText(NFCWriter.this, "Nouvel ID enregistré :"+idNFCSpinner.getSelectedItem().toString()+"", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(NFCWriter.this, "Vous devez saisir 6 chiffres pour l'ID", Toast.LENGTH_LONG).show();
        }

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
