package com.example.fitnesscoach;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.zxing.Result;

import java.util.Scanner;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission.CAMERA;

//The scanner class which allows users to scan in the barcodes of foods within the database.
public class ScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private static final int REQUEST_CAMERA = 1;
    private ZXingScannerView scannerView;

    //Initially starts the scanner as long as the app has permission.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkPermission()){
                Toast.makeText(ScannerActivity.this, "Scan the barcode of your product", Toast.LENGTH_LONG).show();
            }
            else{
                requestPermission();
            }
        }
    }

    private boolean checkPermission(){
        return (ContextCompat.checkSelfPermission(ScannerActivity.this, CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, REQUEST_CAMERA);
    }

    //Handles the scanner depending on the permissions the user has provided. It starts the activity
    //with a positive toast message if it has permission, or gives a negative toast message if not.
    public void onRequestPermissionsResult(int requestCode, String[] permission, int[] grantResults){
        switch(requestCode){
            case REQUEST_CAMERA:
                if(grantResults.length >0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted){
                        Toast.makeText(ScannerActivity.this, "Scan the barcode of your food",Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(ScannerActivity.this, "Permission Denied",Toast.LENGTH_LONG).show();
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                            if(shouldShowRequestPermissionRationale(CAMERA)){
                                displayAlertMessage("Allow access for both permissions", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        requestPermissions(new String[]{CAMERA}, REQUEST_CAMERA);
                                    }
                                });
                            }
                        }
                    }
                }
                break;
        }
    }

    //Provides catching if activity is left and came back to by the user. Basically starts it again.
    @Override
    public void onResume(){
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkPermission()){
                if(scannerView == null){
                    scannerView = new ZXingScannerView(this);
                    setContentView(scannerView);
                }
                scannerView.setResultHandler(this);
                scannerView.startCamera();
            }
            else{
                requestPermission();
            }
        }
    }

    //Stops the activity.
    @Override
    public void onDestroy(){
        super.onDestroy();
        scannerView.stopCamera();
    }

    //Displays an alert message if required for QR or error catching with barcodes.
    public void displayAlertMessage(String message, DialogInterface.OnClickListener listener){
        new AlertDialog.Builder(ScannerActivity.this).setMessage(message).setPositiveButton("OK", listener)
                .setNegativeButton("Cancel", null).create().show();
    }

    @Override
    public void handleResult(final Result result) {

        //Gets the result of the scan, searches the database for it and passes the information
        //to the adding in of a food activity.
        final String scanResult = result.getText();
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();

        final DocumentReference documentReference = fStore.collection("foods").document(scanResult);

        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                    if( documentSnapshot.exists()) {
                        String foodName = documentSnapshot.getString("foodName");
                        double calPerPortion = documentSnapshot.getDouble("calPerPortion");
                        double portionSize = documentSnapshot.getDouble("portionSize");
                        String calPerPortionString = Double.toString(calPerPortion);
                        String portionSizeString = Double.toString(portionSize);
                        Intent i = new Intent(ScannerActivity.this, AddFoodToUser.class);
                        i.putExtra("foodName", foodName);
                        i.putExtra("calPerPortion", calPerPortionString);
                        i.putExtra("portionSize", portionSizeString);
                        startActivity(i);
                    }
                    else{
                        Toast.makeText(ScannerActivity.this, "Please enter this product into the database to scan it", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(ScannerActivity.this, ScannerActivity.class);
                        startActivity(i);
                    }
            }
        });
    }
}
