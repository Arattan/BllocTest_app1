package com.arattan.android.blloctest_app1;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int NOTIFICATION_LISTENER_PERMISSION_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView notificationPackageNameView = this.findViewById(R.id.package_name);
        TextView notificationTitleView = this.findViewById(R.id.title);
        TextView notificationTextView = this.findViewById(R.id.text);
        TextView notificationIdView = this.findViewById(R.id.id);

        startNotificationListenerService();
        showNotificationPreview();
    }

    private void showNotificationPreview() {
        //Check if Notification Permission is already available
        // If it has a toast will appear/notification display flow can begin here
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission has already been granted", Toast.LENGTH_SHORT).show();
        } else {
            //Request Permission
            requestNotificationPermission();
        }
    }

    private void startNotificationListenerService() {
        Intent intent = new Intent(this, NotificationListenerService.class);
        startService(intent);
    }

    private void requestNotificationPermission() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("Permission needed to read notifications")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.CAMERA}, NOTIFICATION_LISTENER_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, NOTIFICATION_LISTENER_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == NOTIFICATION_LISTENER_PERMISSION_CODE) {
            if (grantResults.length == 1 && grantResults[0] == PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }
}


