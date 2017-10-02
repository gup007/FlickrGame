package com.myntra.flickrgame;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;

/**
 * Created by SurvivoR on 7/3/2017.
 * {@link BaseActivity}
 */

public class BaseActivity extends FragmentActivity {

    private ProgressDialog mProcessDialog;

    protected void showLoadingDialog() {
        if (isFinishing()) {
            return;
        }
        if (mProcessDialog == null) {
            mProcessDialog = ProgressDialog.show(this, null, null);
        }
        mProcessDialog.show();
    }

    protected void hideLoadingDialog() {
        if (isFinishing()) {
            return;
        }
        if (mProcessDialog != null && mProcessDialog.isShowing()) {
            mProcessDialog.dismiss();
        }
    }

    protected void showAlertDialog(String message) {
        showAlertDialog(message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    protected void showAlertDialog(String message, DialogInterface.OnClickListener listener) {
        if (isFinishing()) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton("Restart",listener);
        AlertDialog alert = builder.create();
        alert.show();
    }
}
