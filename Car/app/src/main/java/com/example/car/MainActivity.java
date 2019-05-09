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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

        btnUp.setOnTouchListener(new View.OnTouchListener() {
           public boolean onTouch(View v, MotionEvent event) {
               switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        up();
                    break;
                    case MotionEvent.ACTION_UP:
                        mConnectedThread.write("C");
                    break;
                    }
                return false;
           }
        });
        btnStop.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                mConnectedThread.write("C");
                return false;
                }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mConnectedThread.write("9");
            }
        });

        btnLeft.setOnTouchListener(new View.OnTouchListener() {
           public boolean onTouch(View v, MotionEvent event) {
              switch (event.getAction()) {
                  case MotionEvent.ACTION_DOWN:
                      mConnectedThread.write("A");
                      break;
                  case MotionEvent.ACTION_UP:
                      mConnectedThread.write("T");
                  break;
                  }
              return false;
           }
        });

        btnRight.setOnTouchListener(new View.OnTouchListener() {
           public boolean onTouch(View v, MotionEvent event) {
              switch (event.getAction()) {
                 case MotionEvent.ACTION_DOWN:
                    mConnectedThread.write("D");
                  break;
                 case MotionEvent.ACTION_UP:
                    mConnectedThread.write("T");
                 break;
              }
              return false;
           }
        });

        public void onResume() {
            super.onResume();
            conectBluetooth();
            }
            
        void conectBluetooth(){
            Log.d(TAG, "...onResume - Попытка соединения ..");
            // Set up a pointer to the remote node using it's address.
            String address = "98:D3:33:80:56:5D";
            BluetoothDevice device = btAdapter.getRemoteDevice(address);
            // Two things are needed to make a connection:
            //   A MAC address, which we got above.
            //   A Service ID or UUID.  In this case we are using the
            //     UUID for SPP.
            try {
                btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
                } catch (IOException e) {
                     errorExit("Fatal Error", "In onResume() and socket create failed: " + e.getMessage() + ".");
               }

            // Discovery is resource intensive.  Make sure it isn't going on
            // when you attempt to connect and pass your message.
            btAdapter.cancelDiscovery();
            // Establish the connection.  This will block until it connects.
            Log.d(TAG, "...Соединяемся..");
                try {
                   btSocket.connect();
                   Log.d(TAG, "...Соединение установлено и готово к передачи данных...");
                } catch (IOException e) {

                    Log.d(TAG, "...Не удалось...");
               }
            // Create a data stream so we can talk to server.
            Log.d(TAG, "...Создание Socket...");
            mConnectedThread = new ConnectedThread(btSocket);
            mConnectedThread.start();
            }

        public void onPause(){
               super.onPause();
               Log.d(TAG, "...In onPause()...");
               try     {
                   btSocket.close();
                 } catch (IOException e2) {
               errorExit("Fatal Error", "In onPause() and failed to close socket." + e2.getMessage() + ".");
                }
            }
                     
            
        private class ConnectedThread extends Thread { 
            private final BluetoothSocket mmSocket;
            //private final InputStream mmInStream;
            private final OutputStream mmOutStream;
            public ConnectedThread(BluetoothSocket socket) {
                mmSocket = socket;
                InputStream tmpIn = null;
                OutputStream tmpOut = null;
             // Get the input and output streams, using temp objects because
             // member streams are final
             try {
                tmpOut = socket.getOutputStream();
                 } catch (IOException e) {
                 Log.d(TAG, "..Oшибка!!!!!!!!!!!!!!: " + tmpOut + "...");
             }
            
            mmOutStream = tmpOut; 
            }
            /* Call this from the main activity to send data to the remote device */
            public void write(String message) {
               	Log.d(TAG, "..Данные для отправки: " + message + "...");
                 	byte[] msgBuffer = message.getBytes();
                   	try {
                       mmOutStream.write(msgBuffer);
                       Log.d(TAG, "..Данные для отправки: " + msgBuffer + "...");
                        } catch (IOException e) {
                       Log.d(TAG, "...Ошибка отправки данных: " + e.getMessage() + "...");
                    conectBluetooth(); 
                }
            }
            
            public void cancel() {
                 try {
                      mmSocket.close();
                     } catch (IOException e) { }
            }
        }
    }}
}

        private void errorExit(String fatal_error, String bluetooth_не_поддерживается){
            Toast.makeText(getBaseContext(), title + " - " + message, Toast.LENGTH_LONG).show();
            finish();
        }
    }
}
