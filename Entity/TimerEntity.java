package com.li.ble220.Entity;

/**
 * 设备计时器实体类
 */
public class TimerEntity {
	
	private String id;
	private String eid;			//	设备id
	private String startTime;	//	设备定时器启动时间
	private String location;	//	设备所在场景
	private String state;		//	设备启动状态
	private String name;
	private String valuse;	//设备的控制数据
	private String isClosed;	//设备开启状态
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEid() {
		return eid;
	}
	public void setEid(String eid) {
		this.eid = eid;
	}
	
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValuse() {
		return valuse;
	}
	public void setValuse(String valuse) {
		this.valuse = valuse;
	}
	public String getIsClosed() {
		return isClosed;
	}
	public void setIsClosed(String isClosed) {
		this.isClosed = isClosed;
	}
}
