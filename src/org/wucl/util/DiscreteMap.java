package org.wucl.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DiscreteMap extends HashMap<String, Integer> {

	private static final long serialVersionUID = 1L;
	
	private List<String> keyList = new ArrayList<String>();

	public int put(String key) {
		if (key == null) {
			return 0;
		}
		key = key.trim().toLowerCase();
		if (this.containsKey(key)) {
			int value = this.get(key);
			value++;
			this.put(key, value);
			return value;
		} else {
			this.put(key, 1);
			keyList.add(key);
		}
		return 0;
	}


	public int getCount(String key) {
		if (key == null) {
			return 0;
		}
		key = key.trim().toLowerCase();
		if (this.containsKey(key)) {
			return this.get(key);
		}
		return 0;
	}
	
	public List<String> getKeyList(){
		return keyList;
	}

	public static void main(String[] args) {
		DiscreteMap map = new DiscreteMap();
		map.put("1");
		map.put("2");
		map.put("1");
		map.put("1");
		map.put("1");
		map.put("4");
		System.out.println(map.getCount("1"));
		System.out.println(map.getCount("2"));
		System.out.println(map.getCount("3"));
		System.out.println(map.getKeyList());
	}

}
