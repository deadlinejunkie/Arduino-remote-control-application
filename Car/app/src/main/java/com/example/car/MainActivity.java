package com.example.car;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Car";
    Button btnUp, btnStop, btnLeft, btnRight, btnSpeedOne,
            btnSpeedTwo, btnSpeedThree, btnSpeedFour, btnSpeedFive,
            btnSpeedDown, btnClutch;
    Drawable color;
    boolean clutch;
    int speed = 0;

    private static final int REQUEST_ENABLE_BT = 1;
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private ConnectedThread mConnectedThread;
    // SPP UUID
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnUp = findViewById(R.id.btnUp);
        btnStop = findViewById(R.id.btnStop);
        btnClutch = findViewById(R.id.btnClutch);
        btnLeft = findViewById(R.id.btnLeft);
        btnRight = findViewById(R.id.btnRight);
        btnSpeedOne = findViewById(R.id.btnSpeedOne);
        btnSpeedTwo = findViewById(R.id.btnSpeedTwo);
        btnSpeedThree = findViewById(R.id.btnSpeedThree);
        btnSpeedFour = findViewById(R.id.btnSpeedFour);
        btnSpeedFive = findViewById(R.id.btnSpeedFive);
        btnSpeedDown = findViewById(R.id.btnSpeedDown);

        color = btnSpeedDown.getBackground();
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        checkBTState();
        disableSpeed();
    }

    private void checkBTState() {
        // Emulator doesn't support Bluetooth and will return null
        if (btAdapter == null) {
            errorExit("Fatal Error", "Bluetooth не поддерживается");
        } else {
            if (btAdapter.isEnabled()) {
                Log.d(TAG, "...Bluetooth включен ...");
            } else {
                //Prompt user to turn on Bluetooth
                Intent enableBtIntent = new Intent(btAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    private void disableSpeed() {
        btnSpeedDown.setEnabled(false);
        btnSpeedOne.setEnabled(false);
        btnSpeedThree.setEnabled(false);
        btnSpeedTwo.setEnabled(false);
        btnSpeedFour.setEnabled(false);
        btnSpeedFive.setEnabled(false);

        btnClutch.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        clutch = true;
                        enableSpeed();
                        break;
                    case MotionEvent.ACTION_UP:
                        disableSpeed();
                        clutch = false;
                        break;
                }
                return false;
            }
        });

        private void enableSpeed(){
            if (speed > 0) btnSpeedTwo.setEnabled(true);
            if (speed > 1) btnSpeedThree.setEnabled(true);
            if (speed > 2) btnSpeedFour.setEnabled(true);
            if (speed > 3) btnSpeedFive.setEnabled(true);
            btnSpeedOne.setEnabled(true);
            btnSpeedDown.setEnabled(true);
        }


        btnSpeedOne.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mConnectedThread.write("1");
                setDefaultColor();
                down = false;
                speed = 1;
                btnSpeedOne.setBackgroundColor(Color.BLUE);
            }
        });

        btnSpeedTwo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mConnectedThread.write("2");
                speed = 2;
                setDefaultColor();
                down = false;
                btnSpeedTwo.setBackgroundColor(Color.BLUE);
            }
        });

        btnSpeedThree.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mConnectedThread.write("3");
                speed = 3;
                setDefaultColor();
                down = false;
                btnSpeedThree.setBackgroundColor(Color.BLUE);
            }
        });

        btnSpeedFour.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mConnectedThread.write("4");
                setDefaultColor();
                speed = 4;
                down = false;
                btnSpeedFour.setBackgroundColor(Color.BLUE);
            }
        });

        btnSpeedFive.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mConnectedThread.write("5");
                setDefaultColor();
                speed = 5;
                down = false;
                btnSpeedFive.setBackgroundColor(Color.BLUE);
            }
        });

        btnSpeedDown.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                down = true;
                setDefaultColor();
                btnSpeedDown.setBackgroundColor(Color.BLUE);
            }
        });
        private void errorExit(String fatal_error, String bluetooth_не_поддерживается){
            Toast.makeText(getBaseContext(), title + " - " + message, Toast.LENGTH_LONG).show();
            finish();
        }
    }
}



