package unsw.samg.code.miband3.Activities;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.widget.TextView;

import unsw.samg.code.miband3.R;

public class DeviceInfo extends Activity {

    BluetoothAdapter bluetoothAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deviceinfo);

        initialiseObjects();

    }

    void initialiseObjects() {

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        TextView tv;
        tv = (TextView) findViewById(R.id.textView2);
        tv.setText(bluetoothAdapter.getAddress());
        tv = (TextView) findViewById(R.id.textView4);
        tv.setText(bluetoothAdapter.getName());
        tv = (TextView) findViewById(R.id.textView6);
        switch (bluetoothAdapter.getScanMode()) {
            case BluetoothAdapter.SCAN_MODE_NONE:
                tv.setText("Undiscoverable");
                break;
            case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
                tv.setText("Connectable");
                break;
            case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                tv.setText("Connectable and discoverable");
                break;
        }
        tv = (TextView) findViewById(R.id.textView8);
        switch (bluetoothAdapter.getState()) {
            case BluetoothAdapter.STATE_OFF:
                tv.setText("Off");
                break;
            case BluetoothAdapter.STATE_TURNING_OFF:
                tv.setText("Turning off");
                break;
            case BluetoothAdapter.STATE_TURNING_ON:
                tv.setText("Turning on");
                break;
            case BluetoothAdapter.STATE_ON:
                tv.setText("On");
                break;
        }
        tv = (TextView) findViewById(R.id.textView9);
        tv.setText(String.valueOf(bluetoothAdapter.isDiscovering()));
        tv = (TextView) findViewById(R.id.textView11);
        tv.setText(String.valueOf(bluetoothAdapter.isEnabled()));
        // api 26, not supported yet
        tv = (TextView) findViewById(R.id.textView13);
        tv.setText(String.valueOf(false));
        tv = (TextView) findViewById(R.id.textView15);
        tv.setText(String.valueOf(false));
        tv = (TextView) findViewById(R.id.textView17);
        tv.setText(String.valueOf(false));

        tv = (TextView) findViewById(R.id.textView19);
        tv.setText(String.valueOf(bluetoothAdapter.isMultipleAdvertisementSupported()));
        tv = (TextView) findViewById(R.id.textView21);
        tv.setText(String.valueOf(bluetoothAdapter.isOffloadedFilteringSupported()));
        tv = (TextView) findViewById(R.id.textView23);
        tv.setText(String.valueOf(bluetoothAdapter.isOffloadedScanBatchingSupported()));



    }
}
