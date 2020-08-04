package com.github.mvujas.nonogram;

import java.util.List;

public class NonogramState {
	private TileState[][] board;
	private int height, width;
	private List<List<Integer>> verticalNums;
	private List<List<Integer>> horizontalNums;
	
	public NonogramState(
			int height, int width,
			List<List<Integer>> verticalNums,
			List<List<Integer>> horizontalNums) {
		if(verticalNums.size() != width) {
			throw new IllegalArgumentException(
					"There must be number for each column");
		}
		if(horizontalNums.size() != height) {
			throw new IllegalArgumentException(
					"There must be numbers for each row");
		}
		this.horizontalNums = horizontalNums;
		this.verticalNums = verticalNums;
		
		this.height = height;
		this.width = width;
		board = new TileState[height][width];
	}
	
	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}
	
	public TileState[][] getBoard() {
		return board;
	}

	public List<List<Integer>> getVerticalNums() {
		return verticalNums;
	}

	public List<List<Integer>> getHorizontalNums() {
		return horizontalNums;
	}

	private void checkCoordinateValidity(int x, int y) 
			throws IllegalArgumentException  {
		if(x < 0 || x >= width) {
			throw new IllegalArgumentException(
					"X coordinate out of bounds");
		}
		if(y < 0 || y >= height) {
			throw new IllegalArgumentException(
					"Y coordinate out of bounds");
		}
	}
	
	public TileState getTile(int x, int y) 
			throws IllegalArgumentException {
		checkCoordinateValidity(x, y);
		return board[y][x];
	}
	
	public void setTile(int x, int y, TileState value) {
		checkCoordinateValidity(x, y);
		board[y][x] = value;
	}
	
}
