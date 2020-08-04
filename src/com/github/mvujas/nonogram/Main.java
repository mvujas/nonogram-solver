package com.github.mvujas.nonogram;

import java.util.List;

import com.github.mvujas.nonogram.solver.NonogramSolver;

public class Main {
	static NonogramState simpleGame() {
		// Passed
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
		return state;
	}
	
	static NonogramState mediumGame() {
		// Passed
		List<List<Integer>> verticalNums = List.of(
				List.of(2, 1),
				List.of(3, 1),
				List.of(3, 1),
				List.of(1, 1, 2),
				List.of(5, 2),
				List.of(2, 3, 2),
				List.of(7, 2),
				List.of(6, 3),
				List.of(9),
				List.of(3, 6));
			List<List<Integer>> horizontalNums = List.of(
				List.of(2, 1),
				List.of(6),
				List.of(7),
				List.of(1, 3),
				List.of(3, 6),
				List.of(10),
				List.of(2, 2, 2),
				List.of(3),
				List.of(10),
				List.of(7));
			NonogramState state = new NonogramState(
					10, 10, 
					verticalNums, horizontalNums);
		return state;
	}
	
	public static void main(String[] args) {
		NonogramState state = mediumGame();
		
		NonogramSolver.solve(state);
		VisualisationUtils.showState(state);
	}
}
