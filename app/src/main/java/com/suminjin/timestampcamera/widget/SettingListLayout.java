package com.suminjin.timestampcamera.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.suminjin.timestampcamera.R;

/**
 * Created by parkjisun on 2017. 3. 17..
 */

public class SettingListLayout extends LinearLayout {
    private TextView txtTitle;
    private LinearLayout layoutContents;
    private Context context;
    private ViewSwitchManager viewSwitchManager;

    public SettingListLayout(Context context) {
        this(context, null);
    }

    public SettingListLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public SettingListLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }

    private void initViews(Context context) {
        this.context = context;

        // root view의 속성 설정
        ViewGroup.LayoutParams paramsRoot = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setLayoutParams(paramsRoot);
        setOrientation(LinearLayout.VERTICAL);

        // title view 추가
        txtTitle = (TextView) LayoutInflater.from(context).inflate(R.layout.layout_setting_title, null, false);
        addView(txtTitle);

        // contents를 위한 view 추가.
        viewSwitchManager = new ViewSwitchManager();
        layoutContents = new LinearLayout(context);
        LinearLayout.LayoutParams paramsLayoutContents = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutContents.setLayoutParams(paramsLayoutContents);
        layoutContents.setOrientation(LinearLayout.VERTICAL);
        addView(layoutContents);

        if (isInEditMode()) {
            setTitle("Test");
            addContents(new String[]{"aaa", "bbb", "ccc"});
        }
    }

    public void setTitle(int resId) {
        txtTitle.setText(resId);
    }

    public void setTitle(String str) {
        txtTitle.setText(str);
    }

    public void addContents(String[] list) {
        LayoutInflater inflater = LayoutInflater.from(context);
        layoutContents.removeAllViews();
        for (int i = 0; i < list.length; i++) {
            String s = list[i];
            if (!s.isEmpty()) {
                TextView textView = (TextView) inflater.inflate(R.layout.layout_setting_element, null, false);
                textView.setText(s);
                viewSwitchManager.add(i, textView, new ViewSwitchManager.OnClickViewSwitchListener() {
                    @Override
                    public void onClick(View v) {
                        layoutContents.setTag(v.getTag());
                    }
                });
                layoutContents.addView(textView);
            }
        }
    }

    public int getSelectedIndex() {
        return (int) layoutContents.getTag();
    }

    public void setSelectedIndex(int index) {
        layoutContents.setTag(index);
        viewSwitchManager.setSelection(index);
    }
}
