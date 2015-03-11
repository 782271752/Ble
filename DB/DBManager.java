package com.li.ble220.DB;

import java.util.ArrayList;
import java.util.List;

import com.li.ble220.R.id;
import com.li.ble220.Entity.BleEntity;
import com.li.ble220.Entity.Equipment;
import com.li.ble220.Entity.SituaEntity;
import com.li.ble220.Entity.TimerEntity;

import android.R.bool;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBManager {
	
	private Context context = null;;
	private static SQLiteDatabase db = null;
	public static final String BLE_NAME="BLE";
	DataBaseHelper dbHelper;
	
	public DBManager(Context context){
		this.context=context;
		dbHelper = new DataBaseHelper(context, BLE_NAME);
		
	}
	
	/**
	 * 更新蓝牙模块的状态
	 * @param address
	 */
	public void updateState(String address,String value){
		db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("state", value);
		if (db!=null) {
			db.update("ble", values, "address=?",new String[]{address});
			db.close();
		}
	}
	
	public String[] getAddress(){
		String[] address;
		db = dbHelper.getWritableDatabase();
		Cursor cursor = db.query("ble",new String[]{"address"},null,null,null, null, null);
		address=new String[cursor.getCount()];
		int i=0;
		while(cursor.moveToNext()){
			address[i]=cursor.getString(cursor.getColumnIndex("address"));
			i++;
		}
		return address;
		
	}
	
	public void closeDB(){
		db = dbHelper.getWritableDatabase();
		if (db!=null) {
			db.close();
		}
	}
	
	/**
	 * 更新跟蓝牙模块的连接状态
	 * @param address
	 */
	public void updateContect(String address,String value){
		db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("isconnect", value);
		Log.e("更新连接状态",value);
		if (db!=null) {
			db.update("ble", values, "address=?",new String[]{address});
			db.close();
		}
	}
	
	/**
	 * 更新时间
	 * @param id
	 * @param timerEntity
	 * @return
	 */
	public boolean updateTime(String id,TimerEntity timerEntity){
		db = dbHelper.getWritableDatabase();
		boolean success=false;
		ContentValues values = new ContentValues();
		values.put("state", timerEntity.getState());
		values.put("isclosed",timerEntity.getIsClosed());
		values.put("starttime", timerEntity.getStartTime());
		values.put("tvalues", timerEntity.getValuse());
		if (db!=null) {
			db.update("timer", values, "id=?",new String[]{id});
			success=true;
			db.close();
		}
		return success;
	}
	
	/**
	 * 获取蓝牙模块的状态
	 * @param address
	 * @return
	 */
	public String getBleState(String address){
		String state="";
		db = dbHelper.getWritableDatabase();
		Cursor cursor = db.query("ble",new String[]{"state"}, "address =?", new String[]{address}, null, null, null);
		try {
			if(cursor != null && cursor.moveToFirst()){
				state=cursor.getString(cursor.getColumnIndex("state"));
			}
		} finally{
			cursor.close();
			db.close();
		}
		
		return state;
	}
	
	/**
	 * 获取模块的信息
	 * @return
	 */
	public List<BleEntity> getBle(){
		List<BleEntity> bleList=new ArrayList<BleEntity>();
		db = dbHelper.getWritableDatabase();
		Cursor cursor=db.query("ble", null, null, null, null, null, null);
		try{
			while (cursor.moveToNext()) {
				BleEntity bleEntity=new BleEntity();
				bleEntity.setAddress(cursor.getString(cursor.getColumnIndex("address")));
				bleEntity.setState(cursor.getInt(cursor.getColumnIndex("state"))+"");
				bleEntity.setBid(cursor.getInt(cursor.getColumnIndex("bid"))+"");
				bleEntity.setLocation(cursor.getString(cursor.getColumnIndex("name")));
				bleEntity.setIsconnect(cursor.getString(cursor.getColumnIndex("isconnect")));
				bleList.add(bleEntity);
				
			}
		}finally{
			cursor.close();
			db.close();
		}
		return bleList;
		
	}
	
	
	
	
	
	/**
	 * 获取某个房间的设备
	 * @param location
	 * @return
	 */
	public String[] getEquipmentNameByLocation(String location) {
		String[] equipmentName = null;
		db = dbHelper.getWritableDatabase();
		if (db != null) {
			Cursor cursor = db.query("equipment", new String[] { "name" }, "bid = ?",
					new String[] { location }, null, null, null);
			try{
				equipmentName = new String[cursor.getCount()];
				for (int i = 0; cursor.moveToNext(); i++) {
					equipmentName[i] = cursor.getString(cursor.getColumnIndex("name"));
				}
			}finally{
				cursor.close();
				db.close();
			}
			
		}
		return equipmentName;
	}
	
	
	
	/**
	 * 获取某个房间所有设备的id
	 * @return
	 */
	public String[] getEquipmentEid(String location) {
		String[] equipmentEid = null;
		db = dbHelper.getWritableDatabase();
		if (db != null) {
			Cursor cursor = db.query("equipment", new String[] { "eid" }, "bid=?",
					new String[]{location}, null, null,
					null);
			try{
				equipmentEid = new String[cursor.getCount()];
				for (int i = 0; cursor.moveToNext(); i++) {
					equipmentEid[i] = cursor.getString(cursor.getColumnIndex("eid"));
				}
			}finally{
				cursor.close();
				db.close();
			}
			
		}
		return equipmentEid;
	}
	
	/**
	 * 获取全部设备的id
	 * @return
	 */
	public String[] getEquipmentEid(){
		String[] equipmentEid = null;
		db = dbHelper.getWritableDatabase();
		if (db != null) {
			Cursor cursor = db.query("equipment", new String[] { "eid" }, null,
					null, null, null,
					"eid asc");
			try {
				equipmentEid = new String[cursor.getCount()];
				for (int i = 0; cursor.moveToNext(); i++) {
					equipmentEid[i] = cursor.getString(cursor.getColumnIndex("eid"));
				}
			} finally {
				// TODO: handle exception
				cursor.close();
				db.close();
			}
			
		}
		return equipmentEid;
	}
	
	
	/**
	 * 添加定时设备
	 * @param equipmentTimer
	 * @return
	 */
	public Boolean addEquipmentTimer(TimerEntity equipmentTimer) {
		
		db = dbHelper.getWritableDatabase();
		if (db != null) {
			Cursor cursor = db.query("timer", new String[] { "eid", "starttime", "state","location","tvalues"},
					"eid=? and starttime=? and state=? and location = ? and tvalues =? and isclosed =?",
					new String[] { equipmentTimer.getEid(), equipmentTimer.getStartTime(),
							equipmentTimer.getState(), equipmentTimer.getLocation() ,equipmentTimer.getValuse(),
							equipmentTimer.getIsClosed()}, null, null,
					null);
			try {
				if (cursor.moveToNext()) {
					return false;
				}
			} finally{
				cursor.close();
				
			}
			
			ContentValues values = new ContentValues();
			values.put("eid", equipmentTimer.getEid());
			values.put("starttime", equipmentTimer.getStartTime());
			values.put("name",equipmentTimer.getName());
			values.put("state", equipmentTimer.getState());
			values.put("location", equipmentTimer.getLocation());
			values.put("tvalues", equipmentTimer.getValuse());
			
			values.put("isclosed", equipmentTimer.getIsClosed());
			db.insert("timer", null, values);
			db.close();
			return true;
		}
		return null;
	}
	
	
	/**
	 * 获取定时设备
	 * @return
	 */
	public List<TimerEntity> getEquipmentTimer() {
		List<TimerEntity> equipmentTimers = null;
		db = dbHelper.getWritableDatabase();
		if (db != null) {
			Cursor cursor = db.query("timer", new String[] { "id", "eid", "name", "starttime",
					"state", "location","tvalues","isclosed"}, null, null, null, null, "id  desc");
			equipmentTimers = new ArrayList<TimerEntity>();
			try{
				while(cursor.moveToNext()){
					TimerEntity equipmentTimer = new TimerEntity();
					equipmentTimer.setId(cursor.getString(cursor.getColumnIndex("id")));
					equipmentTimer.setEid(cursor.getString(cursor.getColumnIndex("eid")));
					equipmentTimer.setName(cursor.getString(cursor.getColumnIndex("name")));
					equipmentTimer.setStartTime(cursor.getString(cursor.getColumnIndex("starttime")));
					equipmentTimer.setState(cursor.getString(cursor.getColumnIndex("state")));
					equipmentTimer.setLocation(cursor.getString(cursor.getColumnIndex("location")));
					equipmentTimer.setIsClosed(cursor.getString(cursor.getColumnIndex("isclosed")));
					equipmentTimer.setValuse(cursor.getString(cursor.getColumnIndex("tvalues")));
					equipmentTimers.add(equipmentTimer);
				}
			}finally{
				cursor.close();
				db.close();
			}
			
		}
		return equipmentTimers;
	}
	// 获取定时设备
	public TimerEntity[] getTimer() {
		TimerEntity[] equipmentTimers = null;
		db = dbHelper.getWritableDatabase();
		if (db != null) {
			Cursor cursor = db.query("timer", new String[] { "id", "eid", "name", "starttime",
					"state", "location","tvalues","isclosed" }, null, null, null, null, null);
			try {
				equipmentTimers = new TimerEntity[cursor.getCount()];
				for (int i = cursor.getCount() - 1; cursor.moveToNext(); i--) {
					TimerEntity equipmentTimer = new TimerEntity();
					equipmentTimer.setId(cursor.getString(cursor.getColumnIndex("id")));
					equipmentTimer.setEid(cursor.getString(cursor.getColumnIndex("eid")));
					equipmentTimer.setName(cursor.getString(cursor.getColumnIndex("name")));
					equipmentTimer.setStartTime(cursor.getString(cursor.getColumnIndex("starttime")));
					equipmentTimer.setState(cursor.getString(cursor.getColumnIndex("state")));
					equipmentTimer.setLocation(cursor.getString(cursor.getColumnIndex("location")));
					equipmentTimer.setValuse(cursor.getString(cursor.getColumnIndex("tvalues")));
					equipmentTimer.setIsClosed(cursor.getString(cursor.getColumnIndex("isclosed")));
					equipmentTimers[i] = equipmentTimer;
				}
			} finally {
				cursor.close();
				db.close();
			}
			
		}
		return equipmentTimers;
		
	}
	/**
	 * 获取设备位置
	 * @return
	 */
	public String[] getEquipmentLocation() {
		String[] equipmentLocation = null;
		db = dbHelper.getWritableDatabase();
		if (db != null) {
			Cursor cursor = db.query("equipment", new String[] { "bid" }, null, null, null,
					null, null);
			try {
				equipmentLocation = new String[cursor.getCount()];
				for (int i = 0; cursor.moveToNext(); i++) {
					equipmentLocation[i] = cursor.getString(cursor.getColumnIndex("bid"));
				}
			} finally{
				cursor.close();
				db.close();
			}
		}
		return equipmentLocation;
	}
	
	/**
	 *  获取设备名称
	 * @return
	 */
	public String[] getEquipmentName(String location) {
		String[] equipmentName = null;
		db = dbHelper.getWritableDatabase();
		if (db != null) {
			Cursor cursor = db.query("equipment", new String[] { "name" }, "bid=?",
					new String[]{location}, null, null,
					null);
			try {
				equipmentName = new String[cursor.getCount()];
				for (int i = 0; cursor.moveToNext(); i++) {
					equipmentName[i] = cursor.getString(cursor.getColumnIndex("name"));
				}
			} finally{
				cursor.close();
				db.close();
			}
			
		}
		return equipmentName;
	}
	
	/**
	 * 获取设备的开关发送命令
	 * @param eid
	 * @return
	 */
	public Equipment getEquipValues(String eid){
		Equipment equipment=new Equipment();
		db = dbHelper.getWritableDatabase();
		if (db!=null) {
			Cursor cursor=db.query("equipment", new String[]{"ton","toff"}, "eid=?", new String[]{eid}, null, null, null);
			try {
				cursor.moveToFirst();
				equipment.setTonValue(cursor.getString(cursor.getColumnIndex("ton")));
				equipment.setToffValue(cursor.getString(cursor.getColumnIndex("toff")));
			} finally {
				cursor.close();
				db.close();
			}
			
		}
		return equipment;
	}
	
	/**
	 * 删除定时设备记录
	 * @param equipmentTimer
	 */
	public void deleteEquipmentTimer(String id) {
		db = dbHelper.getWritableDatabase();
		if (db != null) {
			db.delete("timer", "id=? ",
					new String[] { id });
			db.close();
		}
	}
	
	/**
	 * 获取情景模式
	 * @return
	 */
	public List<SituaEntity> getSitua(){
		List<SituaEntity> situaEntities=new ArrayList<SituaEntity>();
		db = dbHelper.getWritableDatabase();
		if (db!=null) {
//			Cursor cursor=db.query("situa",new String[]{"id","ischeck","eids","svalues",
//					"locations","starttime"},);
			Cursor cursor=db.query("situa",null,null,null,null,null,"id  asc");
			try {
				while(cursor.moveToNext()){
					SituaEntity situaEntity=new SituaEntity();
					situaEntity.setId(cursor.getString(cursor.getColumnIndex("id")));
					situaEntity.setName(cursor.getString(cursor.getColumnIndex("name")));
					situaEntity.setEids(cursor.getString(cursor.getColumnIndex("eids")));
					situaEntity.setIscheck(cursor.getString(cursor.getColumnIndex("ischeck")));
					Log.e("ischeck", cursor.getString(cursor.getColumnIndex("ischeck")));
					situaEntity.setStartTime(cursor.getString(cursor.getColumnIndex("starttime")));
					situaEntity.setLocations(cursor.getString(cursor.getColumnIndex("locations")));
					situaEntity.setValues(cursor.getString(cursor.getColumnIndex("svalues")));
					situaEntity.setState(cursor.getString(cursor.getColumnIndex("state")));
					
					situaEntities.add(situaEntity);
					
				}
			} finally {
				cursor.close();
				db.close();
			}
			
		}
		return situaEntities;
	}
	
	
	/**
	 * 更新情景模式
	 * @param id
	 * @return
	 */
	public boolean updateSitua(String id,String ischeck){
		boolean success=false;
		db = dbHelper.getWritableDatabase();
		if (db!=null) {
			ContentValues values = new ContentValues();
			values.put("ischeck", ischeck);
			db.update("situa", values, "id=?",new String[]{id});
			success=true;
			db.close();
		}
		return success;
	}
	
	public boolean addSitua(SituaEntity entity){
		db = dbHelper.getWritableDatabase();
		if (db!=null) {
			Cursor cursor = db.query("situa", new String[] {"id"},
					"name =?",
					new String[] { entity.getName()}, null, null,
					null);
			try {
				if (cursor.moveToNext()) {
					return false;
				}
			}finally {
				cursor.close();
			}
			
			
			ContentValues valu=new ContentValues();
			valu.put("name", entity.getName());
			valu.put("ischeck", "100");
			valu.put("eids", entity.getEids());
			valu.put("svalues",entity.getValues());
			valu.put("locations", entity.getLocations());
			valu.put("starttime", entity.getStartTime());
			valu.put("state", entity.getState());
			db.insert("situa", null, valu);
			db.close();
			
		}
		return true;
	}
	
	/**
	 * 删除情景模式
	 * @param id
	 * @return
	 */
	public void delSitua(String id){
		db = dbHelper.getWritableDatabase();
		if (db!=null) {
			db.delete("situa", "id=? ", 
					new String[]{id});
			db.close();
		}
	}
	
	/**
	 * 获取情景模式设备的名称
	 * @param eid
	 * @return
	 */
	public String[] getEquipNames(String eid){
		String[] equipNames =null;
		db = dbHelper.getWritableDatabase();
		if (db!=null) {
			
			String[] eids=eid.split(",");
			for (int i = 0; i < eids.length; i++) {
				Log.e(i+"", eids[i]);
			}
			equipNames=new String[eids.length];
			for (int i = 0; i < eids.length; i++) {
				
				equipNames[i]=getEquipName(eids[i]);
				
			}
		}
		db.close();
		return equipNames;
	}
	
	
	
	/**
	 * 获取某个设备的名称
	 * @param eid
	 * @return
	 */
	public String getEquipName(String eid){
		String name="";
		db = dbHelper.getWritableDatabase();
		if (db!=null) {
			Cursor cursor=db.query("equipment",new String[]{"name"},"eid=?",new String[]{eid},null,null,null);
			try {
				if (cursor.moveToFirst()&&cursor!=null) {
					name=cursor.getString(cursor.getColumnIndex("name"));
				}
			} finally{
				// TODO: handle exception
				cursor.close();
				db.close();
			}
			
		}
		return name;
	}
	
	/**
	 * 获取某个设备的位置
	 * @param eid
	 * @return
	 */
	public String getLocation(String eid){
		String location="";
		db = dbHelper.getWritableDatabase();
		if (db!=null) {
			Cursor cursor=db.query("equipment",new String[]{"bid"},"eid=?",new String[]{eid},null,null,null);
			try {
				if (cursor.moveToFirst()&&cursor!=null) {
					location=cursor.getString(cursor.getColumnIndex("bid"));
					location=getLocationName(location);
				}
			} finally {
				cursor.close();
				db.close();
			}
			
		}
		return location;
	}
	
	
	public String getLocationId(String eid){
		String location="";
		db = dbHelper.getWritableDatabase();
		if (db!=null) {
			Cursor cursor=db.query("equipment",new String[]{"bid"},"eid=?",new String[]{eid},null,null,null);
			try {
				if (cursor.moveToFirst()&&cursor!=null) {
					location=cursor.getString(cursor.getColumnIndex("bid"));
				}
			} finally {
				cursor.close();
				db.close();
			}
			
				
		}
		return location;
	}
	
	/**
	 * 获取某个设备的名称
	 * @param location
	 * @return
	 */
	public String getLocationName(String location){
		String locationName="";
		db = dbHelper.getWritableDatabase();
		if (db!=null) {
			Cursor cursor=db.query("ble",new String[]{"name"},"bid=?",new String[]{location},null,null,null);
			try {
				if (cursor.moveToFirst()&&cursor!=null) {
					locationName=cursor.getString(cursor.getColumnIndex("name"));
				}
			} finally {
				cursor.close();
				db.close();
			}
			
		}
		return locationName;
	}
}
