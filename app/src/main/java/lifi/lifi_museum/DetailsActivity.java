package lifi.lifi_museum;

import android.content.Intent;
import android.database.Cursor;
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

public class DetailsActivity extends AppCompatActivity implements ResultCallBack {
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
            value = this.recupererOeuvre(idElement, false);
        } else if (redirectIntent.getStringExtra("id_LIFI") != null && redirectIntent.getStringExtra("id_LIFI") != ""){
            String idElement = redirectIntent.getStringExtra("id_LIFI");
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
                
                VideoView videoView = (VideoView) findViewById(R.id.videoView);
                MediaController mediaController = new MediaController(this);
                mediaController.setAnchorView(videoView);
                videoView.setMediaController(mediaController);
                videoView.setZOrderOnTop(true);
                videoView.setVideoURI(Uri.parse(f.getAbsolutePath()));
                videoView.start();
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
        server = ConnectServer.getInstance();
        server.get_oeuvres(aq, this);

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
        for (int i = 0; i < server.getOeuvres().size(); i++) {
            oeuvreManager.addOeuvre(server.getOeuvres().get(i));
            if(server.getOeuvres().get(i).getImages() != null) {

                for (int j = 0; j < server.getOeuvres().get(i).getImages().size(); j++) {
                    imageManager.addImage(server.getOeuvres().get(i).getImages().get(j), server.getOeuvres().get(i).getId());
                }
            }
        }
        Cursor cursorOeuvre = oeuvreManager.getOeuvres();
        Cursor cursorImage = imageManager.getImages();
        if (cursorOeuvre.moveToFirst()) {
            do {

                String urlVideo = cursorOeuvre.getString(cursorOeuvre.getColumnIndex(OeuvreManager.KEY_VIDEO_OEUVRE));
                if(urlVideo != null && urlVideo != "")
                server.get_videos(aq, this, urlVideo);
                String urlAudio = cursorOeuvre.getString(cursorOeuvre.getColumnIndex(OeuvreManager.KEY_AUDIO_OEUVRE));
                if(urlAudio != null && urlAudio != "")
                server.get_audios(aq, this, urlAudio);
                Log.d("OEUVRE",
                        cursorOeuvre.getString(cursorOeuvre.getColumnIndex(OeuvreManager.KEY_ID_OEUVRE)) + ",\n" +
                        cursorOeuvre.getString(cursorOeuvre.getColumnIndex(OeuvreManager.KEY_NOM_OEUVRE)) + "\n" +
                        cursorOeuvre.getString(cursorOeuvre.getColumnIndex(OeuvreManager.KEY_VIDEO_OEUVRE)) + "\n" +
                        cursorOeuvre.getString(cursorOeuvre.getColumnIndex(OeuvreManager.KEY_AUDIO_OEUVRE)) + "\n"
//                        cursorOeuvre.getString(cursorOeuvre.getColumnIndex(OeuvreManager.KEY_DESCRIPTION_OEUVRE)) + ",\n" +
//                        cursorOeuvre.getString(cursorOeuvre.getColumnIndex(OeuvreManager.KEY_UPDATEAT_OEUVRE)) + ""
                );
            }
            while (cursorOeuvre.moveToNext());
        }
        if (cursorImage.moveToFirst()) {
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
}
