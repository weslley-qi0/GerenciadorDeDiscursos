package com.qi0.weslley.gerenciadordediscursos;

import android.app.Application;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.qi0.weslley.gerenciadordediscursos.config.ConfiguracaoFirebase;

public class MyApplication extends Application{
	@Override
	public void onCreate() {
		super.onCreate();
		FirebaseDatabase.getInstance().setPersistenceEnabled(true);

		DatabaseReference databaseReference = ConfiguracaoFirebase.getFirebaseDatabase();
		databaseReference.keepSynced(true);
	}
}