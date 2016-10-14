package test.fun.com.simstats;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final int READ_PHONE_STATE_REQUEST = 37;
    private static final int ACCESS_FINE_LOCATION = 1;
    private static String NETWORK_COUNTRY_ISO = "Network Country Iso : ";
    private static String DEVICE_ID = "IMEI/Device ID : ";
    private static String IMSI = "IMSI : ";
    private static String MCC_MNC = "MCC_MNC : ";
    private static String OPERATOR_NAME = "Operator : ";
    private static String CELL_LOCATION = "Cell Location : ";

    private ArrayList<String> mArrayList;
    private ArrayAdapter<String> mAdapter;
    private String mLatitude = "UNAVAILABLE";
    private String mLongitude = "UNAVAILABLE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //request permissions
        requestPermissions();

        mArrayList = new ArrayList<>();

        final ListView mListView = (ListView) findViewById(R.id.lv);
        Button mButtonSimStats = (Button) findViewById(R.id.button);
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mArrayList);
        mListView.setAdapter(mAdapter);
        mButtonSimStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAdapter.clear();
                populateListView(mAdapter);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!hasPermissionToReadLocation()) {
            requestCoarseLocation();
            return;
        }
    }

    private void populateListView(ArrayAdapter<String> mAdapter){
        final TelephonyManager tm = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        Location mLocation = getLastKnownLocation();
        mLatitude = String.valueOf(mLocation.getLatitude());
        mLongitude = String.valueOf(mLocation.getLongitude());

        //Populate the List View with Sim Stats
        mArrayList.add(0, NETWORK_COUNTRY_ISO.concat(tm.getNetworkCountryIso()));
        mArrayList.add(0, DEVICE_ID.concat(tm.getDeviceId()));
        mArrayList.add(0, IMSI.concat(tm.getSubscriberId()));
        mArrayList.add(0, MCC_MNC.concat(tm.getNetworkOperator()));
        mArrayList.add(0, OPERATOR_NAME.concat(tm.getNetworkOperatorName()));
        mArrayList.add(0, CELL_LOCATION.concat(mLatitude).concat(" , ").concat(mLongitude));
        mAdapter.addAll(mArrayList);
    }
    private void requestPermissions() {
        if (!hasPermissionToReadPhoneStats()) {
            requestPhoneStateStats();
            return;
        }
    }

    private boolean hasPermissionToReadPhoneStats() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED) {
            return false;
        } else {
            return true;
        }
    }

    private boolean hasPermissionToReadLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            return false;
        } else {
            return true;
        }
    }

    private void requestPhoneStateStats() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, READ_PHONE_STATE_REQUEST);

    }

    private void requestCoarseLocation() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION);
    }

    private Location getLastKnownLocation() {
        Context appCtx = getApplicationContext();

        if (!hasPermissionToReadLocation()) {
            requestCoarseLocation();
        }

        LocationManager lm = (LocationManager) appCtx.getSystemService(Context.LOCATION_SERVICE);
        if (lm != null) {
            // noinspection ResourceType
            Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                return location;
            }
        }
        return null;
    }

}
