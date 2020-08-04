package com.github.mvujas.nonogram;

import java.util.List;

import com.github.mvujas.nonogram.solver.NonogramSolver;

public class Main {
	public static void main(String[] args) {
		List<List<Integer>> verticalNums = List.of(
			List.of(2),
			List.of(1),
			List.of(3),
			List.of(4),
			List.of(3));
		List<List<Integer>> horizontalNums = List.of(
			List.of(1),
			List.of(2),
			List.of(3),
			List.of(1, 2),
			List.of(2, 2));
		NonogramState state = new NonogramState(
				5, 5, 
				verticalNums, horizontalNums);
		VisualisationUtils.showState(state);
		
		NonogramSolver.solve(state);
	}
}
