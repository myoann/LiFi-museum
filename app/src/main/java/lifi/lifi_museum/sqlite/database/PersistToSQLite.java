package lifi.lifi_museum.sqlite.database;

import android.app.ProgressDialog;
import android.content.Context;

import com.androidquery.AQuery;

import lifi.lifi_museum.requestserver.ConnectServer;
import lifi.lifi_museum.requestserver.ResultCallBack;
import lifi.lifi_museum.sqlite.databasemanager.ImageManager;
import lifi.lifi_museum.sqlite.databasemanager.OeuvreManager;

/**
 * Created by Fabrice on 22/03/2016.
 */
public class PersistToSQLite implements ResultCallBack {
    ConnectServer server = ConnectServer.getInstance();
    AQuery aq;
    int totalNbMedia = 0;
    int currNbMedia = 0;
    boolean totalNbMediaIsSet = false;
    Context settingsActivity;
    public ProgressDialog progress;
//    final Thread t = new Thread() {
//        @Override
//        public void run() {
//
//            while(currNbMedia < totalNbMedia) {
//                try {
//                    sleep(200);
//                    progress.setProgress(currNbMedia);
//                }
//                catch (InterruptedException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
//        }
//    };



    public PersistToSQLite(AQuery aq){
        this.aq = aq;
    }
    public void persistDataToSQLite(Context settingsActivity){
        server.get_oeuvres(aq, this);
        this.settingsActivity = settingsActivity;
        this.progress = new ProgressDialog( this.settingsActivity);
    }

    @Override
    public void ResultCallBack() {

        progress.setMessage("Téléchargement des données");
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setIndeterminate(false);
        progress.setProgress(0);

//        t.start();

        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! JSON RECUPEREEEEEE !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

        OeuvreManager oeuvreManager = new OeuvreManager(aq.getContext()); // gestionnaire de la table "oeuvre"
        ImageManager imageManager = new ImageManager(aq.getContext()); // gestionnaire de la table "oeuvre"
        oeuvreManager.open(); // ouverture de la table en lecture/écriture
        imageManager.open();
        oeuvreManager.dropTableOeuvre();
        imageManager.dropTableImage();
        oeuvreManager.createTableOeuvre();
        imageManager.createTableImage();
        System.out.println("------------------------------ LES OEUVRES ------------------------------");
        for (int i =0;i<server.getOeuvres().size();i++){
            oeuvreManager.addOeuvre(server.getOeuvres().get(i));
            if(server.getOeuvres().get(i).getImages() != null) {
                for(int j=0;j<server.getOeuvres().get(i).getImages().size();j++){
                    server.get_images(aq, this, server.getOeuvres().get(i).getImages().get(j).getUrl());
                    imageManager.addImage(server.getOeuvres().get(i).getImages().get(j), server.getOeuvres().get(i).getId());
                    totalNbMedia++;
                }
            }
            if(server.getOeuvres().get(i).getVideo()!= null){
                server.get_videos(aq, this, server.getOeuvres().get(i).getVideo().getUrl());
                totalNbMedia++;
            }
            if(server.getOeuvres().get(i).getAudio()!= null){
                server.get_audios(aq, this, server.getOeuvres().get(i).getAudio().getUrl());
                totalNbMedia++;
            }
        }
        System.out.println("TESTTESTTESTTESTTESTTESTTESTTESTTEST");

        oeuvreManager.close();
        imageManager.close();
        progress.show();
    }

    @Override
    public void DataSaveInSQLiteCallBack() {
        if(totalNbMediaIsSet != true){
            this.progress.setMax(totalNbMedia);
        }
        totalNbMediaIsSet = true;
        currNbMedia++;
        System.out.println("totalNbMedia = " + totalNbMedia + " currNbMedia =" + currNbMedia);
        this.progress.setProgress(currNbMedia);

        if(totalNbMedia == currNbMedia){
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!! CHARGEMENT TERMINE !!!!!!!!!!!!!!!!!!!!!!!!");
            this.progress.hide();
//            t.stop();
        }
    }
}
