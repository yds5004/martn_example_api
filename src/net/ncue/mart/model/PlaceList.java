package net.ncue.mart.model;

import java.util.ArrayList;

public class PlaceList extends ArrayList<Place>{
	int numFound = 0;
	int start = 0;
	
	public void setNumFound(int numFound) {
		this.numFound = numFound;
	}
	public int getNumFound() {
		return this.numFound;
	}
	
	public void setStart(int start) {
		this.start = start;
	}
	public int getStart() {
		return this.start;
	}
}
