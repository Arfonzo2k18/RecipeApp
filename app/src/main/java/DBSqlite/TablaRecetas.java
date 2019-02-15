package DBSqlite;

import android.content.ContentValues;
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
    public TablaRecetas(Context context) {
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
     * Abre la conexión a la base de datos en modo escritura
     */
    public void open() {
        this.database = openHelper.getWritableDatabase();
    }
    /**
     * Close the database connection.
     */
    public void closeDatabase(){
        if (this.database != null){
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
    this.open();

    c = database.rawQuery("SELECT id_receta, nombre, descripcion, foto, pdf, autor, categoria FROM recetas", null);

    //Nos aseguramos de que existe al menos un registro
    if (c.moveToFirst()) {
        //Recorremos el cursor hasta que no haya más registros
        do {

            arrayRecetas.add(new Receta(c.getInt(0),c.getString(1),c.getString(2),c.getBlob(3),c.getString(4),c.getString(5), c.getString(6)));

        } while(c.moveToNext());
    }
    //cerramos el cursor
    c.close();
    this.closeDatabase();

    //devolvemos el array
    return arrayRecetas;
}

public ArrayList<Receta> recetas_por_autor(String autor) {
    Cursor c;
    //Array donde se devuelven todas las recetas
    ArrayList<Receta> arrayRecetas = new ArrayList<Receta>();
    this.open();

    c = database.rawQuery("SELECT * FROM recetas where autor ='"+ autor +"'", null);

    //Nos aseguramos de que existe al menos un registro
    if (c.moveToFirst()) {
        //Recorremos el cursor hasta que no haya más registros
        do {

            arrayRecetas.add(new Receta(c.getInt(0),c.getString(1),c.getString(2),c.getBlob(3),c.getString(4),c.getString(5), c.getString(6)));

        } while(c.moveToNext());
    }
    //cerramos el cursor
    c.close();
    this.closeDatabase();

    //devolvemos el array
    return arrayRecetas;
}

public ArrayList<Receta> crear_receta(Receta receta) {
    ArrayList<Receta> arrayReceta = new ArrayList<>();

    this.open();
        String nombre = receta.getNombre();
        String descripcion = receta.getDescripcion();
        byte[] foto = receta.getFoto();
        String pdf = receta.getPdf();
        String autor = receta.getAutor();
        String categoria = receta.getCategoria();

        ContentValues valores = new ContentValues();
        valores.put("nombre", receta.getNombre());
        valores.put("descripcion", receta.getDescripcion());
        valores.put("foto", receta.getFoto());
        valores.put("pdf", receta.getPdf());
        valores.put("autor", receta.getAutor());
        valores.put("categoria", receta.getCategoria());

        //insertamos en la base de datos en la tabla recetas
        database.insert("recetas", null, valores);

    this.closeDatabase();

    return arrayReceta;
}

    public ArrayList<Receta> recetas_por_categoria(String categoria) {
        Cursor c;
        //Array donde se devuelven todas las recetas
        ArrayList<Receta> arrayRecetas = new ArrayList<Receta>();
        this.open();

        c = database.rawQuery("SELECT * FROM recetas where categoria ='"+ categoria +"'", null);

        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {

                arrayRecetas.add(new Receta(c.getInt(0),c.getString(1),c.getString(2),c.getBlob(3),c.getString(4),c.getString(5), c.getString(6)));

            } while(c.moveToNext());
        }
        //cerramos el cursor
        c.close();
        this.closeDatabase();

        //devolvemos el array
        return arrayRecetas;
    }


}
