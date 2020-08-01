package com.github.mvujas.nonogramsolver;

import java.util.HashMap;
import java.util.Map;

public class VisualisationUtils {
	private static Map<TileState, Character> tileToCharMapping;
	
	static {
		tileToCharMapping = new HashMap<>();
		tileToCharMapping.put(null, ' ');
		tileToCharMapping.put(TileState.FULL, 'F');
		tileToCharMapping.put(TileState.EMPTY, 'X');
	}
	
	public static char getTileChar(TileState tile) {
		return tileToCharMapping.get(tile);
	}
	
	private static String repeatChar(char c, int n) {
		StringBuilder builder = new StringBuilder(n);
		for(int i = 0; i < n; i++) {
			builder.append(c);
		}
		return builder.toString();
	}
	
	public static void showState(NonogramState state) {
		System.out.println(repeatChar('-', state.getWidth() + 2));
		for(int y = 0; y < state.getHeight(); y++) {
			System.out.print("|");
			for(int x = 0; x < state.getWidth(); x++) {
				System.out.print(
						getTileChar(state.getTile(x, y)));
			}
			System.out.println("|");
		}
		System.out.println(repeatChar('-', state.getWidth() + 2));
	}
}
