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
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import unsw.samg.code.miband3.Helpers.CustomBluetoothProfile;
import unsw.samg.code.miband3.R;

public class Services extends Activity {
    BluetoothAdapter bluetoothAdapter;
    BluetoothGatt bluetoothGatt;
    BluetoothDevice bluetoothDevice;
    TextView service_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);
        Intent intent = getIntent();
        service_text = (TextView) findViewById(R.id.service_text);

        initialiseObjects(intent.getStringExtra("address"));

    }

    void initialiseObjects(String address) {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothDevice = bluetoothAdapter.getRemoteDevice(address);
        bluetoothGatt = bluetoothDevice.connectGatt(this, true, bluetoothGattCallback);
    }

    void initialiseList() {
        service_text.setMovementMethod(new ScrollingMovementMethod());
        String text = "";

        List<BluetoothGattService> services = bluetoothGatt.getServices();
        List<BluetoothGattCharacteristic> characteristics;

        for (int i = 0; i < services.size(); ++i) {
            BluetoothGattService service = services.get(i);
            characteristics = service.getCharacteristics();
            text += "Service: " + service.getUuid().toString() + System.getProperty("line.separator");
            for (int j = 0; j < characteristics.size(); ++j) {
                BluetoothGattCharacteristic characteristic = characteristics.get(j);
                text += "Characteristic: " + characteristic.getUuid().toString() + System.getProperty("line.separator");
            }
            text += System.getProperty("line.separator");
        }
        service_text.setText(text);
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
                service_text.setText("Discovering services");
                bluetoothGatt.discoverServices();
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                bluetoothGatt.disconnect();
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            authenticate();
            initialiseList();
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
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
                    } catch (Exception e) {
                        // do nothing
                    }
                }
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
}
