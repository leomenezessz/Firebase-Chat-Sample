package com.example.leonardomenezes.firebasesimplechat.activitys;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by leonardomenezes on 17/03/2018.
 */

public class DefaultActivity extends AppCompatActivity {

    public static final int GALLERY_PICTURE = 1;

    protected void showToast(String message, int length){
        Toast.makeText(this, message, length).show();
    }

    protected void callGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "Choose a profile picture!"), GALLERY_PICTURE);
    }

    protected boolean isEmpty(EditText editText) {
        return editText.getText().toString().trim().length() == 0;
    }

    protected String getStringFromEditText(EditText editText){
         return editText.getText().toString().trim();
    }
}
