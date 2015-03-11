package com.li.ble220.Entity;

import java.io.Serializable;

public class SituaEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String id;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 是否选中
	 */
	private String ischeck;
	/**
	 * 选中的设备id
	 */
	private String eids;
	/**
	 * 选中设备id的命令
	 */
	private String values;
	/**
	 * 启动时间
	 */
	private String startTime;
	/**
	 * 设备所属位置
	 */
	private String locations;
	
	/**
	 * 状态
	 */
	private String state;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIscheck() {
		return ischeck;
	}
	public void setIscheck(String ischeck) {
		this.ischeck = ischeck;
	}
	public String getEids() {
		return eids;
	}
	public void setEids(String eids) {
		this.eids = eids;
	}
	public String getValues() {
		return values;
	}
	public void setValues(String values) {
		this.values = values;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getLocations() {
		return locations;
	}
	public void setLocations(String locations) {
		this.locations = locations;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	
}
