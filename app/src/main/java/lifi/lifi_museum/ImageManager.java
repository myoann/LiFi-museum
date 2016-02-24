package lifi.lifi_museum;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Fabrice on 22/02/2016.
 */
public class ImageManager {
    private static final String TABLE_NAME = "image";
    public static final String KEY_ID = "id_image";
    public static final String KEY_URL_IMAGE = "url_image";
    public static final String KEY_FOREIGNKEY_OEUVRE_IMAGE = "oeuvreID_mongo";
    public static final String DROP_TABLE_IMAGE = "DROP TABLE IF EXISTS "+TABLE_NAME+" ;";
    public static final String CREATE_TABLE_IMAGE = "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+
            " (" +
            " "+KEY_ID+" INTEGER primary key autoincrement," +
            " "+KEY_URL_IMAGE+" TEXT,"+
            " "+KEY_FOREIGNKEY_OEUVRE_IMAGE+" INTEGER,"+
            " "+"FOREIGN KEY("+KEY_FOREIGNKEY_OEUVRE_IMAGE+") REFERENCES "+OeuvreManager.TABLE_NAME+"("+OeuvreManager.KEY_ID+")"+
            ");";
    private MySQLite maBaseSQLite; // notre gestionnaire du fichier SQLite
    private SQLiteDatabase db;

    // Constructeur
    public ImageManager(Context context) {
        maBaseSQLite = MySQLite.getInstance(context);
    }
    public void createTableImage(){
        this.onCreate(db);
    }
    public void dropTableImage(){
        this.onDrop(db);
    }
    public void open() {
        //on ouvre la table en lecture/écriture
        db = maBaseSQLite.getWritableDatabase();
    }

    public void close() {
        //on ferme l'accès à la BDD
        db.close();
    }

    public long addImage(ConnectServer.Image image,String foreignkey_oeuvre) {
        // Ajout d'un enregistrement dans la table
        ContentValues values = new ContentValues();
        values.put(KEY_URL_IMAGE, image.getUrl());
        values.put(KEY_FOREIGNKEY_OEUVRE_IMAGE,foreignkey_oeuvre);
        // insert() retourne l'id du nouvel enregistrement inséré, ou -1 en cas d'erreur
        return db.insert(TABLE_NAME,null,values);
    }

    public int supImage(ConnectServer.Image image) {
        // suppression d'un enregistrement
        // valeur de retour : (int) nombre de lignes affectées par la clause WHERE, 0 sinon
        String where = KEY_ID+" = ?";
        String[] whereArgs = {image.getUrl()+""};
        return db.delete(TABLE_NAME, where, whereArgs);
    }


    public Cursor getImages() {
        // sélection de tous les enregistrements de la table
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Création de la base de données
        System.out.println(sqLiteDatabase);
        sqLiteDatabase.execSQL(CREATE_TABLE_IMAGE); // création table "oeuvre"
    }
    public void onDrop(SQLiteDatabase sqLiteDatabase) {
        // Suppression de la base de données
        System.out.println(sqLiteDatabase);
        sqLiteDatabase.execSQL(DROP_TABLE_IMAGE); // création table "oeuvre"
    }
}
