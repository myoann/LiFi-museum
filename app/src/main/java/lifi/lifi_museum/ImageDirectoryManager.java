package lifi.lifi_museum;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.androidquery.AQuery;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Yoann on 2/24/2016.
 */
public class ImageDirectoryManager {
    public ImageDirectoryManager() {
    }
    Context context;
    public ImageDirectoryManager(Context context) {
        this.context = context;
    }

    public String saveToInternalStorage(Bitmap bitmapImage, String urlImage) throws IOException {
        ContextWrapper cw = new ContextWrapper(this.context);
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

    public Bitmap loadImageFromStorage(String urlImage)
    {
        int pos = urlImage.lastIndexOf("/");
        String nameImage = urlImage.substring(pos + 1);
        Bitmap b = null;
        try {

            ContextWrapper cw = new ContextWrapper(this.context);
            // path to /data/data/yourapp/app_data/imageDir
           File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
            System.out.println("PATH READ ====="+directory+"/"+nameImage);
            File f = new File(directory,nameImage);
            b = BitmapFactory.decodeStream(new FileInputStream(f));
            System.out.println("BITMAP READ ==="+b);

//            ImageView img=(ImageView)findViewById(R.id.imgPicker);
//            img.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return b;

    }
}
