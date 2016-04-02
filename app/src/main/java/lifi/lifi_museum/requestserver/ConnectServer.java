package lifi.lifi_museum.requestserver;

import android.graphics.Bitmap;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lifi.lifi_museum.sqlite.sqlite.directorymanager.AudioDirectoryManager;
import lifi.lifi_museum.sqlite.sqlite.directorymanager.ImageDirectoryManager;
import lifi.lifi_museum.sqlite.sqlite.directorymanager.VideoDirectoryManager;

/**
 * Created by user on 27/10/2015.
 */
public class ConnectServer {

    private List<Oeuvre> oeuvres = new ArrayList<>();

    protected static ConnectServer serverInstance = null;

    public ConnectServer() {

    }


    public static ConnectServer getInstance() {
        if( serverInstance == null ) {
            serverInstance = new ConnectServer();
        }

        return serverInstance;
    }

    public static class Image {
        public String url;
        public Image(){
        }
        public String getUrl(){return this.url;}
        public void setUrl(String url){this.url = url;}
    }
    public static class Video {
        public String url;
        public Video(){
        }
        public String getUrl(){return this.url;}
        public void setUrl(String url){this.url = url;}
    }
    public static class Audio {
        public String url;
        public Audio(){
        }
        public String getUrl(){return this.url;}
        public void setUrl(String url){this.url = url;}
    }
    public static class Oeuvre {
        public String name;
        public String description;
        public ArrayList<Image> images;
        public String updatedAt;
        public String id;
        public String lifi;
        public Video video;
        public Audio audio;
        public Oeuvre(){

        }
        public Oeuvre(String name, String description, String id) {
            this.name = name;
            this.description = description;
            this.id = id;
            //a voir pour les images
        }

        public Oeuvre(String name, String id) {
            this.name = name;
            this.id = id;
            //a voir pour les images
        }
        public String getLifi() {return lifi;}
        public void setLifi(String lifi) {this.lifi = lifi;}
        public Video getVideo() {return video;}
        public void setVideo(Video video) {this.video = video;}
        public Audio getAudio() {return audio;}
        public void setAudio(Audio audio) {this.audio = audio;}
        public String getUpdatedAt() {return updatedAt;}
        public void setUpdatedAt(String updatedAt) {this.updatedAt = updatedAt;}
        public String getName() {return name;}
        public void setName(String name) {this.name = name;}
        public String getDescription() {return description;}
        public void setDescription(String description) {this.description = description;}
        public ArrayList<Image> getImages() {return images;}
        public void setImages(ArrayList<Image> images) {this.images = images;}
        public String getId() {return id;}
        public void setId(String id) {this.id = id;}
    }

    public void get_oeuvres(AQuery aq, final ResultCallBack listener){

//        String url = "http://134.59.152.117:1337/images/uploads/nomimage.jpg";
        String url = "http://134.59.152.117:1337/oeuvre/getOeuvres";

        aq.ajax(url, JSONArray.class, new AjaxCallback<JSONArray>() {
            @Override
            public void callback(String url, JSONArray json, AjaxStatus status) {
                Gson gson = new Gson();
                oeuvres = gson.fromJson(json.toString(), new TypeToken<ArrayList<Oeuvre>>() {
                }.getType());
                listener.ResultCallBack();
            }
        });
        //aq.id().image(url)
    }

    public void get_images(final AQuery aq, final ResultCallBack listener, String urlImage){
    //        String url = "http://134.59.152.117:1337/images/uploads/nomimage.jpg";
        String url = "http://134.59.152.117:1337/images/uploads/"+urlImage;

        final AjaxCallback<Bitmap> cb = new AjaxCallback<Bitmap>() {
            @Override
            public void callback(String url, Bitmap bmp, AjaxStatus status) {
                try {
                    ImageDirectoryManager idm = new ImageDirectoryManager(aq.getContext(),listener);
                    String directory = idm.saveToInternalStorage(bmp, url);
//                    idm.loadImageFromStorage(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        aq.ajax(url, Bitmap.class, 0, cb);
    }

    public void get_videos(final AQuery aq, final ResultCallBack listener, String urlVideo){
        //        String url = "http://134.59.152.117:1337/images/uploads/nomimage.jpg";
        String url = "http://134.59.152.117:1337/videos/uploads/"+urlVideo;

        final AjaxCallback<File> cb = new AjaxCallback<File>() {
            @Override
            public void callback(String url, File file, AjaxStatus status) {
                try {
                    VideoDirectoryManager vdm = new VideoDirectoryManager(aq.getContext(),listener);
                    String directory = vdm.saveToInternalStorage(file, url);
//                    vdm.loadImageFromStorage(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        aq.ajax(url, File.class, 0, cb);
    }

    public void get_audios(final AQuery aq, final ResultCallBack listener, String urlAudio){
        //        String url = "http://134.59.152.117:1337/images/uploads/nomimage.jpg";
        String url = "http://134.59.152.117:1337/audios/uploads/"+urlAudio;

        final AjaxCallback<File> cb = new AjaxCallback<File>() {
            @Override
            public void callback(String url, File file, AjaxStatus status) {
                try {
                    AudioDirectoryManager adm = new AudioDirectoryManager(aq.getContext(),listener);
                    String directory = adm.saveToInternalStorage(file, url);
//                    adm.loadImageFromStorage(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        aq.ajax(url, File.class, 0, cb);
    }
    public List<Oeuvre> getOeuvres() {
        return oeuvres;
    }

}
