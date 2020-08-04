package com.github.mvujas.nonogram.solver;

public class Interval {
	private int start, end;

	public Interval(int start, int end) {
		super();
		if(end < start) {
			throw new IllegalArgumentException(
					"Interval start must be lower or equal to its end");
		}
		this.start = start;
		this.end = end;
	}

	public int getStart() {
		return start;
	}

	public int getEnd() {
		return end;
	}
	
	public boolean isIn(int n) {
		return start <= n && n <= end;
	}
	
	/**
	 * @return whether integer n is before the interval
	 */
	public boolean isBefore(int n) {
		return n < start;
	}
	

	/**
	 * @return whether integer n is after the interval
	 */
	public boolean isAfter(int n) {
		return n > end;
	}
	
	
}
