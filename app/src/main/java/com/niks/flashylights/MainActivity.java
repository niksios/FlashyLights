package com.niks.flashylights;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    CameraManager cameraManager;
    String cameraId;
    boolean isFlashlightOn = false;
    boolean isFlashlightAvl = false;

    ToggleButton toggleButton;

    private ImageButton btnSwitch;

    TextView text_onoff;
    private Button btnDisco, btnDiscoShort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        // flash switch button
        btnSwitch = (ImageButton) findViewById(R.id.btnSwitch);

        text_onoff = (TextView) findViewById(R.id.text_onoff);

        btnDisco = (Button) findViewById(R.id.btnDisco);

        btnDiscoShort = (Button) findViewById(R.id.btnDiscoShort);


        try {
            cameraId = cameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        //check if flash available

        isFlashlightAvl = getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        btnSwitch.setOnClickListener(view -> toggleFlashlight());

        /*
         * Switch button click event to toggle flash on/off
         */
        btnSwitch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (isFlashlightOn) {
                    // turn off flash
                    turnOffFlash();
                } else {
                    // turn on flash
                    turnOnFlash();
                }
            }
        });

        text_onoff.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (isFlashlightOn) {
                    // turn off flash
                    turnOffFlash();
                } else {
                    // turn on flash
                    turnOnFlash();
                }
            }
        });

        /*
         * Button click event to disco flash light for 10sec
         */
        btnDisco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // turn off flash
                turnOffFlash();
                blink(500, 10);

            }
        });

        /*
         * Button click event to disco flash light for 5sec
         */
        btnDiscoShort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // turn off flash
                turnOffFlash();
                blink(500, 5);
            }
        });

    } // oncreate

    private void blink(final int delay, final int times) {
        Thread t = new Thread() {
            public void run() {
                try {
                    for (int i = 0; i < times * 2; i++) {
                        if (isFlashlightOn) {
                            turnOffFlash_disco();
                        } else {
                            turnOnFlash_disco();
                        }
                        sleep(delay);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    private void toggleFlashlight() {
        try {
            if (isFlashlightAvl) {
                if (isFlashlightOn) {
                    cameraManager.setTorchMode(cameraId, false);
                    isFlashlightOn = false;
                    //change image
                    toggleButtonImage();
                } else {
                    cameraManager.setTorchMode(cameraId, true);
                    isFlashlightOn = true;
                    //change image
                    toggleButtonImage();
                }
            } else {
                Toast.makeText(this, "Device has no FlashLight", Toast.LENGTH_SHORT).show();
            }

        } catch (CameraAccessException e) {
            e.printStackTrace();
            Toast.makeText(this, "Cannot access camera", Toast.LENGTH_SHORT).show();
        }
    }

    /*
     * Toggle switch button images changing image states to on / off
     */
    private void toggleButtonImage() {
        if (isFlashlightOn) {
            btnSwitch.setImageResource(R.mipmap.switch_on);
            text_onoff.setText("ON");
        } else {
            btnSwitch.setImageResource(R.mipmap.switch_off);
            text_onoff.setText("OFF");
        }
    }

    private void turnOnFlash_disco() {
        if (!isFlashlightOn) {
            try {
                cameraManager.setTorchMode(cameraId, true);
            } catch (CameraAccessException e) {
                throw new RuntimeException(e);
            }
            isFlashlightOn = true;
        }
    }

    private void turnOffFlash_disco() {
        if (isFlashlightOn) {
            try {
                cameraManager.setTorchMode(cameraId, false);
            } catch (CameraAccessException e) {
                throw new RuntimeException(e);
            }
            isFlashlightOn = false;
        }
    }

    /*
     * Turning On flash
     */
    private void turnOnFlash() {
        try {
            cameraManager.setTorchMode(cameraId, true);
        } catch (CameraAccessException e) {
            throw new RuntimeException(e);
        }
        isFlashlightOn = true;
        // changing button/switch image
        toggleButtonImage();
    }

    /*
     * Turning Off flash
     */
    private void turnOffFlash() {
        try {
            cameraManager.setTorchMode(cameraId, false);
        } catch (CameraAccessException e) {
            throw new RuntimeException(e);
        }
        isFlashlightOn = false;
        // changing button/switch image
        toggleButtonImage();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Turn off flashlight when the app is stopped
        if (isFlashlightOn) {
            toggleFlashlight();
        }
    }


}
