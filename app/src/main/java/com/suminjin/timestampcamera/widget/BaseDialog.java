package com.suminjin.timestampcamera.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.suminjin.timestampcamera.R;


/**
 * Created by jspark on 2016-03-14.
 */
public abstract class BaseDialog extends Dialog {
    protected View.OnClickListener defaultClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    };
    protected Context context;

    public BaseDialog(Context context) {
        super(context);
        this.context = context;
    }

    protected void initWindowFeatures() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    public void setOnPositiveBtnClickListener(View.OnClickListener listener) {
        findViewById(R.id.btnOk).setOnClickListener(listener);
    }

    public void setOnNegativeBtnClickListener(View.OnClickListener listener) {
        View view = findViewById(R.id.btnCancel);
        view.setOnClickListener(listener);
        view.setVisibility(View.VISIBLE);
    }
}
