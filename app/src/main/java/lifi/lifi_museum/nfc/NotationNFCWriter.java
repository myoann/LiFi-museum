package lifi.lifi_museum.nfc;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import lifi.lifi_museum.R;

public class NotationNFCWriter extends AppCompatActivity {

    public NfcAdapter nfcAdapter;
    private EditText EDnote;
    private boolean continuer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notation_nfcwriter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        this.EDnote   = (EditText)findViewById(R.id.editTextNote);
       /* String tmpNbNote = getIntent().getStringExtra("nbNote");
        String tmpSommeNote = getIntent().getStringExtra("sommeNote");
        if(tmpNbNote!=null){
            this.nbNote = Integer.parseInt(tmpNbNote);
        }
        if(tmpNbNote!=null){
            this.nbNote = Integer.parseInt(tmpSommeNote);
        }*/


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
        System.out.println("icici: nbNote"+NotationNFCActivity.nbNote+"somme"+NotationNFCActivity.sommeNote);
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
        this.continuer = false;
        final Ndef ndef = Ndef.get(tag);
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    ndef.connect();
                    try {
                        int note = Integer.parseInt(NotationNFCWriter.this.EDnote.getText().toString());
                        if(EDnote.getText().toString().length()>0){
                            if(note>=0 && note<=20) {
                                synchronized (EDnote){
                                    NdefMessage message = new NdefMessage(NdefRecord.createApplicationRecord(NotationNFCActivity.sommeNote+":"+NotationNFCActivity.nbNote));
                                    ndef.writeNdefMessage(message);
                                    continuer = true;
                                }

                            }
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

        int note = Integer.parseInt(EDnote.getText().toString());
        if(note>=0 && note<=20){
            NotationNFCActivity.sommeNote += note;
            NotationNFCActivity.nbNote+=1;
            System.out.println("nbNote "+NotationNFCActivity.nbNote+" et somme "+NotationNFCActivity.sommeNote);
            Toast.makeText(NotationNFCWriter.this, "La note suivant a été ajoutée:" + EDnote.getText().toString() + "/20", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(NotationNFCWriter.this, "Vous devez saisir un nombre compris entre 0 et 20", Toast.LENGTH_LONG).show();
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
