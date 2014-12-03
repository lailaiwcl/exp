package org.wucl;

public class Site {
	public String name;
	public double x;
	public double y;

	public Site(String name, double x, double y) {
		this.name = name;
		this.x = x;
		this.y = y;
	}

	public Site(Site c) {
		this.name = c.name;
		this.x = c.x;
		this.y = c.y;
	}

	@Override
	public String toString() {
		return name + ":(" + x + "," + y + ")";
	}
}
