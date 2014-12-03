package org.wucl.util;

import java.util.ArrayList;
import java.util.List;

public class ContinueMap extends DiscreteMap {

	private static final long serialVersionUID = 1L;

	private List<Integer> measure = new ArrayList<Integer>();
	
	public ContinueMap(){
		measure.add(0);
		measure.add(1);
		measure.add(2);
		measure.add(3);
		measure.add(4);
		measure.add(5);
		measure.add(6);
		measure.add(7);
		measure.add(8);
		measure.add(9);
		measure.add(10);
		measure.add(20);
		measure.add(50);
		measure.add(100);
		measure.add(500);
	}
	
	public ContinueMap(List<Integer> measure){
		this();
		if(measure != null && measure.size() >= 2){
			this.measure = measure;
		}
	}
	
	

}
