package lifi.lifi_museum;

import android.content.Context;
import android.content.ContextWrapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Yoann on 2/24/2016.
 */
public class AudioDirectoryManager {
    public AudioDirectoryManager() {
    }
    Context context;
    ResultCallBack listener;
    public AudioDirectoryManager(Context context,ResultCallBack listener) {
        this.context = context;
        this.listener = listener;
    }
    public AudioDirectoryManager(Context context) {
        this.context = context;
    }

    public String saveToInternalStorage(File fileAudio, String urlAudio) throws IOException {
        ContextWrapper cw = new ContextWrapper(this.context);
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("audioDir", Context.MODE_PRIVATE);
        // Create imageDir
        int pos = urlAudio.lastIndexOf("/");
        String nameImage = urlAudio.substring(pos+1);
        System.out.println("WRITEAUDIO ====" + directory.getAbsolutePath());
        File mypath = new File(directory,nameImage);

        FileOutputStream fos = null;
        FileInputStream fis = new FileInputStream(fileAudio);
        try {
            fos = new FileOutputStream(mypath);

            byte[] buf = new byte[1024];
            int len;
            while ((len = fis.read(buf)) > 0) {
                fos.write(buf, 0, len);
            }
            fos.flush();
            fos.close();
            listener.DataSaveInSQLiteCallBack();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            fos.close();
        }
        return directory.getAbsolutePath();
    }

    public File loadImageFromStorage(String urlAudio)
    {
        int pos = urlAudio.lastIndexOf("/");
        String nameAudio = urlAudio.substring(pos + 1);
        File file = null;
            ContextWrapper cw = new ContextWrapper(this.context);
            // path to /data/data/yourapp/app_data/imageDir
            File directory = cw.getDir("audioDir", Context.MODE_PRIVATE);
            System.out.println("AUDIO READ ====="+directory+"/"+nameAudio);
            File f = new File(directory,nameAudio);

//            ImageView img=(ImageView)findViewById(R.id.imgPicker);
//            img.setImageBitmap(b);
        return f;

    }
}
