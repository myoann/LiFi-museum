package lifi.lifi_museum;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class NFCActivity extends AppCompatActivity {

    public static final String TAG = "NFC Creator-Reader";
    private EditText editText;
    private Button btnShare;
    private Button btnClear;
    private CheckBox notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);

        btnShare = (Button) findViewById(R.id.buttonShare);
        btnClear = (Button) findViewById(R.id.buttonClear);
        btnClear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        NFCActivity.this);
                builder.setMessage("Voulez-vous vraiment effacer ce message ?")
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        EditText txt = (EditText) findViewById(R.id.textViewTagNFC);
                                        txt.setText("");
                                        txt.invalidate();
                                    }
                                })
                        .setNegativeButton("Annuler",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface arg0,
                                                        int id) {
                                    }

                                });
                AlertDialog alert = builder.create();
                alert.show();
            }

        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

        switch (id) {
            // if the Chocolate item is selected
            case R.id.apropos:
                // Creation et ouverture d'une boite de dialogue comportant les
                // infos de base du logiciel.
                AlertDialog.Builder alerteAPropos = new AlertDialog.Builder(
                        NFCActivity.this);
                alerteAPropos.setTitle("A propos");
                alerteAPropos
                        .setMessage("NFC Creator-Reader. \n\nVersion v2.0. \n\nRealise en collaboration par CHAABANE Mohamed, ERISEY Nicolas & LE HALPER Nicolas. \n\nCopyright (c) 2013. Tous droits reserves. ");
                // Retour au menu principal.
                alerteAPropos.setPositiveButton("Retour", null);
                alerteAPropos.show();
                return true;
            case R.id.quitter:
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        NFCActivity.this);
                builder.setTitle("Quitter")
                        .setMessage("Quitter l'application ?")
                        .setCancelable(false)
                        .setPositiveButton("Oui",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        NFCActivity.this.finish();
                                    }
                                }).setNegativeButton("Non", null);
                AlertDialog alert = builder.create();
                alert.show();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // initialisation
        String message = "";

        try {
            Bundle bundle = this.getIntent().getExtras();
            message = bundle.getString(NFCReader.MESSAGE, "");
            if (!message.equals("")) {
                ((EditText) findViewById(R.id.textViewTagNFC)).setText(message);
                Toast.makeText(this,
                        "Le message " + message + " a bien ete lu !",
                        Toast.LENGTH_SHORT).show();
                btnClear = (Button) findViewById(R.id.buttonClear);
                btnClear.setEnabled(true);
                btnClear.setTextColor(Color.WHITE);
            } else {
            }
        } catch (Exception e) {
        }
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
