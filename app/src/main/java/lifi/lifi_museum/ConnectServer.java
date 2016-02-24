package lifi.lifi_museum;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.Transformer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    public static class Oeuvre {
        public String name;
        public String description;
        public ArrayList<Image> images;
        public String updatedAt;
        public String id;
        public Oeuvre(){
        }

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
                    String directory = saveToInternalStorage(bmp, aq, url);
                    loadImageFromStorage(directory,url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        aq.ajax(url, Bitmap.class, 0, cb);
    }

    private String saveToInternalStorage(Bitmap bitmapImage,AQuery aq, String urlImage) throws IOException {
        ContextWrapper cw = new ContextWrapper(aq.getContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        int pos = urlImage.lastIndexOf("/");
        String nameImage = urlImage.substring(pos+1);
        System.out.println("nameImage WRITE ====="+nameImage);
        File mypath = new File(directory,nameImage);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            fos.close();
        }
        System.out.println("WRITE ====" + directory.getAbsolutePath());
        return directory.getAbsolutePath();
    }

    private void loadImageFromStorage(String directory, String urlImage)
    {
        int pos = urlImage.lastIndexOf("/");
        String nameImage = urlImage.substring(pos+1);
        try {

//            ContextWrapper cw = new ContextWrapper(aq.getContext());
            // path to /data/data/yourapp/app_data/imageDir
//            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
            System.out.println("PATH READ ====="+directory+"/"+nameImage);
            File f = new File(directory,nameImage);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            System.out.println("BITMAP READ ==="+b);
//            ImageView img=(ImageView)findViewById(R.id.imgPicker);
//            img.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }
    public List<Oeuvre> getOeuvres() {
        return oeuvres;
    }

}
