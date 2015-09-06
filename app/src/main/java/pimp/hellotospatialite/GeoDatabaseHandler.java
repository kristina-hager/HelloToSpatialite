package pimp.hellotospatialite;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import jsqlite.*;
import pimp.spatialite_database_driver.Credits;

/**
 * Created by kristina on 9/2/15.
 */
public class GeoDatabaseHandler {

    private static final String TAG = "GEODBH";
    private static final String TAG_SL = TAG + "_JSQLITE";

    //default android path to app database
    public static String DB_PATH = "/data/data/pimp.hellotospatialite/databases";
    //the name of the db, also in res/raw
    public static String DB_NAME = "city_districts.sqlite";

    private Database spatialiteDb;


    public GeoDatabaseHandler(Context context) throws IOException {

        File cacheDatabase = new File(DB_PATH, DB_NAME);
        if (!cacheDatabase.getParentFile().exists()) {
            File dirDb = cacheDatabase.getParentFile();
            Log.i(TAG,"making directory: " + cacheDatabase.getParentFile());
            if (!dirDb.mkdir()) {
                throw new IOException(TAG_SL + "Could not create dirDb: " + dirDb.getAbsolutePath());
            }
        }

        //can only read data from raw or assets, so need to copy database to an internal file for further work
        //source: http://stackoverflow.com/questions/513084/how-to-ship-an-android-application-with-a-database
        InputStream inputStream = context.getResources().openRawResource(R.raw.city_districts);
        copyDatabase(inputStream, DB_PATH + File.separator + DB_NAME);

        spatialiteDb = new Database();
        try {
            spatialiteDb.open(cacheDatabase.getAbsolutePath(),
                    jsqlite.Constants.SQLITE_OPEN_READWRITE | jsqlite.Constants.SQLITE_OPEN_CREATE);
        } catch (jsqlite.Exception e) {
                Log.e(TAG_SL,e.getMessage());
            }

    }

    //It's a good practice to close the database when finished
    //I'm not sure if one should write a 'finalize' in android
    //so I added this to call from MainActivity onfinish
    public void cleanup() {
        try {
            spatialiteDb.close();
        } catch (jsqlite.Exception e) {
            e.printStackTrace();
        }
    }

    private void copyDatabase(InputStream inputStream, String dbFilename) throws IOException {

        OutputStream outputStream = new FileOutputStream(dbFilename);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer,0,length);
        }

        outputStream.flush();
        outputStream.close();
        inputStream.close();
        Log.i(TAG,"Copied database to " + dbFilename);
    }

    public String queryTableSimple() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("query districts table..");

        String query = "SELECT * from districts order by single_mem";
        stringBuilder.append("Execute query: ").append(query).append("\n");

        try {
            Stmt stmt = spatialiteDb.prepare(query);
            int index = 0;
            while (stmt.step()) {
                String result = stmt.column_string(0);
                stringBuilder.append("\t").append(result).append("\n");
                if (index++ > 10) break;
            }
            stringBuilder.append("\t...");
            stmt.close();
        } catch (jsqlite.Exception e) {
            Log.e(TAG_SL,e.getMessage());
        }

        stringBuilder.append("done\n");

        return stringBuilder.toString();

    }

    public String showVersionsAndCredits() {

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
            stringBuilder.append("\n");
            stmt01.close();
        } catch (jsqlite.Exception e) {
            e.printStackTrace();
        }

        stringBuilder.append("This code relies on open source spatialite:\n");
        stringBuilder.append(new Credits().getCredits());
        stringBuilder.append("\n");

        stringBuilder.append("done..\n");

        return stringBuilder.toString();
    }
}
