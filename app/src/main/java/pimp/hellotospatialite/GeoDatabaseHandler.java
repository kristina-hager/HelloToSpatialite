package pimp.hellotospatialite;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import jsqlite.*;
import pimp.spatialite_database_driver.FunConstants;

/**
 * Created by kristina on 9/2/15.
 */
public class GeoDatabaseHandler {

    private static final String TAG_SL = "GEODB_JSQLITE";
    //default android path to app database
    public static String DB_PATH = "/data/data/pimp.hellotospatialite/databases";
    //the name of the db, also in res/raw
    public static String DB_NAME = "city_districts.sqlite";

    private Database spatialiteDb;


    public GeoDatabaseHandler( Context context, StringBuilder sb ) throws IOException {

        //example of using 'just java' from loaded library
        FunConstants foo = new FunConstants();
        int temp = foo.getFOOBAR();

        File storageDir = new File(DB_PATH);
        File cacheDatabase = new File(storageDir, DB_NAME);

        if (!cacheDatabase.getParentFile().exists()) {
            File dirDb = cacheDatabase.getParentFile();
            if (!dirDb.mkdir()) {
                throw new IOException(TAG_SL + "Could not create dirDb: " + dirDb.getAbsolutePath());
            }
        }

        spatialiteDb = new Database();
        try {
            spatialiteDb.open(cacheDatabase.getAbsolutePath(),
                    jsqlite.Constants.SQLITE_OPEN_READWRITE | jsqlite.Constants.SQLITE_OPEN_CREATE);
        } catch (jsqlite.Exception e) {
                e.printStackTrace();
                Log.e(TAG_SL,e.getMessage());
            }

    }

    public String getSomeResult() {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Checking installed versions of spatialite components..\n");

        try {
            Stmt stmt01 = spatialiteDb.prepare("SELECT spatialite_version()");
            if (stmt01.step()) {
                stringBuilder.append("\t").append("SPATIALITE_VERSION: " + stmt01.column_string(0));
                stringBuilder.append("\n");
            }

            stmt01 = spatialiteDb.prepare("SELECT proj4_version();");
            if (stmt01.step()) {
                stringBuilder.append("\t").append("PROJ4_VERSION: " + stmt01.column_string(0));
                stringBuilder.append("\n");
            }

            stmt01 = spatialiteDb.prepare("SELECT geos_version();");
            if (stmt01.step()) {
                stringBuilder.append("\t").append("GEOS_VERSION: " + stmt01.column_string(0));
                stringBuilder.append("\n");
            }
        } catch (jsqlite.Exception e) {
            e.printStackTrace();
        }

        stringBuilder.append("done..\n");
        return stringBuilder.toString();
    }
}
