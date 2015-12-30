package lifi.lifi_museum;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // getIntent() is a method from the started activity
        Intent redirectIntent = getIntent(); // gets the previously created intent
        String positionElement = redirectIntent.getStringExtra("position");
        String titleElement = redirectIntent.getStringExtra("value");
        String descriptionElement = redirectIntent.getStringExtra("description");
        int imageElement = redirectIntent.getIntExtra("image",0);

        setTitle(titleElement);

        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        TextView t=(TextView)findViewById(R.id.descriptionContent);
        t.setText(descriptionElement);

        ImageView i = (ImageView)findViewById(R.id.imageContent);
        i.setImageResource(imageElement);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}
