package com.li.ble220.Entity;

import java.io.Serializable;

public class SituaEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String id;
	/**
	 * ����
	 */
	private String name;
	/**
	 * �Ƿ�ѡ��
	 */
	private String ischeck;
	/**
	 * ѡ�е��豸id
	 */
	private String eids;
	/**
	 * ѡ���豸id������
	 */
	private String values;
	/**
	 * ����ʱ��
	 */
	private String startTime;
	/**
	 * �豸����λ��
	 */
	private String locations;
	
	/**
	 * ״̬
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
