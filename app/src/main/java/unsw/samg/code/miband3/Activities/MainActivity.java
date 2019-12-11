package unsw.samg.code.miband3.Activities;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import unsw.samg.code.miband3.Helpers.CustomBluetoothProfile;
import unsw.samg.code.miband3.R;

public class MainActivity extends Activity {

    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
    BluetoothAdapter bluetoothAdapter;
    BluetoothGatt bluetoothGatt;
    BluetoothDevice bluetoothDevice;

    Button btnStartConnecting, btnGetHeartRate, btnGetActivityData, btnDeviceInfo, btnServices;
    TextView txtDeviceName, txtPhysicalAddress, txtState, txtByte, txtActivity;

    private boolean authorised = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialiseObjects();
        initialiseComponents();
        initialiseEvents();
        getBondedDevice();

    }
    void getBondedDevice() {

        String mDeviceName = getIntent().getStringExtra(EXTRAS_DEVICE_NAME);
        String mDeviceAddress = getIntent().getStringExtra(EXTRAS_DEVICE_ADDRESS);

        txtDeviceName.setText(mDeviceName);
        txtPhysicalAddress.setText(mDeviceAddress);
        /*
        Set<BluetoothDevice> boundedDevice = bluetoothAdapter.getBondedDevices();
        for (BluetoothDevice bd : boundedDevice) {

            txtPhysicalAddress.setText(bd.getAddress());
        }
        */
    }

    void initialiseObjects() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    void initialiseComponents() {
        btnStartConnecting = (Button) findViewById(R.id.btnStartConnecting);
        btnGetHeartRate = (Button) findViewById(R.id.btnGetHeartRate);
        btnGetActivityData = (Button) findViewById(R.id.activityDataButton);
        btnDeviceInfo = (Button) findViewById(R.id.deviceinfo);
        btnServices = (Button) findViewById(R.id.services);
        txtDeviceName = (TextView) findViewById(R.id.txtDeviceName);
        txtPhysicalAddress = (TextView) findViewById(R.id.txtPhysicalAddress);
        txtState = (TextView) findViewById(R.id.txtState);
        txtByte = (TextView) findViewById(R.id.txtByte);
        txtActivity = (TextView) findViewById(R.id.activityDataText);
    }

    void initialiseEvents() {
        btnStartConnecting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startConnecting();
            }
        });
        btnGetHeartRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHeartRate();
            }
        });
        btnGetActivityData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivityData();
            }
        });

        btnDeviceInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DeviceInfo.class);
                startActivity(intent);
            }
        });
        btnServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Services.class);
                intent.putExtra("address", txtPhysicalAddress.getText().toString());
                startActivity(intent);
            }
        });

    }

    void startConnecting() {

        String address = txtPhysicalAddress.getText().toString();
        bluetoothDevice = bluetoothAdapter.getRemoteDevice(address);

        Log.v("test", "Connecting to " + address);
        Log.v("test", "Device name " + bluetoothDevice.getName());

        bluetoothGatt = bluetoothDevice.connectGatt(this, true, bluetoothGattCallback);

    }

    // BLUETOOTH HELPER FUNCTIONS BELOW
    void getHeartRate() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txtByte.setText("...");
            }
        });

        BluetoothGattService service = bluetoothGatt.getService(CustomBluetoothProfile.HEART_RATE_SERVICE);
        BluetoothGattCharacteristic hrCharacteristic = service.getCharacteristic(CustomBluetoothProfile.HEART_RATE_MEASUREMENT_CHARACTERISTIC);
        BluetoothGattDescriptor hrDescriptor = hrCharacteristic.getDescriptor(CustomBluetoothProfile.HEART_RATE_MEASURMENT_DESCRIPTOR);

        bluetoothGatt.setCharacteristicNotification(hrCharacteristic, true);
        hrDescriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        bluetoothGatt.writeDescriptor(hrDescriptor);

        BluetoothGattCharacteristic hrControl = service.getCharacteristic(CustomBluetoothProfile.UUID_CHARACTERISTIC_HEART_RATE_CONTROL_POINT);
        hrControl.setValue(new byte[]{0x15, 0x2, 1});
        bluetoothGatt.writeCharacteristic(hrControl);


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txtByte.setText("... (query your heartrate from the device) ...");
            }
        });
    }

    void getActivityData() {
        BluetoothGattService service = bluetoothGatt.getService(CustomBluetoothProfile.UUID_SERVICE_MIBAND_SERVICE);
        BluetoothGattCharacteristic characteristic = service.getCharacteristic(CustomBluetoothProfile.UUID_UNKNOWN_CHARACTERISTIC4);
        characteristic.setValue(new byte[]{0x01, 0x01, (byte)0xE3, 0x07, 0x0b, 0x11, 0x17, 0x33, 0x0B, 0x07, 0x00, 0x28});
        bluetoothGatt.writeCharacteristic(characteristic);

        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(CustomBluetoothProfile.UUID_DESCRIPTOR_GATT_CLIENT_CHARACTERISTIC_CONFIG);
        bluetoothGatt.setCharacteristicNotification(service.getCharacteristic(CustomBluetoothProfile.UUID_UNKNOWN_CHARACTERISTIC4), true);
        bluetoothGatt.setCharacteristicNotification(service.getCharacteristic(CustomBluetoothProfile.UUID_CHARACTERISTIC_5_ACTIVITY_DATA), true);
        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        bluetoothGatt.writeDescriptor(descriptor);

        characteristic.setValue(new byte[]{0x02});
        bluetoothGatt.writeCharacteristic(characteristic);
    }

    private void authenticate() {
        BluetoothGattService service = bluetoothGatt.getService(CustomBluetoothProfile.CUSTOM_SERVICE_FEE1);
        BluetoothGattCharacteristic characteristic = service.getCharacteristic(CustomBluetoothProfile.CUSTOM_SERVICE_AUTH_CHARACTERISTIC);
        bluetoothGatt.setCharacteristicNotification(characteristic, true);

        for (BluetoothGattDescriptor descriptor : characteristic.getDescriptors()) {
            if (descriptor.getUuid().equals(CustomBluetoothProfile.CUSTOM_SERVICE_AUTH_DESCRIPTOR)) {
                descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            }
        }

        byte[] authKeyBytes = new byte[]{0x01, 0x08, 0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x40, 0x41, 0x42, 0x43, 0x44, 0x45};
        characteristic.setValue(authKeyBytes);
        bluetoothGatt.writeCharacteristic(characteristic);
    }
    final BluetoothGattCallback bluetoothGattCallback = new BluetoothGattCallback() {

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                bluetoothGatt.discoverServices();
                txtState.setText("Connected");
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                bluetoothGatt.disconnect();
                authorised = false;
                txtState.setText("Disconnected");
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            if (!authorised) {
                authenticate();
                authorised = true;
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    txtState.setText("Something was read!");
                }
            });
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic) {
            UUID characteristicUUID = characteristic.getUuid();
            if (characteristicUUID.equals(CustomBluetoothProfile.CUSTOM_SERVICE_AUTH_CHARACTERISTIC)) {
                // authorisation sequence
                byte[] value = characteristic.getValue();
                if (value[0] == 0x10 && value[1] == 0x01 && value[2] == 0x01) {
                    characteristic.setValue(new byte[]{0x02, 0x08});
                    bluetoothGatt.writeCharacteristic(characteristic);
                } else if (value[0] == 0x10 && value[1] == 0x02 && value[2] == 0x01) {
                    try {
                        byte[] tmpValue = Arrays.copyOfRange(value, 3, 19);
                        Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
                        SecretKeySpec key = new SecretKeySpec(new byte[]{0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x40, 0x41, 0x42, 0x43, 0x44, 0x45}, "AES");
                        cipher.init(Cipher.ENCRYPT_MODE, key);
                        byte[] bytes = cipher.doFinal(tmpValue);

                        byte[] rq = concatenate(new byte[]{0x03, 0x8}, bytes);
                        characteristic.setValue(rq);
                        bluetoothGatt.writeCharacteristic(characteristic);
                        txtState.setText("Connected and authorised");
                    } catch (Exception e) {
                        // do nothing
                    }
                }
            } else if (characteristicUUID.equals(CustomBluetoothProfile.HEART_RATE_MEASUREMENT_CHARACTERISTIC)) {
                byte[] value = characteristic.getValue();
                if (value.length == 2 && value[0] == 0) {
                    final int hrvalue = (value[1] & 0xff);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txtByte.setText(String.valueOf(hrvalue) + " BPM");
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txtByte.setText("Invalid heart rate...");
                        }
                    });
                }
            } else if (characteristicUUID.equals(CustomBluetoothProfile.UUID_CHARACTERISTIC_5_ACTIVITY_DATA) ||
                        characteristicUUID.equals(CustomBluetoothProfile.UUID_UNKNOWN_CHARACTERISTIC4) ){
                txtActivity.setText(characteristic.getValue().toString());
            }
        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorRead(gatt, descriptor, status);
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorWrite(gatt, descriptor, status);
        }

        @Override
        public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
            super.onReliableWriteCompleted(gatt, status);
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            super.onReadRemoteRssi(gatt, rssi, status);
        }

        @Override
        public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
            super.onMtuChanged(gatt, mtu, status);
        }

    };

    public <T> T concatenate(T a, T b) {
        if (!a.getClass().isArray() || !b.getClass().isArray()) {
            throw new IllegalArgumentException();
        }

        Class<?> resCompType;
        Class<?> aCompType = a.getClass().getComponentType();
        Class<?> bCompType = b.getClass().getComponentType();

        if (aCompType.isAssignableFrom(bCompType)) {
            resCompType = aCompType;
        } else if (bCompType.isAssignableFrom(aCompType)) {
            resCompType = bCompType;
        } else {
            throw new IllegalArgumentException();
        }

        int aLen = Array.getLength(a);
        int bLen = Array.getLength(b);

        @SuppressWarnings("unchecked")
        T result = (T) Array.newInstance(resCompType, aLen + bLen);
        System.arraycopy(a, 0, result, 0, aLen);
        System.arraycopy(b, 0, result, aLen, bLen);

        return result;
    }

    /* encryption functions */
    /*
    private byte[] getMD5(byte[] message) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        return md5.digest(message);
    }

    private byte[] handleAESAuth(byte[] value, byte[] secretKey) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException {
        byte[] mValue = Arrays.copyOfRange(value, 3, 19);
        Cipher ecipher = Cipher.getInstance("AES/ECB/NoPadding");
        SecretKeySpec newKey = new SecretKeySpec(secretKey, "AES");
        ecipher.init(Cipher.ENCRYPT_MODE, newKey);
        byte[] enc = ecipher.doFinal(mValue);
        return enc;
    }
*/

}
