package nmarin.appgenerators.com.gavica.database_helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import nmarin.appgenerators.com.gavica.model.Compartiment;


public class OpenHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "suitcaseManager";

    // Table Names
    private static final String TABLE_COSAS = "things";
    private static final String TABLE_COMPS = "compartments";
    private static final String TABLE_MALETAS = "suitcases";
    private static final String TABLE_COSA_COMP = "things_comp";
    private static final String TABLE_ATTR = "attributes";
    private static final String TABLE_RELACIONES = "relations";

    // Common column names
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";

    // things and compartments table - no additional columns

    // suitcases Table - column names
    private static final String KEY_TYPE = "type";
    private static final String KEY_DATE_START = "dateStart";
    private static final String KEY_DATE_RETURN = "dateReturn";

    // thing_comp Table - column names
    private static final String KEY_COSA = "thing";
    private static final String KEY_COMP = "comp";

    // attributes Table - column names
    private static final String KEY_NUMBER = "number";
    private static final String KEY_ATRIB1 = "atrib1";
    private static final String KEY_ATRIB2 = "atrib2";
    private static final String KEY_ATRIB3 = "atrib3";
    private static final String KEY_ATRIB4 = "atrib4";

    // relations Table - column names
    private static final String KEY_COSA_COMP = "comp";
    private static final String KEY_MALETA = "suit";
    private static final String KEY_CHECKED = "checked";
    private static final String KEY_CRITICAL = "critical";
    private static final String KEY_ATTRIBUTES = "attrs";

    // Table Create Statements
    // Thing table create statement
    private static final String CREATE_TABLE_COSA = "CREATE TABLE "
            + TABLE_COSAS + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TITLE
            + " TEXT" + ")";

    private static final String CREATE_TABLE_COMPS = "CREATE TABLE "
            + TABLE_COMPS + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TITLE
            + " TEXT" + ")";

    private static final String CREATE_TABLE_MALETAS = "CREATE TABLE "
            + TABLE_MALETAS + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TITLE
            + " TEXT," + KEY_TYPE + " TEXT," + KEY_DATE_START + " TEXT," + KEY_DATE_RETURN + " TEXT" + ")";

    private static final String CREATE_TABLE_COSA_COMP = "CREATE TABLE "
            + TABLE_COSA_COMP + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_COSA
            + " TEXT," + KEY_COMP +" TEXT" + ")";

    // relations table create statement
    private static final String CREATE_TABLE_ATTRIBUTES = "CREATE TABLE "
            + TABLE_ATTR + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_NUMBER + " INTEGER," + KEY_ATRIB1 + " TEXT,"
            + KEY_ATRIB2 + " TEXT," + KEY_ATRIB3 + " TEXT," + KEY_ATRIB4 + " TEXT" + ")";


    // relations table create statement
    private static final String CREATE_TABLE_RELACIONES = "CREATE TABLE "
            + TABLE_RELACIONES + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_COSA_COMP + " INTEGER," + KEY_MALETA + " INTEGER,"
            + KEY_CHECKED + " INTEGER," + KEY_CRITICAL + " INTEGER," + KEY_ATTRIBUTES + " INTEGER" + ")";

    public OpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        initDatabase(); // Genero una sola instancia.
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_COSA);
        db.execSQL(CREATE_TABLE_COMPS);
        db.execSQL(CREATE_TABLE_MALETAS);
        db.execSQL(CREATE_TABLE_COSA_COMP);
        db.execSQL(CREATE_TABLE_ATTRIBUTES);
        db.execSQL(CREATE_TABLE_RELACIONES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COSAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MALETAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COSA_COMP);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RELACIONES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTR);

        // create new tables
        onCreate(db);
    }


    // METODOS CREATE

    public long createThing(Cosa cosa) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, cosa.getTitle());

        return db.insert(TABLE_COSAS, null, values);
    }

    public long createComp(Compartiment comp) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, comp.getTitle());

        return db.insert(TABLE_COMPS, null, values);
    }

    public long createSuitCase(Maleta maleta) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, maleta.getTitle());
        values.put(KEY_TYPE, maleta.getType());
        values.put(KEY_DATE_START, maleta.getDateStart());
        values.put(KEY_DATE_RETURN, maleta.getDateReturn());

        return db.insert(TABLE_MALETAS, null, values);
    }

    public long createCosaComp(long thing_id, long comp_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_COSA, thing_id);
        values.put(KEY_COMP, comp_id);

        return db.insert(TABLE_COSA_COMP, null, values);
    }

    public long createAttributes(Atributo attr) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NUMBER, attr.getNumero());
        values.put(KEY_ATRIB1, attr.getAtrib_1());
        values.put(KEY_ATRIB2, attr.getAtrib_2());
        values.put(KEY_ATRIB3, attr.getAtrib_3());
        values.put(KEY_ATRIB4, attr.getAtrib_4());

        return db.insert(TABLE_ATTR, null, values);
    }



    // Genera una nueva línea de Relations. Línea vital del sistema.
    public long createRelations(long cosa_comp_id, long suit_id, int checked, int critical, int attrs){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_COSA_COMP, cosa_comp_id);
        values.put(KEY_MALETA, suit_id);
        values.put(KEY_CHECKED, checked);
        values.put(KEY_CRITICAL, critical);
        values.put(KEY_ATTRIBUTES, attrs);

        return  db.insert(TABLE_RELACIONES, null, values);
    }


    // METODOS GET

    public Cosa getCosa(long cosa_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        String selectQuery = "SELECT  * FROM " + TABLE_COSAS + " WHERE "
                + KEY_ID + " = " + cosa_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Cosa td = new Cosa();
        td.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        td.setTitle((c.getString(c.getColumnIndex(KEY_TITLE))));
        c.close();
        db.setTransactionSuccessful();
        db.endTransaction();
        return td;
    }

    public long getCosaIdFromTitle(String title) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        String selectQuery = "SELECT  "+ KEY_ID + " FROM " + TABLE_COSAS + " WHERE "
                + KEY_TITLE + " = " + "'" +title + "'";

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        long td =(c.getInt(c.getColumnIndex(KEY_ID)));
        c.close();
        db.setTransactionSuccessful();
        db.endTransaction();
        return td;
    }


    public Compartiment getComp(long comp_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        String selectQuery = "SELECT  * FROM " + TABLE_COMPS + " WHERE "
                + KEY_ID + " = " + comp_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Compartiment td = new Compartiment();
        td.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        td.setTitle((c.getString(c.getColumnIndex(KEY_TITLE))));
        c.close();
        db.setTransactionSuccessful();
        db.endTransaction();
        return td;
    }

    public long getCompIdFromTitle(String title) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        String selectQuery = "SELECT  "+ KEY_ID + " FROM " + TABLE_COMPS + " WHERE "
                + KEY_TITLE + " = "+ "'" +title + "'";

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        long td =(c.getInt(c.getColumnIndex(KEY_ID)));
        c.close();
        db.setTransactionSuccessful();
        db.endTransaction();
        return td;
    }

    public Maleta getSuitcase(long maleta_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        String selectQuery = "SELECT  * FROM " + TABLE_MALETAS + " WHERE "
                + KEY_ID + " = " + maleta_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Maleta td = new Maleta();
        td.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        td.setTitle((c.getString(c.getColumnIndex(KEY_TITLE))));
        td.setType((c.getString(c.getColumnIndex(KEY_TYPE))));
        td.setDateStart((c.getString(c.getColumnIndex(KEY_DATE_START))));
        td.setDateReturn((c.getString(c.getColumnIndex(KEY_DATE_RETURN))));
        c.close();
        db.setTransactionSuccessful();
        db.endTransaction();
        return td;
    }

    public Comp_Cosa getCompCosa(long comp_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        String selectQuery = "SELECT  * FROM " + TABLE_COSA_COMP + " WHERE "
                + KEY_ID + " = " + comp_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Comp_Cosa td = new Comp_Cosa();
        td.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        td.setComp((c.getInt(c.getColumnIndex(KEY_COMP))));
        td.setCosa((c.getInt(c.getColumnIndex(KEY_COSA))));
        c.close();
        db.setTransactionSuccessful();
        db.endTransaction();
        return td;
    }

    public long getCompCosaByIds(long comp_id, long cosa_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        String selectQuery = "SELECT  "+ KEY_ID +" FROM " + TABLE_COSA_COMP + " WHERE "
                + KEY_COMP + " = " + comp_id +" AND " + KEY_COSA + " = " + cosa_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        long td = (c.getInt(c.getColumnIndex(KEY_ID)));
        c.close();
        db.setTransactionSuccessful();
        db.endTransaction();
        return td;
    }

    public long getRelationByIds(long compcosa_id, long maleta_id){
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        String selectQuery = "SELECT  "+ KEY_ID +" FROM " + TABLE_RELACIONES + " WHERE "
                + KEY_COSA_COMP + " = " + compcosa_id +" AND " + KEY_MALETA + " = " + maleta_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        long td = (c.getInt(c.getColumnIndex(KEY_ID)));
        c.close();
        db.setTransactionSuccessful();
        db.endTransaction();
        return td;
    }

    public Atributo getAttribute(long attr_id){
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        String selectQuery = "SELECT  * FROM " + TABLE_ATTR + " WHERE "
                + KEY_ID + " = " + attr_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Atributo td = new Atributo();
        td.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        td.setNumero((c.getInt(c.getColumnIndex(KEY_NUMBER))));
        td.setAtrib_1((c.getString(c.getColumnIndex(KEY_ATRIB1))));
        td.setAtrib_2((c.getString(c.getColumnIndex(KEY_ATRIB2))));
        td.setAtrib_3((c.getString(c.getColumnIndex(KEY_ATRIB3))));
        td.setAtrib_4((c.getString(c.getColumnIndex(KEY_ATRIB4))));
        c.close();
        db.setTransactionSuccessful();
        db.endTransaction();
        return td;
    }

    // METODOS GET ALL

    /*
 * getting all todos
 * */
    public List<Cosa> getAllCosas() {
        List<Cosa> todos = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_COSAS;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Cosa td = new Cosa();
                td.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                td.setTitle((c.getString(c.getColumnIndex(KEY_TITLE))));

                todos.add(td);
            } while (c.moveToNext());
        }
        c.close();
        db.setTransactionSuccessful();
        db.endTransaction();
        return todos;
    }

    public List<Compartiment> getAllComps() {
        List<Compartiment> todos = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_COMPS;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Compartiment td = new Compartiment();
                td.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                td.setTitle((c.getString(c.getColumnIndex(KEY_TITLE))));

                todos.add(td);
            } while (c.moveToNext());
        }
        c.close();
        db.setTransactionSuccessful();
        db.endTransaction();
        return todos;
    }

    public List<Maleta> getAllSuitcases() {
        List<Maleta> todos = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_MALETAS;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Maleta td = new Maleta();
                td.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                td.setTitle((c.getString(c.getColumnIndex(KEY_TITLE))));
                td.setType((c.getString(c.getColumnIndex(KEY_TYPE))));
                td.setDateStart((c.getString(c.getColumnIndex(KEY_DATE_START))));
                td.setDateReturn((c.getString(c.getColumnIndex(KEY_DATE_RETURN))));
                todos.add(td);
            } while (c.moveToNext());
        }
        c.close();
        db.setTransactionSuccessful();
        db.endTransaction();
        return todos;
    }

    public List<Relation> getAll(){
        List<Relation> todos = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_RELACIONES;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Relation td = new Relation();
                td.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                td.setComp_cosa((c.getInt(c.getColumnIndex(KEY_COSA_COMP))));
                td.setSuit((c.getInt(c.getColumnIndex(KEY_MALETA))));
                td.setChecked((c.getInt(c.getColumnIndex(KEY_CHECKED))));
                td.setCritical((c.getInt(c.getColumnIndex(KEY_CRITICAL))));

                todos.add(td);
            } while (c.moveToNext());
        }
        c.close();
        db.setTransactionSuccessful();
        db.endTransaction();
        return todos;
    }

    public List<Relation> getAllBySuitcase(Maleta maleta){
        List<Relation> todos = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_RELACIONES + " WHERE "
                + KEY_MALETA + " = " + maleta.getId();

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Relation td = new Relation();
                td.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                td.setComp_cosa((c.getInt(c.getColumnIndex(KEY_COSA_COMP))));
                td.setSuit((c.getInt(c.getColumnIndex(KEY_MALETA))));
                td.setChecked((c.getInt(c.getColumnIndex(KEY_CHECKED))));
                td.setCritical((c.getInt(c.getColumnIndex(KEY_CRITICAL))));

                todos.add(td);
            } while (c.moveToNext());
        }
        c.close();
        db.setTransactionSuccessful();
        db.endTransaction();
        return todos;
    }

    public List<Comp_Cosa> getAllComp_CosaByType(String type){
        List<Comp_Cosa> todos = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_COSA_COMP + " WHERE "
                + KEY_COMP + " IN " + converTypeToComps(type) + "ORDER BY COMP";

        //SELECT * FROM TABLE_COSA_COMP WHERE ID IN (id1, id2, ..., idn) order by comp
        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Comp_Cosa td = new Comp_Cosa();
                td.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                td.setComp((c.getInt(c.getColumnIndex(KEY_COMP))));
                td.setCosa((c.getInt(c.getColumnIndex(KEY_COSA))));
                todos.add(td);
            } while (c.moveToNext());
        }
        c.close();
        db.setTransactionSuccessful();
        db.endTransaction();
        return todos;
    }


    // METODOS CREAR INICIALES

    public void createRelationsInitial(Maleta maleta){
        SQLiteDatabase db = this.getWritableDatabase();
        List<Comp_Cosa> lista = getAllComp_CosaByType(maleta.getType());
        db.beginTransaction();
        for (int x = 0; x< lista.size(); x++){
            ContentValues values = new ContentValues();
            values.put(KEY_COSA_COMP, lista.get(x).getId());
            values.put(KEY_MALETA, maleta.getId());
            values.put(KEY_CHECKED, 0);
            values.put(KEY_CRITICAL, 0);
            values.put(KEY_ATTRIBUTES, 1);
            db.insert(TABLE_RELACIONES, null, values);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }


    // METODOS UPDATE

    public void updateRelation (Relation relation){
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        ContentValues values = new ContentValues();
        values.put(KEY_COSA_COMP, relation.getComp_cosa());
        values.put(KEY_MALETA, relation.getSuit());
        values.put(KEY_CHECKED, relation.getChecked());
        values.put(KEY_CRITICAL, relation.getCritical());
        values.put(KEY_ATTRIBUTES, relation.getAttributes());
        db.update(TABLE_RELACIONES, values, KEY_ID + " = ?",
                new String[] { String.valueOf(relation.getId()) });
        db.setTransactionSuccessful();
        db.endTransaction();
    }


    // METODOS DELETE

    // TODO: Crear borrado de Atributo cuando se puedan añadir!

    // "DELETE FROM " + TABLE_ATTRIBUTES + " where not exists (select " + KEY_ATTRIBUTES + " from "
    // + TABLE_RELACIONES+ " where " + TABLE_RELACIONES.ATTRIBUTES = TABLE_ATTRIBUTES.id + ")"

    public void deleteAllAttributes(){
        String selectQuery = "DELETE FROM " + TABLE_ATTR + " where not exists (select " + KEY_ATTRIBUTES + " from "
                 + TABLE_RELACIONES+ " where " + TABLE_RELACIONES + ".attrs = "+ TABLE_ATTR + ".id)" ;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        db.execSQL(selectQuery);
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public void deleteCosa (long cosa_id, long comp_id, long maleta_id){
        // Se obtiene su CompCosa, después de borra de Relations ese elemento usando maleta_id.

        long compcosa_id = getCompCosaByIds(comp_id, cosa_id);

        String selectQuery = "DELETE FROM " + TABLE_RELACIONES + " WHERE "
                + KEY_MALETA + " = " + maleta_id  + " AND " + KEY_COSA_COMP + " = " + compcosa_id;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        db.execSQL(selectQuery);
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public void deleteComp (long comp_id, long maleta_id){
        // Se obtiene su CompCosa, después de borra de Relations ese elemento usando maleta_id.

        String compcosa_id = getCompCosabyComp(comp_id);

        String selectQuery = "DELETE FROM " + TABLE_RELACIONES + " WHERE "
                + KEY_MALETA + " = " + maleta_id  + " AND " + KEY_COSA_COMP + " IN " + compcosa_id;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        db.execSQL(selectQuery);
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public void deleteMaleta (long maleta_id){
        // Se obtiene su CompCosa, después de borra de Relations ese elemento usando maleta_id.

        String selectQuery = "DELETE FROM " + TABLE_MALETAS + " WHERE "
                + KEY_ID + " = " + maleta_id ;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        db.execSQL(selectQuery);

        deleteRelationsByMaleta(maleta_id);

        db.setTransactionSuccessful();
        db.endTransaction();
    }

    private void deleteRelationsByMaleta (long maleta_id){
        // Se borra de Relations todas las relations.
        String selectQuery = "DELETE FROM " + TABLE_RELACIONES + " WHERE "
                + KEY_MALETA + " = " + maleta_id ;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        db.execSQL(selectQuery);
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    // METODOS SECUNDARIOS


    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

    //Método que iniciará la BD de Cosas y Compartimientos siempre que esta este vacía.
    private void initDatabase() {

        if (isEmpty()) {
            elementosBase();
        }
    }

    private boolean isEmpty() {
        return getAllCosas().size() == 0;
    }


    public ArrayList<Cursor> getData(String Query){
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[] { "message" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);

        try{
            String maxQuery = Query ;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);

            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {

                alc.set(0,c);
                c.moveToFirst();

                return alc ;
            }
            return alc;
        } catch(SQLException sqlEx){
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        } catch(Exception ex){
            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }
    }



    private String getCompCosabyComp(long comp_id){
        ArrayList<Integer> array = new ArrayList<>();
        String selectQuery = "SELECT  "+ KEY_ID +" FROM " + TABLE_COSA_COMP + " WHERE "
                + KEY_COMP + " = " +comp_id;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                array.add(c.getInt(c.getColumnIndex(KEY_ID)));
            } while (c.moveToNext());
        }
        c.close();
        db.setTransactionSuccessful();
        db.endTransaction();

        StringBuilder str = new StringBuilder("(");

        for (int x=0; x< array.size(); x++){
            if (x == array.size()-1){
                str.append(array.get(x)).append(")");
            }
            str.append(array.get(x)).append(",");
        }
        return str.toString();
    }


    // Comps: Baño, Electrónica, Ropa, Trabajo, Básico.
    //  ID:    1        2          3       4       5
    // Types: Diversión, Playa, Trabajo, Familia, Romántico, Otros.
    private String converTypeToComps(String type){
        String comps="(";

        switch(type){
            case "Diversión":
                comps = comps + "2,3,5)";
                break;
            case "Trabajo":
                comps = comps + "1,2,3,4,5)";
                break;
            default:
                comps = comps + "1,2,3,4,5)";
                break;
        }
        return comps;
    }



    private void elementosBase(){
        long comp1_id = createComp(new Compartiment("Baño"));
        long cosa4_id = createThing(new Cosa("Champú"));
        long cosa5_id = createThing(new Cosa("Gel"));
        long cosa1_id = createThing(new Cosa("Cepillo de dientes"));


        long comp2_id = createComp( new Compartiment("Electrónica"));
        long cosa2_id = createThing(new Cosa("Cargador de móvil"));

        long comp5_id = createComp(new Compartiment("Ropa"));
        long cosa3_id = createThing(new Cosa("Camiseta"));
        long cosa6_id = createThing(new Cosa("Pantalón"));



        long comp4_id = createComp(new Compartiment("Trabajo"));
        long cosa8_id = createThing(new Cosa("Maletín"));
        long cosa9_id = createThing(new Cosa("Teléfono de Empresa"));


        long comp3_id = createComp(new Compartiment("Básico"));
        long cosa7_id = createThing(new Cosa("Gafas"));

        // Comps: Baño, Electrónica, Ropa, Trabajo, Básico.

        long cos_comp1 = createCosaComp(cosa1_id, comp1_id);
        long cos_comp2 = createCosaComp(cosa2_id, comp2_id);
        long cos_comp3 = createCosaComp(cosa3_id, comp5_id);
        long cos_comp4 = createCosaComp(cosa4_id, comp1_id);
        long cos_comp5 = createCosaComp(cosa5_id, comp1_id);
        long cos_comp6 = createCosaComp(cosa6_id, comp5_id);
        long cos_comp7 = createCosaComp(cosa7_id, comp3_id);

        createCosaComp(cosa8_id, comp4_id);
        createCosaComp(cosa9_id, comp4_id);

        createAttributes(new Atributo(1, "","","",""));
//        Maleta ma1 = new Maleta("Lanzarote2017", "12/02/17","18/02/17" );
//        Maleta ma2 = new Maleta("París2019", "10/01/19","18/01/19" );
//
//        long ma1_id = mydb.createSuitCase(ma1);
//        long ma2_id = mydb.createSuitCase(ma2);

//      long id, long cosa, long comp, long suit, int checked, int critical

//        long r1_id = mydb.createRelations(cosa1_id, comp1_id, ma1_id, 0,0);
//        long r2_id = mydb.createRelations(cosa2_id, comp2_id, ma1_id, 0,1);
    }

}
