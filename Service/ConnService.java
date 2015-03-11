package com.li.ble220.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import com.li.ble220.Fragment.BalconyFragment;
import com.li.ble220.Fragment.BedOneFragment;
import com.li.ble220.Fragment.BedTwoFragment;
import com.li.ble220.Fragment.KitWCFragment;
import com.li.ble220.Fragment.LivingFragment;
import com.li.ble220.Util.Utility;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class ConnService extends Service {

	private String value="";
	DatagramSocket socket;
	int numBytesReceived;
	DatagramPacket packet;
	private String[] values;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
	    
		
		new Thread(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				Integer port = 8888; 
		        // 接收的字节大小，客户端发送的数据不能超过这个大小 
		        byte[] message = new byte[1024]; 
		        try { 
		            // 建立Socket连接 
		            DatagramSocket datagramSocket = new DatagramSocket(port); 
		            DatagramPacket datagramPacket = new DatagramPacket(message, 
		                    message.length); 
		            try { 
		                while (true) { 
		                    // 准备接收数据 
		                    datagramSocket.receive(datagramPacket); 
		                    
		                    
		                    numBytesReceived=datagramPacket.getLength();
		                    
							value=new String(datagramPacket.getData(), 0, numBytesReceived);
		                    
		                    if (value!=null) {
		    					values=value.split(",");
		    				    sendBrocasd(values[0], values[1]);
		    				    Log.e("---value---", values[0]+"---"+values[1]+"");
		                    }
		                } 
		            } catch (IOException e) { 
		                e.printStackTrace(); 
		            } 
		        } catch (SocketException e) { 
		            e.printStackTrace(); 
		        }
			}
			
		}.start();
	    
		
	}
	
	public void sendBrocasd(String location,String value){
		final String BROADCAST = "com.li.ble";
		Intent intent = new Intent(BROADCAST); // 对应setAction()
		intent.putExtra("location", location);
		intent.putExtra("value", value);
		sendBroadcast(intent);
	}
	
	
}
