package DBSqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import Clases.Receta;

public class TablaRecetas {


    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static TablaRecetas instance;

    /**
     * Private constructor to aboid object creation from outside classes.
     *
     * @param context
     */
    private TablaRecetas(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    /**
     * Return a singleton instance of DatabaseAccess.
     *
     * @param context the Context
     * @return the instance of DabaseAccess
     */
    public static TablaRecetas getInstance(Context context) {
        if (instance == null) {
            instance = new TablaRecetas(context);
        }
        return instance;
    }

    /**
     * Open the database connection.
     */
    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    /**
     * Close the database connection.
     */
    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

/**
 * Leer las provincias de la base de datos.
 *
 * @return a List of quotes
 */

public ArrayList<Receta> todos_las_recetas() {
    Cursor c;
    //Array donde se devuelven todas las recetas
    ArrayList<Receta> arrayRecetas = new ArrayList<Receta>();

    c = database.rawQuery("SELECT idreceta, nombre, descripcion, foto, pdf, autor, categoria FROM Recetas", null);

    //Nos aseguramos de que existe al menos un registro
    if (c.moveToFirst()) {
        //Recorremos el cursor hasta que no haya más registros
        do {

            arrayRecetas.add(new Receta(c.getInt(0),c.getString(1),c.getString(2),c.getBlob(3),c.getString(4),c.getString(5), c.getString(6)));

        } while(c.moveToNext());
    }
    //cerramos el cursor
    c.close();

    //devolvemos el array
    return arrayRecetas;
}


}
