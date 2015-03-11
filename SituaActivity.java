package com.li.ble220;

import java.lang.reflect.Field;
import java.util.Date;

import com.li.ble220.DB.DBManager;
import com.li.ble220.Entity.SituaEntity;
import com.li.ble220.Fragment.SituaFragment;
import com.li.ble220.Util.Utility;

import android.R.integer;
import android.R.string;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.drm.DrmStore.RightsStatus;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

public class SituaActivity extends Activity implements android.view.View.OnClickListener{
	
	private CheckBox livCb,livCbO,livCbT,livCbTh,boCb,boCbO,boCbT;
	private CheckBox btCb,btCbO,btCbT,balCb,balCbO,kwCb,kwCbO,kwCbT,kwCbTh;
	private EditText nameEdt;
	
	private DBManager dbManager;
	private String equipmentEid[],locations[];
	private String livO="",livT="",livTh="",boo="",boT="",bto="",btt="",balo="",kwo="",kwt="",kwth="";
	private String liv="",bedO="",bedT="",bal="",kw="";
	private String name="",starttime="";
	private Button ensureBtn,cancelBtn,setTimeBtn;
	private RadioGroup radioGroup;
	private RadioButton onRdb,offRdb;
	private String state="100";
//	private SituaFragment fragment;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);   
		setContentView(R.layout.situa_add);
		initView();
		dbManager=new DBManager(this);
		
		LayoutInflater inflater=LayoutInflater.from(this);
		View view=inflater.inflate(R.layout.situadialog, null);
		nameEdt=(EditText)view.findViewById(R.id.situaedt);
		
//		fragment=(SituaFragment)getFragmentManager().findFragmentByTag("situation");
		
		
		new AlertDialog.Builder(this,R.style.SituaDialog).setTitle("输入名称")
		.setView(view)
		.setPositiveButton("确定", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if (TextUtils.isEmpty(nameEdt.getText().toString())) {
					Toast.makeText(SituaActivity.this, "请输入名称", Toast.LENGTH_LONG).show();
					try 
	                {
	                    Field field = dialog.getClass().getSuperclass().getDeclaredField( "mShowing" );
	                    field.setAccessible( true );
	                    field.set( dialog, 
	                               false ); // false - 使之不能关闭(此为机关所在，其它语句相同)
	                } 
	                catch ( Exception e ) 
	                {
	                    Log.e("--",e.getMessage());
	                    e.printStackTrace();
	                }
				}
				else {
					
					try 
	                {
	                    Field field = dialog.getClass().getSuperclass().getDeclaredField( "mShowing" );
	                    field.setAccessible( true );
	                    field.set( dialog, 
	                               true ); // true - 使之可以关闭(此为机关所在，其它语句相同)
	                    name=nameEdt.getText().toString();
						dialog.dismiss();
	                }
	                catch (Exception e) 
	                {
	                    e.printStackTrace();
	                }
					
				}
				
			}
		})
		.setNegativeButton("取消", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				SituaActivity.this.finish();
				
			}
		}).show();
	}
	
	private void initView(){
		livCb=(CheckBox)findViewById(R.id.living_cb);
		livCb.setOnCheckedChangeListener(listener);
		livCbO=(CheckBox)findViewById(R.id.sitLivLigone);
		livCbO.setOnCheckedChangeListener(listener);
		livCbT=(CheckBox)findViewById(R.id.sitLivLigtwo);
		livCbT.setOnCheckedChangeListener(listener);
		livCbTh=(CheckBox)findViewById(R.id.sitLivLigthree);
		livCbTh.setOnCheckedChangeListener(listener);
		boCb=(CheckBox)findViewById(R.id.bedone_cb);
		boCb.setOnCheckedChangeListener(listener);
		boCbO=(CheckBox)findViewById(R.id.sitbedoneOne);
		boCbO.setOnCheckedChangeListener(listener);
		boCbT=(CheckBox)findViewById(R.id.sitbedonetwo);
		boCbT.setOnCheckedChangeListener(listener);
		btCb=(CheckBox)findViewById(R.id.bedtwo_cb);
		btCb.setOnCheckedChangeListener(listener);
		btCbO=(CheckBox)findViewById(R.id.sitbedtwoOne);
		btCbO.setOnCheckedChangeListener(listener);
		btCbT=(CheckBox)findViewById(R.id.sitbedtwotwo);
		btCbT.setOnCheckedChangeListener(listener);
		balCb=(CheckBox)findViewById(R.id.bal_cb);
		balCb.setOnCheckedChangeListener(listener);
		balCbO=(CheckBox)findViewById(R.id.sitbalLigone);
		balCbO.setOnCheckedChangeListener(listener);
		kwCb=(CheckBox)findViewById(R.id.kw_cb);
		kwCb.setOnCheckedChangeListener(listener);
		kwCbO=(CheckBox)findViewById(R.id.sitKwligOne);
		kwCbO.setOnCheckedChangeListener(listener);
		kwCbT=(CheckBox)findViewById(R.id.sitKwligtwo);
		kwCbT.setOnCheckedChangeListener(listener);
		kwCbTh=(CheckBox)findViewById(R.id.sitKwligthree);
		kwCbTh.setOnCheckedChangeListener(listener);
		
		ensureBtn=(Button)findViewById(R.id.situ_ensure_btn);
		ensureBtn.setOnClickListener(this);
		cancelBtn=(Button)findViewById(R.id.situ_cancel_btn);
		cancelBtn.setOnClickListener(this);
		setTimeBtn=(Button)findViewById(R.id.situ_time_btn);
		setTimeBtn.setOnClickListener(this);
		radioGroup=(RadioGroup)findViewById(R.id.radioGroup);
		onRdb=(RadioButton)findViewById(R.id.radio_on);
		onRdb.setOnCheckedChangeListener(listener);
		offRdb=(RadioButton)findViewById(R.id.radio_off);
		offRdb.setOnCheckedChangeListener(listener);
		onRdb.setChecked(true);
	}
	
	 private OnCheckedChangeListener listener = new OnCheckedChangeListener(){

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// TODO Auto-generated method stub
			switch (buttonView.getId()) {
			
			case R.id.bedone_cb:
				boCbO.setChecked(isChecked);
				boCbT.setChecked(isChecked);
				break;
				
			case R.id.living_cb:
				livCbO.setChecked(isChecked);
				livCbT.setChecked(isChecked);
				livCbTh.setChecked(isChecked);
				break;
			case R.id.bedtwo_cb:
				btCbO.setChecked(isChecked);
				btCbT.setChecked(isChecked);
				break;
			case R.id.bal_cb:
				balCbO.setChecked(isChecked);
				break;
			case R.id.kw_cb:
				kwCbO.setChecked(isChecked);
				kwCbT.setChecked(isChecked);
				kwCbTh.setChecked(isChecked);
				break;
			case R.id.sitLivLigone:
				if (isChecked) {
					livO=getEid(0)+",";
					liv=getLocation(0)+",";
				}else {
					livO="";
					liv="";
				}
				break;
			case R.id.sitLivLigtwo:
				if (isChecked) {
					livT=getEid(1)+",";
					liv=getLocation(0)+",";
				}else {
					livT="";
					liv="";
				}
				break;
			case R.id.sitLivLigthree:
				if (isChecked) {
					livTh=getEid(2)+",";
					liv=getLocation(0)+",";
				}else {
					livTh="";
					liv="";
				}
				break;
			case R.id.sitbedoneOne:
				
				if (isChecked) {
					boo=getEid(3)+",";
					bedO=getLocation(1)+",";
				}else {
					boo="";
					bedO="";
				}
				break;
			case R.id.sitbedonetwo:
				if (isChecked) {
					boT=getEid(4)+",";
					bedO=getLocation(1)+",";
				}else {
					boT="";
					bedO="";
				}
				break;
			case R.id.sitbedtwoOne:
				if (isChecked) {
					bto=getEid(5)+",";
					bedT=getLocation(2)+",";
				}else {
					bto="";
					bedT="";
				}
				break;
			case R.id.sitbedtwotwo:
				if (isChecked) {
					btt=getEid(6)+",";
					bedT=getLocation(2)+",";
				}else {
					btt="";
					bedT="";
				}
				break;
			case R.id.sitbalLigone:
				if (isChecked) {
					balo=getEid(7)+",";
					bal=getLocation(3)+",";
				}else{
					balo="";
					bal="";
				}	
				break;
			case R.id.sitKwligOne:
				if (isChecked) {
					kwo=getEid(8)+",";
					kw=getLocation(4);
				}else {
					kwo="";
					kw="";
				}
				break;
			case R.id.sitKwligtwo:
				if (isChecked) {
					kwt=getEid(9)+",";
					kw=getLocation(4);
				}else {
					kwt="";
					kw="";
				}
				break;
			case R.id.sitKwligthree:
				if (isChecked) {
					kwth=getEid(10)+",";
					kw=getLocation(4);
				}else {
					kwth="";
					kw="";
				}
				break;
			case R.id.radio_on:
				state="100";
				break;
			case R.id.radio_off:
				state="0";
				break;
			default:
				break;
			}
			
		}
		 
	 };
	 
	 
	 
	 public String getEid(int i){
		 
		 equipmentEid=dbManager.getEquipmentEid();
		 return equipmentEid[i];
		 
	 }
	 public String getLocation(int i){
		 locations=dbManager.getEquipmentLocation();
		 return locations[i];
	 }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.situ_cancel_btn:
			SituaActivity.this.finish();
			break;
		case R.id.situ_ensure_btn:
			Log.e("-------eid------", livO+livT+livTh+boo+boT+bto+btt+balo+kwo+kwt+kwth+"");
			if (TextUtils.isEmpty(livO+livT+livTh+boo+boT+bto+btt+balo+kwo+kwt+kwth)) {
				Utility.showToast(this, "请选中相关设备", Toast.LENGTH_LONG);
				return;
			}
			if (TextUtils.isEmpty(starttime)) {
				Utility.showToast(this, "请选择启动时间", Toast.LENGTH_LONG);
				return;
			}
			SituaEntity entity=new SituaEntity();
			entity.setName(name);
			entity.setEids(livO+livT+livTh+boo+boT+bto+btt+balo+kwo+kwt+kwth);
			entity.setLocations(liv+bedO+bedT+bal+kw);
			entity.setState(state);
			entity.setStartTime(starttime);
			if (dbManager.addSitua(entity)) {
				Utility.showToast(this, "添加成功", Toast.LENGTH_LONG);
				MainActivity.timer.startSitua();
//				fragment.refresh();
				this.finish();
			}else {
				Utility.showToast(this, "该情景模式名称已存在", Toast.LENGTH_LONG);
			}
			break;
		case R.id.situ_time_btn:
			new TimePickerDialog(SituaActivity.this, mTimeSetListener, new Date().getHours(), new Date().getMinutes(), false).show();
			break;
		default:
			break;
		}
	}
	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		boolean flag = false;

		@Override
		public void onTimeSet(final TimePicker view, int hourOfDay, int minute) {
			if (flag) {
				flag = false;
				return;
			}
			starttime=hourOfDay + ":" + minute +"";
		}
	};
	
}
