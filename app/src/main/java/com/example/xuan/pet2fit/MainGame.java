package com.example.xuan.pet2fit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.app.Activity;
import android.preference.PreferenceManager;

import com.google.android.gms.common.Scopes;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.common.api.*;
import com.google.android.gms.fitness.data.*;
import com.google.android.gms.fitness.request.*;
import com.google.android.gms.fitness.result.DataSourcesResult;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class MainGame extends Activity implements OnDataPointListener,
        GoogleApiClient.ConnectionCallbacks {
    private GoogleApiClient mApiClient = null;
    private static final int REQUEST_OAUTH = 1;
    private static final String AUTH_PENDING = "auth_state_pending";
    private boolean authInProgress = false;
    private int step_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_game);

        // Check if the app is trying to authorize
        // against the Fitness API
        if (savedInstanceState != null) {
            authInProgress = savedInstanceState.getBoolean(AUTH_PENDING);
        }

        mApiClient = new GoogleApiClient.Builder(this)
                // Specify the API that the app needs
                .addApi(Fitness.SENSORS_API)
                        // Specify the OAuth 2.0 scopes
                .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ))
                        // Registers a listener to receive connection events
                .addConnectionCallbacks(this)
                        // Adds a listener to register to receive connection failed events
                .build(); // Build an GoogleAPIClient to communicate with Google APIs
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Timestamp when the app is closed
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().putLong("last_used",System.currentTimeMillis()).commit();
        prefs.edit().putInt("last_step_count", step_count).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Timestamp when the app is re-opened
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        long currentTime = System.currentTimeMillis();
        long lastTime = prefs.getLong("last_used", currentTime);

        // Calculate new pet's health
        long been_away = ((currentTime - lastTime)/1000); // to seconds
        ThePet.setCurrentHealth(ThePet.getCurrentHealth() - (int) been_away);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(MainGame.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mApiClient.connect(); // Connect to Google Play services
    }

    @Override
    // If the connection is successful, this method got called
    public void onConnected(Bundle bundle) {
        DataSourcesRequest dataSourceRequest; // Type of data the app wants
        DataSourcesRequest.Builder data_builder = new DataSourcesRequest.Builder();

        data_builder.setDataTypes(DataType.TYPE_STEP_COUNT_CUMULATIVE); // Step counts data
        data_builder.setDataSourceTypes(DataSource.TYPE_RAW); // Raw data
        dataSourceRequest = data_builder.build(); // Construct the data source

        // When data can be retrieved from the device
        ResultCallback<DataSourcesResult> dataSourcesResultCallback = new ResultCallback<DataSourcesResult>() {
            @Override
            public void onResult(DataSourcesResult dataSourcesResult) {
                for(DataSource dataSource : dataSourcesResult.getDataSources()) {
                    if(DataType.TYPE_STEP_COUNT_CUMULATIVE.equals(dataSource.getDataType())) {
                        registerFitnessDataListener(dataSource, DataType.TYPE_STEP_COUNT_CUMULATIVE);
                    }
                }
            }
        };

        // Find the data sources that the app wants
        Fitness.SensorsApi.findDataSources(mApiClient, dataSourceRequest)
                .setResultCallback(dataSourcesResultCallback);
    }

    // Helper method
    // It is called when the requested data is found
    private void registerFitnessDataListener(DataSource dataSource, DataType dataType) {
        SensorRequest request; // Request for live updates from data source
        // A builder used to construct the quest
        SensorRequest.Builder sensor_builder = new SensorRequest.Builder();

        sensor_builder.setDataSource(dataSource); // Specify the data source of the request
        sensor_builder.setDataType(dataType); // Specify data type of the request
        sensor_builder.setSamplingRate(10, TimeUnit.SECONDS);  // Period between requests

        request = sensor_builder.build(); // Construct the request

        // Add the request
        Fitness.SensorsApi.add(mApiClient, request, this)
                // Deliver result through callback
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        if (status.isSuccess()) { // Request for Sensor API is succeeded
                        }
                    }
                });
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    // When data changes, it invokes this
    public void onDataPoint(DataPoint dataPoint) {
        for( final Field field : dataPoint.getDataType().getFields() ) {
            final Value value = dataPoint.getValue(field);

            step_count = value.asInt();

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            int last_count = prefs.getInt("last_step_count", 0);

            if (last_count != 0) {
                ThePet.setCurrentXP(ThePet.getCurrentXP() + (step_count - last_count));

                if (ThePet.getCurrentXP() >= ThePet.getLevelXP()) {
                    int new_xp = ThePet.getCurrentXP()-ThePet.getLevelXP();
                    XmlResourceParser xrp = this.getResources().getXml(R.xml.levels);
                    try {
                        ThePet.setPetLevel(xrp, ThePet.getCurrentLevel() + 1);
                        ThePet.setCurrentXP(new_xp);

                        final AlertDialog.Builder alert_builder = new AlertDialog.Builder(this);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                alert_builder.setMessage("Level Up! Pet is now level "
                                        + ThePet.getCurrentLevel());
                                alert_builder.setCancelable(true);
                                alert_builder.setPositiveButton("Yay!",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                                alert_builder.create().show();
                            }
                        });


                    }
                    catch (IOException e) { }
                    catch (XmlPullParserException e) { }
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MainGameView top_view = (MainGameView) findViewById(R.id.top_main_view);
                        top_view.invalidate();
                    }
                });
            }
        }
    }


    @Override
    protected void onStop() {
        super.onStop();

        Fitness.SensorsApi.remove( mApiClient, this )
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        if (status.isSuccess()) {
                            mApiClient.disconnect();
                        }
                    }
                });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(AUTH_PENDING, authInProgress);
    }
}
