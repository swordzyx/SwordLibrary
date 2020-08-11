package com.example.bluetoothpratice.bluetoothChat;

import android.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.bluetoothpratice.R;
import com.example.bluetoothpratice.common.logger.Log;
import com.example.bluetoothpratice.common.util.Constants;

public class BluetoothChatFragment extends Fragment {
    private static final String TAG = "BluetoothChatFragment";
    private BluetoothAdapter mBluetoothAdapter;

    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;


    private ArrayAdapter<String> mConversationArrayAdapter;
    private StringBuffer mOutBuffer;
    private String mConnectedDeviceName;

    private ListView mConversationView;
    private EditText mOutEditText;
    private Button mSendButton;

    private BluetoothChatService mChatService = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter == null){
            Activity activity = getActivity();
            Toast.makeText(activity, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            activity.finish();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,  @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bluetooth_chat, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mConversationView = (ListView) view.findViewById(R.id.in);
        mOutEditText = (EditText) view.findViewById(R.id.edit_text_out);
        mSendButton = (Button) view.findViewById(R.id.button_send);
    }

    @Override
    public void onStart(){
        super.onStart();

        if (!mBluetoothAdapter.isEnabled()){
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, REQUEST_ENABLE_BT);
        }else{
            setupChat();
        }
    }

    //onActivityResult
    @Override
    public void onResume(){
        super.onResume();

        if (mChatService != null) {
            if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
                mChatService.start();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        switch(requestCode){
            case REQUEST_ENABLE_BT:
                if (resultCode == Activity.RESULT_OK){
                    setupChat();
                }else{
                    Log.d(TAG, "BT not enable");
                    Toast.makeText(getActivity(), R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
                break;
            case REQUEST_CONNECT_DEVICE_SECURE:
                if (resultCode == Activity.RESULT_OK)
                    connectDevice(data, true);
                break;
            case REQUEST_CONNECT_DEVICE_INSECURE:
                if (resultCode == Activity.RESULT_OK)
                    connectDevice(data, false);
                break;
            default:
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.bluetooth_chat, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.secure_connect_scan:
                Intent serverIntent = new Intent(getActivity(), DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
                return true;
            case R.id.insecure_connect_scan:
                Intent intent = new Intent(getActivity(), DeviceListActivity.class);
                startActivityForResult(intent, REQUEST_CONNECT_DEVICE_INSECURE);
                return true;
            case R.id.discoverable:
                ensureDiscoverable();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void connectDevice(Intent data, boolean b) {
        String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);

        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        mChatService.connect(device, b);
    }

    private TextView.OnEditorActionListener mWriterListener = new TextView.OnEditorActionListener(){

        @Override
        public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
            if (id == EditorInfo.IME_NULL && keyEvent.getAction() == KeyEvent.ACTION_UP){
                String message = textView.getText().toString();
                sendMessage(message);
            }
            return true;
        }
    };

    private void sendMessage(String message) {
        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED){
            Toast.makeText(getActivity(), R.string.not_connected, Toast.LENGTH_SHORT).show();
            return ;
        }

        if (message.length() > 0){
            byte[] send = message.getBytes();
            mChatService.write(send);

            mOutBuffer.setLength(0);
            mOutEditText.setText(mOutBuffer);
        }
    }

    private void setupChat() {
        mConversationArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.message);

        mConversationView.setAdapter(mConversationArrayAdapter);

        mOutEditText.setOnEditorActionListener(mWriterListener);

        mSendButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                TextView textView = (TextView)getActivity().findViewById(R.id.edit_text_out);
                String message = (String)textView.getText();
                sendMessage(message);
            }
        });

        mChatService = new BluetoothChatService(getActivity(), mHandler);

        mOutBuffer = new StringBuffer();
    }


    private final Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case Constants.MESSAGE_DEVICE_NAME:
                    mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
                    Toast.makeText(getActivity(), "Connect to " + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    break;
                case Constants.MESSAGE_STATE_CHANGE:
                    handleStateChange(msg.arg1);
                    break;
                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    String message = new String(readBuf, 0, msg.arg1);
                    mConversationArrayAdapter.add(mConnectedDeviceName + " " + message);
                    break;
                case Constants.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[])msg.obj;
                    String writeMessage = new String(writeBuf, 0, msg.arg1);
                    mConversationArrayAdapter.add(writeMessage);
                    break;
                case Constants.MESSAGE_TOAST:
                    Toast.makeText(getActivity(), msg.getData().getString(Constants.TOAST), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private void handleStateChange(int state) {
        switch(state){
            case BluetoothChatService.STATE_LISTEN:
            case BluetoothChatService.STATE_NONE:
                setStatus(R.string.title_not_connected);
                break;
            case BluetoothChatService.STATE_CONNECTING:
                setStatus(R.string.title_connecting);
                break;
            case BluetoothChatService.STATE_CONNECTED:
                setStatus(getString(R.string.title_connected_to, mConnectedDeviceName));
                mConversationArrayAdapter.clear();
                break;
        }
    }

    private void ensureDiscoverable(){
        if (mBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE){
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            startActivity(intent);
        }

    }

    private void setStatus(int resId){
        FragmentActivity activity = getActivity();
        if (activity == null){
            return ;
        }
        ActionBar actionBar = activity.getActionBar();
        if (null == actionBar) return;
        actionBar.setSubtitle(resId);
    }

    private void setStatus(CharSequence text){
        FragmentActivity activity = getActivity();
        if (activity == null){
            return ;
        }
        ActionBar actionBar = activity.getActionBar();
        if (null == actionBar) return;
        actionBar.setSubtitle(text);
    }

}
