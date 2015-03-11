package com.li.ble220.Util;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.R.integer;
import android.app.Activity;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

/**
 * 连接ble工具类，分析读取特性
 * @author li_zhuonan
 * 2013-12-17
 *
 */

public class ConnectUtility {
	private BleUtility bleUtility;
	private String address="";
	
	private BluetoothGattCharacteristic bluetoothGattCharacteristic;
	
	private static ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics =
            new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
	
	public ConnectUtility(Context context,String address,BleUtility bleUtility){
//		this.mBluetoothLeService=mBluetoothLeService;
		this.address=address;
		this.bleUtility=bleUtility;
		
	}
	
	
	
	public void connectBle(){
		if (!bleUtility.initialize()) {
            Log.e("Connect", "无法初始化蓝牙");  	
        }
        // Automatically connects to the device upon successful start-up initialization.
        bleUtility.connect(address);	
	}
	
	
	/**
	 * 接收所有的Gatt服务，并且分析接收到所有的特性，提取写入的特性
	 * @param gattServices
	 * @return
	 */
    public BluetoothGattCharacteristic displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null){
        	Log.e("gattServices的值","null");
        	return null;
        }
        String uuid = null;
        ArrayList<ArrayList<HashMap<String, String>>> gattCharacteristicData
                = new ArrayList<ArrayList<HashMap<String, String>>>();
        mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();

        for (BluetoothGattService gattService : gattServices) {
            
        	

            ArrayList<HashMap<String, String>> gattCharacteristicGroupData =
                    new ArrayList<HashMap<String, String>>();
            List<BluetoothGattCharacteristic> gattCharacteristics =
                    gattService.getCharacteristics();
            ArrayList<BluetoothGattCharacteristic> charas =
                    new ArrayList<BluetoothGattCharacteristic>();

            // Loops through available Characteristics.
            for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                charas.add(gattCharacteristic);
                uuid = gattCharacteristic.getUuid().toString();
                
                
                if(uuid.equals("0000fff3-0000-1000-8000-00805f9b34fb")){
                	bluetoothGattCharacteristic=gattCharacteristic;
                	Log.e("bluetoothGattCharacteristic", bluetoothGattCharacteristic+"");
                }
                
                
            }
            mGattCharacteristics.add(charas);
            
            gattCharacteristicData.add(gattCharacteristicGroupData);
            
        }
        return bluetoothGattCharacteristic;
    }
    
    public void writeCharactic(Context context,int value){
    	
    	if (bluetoothGattCharacteristic!=null) {
    		bluetoothGattCharacteristic.setValue(value, BluetoothGattCharacteristic.FORMAT_UINT8, 0);
    		bleUtility.wirteCharacteristic(bluetoothGattCharacteristic);
		}else {
			Toast.makeText(context, "连接不到该设备，请重试", Toast.LENGTH_SHORT).show();
		}

    }
    
    
    
}
