package com.github.mvujas.nonogram.solver;

import java.util.ArrayList;
import java.util.List;

import com.github.mvujas.nonogram.TileState;

public class FullZoneList {
	private List<Interval> fullZones;

	public FullZoneList(List<Interval> fullZones) {
		super();
		if(fullZones == null) {
			throw new NullPointerException(
					"Full zone list cannot be null");
		}
		this.fullZones = new ArrayList<>(fullZones);
	}
	
	public boolean isInFullZone(int n) {
		Boolean isIn = null;
		for(int i = 0; 
				i < fullZones.size() && isIn == null; 
				i++) {
			Interval zone = fullZones.get(i);
			if(zone.isBefore(n)) {
				isIn = false;
			}
			else if(zone.isIn(n)) {
				isIn = true;
			}
		}
		if(isIn == null) {
			isIn = false;
		}
		return isIn;
	}

	public TileState tileAtPos(int n) {
		return isInFullZone(n) ? 
				TileState.FULL : TileState.EMPTY;
	}
	
	@Override
	public String toString() {
		return fullZones.toString();
	}
	
	
}
