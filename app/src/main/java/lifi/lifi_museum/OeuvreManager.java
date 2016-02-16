package lifi.lifi_museum;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Fabrice on 16/02/2016.
 */
public class OeuvreManager {
    private static final String TABLE_NAME = "oeuvre";
    public static final String KEY_ID = "id_oeuvre";
    public static final String KEY_ID_OEUVRE="id_oeuvre_mongo";//id MONGODB
    public static final String KEY_NOM_OEUVRE="nom_oeuvre";
    public static final String KEY_DESCRIPTION_OEUVRE="description_oeuvre";
    public static final String KEY_EPOQUE_OEUVRE="epoque_oeuvre";
    public static final String DROP_TABLE_OEUVRE = "DROP TABLE IF EXISTS oeuvre;";
    public static final String CREATE_TABLE_OEUVRE = "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+
            " (" +
            " "+KEY_ID+" INTEGER primary key autoincrement," +
            " "+KEY_ID_OEUVRE+" TEXT," +
            " "+KEY_NOM_OEUVRE+" TEXT," +
            " "+KEY_DESCRIPTION_OEUVRE+" TEXT," +
            " "+KEY_EPOQUE_OEUVRE+" TEXT" +
            ");";
    private MySQLite maBaseSQLite; // notre gestionnaire du fichier SQLite
    private SQLiteDatabase db;

    // Constructeur
    public OeuvreManager(Context context) {
        maBaseSQLite = MySQLite.getInstance(context);
    }
    public void createTableOeuvre(){
        maBaseSQLite.onCreate(db);
    }
    public void dropTableOeuvre(){
        maBaseSQLite.onDrop(db);
    }
    public void open() {
        //on ouvre la table en lecture/écriture
        db = maBaseSQLite.getWritableDatabase();
    }

    public void close() {
        //on ferme l'accès à la BDD
        db.close();
    }

    public long addOeuvre(ConnectServer.Oeuvre oeuvre) {
        // Ajout d'un enregistrement dans la table
        ContentValues values = new ContentValues();
        values.put(KEY_ID_OEUVRE, oeuvre.getId());
        values.put(KEY_NOM_OEUVRE, oeuvre.getNom());
        values.put(KEY_DESCRIPTION_OEUVRE, oeuvre.getDescription());
        values.put(KEY_EPOQUE_OEUVRE, oeuvre.getEpoque());
        // insert() retourne l'id du nouvel enregistrement inséré, ou -1 en cas d'erreur
        return db.insert(TABLE_NAME,null,values);
    }

//    public int modOeuvre(ConnectServer.Oeuvre oeuvre) {
//        // modification d'un enregistrement
//        // valeur de retour : (int) nombre de lignes affectées par la requête
//        ContentValues values = new ContentValues();
//        values.put(KEY_NOM_OEUVRE, oeuvre.nom);
//        String where = KEY_ID_OEUVRE+" = ?";
//        String[] whereArgs = {oeuvre.nom+""};
//        return db.update(TABLE_NAME, values, where, whereArgs);
//    }

//    public int supOeuvre(ConnectServer.Oeuvre oeuvre) {
//        // suppression d'un enregistrement
//        // valeur de retour : (int) nombre de lignes affectées par la clause WHERE, 0 sinon
//        String where = KEY_ID_OEUVRE+" = ?";
//        String[] whereArgs = {oeuvre.nom+""};
//        return db.delete(TABLE_NAME, where, whereArgs);
//    }

    public ConnectServer.Oeuvre getOeuvre(int id) {
        // Retourne l'animal dont l'id est passé en paramètre
        ConnectServer.Oeuvre oeuvre = new ConnectServer.Oeuvre();
        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+KEY_ID_OEUVRE+"="+id, null);
        if (c.moveToFirst()) {
            oeuvre.setId(c.getString(c.getColumnIndex(KEY_ID_OEUVRE)));
            oeuvre.setNom(c.getString(c.getColumnIndex(KEY_NOM_OEUVRE)));
            oeuvre.setDescription(c.getString(c.getColumnIndex(KEY_DESCRIPTION_OEUVRE)));
            oeuvre.setEpoque(c.getString(c.getColumnIndex(KEY_EPOQUE_OEUVRE)));
            c.close();
        }
        return oeuvre;
    }

    public Cursor getOeuvres() {
        // sélection de tous les enregistrements de la table
        return db.rawQuery("SELECT * FROM "+TABLE_NAME, null);
    }


}