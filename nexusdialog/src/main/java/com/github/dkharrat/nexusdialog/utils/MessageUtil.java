package com.github.dkharrat.nexusdialog.utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

public class MessageUtil {
    public static AlertDialog showAlertMessage(String title, String message, Context context) {
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog, int which) {
               alertDialog.dismiss();
           }
        });
        alertDialog.show();
        return alertDialog;
    }

    public static ProgressDialog newProgressIndicator(String message, Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.setIndeterminate(true);

        return progressDialog;
    }
}
