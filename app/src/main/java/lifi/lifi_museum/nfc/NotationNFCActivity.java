package lifi.lifi_museum.nfc;

import android.app.PendingIntent;
import android.content.Intent;
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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import lifi.lifi_museum.R;
import lifi.lifi_museum.activity.DetailsActivity;

public class NotationNFCActivity extends AppCompatActivity {
    public NfcAdapter nfcAdapter;
    private TextView TVNote;
    private Button bNotation;
    private Button bNoter;
    public static int nbNote=0;
    public static int sommeNote=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notation_nfc);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        this.TVNote = (TextView) findViewById(R.id.TVNote);
        this.bNotation = (Button) findViewById(R.id.Noter_btn);
        this.bNotation.setVisibility(View.INVISIBLE);

        this.bNotation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent redirectIntent = new Intent(NotationNFCActivity.this, NotationNFCWriter.class);
                /*redirectIntent.putExtra("nbNote", NotationNFCActivity.this.nbNote+"");
                redirectIntent.putExtra("sommeNote", NotationNFCActivity.this.sommeNote+"");*/
                NotationNFCActivity.this.startActivity(redirectIntent);
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    String getTextData(byte[] payload) {
        String str = new String(payload);
        /*String texteCode = ((payload[0] & amp; 0deux00) == 0) ' "UTF-8" : "UTF-16";
        int langageCodeTaille = payload[0] &amp; 0077;
        return new String(payload, langageCodeTaille + 1, payload.length - langageCodeTaille - 1, texteCode);*/
       /* String texteCode = ((payload[0] &amp; 0deux00) == 0) ' "UTF-8" : "UTF-16";
        int langageCodeTaille = payload[0] &amp; 0077;*/
        return str;//.substring(3);
    }

    @Override
    protected void onResume() {
        super.onResume();
       /* this.sommeNote = 0;
        this.nbNote = 0;
        this.bNotation.setVisibility(View.INVISIBLE);
        this.TVNote.setText("");*/
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
        String message = "";
        int j = 0;
        for(NdefMessage m : messages){
            NdefRecord record = m.getRecords()[j];
            byte[] id = record.getId();
            short tnf = record.getTnf();
            byte[] type = record.getType();
            message = this.getTextData(record.getPayload());

            j++;
        }
        if (message.length() > 0) {
            System.out.println("message--->" + message);
            String[] buf1 = message.split(":");
            if(buf1.length>1){
                sommeNote = Integer.parseInt(buf1[0]);
                nbNote = Integer.parseInt(buf1[1]);
                this.TVNote.setText("Moyenne : " + this.calculerMoyenne(sommeNote,nbNote) + "/20");
            }else{
                this.TVNote.setText("Pas de note disponible pour cette oeuvre");
            }

            this.bNotation.setVisibility(View.VISIBLE);
            final Intent redirectIntent = new Intent(this, DetailsActivity.class);
           /* redirectIntent.putExtra("id_LIFI", unmessage);
            this.startActivity(redirectIntent);*/
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

    public int calculerMoyenne(int asommeNote, int anbNote){
        if(anbNote ==0){
            return 0;
        }
        return asommeNote/anbNote;
    }

}
