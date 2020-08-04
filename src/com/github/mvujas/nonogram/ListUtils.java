package com.github.mvujas.nonogram;

import java.util.List;

public class ListUtils {
	public static int sum(List<Integer> list) {
		int sum = 0;
		for(int i: list) {
			sum += i;
		}
		return sum;
	}
}
