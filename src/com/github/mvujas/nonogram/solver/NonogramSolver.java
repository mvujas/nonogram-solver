package com.github.mvujas.nonogram.solver;

import java.util.LinkedList;
import java.util.List;

import com.github.mvujas.nonogram.ListUtils;
import com.github.mvujas.nonogram.NonogramState;

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
	
	public void findMissing() {
		int width = nonogramState.getWidth();
		for(int x = 0; x < width; x++) {
			List<Integer> nums = nonogramState
					.getHorizontalNums().get(x);
			System.out.println("y = " + x  + ": " 
					+ generateCombinations(nums, width));
		}
		
		int height = nonogramState.getHeight();
		for(int y = 0; y < height; y++) {
			List<Integer> nums = nonogramState
					.getVerticalNums().get(y);
			System.out.println("x = " + y  + ": " 
					+ generateCombinations(nums, height));
		}
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
	
	
	
	
	
	public static void solve(NonogramState nonogramState) {
		new NonogramSolver(nonogramState).findMissing();
	}
}
