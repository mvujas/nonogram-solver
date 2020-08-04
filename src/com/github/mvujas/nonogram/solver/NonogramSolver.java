package com.github.mvujas.nonogram.solver;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

import com.github.mvujas.nonogram.ListUtils;
import com.github.mvujas.nonogram.NonogramState;
import com.github.mvujas.nonogram.TileState;

public class NonogramSolver {
	private NonogramState nonogramState;
	private List<FullZoneCombinations> horizontalCombinations;
	private List<FullZoneCombinations> verticalCombinations;
	
	private NonogramSolver(NonogramState nonogramState) {
		super();
		if(nonogramState == null) {
			throw new NullPointerException(
					"Nonogram state cannot be null");
		}
		this.nonogramState = nonogramState;
	}
	
	private FullZoneCombinations generateCombinations(
			List<Integer> nums,
			int lineSize) {
		FullZoneCombinations combinations = 
				new FullZoneCombinations();
		List<Interval> currentCombination = 
				new LinkedList<>();
		generateCombinationsRecursively(
				combinations, 
				currentCombination, 
				nums, 
				ListUtils.sum(nums), 
				0, 
				lineSize, 
				0);
		return combinations;
	}
	
	private void generateCombinationsRecursively(
			FullZoneCombinations combinations,
			List<Interval> currentCombination,
			List<Integer> nums,
			int remainingNumsSum,
			int lineStart,
			int lineSize,
			int currentNumIndex) {
		int remainingNums = nums.size() - currentNumIndex;
		if(remainingNums == 0) {
			FullZoneList combination = 
					new FullZoneList(currentCombination);
			combinations.addCombination(combination);
		} 
		else {
			int effectiveLineEnd = 
					lineSize - (remainingNums - 1) - remainingNumsSum;
			for(int i = lineStart; i <= effectiveLineEnd; i++) {
				int currentNum = nums.get(currentNumIndex);
				int intervalEnd = i + currentNum - 1;
				Interval interval = 
						new Interval(i, intervalEnd);
				currentCombination.add(interval);
				generateCombinationsRecursively(
						combinations, 
						currentCombination, 
						nums, 
						remainingNumsSum - currentNum, 
						intervalEnd + 2, 
						lineSize, 
						currentNumIndex + 1);
				// Every interval corresponds to its num, therefore 
				// currentNumIndexcan be as the index of the latest added interval
				currentCombination.remove(currentNumIndex); 
			}
		}
	}
	
	private List<FullZoneCombinations> generateListOfCombinations(
			List<List<Integer>> listOfNums, int lineSize) {
		return listOfNums.stream()
				.map(nums -> generateCombinations(nums, lineSize))
				.collect(Collectors.toList());
	}
	
	private void set(int x, int y, TileState value) {
		FullZoneCombinations verticalCombination = 
				verticalCombinations.get(x);
		FullZoneCombinations horizontalCombination =
				horizontalCombinations.get(y);
		
		nonogramState.setTile(x, y, value);
		verticalCombination.filterCombinationsWithoutTileStateAtPosition(
				value, y);
		horizontalCombination.filterCombinationsWithoutTileStateAtPosition(
				value, x);
	}
	
	private boolean isSolved() {
		boolean solved = true;
		for(int y = 0; y < nonogramState.getHeight() && solved; y++) {
			for(int x = 0; x < nonogramState.getWidth() && solved; x++) {
				solved &= (nonogramState.getTile(x, y) != null);
			}
		}
		return solved;
	}
	
	public void findMissing() {
		verticalCombinations = generateListOfCombinations(
				nonogramState.getVerticalNums(), 
				nonogramState.getWidth());
		horizontalCombinations = generateListOfCombinations(
				nonogramState.getHorizontalNums(), 
				nonogramState.getHeight());
		
		// TODO: refactor, try to speed up
		while(!isSolved()) {
			for(int x = 0; x < nonogramState.getWidth(); x++) {
				FullZoneCombinations combinations = verticalCombinations.get(x);
				for(int y = 0; y < nonogramState.getHeight(); y++) {
					if(nonogramState.getTile(x, y) == null) {
						TileState tile = combinations.unambiguousStateAtPosition(y);
						if(tile != null) {
							set(x, y, tile);
						}
					}
				}
			}
			
			for(int y = 0; y < nonogramState.getHeight(); y++) {
				FullZoneCombinations combinations = horizontalCombinations.get(y);
				for(int x = 0; x < nonogramState.getWidth(); x++) {
					if(nonogramState.getTile(x, y) == null) {
						TileState tile = combinations.unambiguousStateAtPosition(x);
						if(tile != null) {
							set(x, y, tile);
						}
					}
				}
			}
		}
	}
	
	
	public static void solve(NonogramState nonogramState) {
		new NonogramSolver(nonogramState).findMissing();
	}
}
