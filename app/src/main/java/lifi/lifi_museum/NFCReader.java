package lifi.lifi_museum;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by Yoann on 12/15/2015.
 */
public class NFCReader extends AppCompatActivity {

    public static final String TAG = "NFC Creator-Reader";

    public static final int REQUEST_CODE = 1000;
    private PendingIntent mPendingIntent;
    private IntentFilter ndefDetected;
    public final static String MESSAGE = "message";
    private static String message = "";
    private NfcAdapter nfcAdapter = null;
    private NdefMessage msg = null;
    private Menu leMenu = null;
    public final static String DOMAIN = "http://www.mbds-fr.org/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(TAG);
        setContentView(R.layout.nfcreading);
        if (message.equals("")) {
            try {
                Bundle bundle = this.getIntent().getExtras();
                message = bundle.getString(MESSAGE, "");
            } catch (Exception e) {
                // pas de message :)
            }
        }
    }


    public NdefMessage createNdefMessage(String text, String mimeType)
    {
        System.out.println("DANS createNdefMessage");

        //Message de type MIME270
        NdefMessage msg = new NdefMessage(
                NdefRecord.createUri(text)
        );
        return msg;
    }

    @Override
    public void onNewIntent(Intent intent) {
        System.out.println("DANS onNewIntent");

        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action) ||
                NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action) ||
                NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
            resolveIntent(intent) ;
        }
    }

    @SuppressLint("NewApi")
    private void resolveIntent(Intent intent) {
        System.out.println("DANS resolveIntent");

        boolean isNdefMsgFound = false;
        boolean isWritable = false;
        Tag tag = null;
        Ndef ndef = null;
        int content = -1;
        String[] technologies = null;
        try {
            //Infos sur le tag
            tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            technologies = tag.getTechList();
            content = tag.describeContents();
            ndef = Ndef.get(tag);
            isWritable = ndef.isWritable();
            boolean canMakeReadOnly = ndef.canMakeReadOnly();
            byte[] id =tag.getId();
        } catch (Exception e) {
            // il ne s'agit pas d'un tag....
        }

        if (tag!=null && !message.equals("") && isWritable) {
            Toast.makeText(this, "Enregistrement sur le tag reussi !",
                    Toast.LENGTH_SHORT).show();
            //Ecriture
            writeTag(msg, tag);

            message = "";
            msg=null;


            Intent main = new Intent(getBaseContext(), NFCActivity.class);
            startActivity(main);
            finish();
            // On ne lit pas le contenu...
            return;
        }

        //Lecture
        Parcelable[] rawMsgs =
                intent.getParcelableArrayExtra(
                        NfcAdapter.EXTRA_NDEF_MESSAGES);
        NdefMessage[] msgs;
        String receivedMessages = "";
        if (rawMsgs != null) {
            try {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                    NdefRecord record = msgs[i].getRecords()[i];
                    System.out.println("record  ============="+record );

                    //Infos sur le tag...
                    //byte[] idRec = "".getBytes();
                    //short tnf = 0;
                    byte[] type = "".getBytes();
                    try {
                        //idRec = record.getId();
                        //tnf = record.getTnf();
                        type = record.getType();
                    } catch (Exception e) {
                    }
                    //Message contenu sur le tag sous forme d'URI
                    if (Arrays.equals(type, NdefRecord.RTD_SMART_POSTER) ||
                            Arrays.equals(type, NdefRecord.RTD_URI) ||
                            Arrays.equals(type, NdefRecord.RTD_TEXT)) {
                        Uri uri = record.toUri();
                        System.out.println("URI");
                        System.out.println(uri);
                        System.out.println("FIN URI");
                        receivedMessages += uri.toString().replace(DOMAIN,"")+" ";
                    }
                    isNdefMsgFound = true;
                }
            } catch (Exception e) {
                System.out.println("======================================================");

                System.out.println(e);
                System.out.println("lll======================================================");

                Toast.makeText(this, "NDEF type not managed!..",
                        Toast.LENGTH_SHORT).show();
            }
        }
        if (isNdefMsgFound) {
            Bundle bundle = new Bundle();
            bundle.putString(MESSAGE, receivedMessages);
            Intent main = new Intent(getBaseContext(), NFCActivity.class);
            main.putExtras(bundle);
            startActivity(main);
            finish();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        if (!message.equals("")) {
            NfcAdapter nfcAdapter =
                    NfcAdapter.getDefaultAdapter(getApplicationContext());
            msg = createNdefMessage(DOMAIN+message, "text/plain");
            nfcAdapter.setNdefPushMessage(msg, this);
        }

        if (nfcAdapter==null)
            nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
                getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        // Intent filters
        ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        Intent intent = getIntent();
        //Lecture/Ecriture...
        resolveIntent(intent);
    }

    public static boolean writeTag(final NdefMessage msg, final Tag	tag) {
        System.out.println("DANS WRITE TAG");
        try {
            int size = msg.toByteArray().length;
            Ndef ndef = Ndef.get(tag);
            if (ndef != null) {
                ndef.connect();
                if (!ndef.isWritable()) {
                    return false;
                }
                if (ndef.getMaxSize() < size) {
                    return false;
                }
                ndef.writeNdefMessage(msg);
                ndef.close();
                return true;
            } else {
                NdefFormatable format =	NdefFormatable.get(tag);
                if (format != null) {

                    try {
                        format.connect();
                        format.format(msg);
                        format.close() ;
                        return true;
                    } catch (IOException e) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_nfc, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setResult(RESULT_CANCELED);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void onDestroy() {
        super.onDestroy();
    }
}

