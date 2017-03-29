package com.suminjin.timestampcamera;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.suminjin.timestampcamera.widget.SettingListLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by parkjisun on 2017. 3. 9..
 */

public class SettingActivity extends Activity {


    private SettingListLayout pictureSizeLayout;
    private SettingListLayout textPositionLayout;
    private String[] pictureSizeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        LinearLayout layout = (LinearLayout) findViewById(R.id.layoutSettingContents);

        // 사진 크기
        pictureSizeList = Config.getSharedPreferenceString(this, Config.PREF_KEY_PICTURE_SIZE_LIST).split(Config.SPLIT_CHAR);
        pictureSizeLayout = new SettingListLayout(this);
        pictureSizeLayout.setTitle(R.string.picture_size);
        pictureSizeLayout.addContents(pictureSizeList);
        String pictureSizeStr = Config.getSharedPreferenceString(this, Config.PREF_KEY_PICTURE_SIZE);
        int i = 0;
        for (; i < pictureSizeList.length; i++) {
            String s = pictureSizeList[i];
            if (pictureSizeStr.equals(s)) {
                break;
            }
        }
        if (i < pictureSizeList.length) {
            pictureSizeLayout.setSelectedIndex(i);
        }
        layout.addView(pictureSizeLayout);

        // 텍스트 위치
        textPositionLayout = new SettingListLayout(this);
        textPositionLayout.setTitle(R.string.text_position);
        String[] temp = getResources().getStringArray(R.array.text_position);
        textPositionLayout.addContents(temp);
        layout.addView(textPositionLayout);
    }

    public void onClickBtnSave(View v) {
        Intent intent = new Intent();
        intent.putExtra(Config.PREF_KEY_PICTURE_SIZE, pictureSizeList[pictureSizeLayout.getSelectedIndex()]);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void onClickBtnCancel(View v) {
        finish();
    }
}
