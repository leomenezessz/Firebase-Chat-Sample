package com.example.leonardomenezes.firebasesimplechat.model;

import android.graphics.Bitmap;
import android.graphics.drawable.VectorDrawable;
import android.widget.ImageView;
import com.example.leonardomenezes.firebasesimplechat.Util;
import java.io.Serializable;
import java.util.Random;

/**
 * Created by leonardomenezes on 14/03/2018.
 */

public class User implements Serializable{

    private String name;
    private String profilePicture;
    private String uid;

    public User (){}

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public User setProfilePicture(ImageView profilePicture) {

        if(profilePicture.getDrawable() instanceof VectorDrawable){
            this.profilePicture = null;
            return this;
        }

        Bitmap bitmap = Util.getBitmapFromAImageView(profilePicture);
        byte [] bytes = Util.compressBitmapToByteArray(bitmap, Bitmap.CompressFormat.PNG, 100);
        this.profilePicture =  Util.encodeByteToStringBase64(bytes);
        return this;
    }

    public String getUid() {
        return uid;
    }

    public User generateUid() {
        this.uid =  String.valueOf(new Random().nextLong());
        return this;
    }
}
