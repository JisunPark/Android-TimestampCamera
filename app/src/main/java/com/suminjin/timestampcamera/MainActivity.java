package com.suminjin.timestampcamera;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.suminjin.timestampcamera.widget.CustomProgressDialog;
import com.suminjin.timestampcamera.widget.ImageDialog;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends Activity {

    private static final int REQUEST_CODE_CHANGE_SETTING = 1;

    private static final int TIME_STAMP_UPDATE_INTERVAL = 1000;

    private Camera camera;
    private CameraPreview preview;

    private TextView txtDate;
    private SimpleDateFormat sdf;
    private Handler handler = new Handler();
    private Runnable runnableSetDateText = new Runnable() {
        @Override
        public void run() {
            txtDate.setText(sdf.format(new Date()));
            handler.postDelayed(runnableSetDateText, TIME_STAMP_UPDATE_INTERVAL);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 시간 표시 텍스트
        txtDate = (TextView) findViewById(R.id.txtDate);
        sdf = new SimpleDateFormat(Config.TIME_STAMP_FORMAT, Locale.getDefault());
        txtDate.setText(sdf.format(new Date()));
        handler.postDelayed(runnableSetDateText, TIME_STAMP_UPDATE_INTERVAL);

        // 설정 버튼
        ImageView btnSetting = (ImageView) findViewById(R.id.btnSetting);
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingIntent = new Intent(MainActivity.this, SettingActivity.class);
                startActivityForResult(settingIntent, REQUEST_CODE_CHANGE_SETTING);
            }
        });

        // camera preview 시작
        startPreview(null);
    }

    /**
     * camera와 preview 설정
     */
    private void startPreview(String pictureSizeStr) {
        if (camera != null) {
            camera.release();
            camera = null;
        }

        camera = getCameraInstance();

        if (camera == null) {
            Log.e("jisunLog", "Failed camera open");
        } else {

            FrameLayout layoutPreview = (FrameLayout) findViewById(R.id.layoutPreview);
            if (preview != null) {
                layoutPreview.removeView(preview);
                preview = null;
            }

            preview = new CameraPreview(this, camera);
            preview.setKeepScreenOn(true);

            // 저장 사진과 preview의 사이즈 등을 설정
            ImageManager.adjustCameraParameters(this, camera, pictureSizeStr);

            // preview가 보여지는 화면의 비율 설정
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            FrameLayout.LayoutParams p = (FrameLayout.LayoutParams) layoutPreview.getLayoutParams();
            p.height = displayMetrics.widthPixels / 3 * 4;
            layoutPreview.setLayoutParams(p);

            // preview를 layout에 추가하고, 날짜 영역을 화면 상위로 올림
            layoutPreview.addView(preview);
            findViewById(R.id.txtDate).bringToFront();
        }
    }

    public static int setCameraDisplayOrientation(Activity activity,
                                                  int cameraId, android.hardware.Camera camera) {
        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }

        return result;
    }

    public Camera getCameraInstance() {
        Camera camera = null;

        int numCams = Camera.getNumberOfCameras();
        if (numCams > 0) {
            try {
                // Camera.CameraInfo.CAMERA_FACING_FRONT or Camera.CameraInfo.CAMERA_FACING_BACK
                int cameraFacing = Camera.CameraInfo.CAMERA_FACING_BACK;
                camera = Camera.open(cameraFacing);
                // camera orientation
                camera.setDisplayOrientation(setCameraDisplayOrientation(this, cameraFacing, camera));
                // get Camera parameters
                Camera.Parameters params = camera.getParameters();
                // picture image orientation
                params.setRotation(setCameraDisplayOrientation(this, cameraFacing, camera));
                camera.startPreview();

            } catch (RuntimeException ex) {
                Toast.makeText(this, "camera_not_found ] " + ex.getMessage().toString(), Toast.LENGTH_LONG).show();
                Log.d(Config.TAG, "camera_not_found ] " + ex.getMessage().toString());
            }
        }

        return camera;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_CHANGE_SETTING:
                if (resultCode == RESULT_OK) {


                    // TODO jisun : 사진 저장 사이즈 변경하기
                    String str = data.getStringExtra(Config.PREF_KEY_PICTURE_SIZE);

                    ((FrameLayout) findViewById(R.id.layoutPreview)).removeView(preview);
                    preview = null;

                    startPreview(str);
                }
                break;
            default:
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public void onClickCapture(View v) {
        camera.takePicture(null, null, new Camera.PictureCallback() {

            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                new AsyncTask<byte[], Void, File>() {
                    public float textSize;
                    private CustomProgressDialog progressDialog;

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        progressDialog = new CustomProgressDialog(MainActivity.this);
                        progressDialog.setCancelable(false);
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();

                        textSize = txtDate.getTextSize();
                        textSize = txtDate.getTextSize();
                    }

                    @Override
                    protected File doInBackground(byte[]... params) {
                        byte[] data = params[0];
                        Bitmap bitmap = ImageManager.saveImageWithTimeStamp(MainActivity.this, data, 0, data.length, textSize);
                        File file = ImageManager.saveFile(bitmap);
                        refreshGallery(file);
                        return file;
                    }

                    @Override
                    protected void onPostExecute(File file) {
                        progressDialog.dismiss();

                        if (file == null) {
                            Log.e(Config.TAG, "Error creating media file, check storage permissions");
                        } else {
                            ImageDialog dialog = new ImageDialog(MainActivity.this, file);
                            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    MainActivity.this.camera.startPreview();
                                }
                            });
                            dialog.show();
                        }

                        super.onPostExecute(file);
                    }
                }.execute(data);
            }
        });
    }

    private void refreshGallery(File file) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(Uri.fromFile(file));
        sendBroadcast(mediaScanIntent);
    }

    public void onClickBtnGallery(View v) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setType("image/*");
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(runnableSetDateText);
        if (camera != null) {
            camera.release();
            camera = null;
        }
        super.onDestroy();
    }
}
