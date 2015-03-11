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
		        // ���յ��ֽڴ�С���ͻ��˷��͵����ݲ��ܳ��������С 
		        byte[] message = new byte[1024]; 
		        try { 
		            // ����Socket���� 
		            DatagramSocket datagramSocket = new DatagramSocket(port); 
		            DatagramPacket datagramPacket = new DatagramPacket(message, 
		                    message.length); 
		            try { 
		                while (true) { 
		                    // ׼���������� 
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
		Intent intent = new Intent(BROADCAST); // ��ӦsetAction()
		intent.putExtra("location", location);
		intent.putExtra("value", value);
		sendBroadcast(intent);
	}
	
	
}
