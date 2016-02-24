package lifi.lifi_museum;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.androidquery.AQuery;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.sql.SQLOutput;
import java.util.ArrayList;

public class ArtsListingActivity extends AppCompatActivity implements ResultCallBack {
    ListView listView ;
    ConnectServer server;
    AQuery aq = new AQuery(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_arts_listing);

        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.list);

        final int[] images = new int[] {
                R.drawable.joconde,
                R.drawable.lecri,
                R.drawable.portraitadele,
                R.drawable.baldumoulin,
                R.drawable.doramaarauchat,
                R.drawable.garconalapipe,
                R.drawable.guernica,
                R.drawable.lereve
        };

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data
        ArrayList<ConnectServer.Oeuvre> values = this.recupererListeOeuvre();
        final ArrayList<String> valuesId = new ArrayList<String>();
        ArrayList<String> valuesName = new ArrayList<String>();
        for (int i=0; i<values.size(); i++) {
            valuesId.add(values.get(i).getId());
            valuesName.add(values.get(i).getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, valuesName);


        // Assign adapter to ListView
        listView.setAdapter(adapter);

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;

                // ListView Clicked item value
                String itemValue = (String) listView.getItemAtPosition(position);

                // Show Alert
                Toast.makeText(getApplicationContext(),
                        "Position :" + itemPosition + "  ListItem : " + itemValue, Toast.LENGTH_LONG)
                        .show();

                // redirect to new page
                Intent redirectIntent = new Intent(ArtsListingActivity.this, DetailsActivity.class);
//                redirectIntent.putExtra("position", itemPosition);
                redirectIntent.putExtra("id", valuesId.get(itemPosition));
                startActivity(redirectIntent);

            }

        });


        server = ConnectServer.getInstance();
        server.get_oeuvres(aq, this);

        // Add the Up Action
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    public ArrayList<ConnectServer.Oeuvre> recupererListeOeuvre(){
       ArrayList<ConnectServer.Oeuvre> res = new ArrayList<ConnectServer.Oeuvre>();
        String id;
        String name;
        ConnectServer.Oeuvre oeuvre = new ConnectServer.Oeuvre();
        OeuvreManager oeuvreManager = new OeuvreManager(this); // gestionnaire de la table "oeuvre"
       oeuvreManager.open(); // ouverture de la table en lecture/écriture

        Cursor cursorOeuvre = oeuvreManager.getOeuvres();

        if (cursorOeuvre.moveToFirst())
        {
            do {
                id = cursorOeuvre.getString(cursorOeuvre.getColumnIndex(OeuvreManager.KEY_ID_OEUVRE));
                name = cursorOeuvre.getString(cursorOeuvre.getColumnIndex(OeuvreManager.KEY_NOM_OEUVRE));
                oeuvre = new ConnectServer.Oeuvre(name, id);
                res.add(oeuvre);
                //                Log.d("OEUVRE",
//                        cursorOeuvre.getString(cursorOeuvre.getColumnIndex(OeuvreManager.KEY_ID_OEUVRE)) + ",\n" +
//                                cursorOeuvre.getString(cursorOeuvre.getColumnIndex(OeuvreManager.KEY_NOM_OEUVRE)) + "\n"
//                        cursorOeuvre.getString(cursorOeuvre.getColumnIndex(OeuvreManager.KEY_DESCRIPTION_OEUVRE)) + ",\n" +
//                                cursorOeuvre.getString(cursorOeuvre.getColumnIndex(OeuvreManager.KEY_UPDATEAT_OEUVRE)) + ""
//                );
            }
            while (cursorOeuvre.moveToNext());
        }
        return res;
    }

    @Override
    public void ResultCallBack() {

        Log.d("Oeuvre 1", server.getOeuvres().get(0) + "");
        OeuvreManager oeuvreManager = new OeuvreManager(this); // gestionnaire de la table "oeuvre"
        ImageManager imageManager = new ImageManager(this); // gestionnaire de la table "oeuvre"
        oeuvreManager.open(); // ouverture de la table en lecture/écriture
        imageManager.open();
        oeuvreManager.dropTableOeuvre();
        imageManager.dropTableImage();
        oeuvreManager.createTableOeuvre();
        imageManager.createTableImage();
        System.out.println("---------------Oeuvre SIZE :" + server.getOeuvres().size() + " ---------------");
        for (int i =0;i<server.getOeuvres().size();i++){
            oeuvreManager.addOeuvre(server.getOeuvres().get(i));
            for(int j=0;j<server.getOeuvres().get(i).getImages().size();j++){
                imageManager.addImage(server.getOeuvres().get(i).getImages().get(j), server.getOeuvres().get(i).getId());
            }
        }
        Cursor cursorOeuvre = oeuvreManager.getOeuvres();
        Cursor cursorImage = imageManager.getImages();
        if (cursorOeuvre.moveToFirst())
        {
            do {
                Log.d("OEUVRE",
                        cursorOeuvre.getString(cursorOeuvre.getColumnIndex(OeuvreManager.KEY_ID_OEUVRE)) + ",\n" +
                        cursorOeuvre.getString(cursorOeuvre.getColumnIndex(OeuvreManager.KEY_NOM_OEUVRE)) + "\n"
//                        cursorOeuvre.getString(cursorOeuvre.getColumnIndex(OeuvreManager.KEY_DESCRIPTION_OEUVRE)) + ",\n" +
//                        cursorOeuvre.getString(cursorOeuvre.getColumnIndex(OeuvreManager.KEY_UPDATEAT_OEUVRE)) + ""
                );
            }
            while (cursorOeuvre.moveToNext());
        }
        if (cursorImage.moveToFirst())
        {
            do {
                String urlImage = cursorImage.getString(cursorImage.getColumnIndex(ImageManager.KEY_URL_IMAGE));
                Log.d("IMAGE",
                    cursorImage.getString(cursorImage.getColumnIndex(ImageManager.KEY_URL_IMAGE)) + ",\n" +
                    cursorImage.getString(cursorImage.getColumnIndex(ImageManager.KEY_FOREIGNKEY_OEUVRE_IMAGE)) + "\n"
                );

                server.get_images(aq, this, urlImage);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            while (cursorImage.moveToNext());

        }
        cursorImage.close();
        cursorOeuvre.close(); // fermeture du curseur
        // fermeture du gestionnaire
        oeuvreManager.close();
        imageManager.close();
    }
//    @Override
//    public void ResultCallBackImage(){
//
//    }

}
