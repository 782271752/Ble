package com.li.ble220.Util;

import java.util.List;
import java.util.UUID;



import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * ble设备操作类，连接Gatt，读写特性
 * @author li_zhuonan
 *  2013-12-22
 *
 */
public class BleUtility {
	
	private final static String TAG = "BleUtility";
	private BluetoothManager mBluetoothManager;
	private BluetoothAdapter mBluetoothAdapter;
	private String mBluetoothDeviceAddress;
	private BluetoothGatt mBluetoothGatt;
	private int mConnectionState = STATE_DISCONNECTED;
	private static final int STATE_DISCONNECTED = 0;
	private static final int STATE_CONNECTING = 1;
	private static final int STATE_CONNECTED = 2;

	public String ACTION_GATT_CONNECTED = "";
	public String ACTION_GATT_DISCONNECTED = "";
	public String ACTION_GATT_SERVICES_DISCOVERED = "";
	public String ACTION_DATA_AVAILABLE = "";
	public String EXTRA_DATA = "";

	
	
	private Context context;
	private String address;
	
	public BleUtility(Context context,String address){
		this.context=context;
		this.address=address;
		setAction(address);
		
	}
	
	public void setAction(String address){
		if (address.equals("00:17:EA:8F:86:A1")) {
			ACTION_GATT_CONNECTED = "LIVING_GATT_CONNECTED";
			ACTION_GATT_DISCONNECTED = "LIVING_GATT_DISCONNECTED";
			ACTION_GATT_SERVICES_DISCOVERED = "LIVING_GATT_SERVICES_DISCOVERED";
			ACTION_DATA_AVAILABLE = "LIVING_DATA_AVAILABLE";
			EXTRA_DATA = "LIVING_EXTRA_DATA";
		}else if(address.equals("00:17:EA:93:8B:A1")){
			ACTION_GATT_CONNECTED = "BED1_GATT_CONNECTED";
			ACTION_GATT_DISCONNECTED = "BED1_GATT_DISCONNECTED";
			ACTION_GATT_SERVICES_DISCOVERED = "BED1_GATT_SERVICES_DISCOVERED";
			ACTION_DATA_AVAILABLE = "BED1_DATA_AVAILABLE";
			EXTRA_DATA = "BED1_EXTRA_DATA";
		}else if (address.equals("00:17:EA:8F:88:6D")) {
			ACTION_GATT_CONNECTED = "BED2_GATT_CONNECTED";
			ACTION_GATT_DISCONNECTED = "BED2_GATT_DISCONNECTED";
			ACTION_GATT_SERVICES_DISCOVERED = "BED2_GATT_SERVICES_DISCOVERED";
			ACTION_DATA_AVAILABLE = "BED2_DATA_AVAILABLE";
			EXTRA_DATA = "BED2_EXTRA_DATA";
		}else if(address.equals("00:17:EA:93:BF:24")){
			ACTION_GATT_CONNECTED = "BAL_GATT_CONNECTED";
			ACTION_GATT_DISCONNECTED = "BAL_GATT_DISCONNECTED";
			ACTION_GATT_SERVICES_DISCOVERED = "BAL_GATT_SERVICES_DISCOVERED";
			ACTION_DATA_AVAILABLE = "BAL_DATA_AVAILABLE";
			EXTRA_DATA = "BAL_EXTRA_DATA";
		}else if (address.equals("00:17:EA:93:89:DC")) {
			ACTION_GATT_CONNECTED = "KW_GATT_CONNECTED";
			ACTION_GATT_DISCONNECTED = "KW_GATT_DISCONNECTED";
			ACTION_GATT_SERVICES_DISCOVERED = "KW_GATT_SERVICES_DISCOVERED";
			ACTION_DATA_AVAILABLE = "KW_DATA_AVAILABLE";
			EXTRA_DATA = "KW_EXTRA_DATA";
		}
	}
	
	private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
		@Override
		public void onConnectionStateChange(BluetoothGatt gatt, int status,
				int newState) {
			String intentAction;
			System.out.println("=======status:" + status);
			if (newState == BluetoothProfile.STATE_CONNECTED) {
				intentAction = ACTION_GATT_CONNECTED;
				mConnectionState = STATE_CONNECTED;
				broadcastUpdate(intentAction);
				Log.i(TAG, "已连接");
				Log.i(TAG, "尝试读取服务:"
						+ mBluetoothGatt.discoverServices());

			} else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
				intentAction = ACTION_GATT_DISCONNECTED;
				mConnectionState = STATE_DISCONNECTED;
				Log.i(TAG, "连接不到Gatt");
				broadcastUpdate(intentAction);
			}
		}

		@Override
		public void onServicesDiscovered(BluetoothGatt gatt, int status) {
			if (status == BluetoothGatt.GATT_SUCCESS) {
				broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
				Log.e(TAG,"发现服务");
			} else {
				Log.e(TAG, "连接不成功，接受服务的状态 " + status);
			}
		}

		@Override
		public void onCharacteristicRead(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic, int status) {
			Log.e(TAG, "已读取特性");
			if (status == BluetoothGatt.GATT_SUCCESS) {
				broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
			}
		}

		@Override
		public void onDescriptorWrite(BluetoothGatt gatt,
				BluetoothGattDescriptor descriptor, int status) {

			Log.e(TAG,"onDescriptorWriteonDescriptorWrite = " + status
					+ ", descriptor =" + descriptor.getUuid().toString());
		}

		@Override
		public void onCharacteristicChanged(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic) {
			broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
			if (characteristic.getValue() != null) {

				System.out.println(characteristic.getStringValue(0));
			}
		}

		

		public void onCharacteristicWrite(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic, int status) {

		};
	};

	private void broadcastUpdate(final String action) {
		final Intent intent = new Intent(action);
		context.sendBroadcast(intent);
	}

	private void broadcastUpdate(final String action,
			final BluetoothGattCharacteristic characteristic) {
		final Intent intent = new Intent(action);

		
			final byte[] data = characteristic.getValue();
			if (data != null && data.length > 0) {
				final StringBuilder stringBuilder = new StringBuilder(
						data.length);
				for (byte byteChar : data)
					stringBuilder.append(String.format("%02X ", byteChar));

				System.out.println("ppp" + new String(data) + "\n"
						+ stringBuilder.toString());
				intent.putExtra(EXTRA_DATA, new String(data) + "\n"
						+ stringBuilder.toString());
			}
		
		context.sendBroadcast(intent);
	}
	
	/**
	 * 本地蓝牙初始化
	 * @return
	 */
	public boolean initialize() {
		if (mBluetoothManager == null) {
			mBluetoothManager = (BluetoothManager)(context.getSystemService(Context.BLUETOOTH_SERVICE));
			if (mBluetoothManager == null) {
				Log.e(TAG, "Unable to initialize BluetoothManager.");
				return false;
			}
		}

		mBluetoothAdapter = mBluetoothManager.getAdapter();
		if (mBluetoothAdapter == null) {
			Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
			return false;
		}

		return true;
	}

	
	/**
	 * 连接蓝牙模块
	 * @param address
	 * @return
	 */
	public boolean connect(final String address) {
		if (mBluetoothAdapter == null || address == null) {
			Log.w(TAG,
					"蓝牙适配器未定义或地址不合法");
			return false;
		}

		if (mBluetoothDeviceAddress != null
				&& address.equals(mBluetoothDeviceAddress)
				&& mBluetoothGatt != null) {
			Log.d(TAG,
					"Trying to use an existing mBluetoothGatt for connection.");
			if (mBluetoothGatt.connect()) {
				mConnectionState = STATE_CONNECTING;
				return true;
			} else {
				return false;
			}
		}

		final BluetoothDevice device = mBluetoothAdapter
				.getRemoteDevice(address);
		if (device == null) {
			Log.w(TAG, "Device not found.  Unable to connect.");
			return false;
		}
		mBluetoothGatt = device.connectGatt(context, false, mGattCallback);
		mBluetoothDeviceAddress = address;
		mConnectionState = STATE_CONNECTING;
		return true;
	}

	
	/**
	 * 取消Gatt连接
	 */
	public void disconnect() {
		if (mBluetoothAdapter == null || mBluetoothGatt == null) {
			Log.w(TAG, "BluetoothAdapter not initialized");
			return;
		}
		mBluetoothGatt.disconnect();
	}

	/**
	 * 关闭Gatt连接
	 */
	public void close() {
		if (mBluetoothGatt == null) {
			return;
		}
		mBluetoothGatt.close();
		mBluetoothGatt = null;
	}

	
	/**
	 * 写入特性
	 * @param characteristic
	 */
	public void wirteCharacteristic(BluetoothGattCharacteristic characteristic) {

		if (mBluetoothAdapter == null || mBluetoothGatt == null) {
			Log.w(TAG, "BluetoothAdapter not initialized");
			return;
		}

		mBluetoothGatt.writeCharacteristic(characteristic);

	}

	/**
	 * 读出特性
	 * @param characteristic
	 */
	public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
		if (mBluetoothAdapter == null || mBluetoothGatt == null) {
			Log.w(TAG, "BluetoothAdapter not initialized");
			return;
		}
		mBluetoothGatt.readCharacteristic(characteristic);
	}

	
	public void setCharacteristicNotification(
			BluetoothGattCharacteristic characteristic, boolean enabled) {
		if (mBluetoothAdapter == null || mBluetoothGatt == null) {
			Log.w(TAG, "BluetoothAdapter not initialized");
			return;
		}
		mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);
		
	}

	
	/**
	 * 获取模块的GATT服务
	 * @return
	 */
	public List<BluetoothGattService> getSupportedGattServices() {
		if (mBluetoothGatt == null){
			return null;}
		return mBluetoothGatt.getServices();
	}

	
}
