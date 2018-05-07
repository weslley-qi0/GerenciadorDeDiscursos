package com.qi0.weslley.gerenciadordediscursos.Config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfiguraçãoFirebase {

    private static FirebaseAuth firebaseAuth;
    private static DatabaseReference databaseReference;

    //Retorna uma instancia de FirebaseAuth
    public static FirebaseAuth getFirebaseAuth(){
        if (firebaseAuth == null){
            firebaseAuth = FirebaseAuth.getInstance();
        }
        return firebaseAuth;
    }

    //Retorna uuma Instancia de FirebaseDatabaseRef
    public static DatabaseReference getFirebaseDatabase(){
        if (databaseReference == null){
            databaseReference = FirebaseDatabase.getInstance().getReference();
        }
        return databaseReference;
    }
}
