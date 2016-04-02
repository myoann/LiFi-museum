package lifi.lifi_museum.sqlite.sqlite.directorymanager;

import android.content.Context;
import android.content.ContextWrapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import lifi.lifi_museum.requestserver.ResultCallBack;

/**
 * Created by Yoann on 2/24/2016.
 */
public class VideoDirectoryManager {
    public VideoDirectoryManager() {
    }
    Context context;
    ResultCallBack listener;
    public VideoDirectoryManager(Context context,ResultCallBack listener) {
        this.context = context;
        this.listener = listener;
    }
    public VideoDirectoryManager(Context context) {
        this.context = context;
    }

    public String saveToInternalStorage(File fileVideo, String urlVideo) throws IOException {
        ContextWrapper cw = new ContextWrapper(this.context);
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("videoDir", Context.MODE_PRIVATE);
        // Create imageDir
        int pos = urlVideo.lastIndexOf("/");
        String nameVideo = urlVideo.substring(pos+1);
        System.out.println("WRITE VIDEO ====" + directory.getAbsolutePath());
        File mypath = new File(directory,nameVideo);

        FileOutputStream fos = null;
        FileInputStream fis = new FileInputStream(fileVideo);
        try {
            fos = new FileOutputStream(mypath);

            byte[] buf = new byte[1024];
            int len;
            while ((len = fis.read(buf)) > 0) {
                fos.write(buf, 0, len);
            }
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            fos.close();
            listener.DataSaveInSQLiteCallBack();
        }
        return directory.getAbsolutePath();
    }

    public File loadImageFromStorage(String urlVideo)
    {
        int pos = urlVideo.lastIndexOf("/");
        String nameVideo = urlVideo.substring(pos + 1);
        File file = null;
        ContextWrapper cw = new ContextWrapper(this.context);
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("videoDir", Context.MODE_PRIVATE);
        System.out.println("PATH READ VIDEO ====="+directory+"/"+nameVideo);
        File f = new File(directory,nameVideo);

//            ImageView img=(ImageView)findViewById(R.id.imgPicker);
//            img.setImageBitmap(b);
        return f;

    }
}
