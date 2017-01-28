package fun.mota.com.barscanner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import fun.mota.com.barscanner.event.CommandEvent;
import fun.mota.com.barscanner.net.NetworkService;
import fun.mota.com.barscanner.pojo.ScanResult;

public class MainActivity extends AppCompatActivity {
    // Holds the app context to pass around
    private Context mContext;
    private ScanResult mLastSuccessfulScanResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this.getApplicationContext();
        startService(new Intent(MainActivity.this, NetworkService.class));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the scan
                startScanning();
            }
        });
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    //initializations
    private void init() {
        // TODO store/retrieve the last successful scan result from DB into
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        //Fill the Pojo with retrieved scan result

        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        retrieveScanResult(scanningResult);
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

    private void startScanning() {
        IntentIntegrator scanIntegrator = new IntentIntegrator(this);
        scanIntegrator.initiateScan();
    }

    private void retrieveScanResult(IntentResult result) {

        if (result != null) {
            //we have a result
            ScanResult scanResult = new ScanResult();
            scanResult.setContent(result.getContents());
            scanResult.setFormat(result.getFormatName());
            // Publish the scan results to subscribers
            EventBus.getDefault().post(scanResult);
        } else {
            // TODO : Some UI way to display null result, maybe a toast
            Log.i("Mota", "No Result");
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEvent(CommandEvent event) {
        switch((CommandEvent.Type)event.getType()){
            case UPLOAD_SCAN_RESULT_SUCCESS:
                // TODO apropriate UI notify
                break;
            case UPLOAD_SCAN_RESULT_FAILED:
                // TODO apropriate UI notify
                break;
            default:
                // do nothing
                break;
        }
    }
}
