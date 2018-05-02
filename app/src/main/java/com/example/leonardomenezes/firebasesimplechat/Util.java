package com.example.leonardomenezes.firebasesimplechat;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.widget.ImageView;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.example.leonardomenezes.firebasesimplechat.model.User;
import com.google.gson.Gson;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by leonardomenezes on 18/03/2018.
 */

public class Util extends AppCompatActivity {

    private static final String USER_PREF = "user-pref";

    public static byte[] compressBitmapToByteArray(Bitmap resized, Bitmap.CompressFormat format, int imageQuality ) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Bitmap bitmap = Bitmap.createScaledBitmap(resized, 300 ,300 , false );
        bitmap.compress(format, imageQuality, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static Bitmap getBitmapFromAImageView(ImageView imageView){
        return ((GlideBitmapDrawable)imageView.getDrawable().getCurrent()).getBitmap();
    }

    public static String encodeByteToStringBase64(byte[] b){
        byte [] bytes = Base64.encode(b, Base64.NO_WRAP);
        return new String(bytes);
    }

    public static byte [] decodeStringBase64(String stringBase64){
        return stringBase64 == null ? null : Base64.decode(stringBase64, Base64.NO_WRAP);
    }

    public static void saveUser(User user, Context context){
        if (getSavedUser(context) == null) {
            SharedPreferences preferences = context.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE);
            preferences.edit().putString("user", new Gson().toJson(user)).apply();
        }
    }

    public static User getSavedUser(Context context){
        SharedPreferences preferences = context.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE);
        String jsonUserString = preferences.getString("user", null);
        return new Gson().fromJson(jsonUserString, User.class);
    }

    public static void clearSavedUser(Context context){
        SharedPreferences.Editor preferences = context.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE).edit();
        preferences.clear().apply();
    }

    public static String getCurrentTime(){
        return new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());
    }
}
