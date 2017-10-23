package com.acadgild.deletecontact;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView contactId;
    private final static int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contactId = (TextView) findViewById(R.id.contact_id);


        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {



            } else {

                //  request the permission.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);


            }
        }else{
            //contactId.setText(String.valueOf(getContactID(getContentResolver(), "8285385442")));
            contactId.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteContact();
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                    contactId.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            deleteContact();
                        }
                    });
                } else {

                    // permission denied, Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "You've denied the required permission.", Toast.LENGTH_LONG);
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void deleteContact() {

        //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CONTACTS}, 1);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View view = inflater.inflate(R.layout.delete_contact_form, null);

        final EditText phone = view.findViewById(R.id.input_contact_phone);

        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.delete_contact, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String contactPhone = phone.getText().toString();

                        // Uri contactUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(contactPhone));
                        // grantUriPermission("com.compkerworld.playingwithcontacts.utils", contactUri, Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                        int deleteStatus = ContactHelper.deleteContact(getContentResolver(), contactPhone);

                        if (deleteStatus == 1) {
                            Toast.makeText(getApplicationContext(), "Deleted successfully.", Toast.LENGTH_LONG).show();
                        } else if(deleteStatus == 0){
                            Toast.makeText(getApplicationContext(), "No such number exists.", Toast.LENGTH_LONG).show();
                        } else{
                            Toast.makeText(getApplicationContext(), "Failed to delete.", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton(R.string.cancel_contact, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //MainActivity.this.getDialog().cancel();
                    }
                });
        AlertDialog dialog = builder.create();

        dialog.show();
    }

}


