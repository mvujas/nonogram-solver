package com.github.mvujas.nonogramsolver;

public class Main {
	public static void main(String[] args) {
		NonogramState state = new NonogramState(10, 10, null, null);
		state.setTile(0, 0, TileState.FULL);
		state.setTile(1, 1, TileState.EMPTY);
		VisualisationUtils.showState(state);
	}
}
