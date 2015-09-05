package pimp.hellotospatialite;

import android.content.Context;

import jsqlite.Database;
import pimp.spatialite_database_driver.FunConstants;

/**
 * Created by kristina on 9/2/15.
 */
public class GeoDatabaseHandler {

    public GeoDatabaseHandler( Context context, StringBuilder sb ) {

        FunConstants foo = new FunConstants();
        int temp = foo.getFOOBAR();

        Database db = new Database();

    }

    public String getSomeResult() {
        return "some result!";
    }
}
