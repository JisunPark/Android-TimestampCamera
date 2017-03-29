package com.suminjin.timestampcamera.widget;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.suminjin.timestampcamera.R;

public class CustomDialog extends BaseDialog {

    public CustomDialog(Context context, String title, String msg) {
        super(context);
        initViews(title, msg);
    }

    public CustomDialog(Context context, int titleResId, String msg) {
        super(context);
        String title = context.getString(titleResId);
        initViews(title, msg);
    }

    public CustomDialog(Context context, int titleResId, int msgResId) {
        super(context);
        String title = context.getString(titleResId);
        String msg = context.getString(msgResId);
        initViews(title, msg);
    }

    private void initViews(String title, String msg) {
        initWindowFeatures();
        setContentView(R.layout.dialog_custom);

        // ok
        findViewById(R.id.btnOk).setOnClickListener(defaultClickListener);

        // cancel
        View view = findViewById(R.id.btnCancel);
        view.setOnClickListener(defaultClickListener);
        view.setVisibility(View.GONE);

        // title, msg
        ((TextView) findViewById(R.id.title)).setText(title);
        ((TextView) findViewById(R.id.msg)).setText(msg);

        // close
        findViewById(R.id.btnClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
