package com.github.mvujas.nonogram.solver;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import com.github.mvujas.nonogram.TileState;

public class FullZoneCombinations {
	private List<FullZoneList> combinations;

	public FullZoneCombinations(
			List<FullZoneList> fullZoneLists) {
		super();
		this.combinations = (fullZoneLists == null) ?
				new LinkedList<>() :
				new LinkedList<>(fullZoneLists);
	}
	
	public FullZoneCombinations() {
		this(null);
	}
	
	public void addCombination(FullZoneList combination) {
		combinations.add(combination);
	}
	
	public int count() {
		return combinations.size();
	}

	/**
	 * @return If all combinations have same tile state at position n
	 * 		returns the state, otherwise returns null
	 */
	public TileState unambiguousStateAtPosition(int n) {
		boolean isUnambiguous = true;
		TileState tileState = null;
		for(int i = 0;
				i < combinations.size() && isUnambiguous;
				i++) {
			FullZoneList combination = combinations.get(i);
			TileState combinationTileState = 
					combination.tileAtPos(n);
			if(tileState == null) {
				tileState = combinationTileState;
			} 
			else if(tileState != combinationTileState) {
				isUnambiguous = false;
			}
		}
		return isUnambiguous ? tileState : null;
	}
	
	public void filterCombinationsWithoutTileStateAtPosition(
			TileState state, int n) {
		combinations = combinations.stream()
				.filter(a -> a.tileAtPos(n) == state)
				.collect(Collectors.toList());
	}
	
	@Override
	public String toString() {
		return combinations.toString();
	}
	
}
