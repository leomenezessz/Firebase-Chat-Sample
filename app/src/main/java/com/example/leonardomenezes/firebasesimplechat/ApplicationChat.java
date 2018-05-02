package com.example.leonardomenezes.firebasesimplechat;

import android.app.Application;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by leonardomenezes on 14/03/2018.
 */

public class ApplicationChat extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.getApps(this);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
