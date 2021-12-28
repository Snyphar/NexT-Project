package com.HBridge.NEX_T;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;


public class OtherActivity extends Activity {

    private final String DEVICE_ADDRESS="20:13:10:15:33:66";
    private final UUID PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");//Serial Port Service ID
    private BluetoothDevice device;
    private BluetoothSocket socket;
    private OutputStream outputStream;
    private InputStream inputStream;


    boolean deviceConnected=false;
    Thread thread;
    Thread input;
    byte buffer[];
    int bufferPosition;
    boolean stopThread;
    boolean isUpdated = false;
    String temperature,humidity,Button;


     private  ToggleButton b[] = new ToggleButton[7];
     private   ToggleButton b2 ;
     private  ToggleButton b3;
    private  ToggleButton l0;
    private  ToggleButton l1;
    private  ToggleButton l2;
    private  ToggleButton l3;
     private  Button connect;

    private TextView out ;
    private TextView status ;

    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_layout);


        b[0] =  findViewById(R.id.relay01);
        b[1] =  findViewById(R.id.relay02);
        b[2] =  findViewById(R.id.relay03);
        b[3] =  findViewById(R.id.light0);
        b[4] =  findViewById(R.id.light1);
        b[5] =  findViewById(R.id.light2);
        b[6] =  findViewById(R.id.light3);

        connect = findViewById(R.id.connected);

        out =  findViewById(R.id.ouput);
         status =  findViewById(R.id.status);





        setUiEnabled(false);
        connect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                if(deviceConnected){
                    try {
                        Stop();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                else{
                    try {
                        Start();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });



        b[0].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    Constants.r_states[0] = 1;
                } else {
                    // The toggle is disabled
                    Constants.r_states[0] = 0;
                }
                //send(Data());

            }
        });
        b[1].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    Constants.r_states[1] = 1;
                } else {
                    // The toggle is disabled
                    Constants.r_states[1] = 0;
                }
                //send(Data());

            }
        });
        b[2].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    Constants.r_states[2] = 1;
                } else {
                    // The toggle is disabled
                    Constants.r_states[2] = 0;
                }
                //send(Data());
            }
        });
        b[3].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    Constants.r_states[3] = 1;
                } else {
                    // The toggle is disabled
                    Constants.r_states[3] = 0;
                }
                //send(Data());

            }
        });
        b[4].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    Constants.r_states[4] = 1;
                } else {
                    // The toggle is disabled
                    Constants.r_states[4] = 0;
                }
                //send(Data());

            }
        });
        b[5].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    Constants.r_states[5] = 1;
                } else {
                    // The toggle is disabled
                    Constants.r_states[5] = 0;
                }
                //send(Data());

            }
        });
        b[6].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    Constants.r_states[6] = 1;
                } else {
                    // The toggle is disabled
                    Constants.r_states[6] = 0;
                }
                //send(Data());

            }
        });






    }
    protected void onDestroy() {

        super.onDestroy();
        try {
            Stop();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void setUiEnabled(boolean bool)
    {

        for(int i= 0; i<b.length;i++){
            b[i].setEnabled(bool);
        }
        /*b1.setEnabled(bool);
        b2.setEnabled(bool);
        b3.setEnabled(bool);

        l0.setEnabled(bool);
        l1.setEnabled(bool);
        l2.setEnabled(bool);
        l3.setEnabled(bool);*/





    }

    public boolean BTinit()
    {
        boolean found=false;
        BluetoothAdapter bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(),"Device doesn't Support Bluetooth",Toast.LENGTH_SHORT).show();
        }
        if(!bluetoothAdapter.isEnabled())
        {
            Intent enableAdapter = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableAdapter, 0);
                try {
                    Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();
        if(bondedDevices.isEmpty())
        {
            Toast.makeText(getApplicationContext(),"Please Pair the Device first",Toast.LENGTH_SHORT).show();
        }
        else
        {
            for (BluetoothDevice iterator : bondedDevices)
            {
                String deviceName = iterator.getName();
                if(deviceName.contains("HC-06"))
                {
                    device=iterator;
                    found=true;
                    break;
                }
            }
        }
        return found;
    }

    public boolean BTconnect()
    {
        boolean connected=true;
        status.setText("Connecting...");
        connect.setEnabled(false);
        try {
            socket = device.createRfcommSocketToServiceRecord(PORT_UUID);
            socket.connect();
        } catch (IOException e) {
            e.printStackTrace();
            connected=false;
        }
        if(connected)
        {
            try {
                outputStream=socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                inputStream=socket.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        connect.setEnabled(true);


        return connected;
    }

    public void Start() throws InterruptedException {
        if(BTinit())
        {
            if(BTconnect())
            {
                setUiEnabled(true);
                deviceConnected=true;
                send("c");
                beginListenForData();
                status.setText("Connected");
                connect.setText("Disconnect");
                /*while(!isUpdated){

                }*/


                input = new Thread(new Runnable() {
                    @Override
                    public void run() {


                        while(deviceConnected && socket.isConnected()){

                            if(isUpdated){

                                send(Data());

                            }
                            else {
                                send("c");
                            }

                            try {
                                Thread.sleep(1500);
                            } catch (InterruptedException e) {
                                stopThread = true;
                                try {
                                    Stop();
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }
                                e.printStackTrace();
                            }
                        }
                        if(!socket.isConnected()){
                            deviceConnected = false;
                            try {
                                Stop();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                });
                input.start();


            }


        }
    }

    void beginListenForData()
    {
        final Handler handler = new Handler();
        stopThread = false;
        buffer = new byte[1024];
        Thread thread  = new Thread(new Runnable()
        {
            public void run()
            {
                while(!Thread.currentThread().isInterrupted() && !stopThread)
                {

                    try
                    {
                        int byteCount = inputStream.available();
                        if(byteCount > 12)
                        {
                            byte[] rawBytes = new byte[byteCount];
                            inputStream.read(rawBytes);
                            final String string=new String(rawBytes,"UTF-8");
                            handler.post(new Runnable() {
                                public void run()
                                {
                                    String data = Handle_Data(string);
                                    if(!isUpdated){
                                        Log.d("Handled","String Hnadles");
                                        setUiEnabled(true);
                                        set_UI(Button);
                                    }

                                    out.setText(data);


                                }
                            });

                        }
                    }
                    catch (IOException ex)
                    {
                        stopThread = true;
                        Toast.makeText(getApplicationContext(),"Device disconnected!",Toast.LENGTH_SHORT).show();
                        out.setText("Disconnected!!");
                    }
                }
            }
        });

        thread.start();
    }

    public void send(String msg) {

        msg.concat("\n");
        try {
            outputStream.write(msg.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void Stop() throws IOException {
        stopThread = true;
        outputStream.close();
        inputStream.close();
        socket.close();
        setUiEnabled(false);
        deviceConnected=false;
        status.setText("Disconncted");
        connect.setText("Connect");
    }
    public String Data(){
        String data = "C";
        for(int i =0;i<Constants.r_states.length;i++){
            data += String.valueOf(Constants.r_states[i]);
        }
        return data;
    }
    public String Handle_Data(String data){


        int comma[] ={0,0};
        comma[0] = data.indexOf(',');
        comma[1] = data.lastIndexOf(",");
        if(comma[0]>0 && comma[1]>0){
            Button = data.substring(0,comma[0]);
            temperature = data.substring(comma[0]+1,comma[1]);
            humidity = data.substring(comma[1]+1,data.length()-1);

        }
        else{
            Button = "0000000";
            temperature = "0";
            humidity = "0";

        }



        String out =  "Buttons: " + Button + "Temperature: " + temperature +"C" + "Humidity: " + humidity +"%";

        return out;



    }
    public void  set_UI(String button){
        String o = "";
        for(int i = 0; i<7;i++){
            if(button.charAt(i) == '1'){
                b[i].setChecked(true);
                Constants.r_states[i] = 1;
            }
            else if(button.charAt(i) == '0'){
                b[i].setChecked(false);
                Constants.r_states[i] = 0;
                o = o + Constants.r_states[i];
            }


        }

        Log.d("Handled",o);
        out.setText(o);

        isUpdated = true;

    }








}
