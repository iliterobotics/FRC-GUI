package org.ilite.gui.server;

import java.io.File;
import java.util.Map;

import org.ilite.vision.data.JSONManager;

public class ServerData {
	
	public static final String TBL_NAME = "shooter";
	private static final String FILENAME = "serverprops.json";
	private static final String DEF_ADDRESS = "localhost";
	private static final String DEF_TBLNAME = "no_name";
	private static final Integer DEF_PORT = 443;
	
	public static String getURL(){
		return "http://" + getAddress() + ":" + getPort();
	}
	
	public static int getPort(){
		Object portObj = getValue("port");
		if(portObj != null && portObj instanceof Number){
			return ((Number)portObj).intValue();
		}
		return DEF_PORT;
	}
	
	public static String getAddress(){
		Object addObj = getValue("address");
		return addObj == null?DEF_ADDRESS:addObj.toString();
	}
	
	public static String getTableName(){
		Object tblObj = getValue("table_name");
		return tblObj == null?DEF_ADDRESS:tblObj.toString();
	}
	
	public static Object getValue(String key){
		Map<String, Object> map;
		try {
			map = JSONManager.read(new File(FILENAME));
			if(map.containsKey(key)){
				return map.get(key);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
