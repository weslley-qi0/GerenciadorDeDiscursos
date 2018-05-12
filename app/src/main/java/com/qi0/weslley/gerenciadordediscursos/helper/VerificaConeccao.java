package com.qi0.weslley.gerenciadordediscursos.helper;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;

public class VerificaConeccao {

    public static boolean isOnline(Activity activity) {
        ConnectivityManager manager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);

        return manager.getActiveNetworkInfo() != null &&
                manager.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}
