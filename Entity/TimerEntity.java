package com.li.ble220.Entity;

/**
 * �豸��ʱ��ʵ����
 */
public class TimerEntity {
	
	private String id;
	private String eid;			//	�豸id
	private String startTime;	//	�豸��ʱ������ʱ��
	private String location;	//	�豸���ڳ���
	private String state;		//	�豸����״̬
	private String name;
	private String valuse;	//�豸�Ŀ�������
	private String isClosed;	//�豸����״̬
	
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
