package unsw.samg.code.miband3.Helpers;

import java.util.UUID;
public class CustomBluetoothProfile {


    //Custom service 3 components
    public static UUID CUSTOM_SERVICE_FEE1 = UUID.fromString("0000fee1-0000-1000-8000-00805f9b34fb");
    public static UUID CUSTOM_SERVICE_AUTH_CHARACTERISTIC = UUID.fromString("00000009-0000-3512-2118-0009af100700");
    public static UUID CUSTOM_SERVICE_AUTH_DESCRIPTOR = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");

    //Device information profile
    static UUID DEVICE_INFORMATION_SERVICE = UUID.fromString("0000180a-0000-1000-8000-00805f9b34fb");
    static UUID SERIAL_NUMBER = UUID.fromString("00002a25-0000-1000-8000-00805f9b34fb");
    static UUID HARDWARE_REVISION_STRING = UUID.fromString("00002a27-0000-1000-8000-00805f9b34fb");
    static UUID SOFTWARE_REVISION_STRING = UUID.fromString("00002a28-0000-1000-8000-00805f9b34fb");

    //Generic access profile
    static UUID GENERIC_ACCESS_SERVICE = UUID.fromString("00001800-0000-1000-8000-00805f9b34fb");
    static UUID DEVICE_NAME_CHARACTERISTIC = UUID.fromString("00002a00-0000-1000-8000-00805f9b34fb");
    static UUID APPEARANCE_CHARACTERISTIC = UUID.fromString("00002a01-0000-1000-8000-00805f9b34fb");
    static UUID PERIPHERAL_PREFERRED_CONNECTION_CHARACTERISTIC = UUID.fromString("00002a04-0000-1000-8000-00805f9b34fb");

    //Generic attribute profile
    static UUID GENERIC_ATTRIBUTE_SERVICE = UUID.fromString("00001801-0000-1000-8000-00805f9b34fb");
    static UUID SERVICE_CHANGED_CHARACTERISTIC = UUID.fromString("00002a05-0000-1000-8000-00805f9b34fb");
    static UUID SERVICE_CHANGED_DESCRIPTOR = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");

    //Space left for custom service 00001530

    //Alert notification profile
    static UUID ALERT_NOTIFICATION_SERVICE = UUID.fromString("00001811-0000-1000-8000-00805f9b34fb");
    static UUID NEW_ALERT_CHARACTERISTIC = UUID.fromString("00002a46-0000-1000-8000-00805f9b34fb");
    static UUID ALERT_NOTIFICATION_CONTROL_POINT = UUID.fromString("00002a44-0000-1000-8000-00805f9b34fb");
    static UUID ALERT_NOTIFICATION_CONTROL_POINT_DESCRIPTOR = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");

    //Immediate alert profile
    static UUID IMMEDIATE_ALERT_SERVICE = UUID.fromString("00001802-0000-1000-8000-00805f9b34fb");
    static UUID ALERT_LEVEL_CHARACTERISTIC = UUID.fromString("00002a06-0000-1000-8000-00805f9b34fb");

    //Heart rate monitoring profile
    public static UUID HEART_RATE_SERVICE = UUID.fromString("0000180d-0000-1000-8000-00805f9b34fb");
    public static UUID HEART_RATE_MEASUREMENT_CHARACTERISTIC = UUID.fromString("00002a37-0000-1000-8000-00805f9b34fb");
    public static UUID HEART_RATE_MEASURMENT_DESCRIPTOR = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    static UUID HEART_RATE_CONTROL_POINT_CHARACTERISTIC = UUID.fromString("00002a39-0000-1000-8000-00805f9b34fb");


    /*----------------UUID strings for the switch statements----------*/
    static final String DEVICE_INFORMATION_SERVICE_STRING = "0000180a-0000-1000-8000-00805f9b34fb";
    static final String CUSTOM_SERVICE_AUTH_CHARACTERISTIC_STRING = "00000009-0000-3512-2118-0009af100700";
    static final String HEART_RATE_MEASUREMENT_CHARACTERISTIC_STRING = "00002a37-0000-1000-8000-00805f9b34fb";
    static final String GENERIC_ACCESS_SERVICE_STRING = "00001800-0000-1000-8000-00805f9b34fb";
    static final String GENERIC_ATTRIBUTE_SERVICE_STRING = "00001801-0000-1000-8000-00805f9b34fb";
    static final String ALERT_NOTIFICATION_SERVICE_STRING = "00001811-0000-1000-8000-00805f9b34fb";
    static final String IMMEDIATE_ALERT_SERVICE_STRING = "00001802-0000-1000-8000-00805f9b34fb";






    public static final String BASE_UUID = "0000%s-0000-1000-8000-00805f9b34fb";
    public static final String MAC_ADDRESS_FILTER_1_1A = "88:0F:10";
    public static final String MAC_ADDRESS_FILTER_1S = "C8:0F:10";
    public static final UUID UUID_SERVICE_MIBAND_SERVICE = UUID.fromString(String.format(BASE_UUID, "FEE0"));
    public static final UUID UUID_SERVICE_MIBAND2_SERVICE = UUID.fromString(String.format(BASE_UUID, "FEE1"));
    public static final UUID UUID_SERVICE_HEART_RATE = UUID.fromString(String.format(BASE_UUID, "180D"));
    public static final UUID UUID_SERVICE_FIRMWARE_SERVICE = UUID.fromString("00001530-0000-3512-2118-0009af100700");

    public static final UUID UUID_CHARACTERISTIC_FIRMWARE = UUID.fromString("00001531-0000-3512-2118-0009af100700");
    public static final UUID UUID_CHARACTERISTIC_FIRMWARE_DATA = UUID.fromString("00001532-0000-3512-2118-0009af100700");

    public static final UUID UUID_UNKNOWN_CHARACTERISTIC0 = UUID.fromString("00000000-0000-3512-2118-0009af100700");
    public static final UUID UUID_UNKNOWN_CHARACTERISTIC1 = UUID.fromString("00000001-0000-3512-2118-0009af100700");
    public static final UUID UUID_UNKNOWN_CHARACTERISTIC2 = UUID.fromString("00000002-0000-3512-2118-0009af100700");
    /**
     * Alarms, Display and other configuration.
     */
    public static final UUID UUID_CHARACTERISTIC_3_CONFIGURATION = UUID.fromString("00000003-0000-3512-2118-0009af100700");
    public static final UUID UUID_UNKNOWN_CHARACTERISTIC4 = UUID.fromString("00000004-0000-3512-2118-0009af100700");
    public static final UUID UUID_CHARACTERISTIC_5_ACTIVITY_DATA = UUID.fromString("00000005-0000-3512-2118-0009af100700");
    public static final UUID UUID_CHARACTERISTIC_6_BATTERY_INFO = UUID.fromString("00000006-0000-3512-2118-0009af100700");
    public static final UUID UUID_CHARACTERISTIC_7_REALTIME_STEPS = UUID.fromString("00000007-0000-3512-2118-0009af100700");
    public static final UUID UUID_CHARACTERISTIC_8_USER_SETTINGS = UUID.fromString("00000008-0000-3512-2118-0009af100700");
    // service uuid fee1
    public static final UUID UUID_CHARACTERISTIC_AUTH = UUID.fromString("00000009-0000-3512-2118-0009af100700");
    public static final UUID UUID_CHARACTERISTIC_DEVICEEVENT = UUID.fromString("00000010-0000-3512-2118-0009af100700");

    public static final UUID UUID_CHARACTERISTIC_CHUNKEDTRANSFER = UUID.fromString("00000020-0000-3512-2118-0009af100700");

    public static final UUID UUID_CHARACTERISTIC_HEART_RATE_CONTROL_POINT = UUID.fromString(String.format(BASE_UUID, "2A39"));
    public static final UUID UUID_CHARACTERISTIC_HEART_RATE_MEASUREMENT = UUID.fromString(String.format(BASE_UUID, "2A37"));

    public static final UUID UUID_DESCRIPTOR_GATT_CLIENT_CHARACTERISTIC_CONFIG = UUID.fromString(String.format(BASE_UUID, "2902"));




    public static class Basic {
        public static UUID service = UUID.fromString("0000fee0-0000-1000-8000-00805f9b34fb");
        public static UUID service1 = UUID.fromString("0000fee1-0000-1000-8000-00805f9b34fb");
        public static UUID CUSTOM_SERVICE_AUTH_CHARACTERISTIC = UUID.fromString("00000009-0000-3512-2118-0009af100700");
        public static UUID batteryCharacteristic = UUID.fromString("00000006-0000-3512-2118-0009af100700");
        public static UUID sensor = UUID.fromString("00000001–0000–3512–2118–0009af100700");
    }

    public static class AlertNotification {
        public static UUID service = UUID.fromString("00001802-0000-1000-8000-00805f9b34fb");
        public static UUID alertCharacteristic = UUID.fromString("00002a06-0000-1000-8000-00805f9b34fb");
    }

    public static class HeartRate {
        public static UUID service = UUID.fromString("0000180d-0000-1000-8000-00805f9b34fb");
        public static UUID measurementCharacteristic = UUID.fromString("00002a37-0000-1000-8000-00805f9b34fb");
        public static UUID descriptor = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
        public static UUID controlCharacteristic = UUID.fromString("00002a39-0000-1000-8000-00805f9b34fb");
    }

}
