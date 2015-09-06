package pimp.hellotospatialite;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private GeoDatabaseHandler gdbHandler;
    private TextView communicateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        communicateTextView = (TextView) findViewById(R.id.communicate);

        //Note: GeoDatabaseHandler here isn't doing too much work since this is a simple example
        // if in your app, copying the DB and/or doing queries requires a lot of processing time
        //then you probably want to do this in a thread.
        try {
            gdbHandler = new GeoDatabaseHandler(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        gdbHandler.cleanup();
    }

    public void getVersionInfo(View view) {
        if (communicateTextView != null) communicateTextView.setText(gdbHandler.showVersionsAndCredits());
    }

    public void runSimpleTest(View view) {
        if (communicateTextView != null) communicateTextView.setText(gdbHandler.queryTableSimple());
    }

    public void runPointInPolygon(View view) {
        if (communicateTextView != null) communicateTextView.setText(gdbHandler.queryPointInPolygon());
    }
}
