package com.HBridge.NEX_T;

import java.util.UUID;

/**
 * Created by da Ent on 1-11-2015.
 */
public interface Constants {

    String TAG = "Arduino - Android";
    int REQUEST_ENABLE_BT = 1;

    // message types sent from the BluetoothChatService Handler
    int MESSAGE_STATE_CHANGE = 1;
    int MESSAGE_READ = 2;
    int MESSAGE_WRITE = 3;
    int MESSAGE_SNACKBAR = 4;

    // Constants that indicate the current connection state
    int STATE_NONE = 0;       // we're doing nothing
    int STATE_ERROR = 1;
    int STATE_CONNECTING = 2; // now initiating an outgoing connection
    int STATE_CONNECTED = 3;  // now connected to a remote device

    boolean isConnected = false;



    UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    // Key names received from the BluetoothChatService Handler
    String EXTRA_DEVICE  = "EXTRA_DEVICE";
    String SNACKBAR = "toast";
    int isUpdated = 0;
    int r_states[] = {0,0,0,0,0,0,0};
    double temp = 0.00;
    double hum = 0;
    String button = "";


}
