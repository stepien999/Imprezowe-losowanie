package chu.losowanie;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static String DB_PATH = "/data/data/chu.losowanie/databases/";
    private static String DB_NAME = "baza.db";
    private SQLiteDatabase myDataBase;
    private final Context myContext;

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    public DataBaseHelper(Context context) {

        super(context, DB_NAME, null, 1);
        this.myContext = context;
    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();

        if(dbExist){
            //nic nie rob, bd juz istnieje
        }
        else
        {
            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();

            try
            {
                copyDataBase();
            }
            catch (IOException e)
            {
                throw new Error("Błąd kopiowania bazy");
            }
        }
    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){

        SQLiteDatabase checkDB = null;

        try
        {
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        }
        catch(SQLiteException e){
            //bd jeszcze nie istnieje
        }

        if(checkDB != null)
        {
            checkDB.close();
        }

        return checkDB != null ? true : false;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException{

        //otwarcie lokalnej bd jako strumienia wejsciowego
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        //sciezka do utworzonej pustej bd
        String outFileName = DB_PATH + DB_NAME;

        //otwarcie pustej bazy jako strumienia wyjsciowego
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer B z wejscia do wyjscia
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0)
        {
            myOutput.write(buffer, 0, length);
        }

        //zamkniecie potokow
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void openDataBase() throws SQLException {

        //Otwarcie bd
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

    }

    public synchronized void close() {

        if(myDataBase != null)
            myDataBase.close();

        super.close();

    }

    public Cursor dajWszystkie(){
        String[] kolumny = {"id","tresc","kat"};
        SQLiteDatabase db = getReadableDatabase();
        Cursor kursor = db.query("Zadania",kolumny,null,null,null,null,null);
        return kursor;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }
}
