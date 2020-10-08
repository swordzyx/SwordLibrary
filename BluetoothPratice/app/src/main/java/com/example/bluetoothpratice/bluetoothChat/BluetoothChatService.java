package com.example.bluetoothpratice.bluetoothChat;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Output;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import androidx.annotation.Nullable;

import com.example.bluetoothpratice.common.logger.Log;
import com.example.bluetoothpratice.common.util.Constants;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class BluetoothChatService{
    public static final String TAG = "BluetoothChatService";
    private static final String NAME_SECURE = "BluetoothChatSecure";
    private static final String NAME_INSECURE = "BluetoothChatInsecure";

    private static final UUID MY_UUID_SECURE = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
    private static final UUID MY_UUID_INSECURE = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

    private BluetoothAdapter bluetoothAdapter;
    private int state = STATE_NONE;

    private AcceptThread mSecureAcceptThread;
    private AcceptThread mInsecureAcceptThread;
    private ConnectThread mConnectThread;
    private ConnectedThread mConnectedThread;
    public Handler mHandler;
    private int mNewState;


    public static final int STATE_NONE = 0;
    public static final int STATE_LISTEN =  1;
    public static final int STATE_CONNECTING = 2;
    public static final int STATE_CONNECTED = 3;



    public BluetoothChatService(Context context, Handler handler){
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mHandler = handler;
        state = STATE_NONE;
        mNewState = state;
    }

    //就是开启蓝牙连接的监听线程
    public synchronized void start(){
        if (mConnectedThread != null){
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        if (mConnectThread != null){
            mConnectThread.cancel();
            mConnectThread = null;
        }

        if (mSecureAcceptThread == null){
            mSecureAcceptThread = new AcceptThread(true);
            mSecureAcceptThread.start();
        }

        if (mInsecureAcceptThread == null){
            mInsecureAcceptThread = new AcceptThread(false);
            mInsecureAcceptThread.start();
        }

        updateUserInterfaceTitle();
    }

    public synchronized void connect(BluetoothDevice device, boolean secure){
        if (state == STATE_CONNECTING){
            if (mConnectThread != null){
                mConnectThread.cancel();
                mConnectThread = null;
            }
        }

        if (mConnectedThread != null){
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        mConnectThread = new ConnectThread(device, secure);
        mConnectThread.start();

        updateUserInterfaceTitle();
    }

    public synchronized void connected(BluetoothSocket socket, BluetoothDevice device, String socketType){
        if (mConnectThread != null){
            mConnectThread.cancel();
            mConnectThread = null;
        }

        if (mConnectedThread != null){
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        if (mSecureAcceptThread != null){
            mSecureAcceptThread.cancel();
            mSecureAcceptThread = null;
        }

        if (mInsecureAcceptThread != null){
            mInsecureAcceptThread.cancel();
            mInsecureAcceptThread = null;
        }

        mConnectedThread = new ConnectedThread(socket, device, socketType);
        mConnectedThread.start();

        Message msg = mHandler.obtainMessage(Constants.MESSAGE_DEVICE_NAME);
        Bundle arg = new Bundle();
        arg.putString(Constants.DEVICE_NAME, device.getName());
        msg.setData(arg);
        mHandler.sendMessage(msg);

        updateUserInterfaceTitle();
    }


    public int getState(){
        return state;
    }

    public void setState(int s){
        state = s;
    }

    public synchronized void stop(){
        if (mConnectThread != null){
            mConnectThread.cancel();
            mConnectThread = null;
        }

        if (mConnectedThread != null){
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        if (mSecureAcceptThread != null){
            mSecureAcceptThread.cancel();
            mSecureAcceptThread = null;
        }

        if (mInsecureAcceptThread != null){
            mInsecureAcceptThread.cancel();
            mInsecureAcceptThread = null;
        }

        state = STATE_NONE;

        updateUserInterfaceTitle();

    }

    public void write(byte[] data){
        ConnectedThread t;

        synchronized (this){
            if (state != STATE_CONNECTED) return;
            t = mConnectedThread;
        }

        t.write(data);
    }

    private void connectionFailed(){
        Message msg = mHandler.obtainMessage(Constants.MESSAGE_TOAST);
        Bundle arg = new Bundle();
        arg.putString(Constants.TOAST, "Unable to connect device");
        msg.setData(arg);
        mHandler.sendMessage(msg);

        state = STATE_NONE;

        updateUserInterfaceTitle();

        BluetoothChatService.this.start();
    }

    private void connectionLost(){
        Message msg = mHandler.obtainMessage();
        Bundle arg = new Bundle();
        arg.putString(Constants.TOAST, "Device connection was lost");
        msg.setData(arg);
        mHandler.sendMessage(msg);

        updateUserInterfaceTitle();

        BluetoothChatService.this.start();
    }

    private void updateUserInterfaceTitle(){
        state = getState();
        Log.d(TAG, "updateUserInterfaceTitle() " + mNewState + "->" + state);
        mNewState = state;

        mHandler.obtainMessage(Constants.MESSAGE_STATE_CHANGE, mNewState, -1).sendToTarget();
    }

    class AcceptThread extends Thread {
        final BluetoothServerSocket serverSocket;
        final String mSocketType ;

        AcceptThread(boolean secura){
            BluetoothServerSocket tmp = null;
            mSocketType = secura?"Secure":"InSecure";
            try{
                if (secura){
                    tmp = bluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME_SECURE, MY_UUID_SECURE);
                }else{
                    tmp = bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(NAME_INSECURE, MY_UUID_INSECURE);
                }
            } catch (IOException e){
                e.printStackTrace();
            }
            serverSocket = tmp;
            state = STATE_LISTEN;
        }

        @Override
        public void run (){
            Log.d(TAG, "Socket Type: " + mSocketType +
                    "BEGIN mAcceptThread" + this);
            setName("AcceptThread " + mSocketType);

            BluetoothSocket bluetoothSocket = null;

            while(state != STATE_CONNECTED){
                try{
                    bluetoothSocket = serverSocket.accept();
                }catch(IOException e){
                    Log.e(TAG, "Socket Type：" + mSocketType + "accept failed " + e.getMessage());
                    break;
                }

                if (bluetoothSocket != null){
                    synchronized (BluetoothChatService.this){
                        switch (state){
                            case STATE_LISTEN:
                            case STATE_CONNECTING:
                                connected(bluetoothSocket, bluetoothSocket.getRemoteDevice(), mSocketType);
                                break;
                            case STATE_NONE:
                            case STATE_CONNECTED:
                                try {
                                    bluetoothSocket.close();
                                } catch (IOException e) {
                                    Log.e(TAG, "Could not close unwanted socket" + e.getMessage());
                                    e.printStackTrace();
                                }
                                break;
                        }
                    }
                }
                Log.i(TAG, "END mAcceptThread, socket Type: " + mSocketType);
            }
        }

        public void cancel(){
            Log.d(TAG, mSocketType + " cancel() " + this);
            if (serverSocket != null){
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    Log.e(TAG, mSocketType + " close() of server failed " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }

    }

    class ConnectThread extends Thread{
        private final BluetoothSocket mSocket;
        private final BluetoothDevice mDevice;
        private final String mSocketType;

        public ConnectThread(BluetoothDevice device, boolean secure){
            BluetoothSocket tmp = null;
            mDevice = device;
            mSocketType = secure ? "Secure" : "InSecure";
            try{
                if (secure){
                    tmp = device.createRfcommSocketToServiceRecord(MY_UUID_SECURE);
                }else{
                    tmp = device.createInsecureRfcommSocketToServiceRecord(MY_UUID_INSECURE);
                }
            }catch(IOException e){
                Log.e(TAG, "Socket Type: " + mSocketType + " create() failed " + e.getMessage());
            }
            mSocket = tmp;

            state = STATE_CONNECTING;
        }

        @Override
        public void run(){
            Log.i(TAG, "Begin ConnectThread SocketType：" + mSocketType);
            setName("ConnectThread " + mSocketType);

            bluetoothAdapter.cancelDiscovery();

            try {
                mSocket.connect();
            } catch (IOException e) {
                try {
                    mSocket.close();
                } catch (IOException ex) {
                    Log.e(TAG, "unable to close() " + mSocketType + " socket during connection failure " + ex.getMessage());
                }
                connectionFailed();
                return ;
            }

            connected(mSocket, mDevice, mSocketType);
        }

        public void cancel(){
            Log.i(TAG, "Socket Type: " + mSocketType + " cancel()");
            if (mSocket != null){
                try {
                    mSocket.close();
                } catch (IOException e) {
                    Log.e(TAG, "Socket Type " + mSocketType + " cancel() failed" + e.getMessage());
                }
            }
        }
    }

    class ConnectedThread extends Thread{
        private BluetoothSocket mSocket;

        private OutputStream bluetoothOutput;
        private InputStream bluetoothInput;

        public ConnectedThread(BluetoothSocket socket, BluetoothDevice device, String socketType){
            Log.d(TAG, "create ConnectedThread SocketType：" + socketType);
            OutputStream tmpOutput = null;
            InputStream tmpInput = null;

            mSocket = socket;
            try {
                tmpInput = socket.getInputStream();
                tmpOutput = socket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "create ConnectedThread failed, SocketType: " + socketType);
            }

            bluetoothOutput = tmpOutput;
            bluetoothInput = tmpInput;
            state = STATE_CONNECTED;
        }

        @Override
        public void run(){
            byte[] bytes = new byte[1024];
            int count = 0;

            while(state == STATE_CONNECTED){
                try {
                    count = bluetoothInput.read(bytes);

                    mHandler.obtainMessage(Constants.MESSAGE_READ, -1, -1, bytes);
                } catch (IOException e) {
                    Log.e(TAG, "read failed, disconnected " + e.getMessage());
                    connectionLost();
                    break;
                }
            }
        }

        public void write(byte[] data){
            try {
                bluetoothOutput.write(data);
                mHandler.obtainMessage(Constants.MESSAGE_WRITE, -1, -1, data).sendToTarget();
            } catch (IOException e) {
                Log.e(TAG, "write failed " + e.getMessage());
            }
        }

        public void cancel(){
            try{
                mSocket.close();
            }catch(IOException e){

            }
        }
    }


}
