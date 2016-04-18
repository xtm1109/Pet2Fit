package com.example.xuan.pet2fit;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.common.api.*;

public class MainActivity extends Activity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private GoogleApiClient mApiClient = null;
    private static final int REQUEST_OAUTH = 1;
    private static final String AUTH_PENDING = "auth_state_pending";
    private boolean authInProgress = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                .addOnConnectionFailedListener(this)
                .build(); // Build an GoogleAPIClient to communicate with Google APIs
    }

    @Override
    protected void onStart() {
        super.onStart();
        mApiClient.connect(); // Connect to Google Play services
    }


    @Override
    // If connection is failed, this method got called
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if(!authInProgress) { // the app is not authorizing the API
            try {
                // Try to authorize the API
                authInProgress = true;
                // Call MainActivity to resolve this failure
                connectionResult.startResolutionForResult(MainActivity.this, REQUEST_OAUTH);
            } catch(IntentSender.SendIntentException e) {}
        } else {
            System.out.println("Authorization is in process.");
        }
    }

    @Override
    // startResolutionForResult triggers this
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_OAUTH) { // Identify who this result came from
            authInProgress = false;

            if (resultCode == RESULT_OK) { // Successfully authorize
                // API client is not running yet, connect again
                if (!mApiClient.isConnecting() && !mApiClient.isConnected()) {
                    mApiClient.connect();
                }
            }
            else if (resultCode == RESULT_CANCELED) { // User cancels during authorization
                System.out.println("Google API authorization is canceled.");
                mApiClient.connect();
            }
        }
        else {
            System.out.println("Wrong result - Not from authorization");
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(AUTH_PENDING, authInProgress);
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}