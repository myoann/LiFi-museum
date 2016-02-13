package lifi.lifi_museum;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.Transformer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

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

    public static class Oeuvre {

        public String nom;
        public String description;
        public String epoque;
        public String createdAt;
        public String updatedAt;
        public String id;
    }

    private static class GsonTransformer implements Transformer {

        public <T> T transform(String url, Class<T> type, String encoding, byte[] data, AjaxStatus status) {
            Gson g = new Gson();
            return g.fromJson(new String(data), type);
        }
    }

    public void get_oeuvres(AQuery aq, final ResultCallBack listener){

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
    }

    public List<Oeuvre> getOeuvres() {
        return oeuvres;
    }

}
