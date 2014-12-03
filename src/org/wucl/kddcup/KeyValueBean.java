package org.wucl.kddcup;

public class KeyValueBean {
	public String key;
	public String value;
	
	public KeyValueBean() {
		super();
	}
	public KeyValueBean(String key, String value) {
		super();
		this.key = key;
		this.value = value;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return "KeyValueBean [key=" + key + ", value=" + value + "]";
	}
	

}
