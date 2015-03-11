package com.li.ble220.Entity;

public class BleEntity {
	
	//蓝牙模块所属空间
	private String bid;
	//蓝牙模块的macaddress
	private String address;
	//模块的状态，0代表关闭，1代表运行
	private String state;
	//模块所属地方
	private String location;
	
	private String isconnect;
	
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getBid() {
		return bid;
	}
	public void setBid(String bid) {
		this.bid = bid;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getIsconnect() {
		return isconnect;
	}
	public void setIsconnect(String isconnect) {
		this.isconnect = isconnect;
	}
	
	

}
