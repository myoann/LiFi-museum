package lifi.lifi_museum.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.androidquery.AQuery;

import lifi.lifi_museum.R;
import lifi.lifi_museum.sqlite.database.PersistToSQLite;

public class SettingsActivity extends AppCompatActivity {
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        button = (Button) findViewById(R.id.btn_synchronier);
        button.setOnClickListener(synchronation_db);
    }

    View.OnClickListener synchronation_db = new View.OnClickListener() {
        public void onClick(View v) {
            PersistToSQLite persistToSQLite = new PersistToSQLite(new AQuery(SettingsActivity.this));
            persistToSQLite.persistDataToSQLite(SettingsActivity.this);
        }
    };

}
