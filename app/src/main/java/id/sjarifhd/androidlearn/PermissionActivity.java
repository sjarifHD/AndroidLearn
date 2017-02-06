package id.sjarifhd.androidlearn;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import id.sjarifhd.androidlearn.helper.PermissionUtil;

public class PermissionActivity extends AppCompatActivity {

    private static final String TAG = PermissionActivity.class.getSimpleName();

    private static final int REQUEST_CAMERA = 0;
    private static final int REQUEST_CONTACTS = 1;
    private static String[] PERMISSIONS_CONTACT = {Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS};
    private static final int REQUEST_STORAGE = 2;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int REQUEST_LOCATION = 3;
    private static String[] PERMISSIONS_LOCATION = {Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};

    private View mLayout;

    private Button btnCamera;
    private Button btnContact;
    private Button btnStorage;
    private Button btnLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        mLayout = findViewById(R.id.layout_permission);

        btnCamera = (Button) findViewById(R.id.btn_camera);
        btnContact = (Button) findViewById(R.id.btn_contact);
        btnStorage = (Button) findViewById(R.id.btn_storage);
        btnLocation = (Button) findViewById(R.id.btn_location);

        onClickedButton();

    }



    private void onClickedButton() {
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCamera();
            }
        });

        btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showContacts();
            }
        });

        btnStorage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocation();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA:
                // BEGIN_INCLUDE(permission_result)
                // Received permission result for camera permission.
                Log.i(TAG, "Received response for Camera permission request.");

                // Check if the only required permission has been granted
                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Camera permission has been granted, preview can be displayed
                    Log.i(TAG, "CAMERA permission has now been granted. Showing preview.");
                    Snackbar.make(mLayout, R.string.permision_available_camera,
                            Snackbar.LENGTH_SHORT).show();
                } else {
                    Log.i(TAG, "CAMERA permission was NOT granted.");
                    Snackbar.make(mLayout, R.string.permissions_not_granted,
                            Snackbar.LENGTH_SHORT).show();

                }
                // END_INCLUDE(permission_result)
                break;
            case REQUEST_CONTACTS:
                Log.i(TAG, "Received response for contact permissions request.");

                // We have requested multiple permissions for contacts, so all of them need to be
                // checked.
                if (PermissionUtil.verifyPermissions(grantResults)) {
                    // All required permissions have been granted, display contacts fragment.
                    Snackbar.make(mLayout, R.string.permision_available_contacts,
                            Snackbar.LENGTH_SHORT)
                            .show();
                } else {
                    Log.i(TAG, "Contacts permissions were NOT granted.");
                    Snackbar.make(mLayout, R.string.permissions_not_granted,
                            Snackbar.LENGTH_SHORT)
                            .show();
                }
                break;
            case REQUEST_LOCATION:
                Log.i(TAG, "Received response for access location permissions request.");

                if (PermissionUtil.verifyPermissions(grantResults)) {
                    Snackbar.make(mLayout, R.string.permision_available_location,
                            Snackbar.LENGTH_SHORT)
                            .show();
                } else {
                    Log.i(TAG, "Access location permissions were NOT granted.");
                    Snackbar.make(mLayout, R.string.permissions_not_granted,
                            Snackbar.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    private void showCamera() {
        Log.i(TAG, "Show camera button pressed. Checking permission.");
        // BEGIN_INCLUDE(camera_permission)
        // Check if the Camera permission is already available.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Camera permission has not been granted.
            requestCameraPermission();
        } else {
            // Camera permissions is already available, show the camera preview.
            Log.i(TAG,
                    "CAMERA permission has already been granted. Displaying camera preview.");
            showCameraPreview();
        }
        // END_INCLUDE(camera_permission)
    }

    private void requestCameraPermission() {
        Log.i(TAG, "CAMERA permission has NOT been granted. Requesting permission.");
        // BEGIN_INCLUDE(camera_permission_request)
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.
            Log.i(TAG,
                    "Displaying camera permission rationale to provide additional context.");
            Snackbar.make(mLayout, R.string.permission_camera_rationale,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(PermissionActivity.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    REQUEST_CAMERA);
                        }
                    })
                    .show();
        } else {
            // Camera permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA);
        }
        // END_INCLUDE(camera_permission_request)
    }

    private void showCameraPreview() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivityForResult(intent, 0);
    }

    private void showContacts() {
        Log.i(TAG, "Show contacts button pressed. Checking permissions.");

        // Verify that all required contact permissions have been granted.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            // Contacts permissions have not been granted.
            Log.i(TAG, "Contact permissions has NOT been granted. Requesting permissions.");
            requestContactsPermissions();

        } else {
            // Contact permissions have been granted. Show the contacts fragment.
            Log.i(TAG,
                    "Contact permissions have already been granted. Displaying contact details.");
            showContactDetails();
        }
    }

    private void requestContactsPermissions() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_CONTACTS)
                || ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_CONTACTS)) {

            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example, if the request has been denied previously.
            Log.i(TAG,
                    "Displaying contacts permission rationale to provide additional context.");

            // Display a SnackBar with an explanation and a button to trigger the request.
            Snackbar.make(mLayout, R.string.permission_contacts_rationale,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat
                                    .requestPermissions(PermissionActivity.this, PERMISSIONS_CONTACT,
                                            REQUEST_CONTACTS);
                        }
                    })
                    .show();
        } else {
            // Contact permissions have not been granted yet. Request them directly.
            ActivityCompat.requestPermissions(this, PERMISSIONS_CONTACT, REQUEST_CONTACTS);
        }
        // END_INCLUDE(contacts_permission_request)
    }

    private void showContactDetails() {
    }

    private void getLocation() {
//        double longitude = 0.0;
//        double latitude = 0.0;
        boolean isGPSEnabled;
        boolean isNetworkEnabled;

        /**
         * Location Manager
         * Location Listener
         */
        String locationNetworkProvider = LocationManager.NETWORK_PROVIDER;
        // Or, use GPS location data:
        String locationGPSProvider = LocationManager.GPS_PROVIDER;

        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                makeUseOfNewLocation(location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
                Log.v(TAG, "Your "+ provider + " is not enabled");
            }
        };


        Log.i(TAG, "Access location button pressed. Checking permissions.");

        // Verify that all required access location permissions have been granted.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Access location permissions has NOT been granted. Requesting permissions.");
            requestLocationPermission();
        } else {
            Log.i(TAG,
                    "Access location permissions have already been granted. Getting location");

            locationManager.requestLocationUpdates(locationGPSProvider, 0, 0, locationListener);
            locationManager.removeUpdates(locationListener);
            //            Location location = locationManager.getLastKnownLocation(locationGPSProvider);
//            if (location != null) {
//                latitude = location.getLatitude();
//                longitude = location.getLongitude();
//                Log.v(TAG, "Latitude: " + latitude + " --- Longitude: " + longitude);
//            }
        }
    }

    private void makeUseOfNewLocation(Location location) {
        double longitude;
        double latitude;

        latitude = location.getLatitude();
        longitude = location.getLongitude();

        Log.v(TAG, "Latitude: " + latitude + " --- Longitude: " + longitude);
    }

    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                || ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)) {

            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example, if the request has been denied previously.
            Log.i(TAG,
                    "Displaying access location permission rationale to provide additional context.");

            // Display a SnackBar with an explanation and a button to trigger the request.
            Snackbar.make(mLayout, R.string.permission_location_rationale,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat
                                    .requestPermissions(PermissionActivity.this, PERMISSIONS_LOCATION,
                                            REQUEST_LOCATION);
                        }
                    })
                    .show();
        } else {
            // Access location permissions have not been granted yet. Request them directly.
            ActivityCompat.requestPermissions(this, PERMISSIONS_LOCATION, REQUEST_LOCATION);
        }
    }

}
