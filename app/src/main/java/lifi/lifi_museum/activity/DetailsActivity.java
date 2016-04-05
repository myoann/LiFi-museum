package lifi.lifi_museum.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.androidquery.AQuery;

import java.io.File;
import java.util.ArrayList;

import lifi.lifi_museum.requestserver.ConnectServer;
import lifi.lifi_museum.R;
import lifi.lifi_museum.sqlite.sqlite.directorymanager.AudioDirectoryManager;
import lifi.lifi_museum.sqlite.sqlite.directorymanager.ImageDirectoryManager;
import lifi.lifi_museum.sqlite.databasemanager.ImageManager;
import lifi.lifi_museum.sqlite.databasemanager.OeuvreManager;
import lifi.lifi_museum.sqlite.sqlite.directorymanager.VideoDirectoryManager;

public class DetailsActivity extends AppCompatActivity {
    ListView listView ;
    ConnectServer server;
    AQuery aq = new AQuery(this);
    private MediaPlayer mpintro = new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // getIntent() is a method from the started activity
        Intent redirectIntent = getIntent(); // gets the previously created intent
        ConnectServer.Oeuvre value = new ConnectServer.Oeuvre();
        value.setName("");
        value.setDescription("");
        value.setImages(new ArrayList<ConnectServer.Image>());
        ConnectServer.Audio a = new ConnectServer.Audio();
        value.setAudio(a);
        ConnectServer.Video v = new ConnectServer.Video();
        value.setVideo(v);
        if (redirectIntent.getStringExtra("id") != null && redirectIntent.getStringExtra("id") != "") {
            String idElement = redirectIntent.getStringExtra("id");
            System.out.println("DANS ID === "+idElement);

            value = this.recupererOeuvre(idElement, false);
        } else if (redirectIntent.getStringExtra("id_LIFI") != null && redirectIntent.getStringExtra("id_LIFI") != ""){
            String idElement = redirectIntent.getStringExtra("id_LIFI");
            System.out.println("DANS ID LIFI ==== "+idElement);
            value = this.recupererOeuvre(idElement, true);
        }

        setTitle(value.getName());

        TextView t=(TextView)findViewById(R.id.descriptionContent);
        t.setText(value.getDescription());

        ImageView i = (ImageView)findViewById(R.id.imageContent);

        if(value.getImages() != null) {

            if( value.getImages().size()>0 && value.getImages().get(0).getUrl() != ""){
                System.out.println("DANS DETAILS ACTIVITY IMAGE ============"+value.getImages().get(0).getUrl());
                ImageDirectoryManager idm = new ImageDirectoryManager(this);
                Bitmap bpm = idm.loadImageFromStorage(value.getImages().get(0).getUrl());
                i.setImageBitmap(bpm);
            }
        }

        if(value.getVideo().getUrl() != null) {

            if( value.getVideo().getUrl() != ""){
                System.out.println("DANS DETAILS ACTIVITY VIDEO ============" + value.getVideo().getUrl());
                VideoDirectoryManager vdm = new VideoDirectoryManager(this);
                File f = vdm.loadImageFromStorage(value.getVideo().getUrl());
                Log.d("Vidéo", "" + f);

                Intent videoPlaybackActivity = new Intent(this, VideoPlayer.class);
                videoPlaybackActivity.putExtra("filePath", f.getPath());
                startActivity(videoPlaybackActivity);
            }
        }

        if(value.getAudio().getUrl() != null) {

            if( value.getAudio().getUrl() != ""){
                System.out.println("DANS DETAILS ACTIVITY VIDEO ============"+value.getAudio().getUrl());
                AudioDirectoryManager adm = new AudioDirectoryManager(this);
                File f = adm.loadImageFromStorage(value.getAudio().getUrl());
                Log.d("Audio", ""+f);
                mpintro = MediaPlayer.create(this, Uri.parse(f.getAbsolutePath()));
                mpintro.setLooping(true);
                mpintro.start();
            }
        }
//        server = ConnectServer.getInstance();
//        server.get_oeuvres(aq, this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mpintro.isPlaying())
        mpintro.stop();
    }

    public ConnectServer.Oeuvre recupererOeuvre(String idElement, boolean isLIFI){
        ConnectServer.Oeuvre oeuvre = new ConnectServer.Oeuvre();
        OeuvreManager oeuvreManager = new OeuvreManager(this); // gestionnaire de la table "oeuvre"
        oeuvreManager.open(); // ouverture de la table en lecture/écriture
        if (isLIFI) {
            oeuvre = oeuvreManager.getOeuvreByIdLIFI(idElement);
        } else {
            oeuvre = oeuvreManager.getOeuvre(idElement);
        }
        String idUniversel = oeuvre.getId();
        ImageManager imageManager = new ImageManager(this);
        imageManager.open(); // ouverture de la table en lecture/écriture
        if (imageManager.getListImages(idUniversel).size() > 0) {
            ArrayList<ConnectServer.Image> images = imageManager.getListImages(idUniversel);
            oeuvre.setImages(images);
        }
        imageManager.close();
        oeuvreManager.close();
        return oeuvre;
    }


}
