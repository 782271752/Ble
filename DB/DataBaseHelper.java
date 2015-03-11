package com.li.ble220.DB;

import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {

	private static int VERSION = 1;
	/**
	 * 控制设备的编号
	 */
	
	public DataBaseHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	public DataBaseHelper(Context context, String name, int version) {
		this(context, name, null, version);
		VERSION = version;
	}

	public DataBaseHelper(Context context, String name) {
		this(context, name, VERSION);
	}

	//	初始化数据库信息
	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL("create table ble(id INTEGER PRIMARY KEY	AUTOINCREMENT,bid int,"
				+ "address varchar(20),name varchar(10), state int,isconnect int)");
		db.execSQL("create table equipment(id INTEGER PRIMARY KEY AUTOINCREMENT, eid int, "
				+ "name varchar(10), bid int, state int,ton int,toff int)");
		db.execSQL("create table timer(id INTEGER PRIMARY KEY AUTOINCREMENT, eid int, "
				+ "name varchar(10), starttime varchar(10), location int, state int,tvalues int,isclosed int)");
		db.execSQL("create table situa(id INTEGER PRIMARY KEY AUTOINCREMENT,name varchar(20),"
				+ "ischeck int,eids varchar(40),svalues varchar(40),locations varchar(20),"
				+ "starttime varchar(10),state int)");
		String[] macAddress={"00:17:EA:8F:86:A1","00:17:EA:93:8B:A1","00:17:EA:8F:88:6D",
				"00:17:EA:93:BF:24","00:17:EA:93:89:DC"};
		int[] bId={1,2,3,4,5};
		String[] name={"客厅","卧室1","卧室2","阳台","厨卫"};
		for (int i = 0; i < macAddress.length; i++) {
			ContentValues values = new ContentValues();
			values.put("bid", bId[i]);
			values.put("address", macAddress[i]);
			values.put("name",name[i]);
			values.put("state", 0);
			values.put("isconnect",0);
			db.insert("ble", null, values);
		}
		
		int[] eid={11,12,21,22,31,32,41,51,52,53};
		String[] ename={"灯1","灯2","灯1","灯2","灯1","灯2","灯1","灯1","灯2","灯3"};
		int[] on={1,3,3,5,1,3,1,1,3,5};
		int[] off={2,4,4,6,2,4,0,2,4,6};
		int[] bids={1,1,2,2,3,3,4,5,5,5};
		for (int i = 0; i < ename.length; i++) {
			ContentValues values = new ContentValues();
			values.put("eid", eid[i]);
			values.put("name", ename[i]);
			values.put("bid",bids[i]);
			values.put("state", 0);
			values.put("ton", on[i]);
			values.put("toff", off[i]);
			db.insert("equipment", null, values);
		}
		
		
		String eids="21,22,31,32,51,";
		String sitname="起床模式";
		String ischeck="100";
		String values="3,5,1,3,1";
		String locations="2,3,5";
		String starttime="6:00";
		
		
		ContentValues valu=new ContentValues();
		valu.put("name", sitname);
		valu.put("ischeck", ischeck);
		valu.put("eids", eids);
		valu.put("svalues",values );
		valu.put("locations", locations);
		valu.put("starttime", starttime);
		valu.put("state", "100");
		db.insert("situa", null, valu);
		

	}

	@Override
	// 数据库版本升级
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		System.out.println("Upgrade The Old DataBase... " + oldVersion + " to " + newVersion);
	}

}
