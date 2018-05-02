package com.example.leonardomenezes.firebasesimplechat.activitys;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.leonardomenezes.firebasesimplechat.R;
import com.example.leonardomenezes.firebasesimplechat.Util;
import com.example.leonardomenezes.firebasesimplechat.model.User;

import butterknife.BindView;
import butterknife.ButterKnife;


public class StartActivity extends DefaultActivity {

    @BindView(R.id.button_enter_chat) Button buttonEnterChat;
    @BindView(R.id.inputNick) EditText editTextNick;
    @BindView(R.id.profilePicture) ImageView profilePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);

        checkUser();

        buttonEnterChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isEmpty(editTextNick)){
                    showToast("Please fill a name!", Toast.LENGTH_LONG);
                    return;
                }

                User user = new User().setName(getStringFromEditText(editTextNick)).setProfilePicture(profilePicture).generateUid();
                Intent intent = new Intent(StartActivity.this, ChatRoomActivity.class);
                intent.putExtra("user", user);
                Util.saveUser(user, StartActivity.this);
                startActivity(intent);
            }
        });

        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callGallery();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == GALLERY_PICTURE) && (resultCode == RESULT_OK)) {
            Uri uriPicture = data.getData();
            Glide.with(this).load(uriPicture).into(profilePicture);
        }
    }

    private void checkUser(){

        User user = Util.getSavedUser(StartActivity.this);

        if (user == null){
            return;
        }

        Intent intent = new Intent(StartActivity.this, ChatRoomActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }
}
