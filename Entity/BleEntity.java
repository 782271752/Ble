package com.li.ble220.Entity;

public class BleEntity {
	
	//����ģ�������ռ�
	private String bid;
	//����ģ���macaddress
	private String address;
	//ģ���״̬��0����رգ�1��������
	private String state;
	//ģ�������ط�
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
