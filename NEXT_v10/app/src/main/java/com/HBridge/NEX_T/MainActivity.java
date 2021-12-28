package com.HBridge.NEX_T;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Set;



public class MainActivity extends AppCompatActivity {

    private BluetoothDevice device;
    public BroadcastReceiver mReceiver;
    boolean found=false;
    public static final String EXTRA_MESSAGE = "com.example.snyph.h_bridgev01";
    private  String adress = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button connect = findViewById(R.id.connectbtn);

        if(BTfind()){
            Intent intent = new Intent(MainActivity.this, OtherActivity.class);


            intent.putExtra(EXTRA_MESSAGE,adress);
            startActivity(intent);
        }

        else{
            connect.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Perform action on click

                    if(BTfind()){
                        Intent intent = new Intent(MainActivity.this, OtherActivity.class);


                        intent.putExtra(EXTRA_MESSAGE,adress);
                        startActivity(intent);
                    }
                }
            });

        }


    }

    public boolean BTfind()
    {

        BluetoothAdapter bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(),"Device doesnt Support Bluetooth",Toast.LENGTH_SHORT).show();
        }
        if(!bluetoothAdapter.isEnabled())
        {
            Intent enableAdapter = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableAdapter, 0);
            try {
                Thread.sleep(1000);
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

                    adress = iterator.getAddress();
                    Toast.makeText(getBaseContext(),"Device found", Toast.LENGTH_SHORT).show();
                    found=true;
                    break;
                }
            }



        }
        return found;
    }
}

