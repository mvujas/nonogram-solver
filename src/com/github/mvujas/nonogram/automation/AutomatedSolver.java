package com.github.mvujas.nonogram.automation;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import com.github.mvujas.nonogram.NonogramState;
import com.github.mvujas.nonogram.VisualisationUtils;
import com.github.mvujas.nonogram.solver.NonogramSolver;

enum Orientation {
	VERTICAL,
	HORIZONTAL
}

@FunctionalInterface
interface SelectorCreator {
	By createSelector(int blockIndex, int boxIndex);
}

public class AutomatedSolver {
	
	static List<List<Integer>> getNums(
			WebDriver driver, SelectorCreator selectorCreator) {
		List<List<Integer>> listOfNums = new LinkedList<>();
		
		int currentBlockIndex;
		boolean moreBlocksExist;
		for(currentBlockIndex = 0, moreBlocksExist = true;
				moreBlocksExist;
				currentBlockIndex++) {
			int currentBoxIndex;
			boolean moreBoxesExist;
			List<Integer> nums = new LinkedList<>();
			for(currentBoxIndex = 0, moreBoxesExist = true;
					moreBoxesExist;
					currentBoxIndex++) {
				try {
					WebElement box = 
							driver.findElement(
									selectorCreator.createSelector(
											currentBlockIndex, currentBoxIndex));
					int number = Integer.parseInt(box.getText());
					nums.add(number);
				} catch (NoSuchElementException e) {
					moreBoxesExist = false;
					if(currentBoxIndex == 0) {
						moreBlocksExist = false;
					}
				}
			}
			if(!nums.isEmpty()) {
				listOfNums.add(nums);
			}
		}
		return listOfNums;
	}
	
	static List<List<Integer>> getVerticalNums(WebDriver driver) {
		return getNums(driver, new SelectorCreator() {
			@Override
			public By createSelector(int blockIndex, int boxIndex) {
				String cssSelector = String.format("#nmv%d_%d div", 
						blockIndex, 
						boxIndex);
				return By.cssSelector(cssSelector);
			}
		});
	}
	
	static List<List<Integer>> getHorizontalNums(WebDriver driver) {
		return getNums(driver, new SelectorCreator() {
			@Override
			public By createSelector(int blockIndex, int boxIndex) {
				String cssSelector = String.format("#nmh%d_%d div", 
						boxIndex, 
						blockIndex);
				return By.cssSelector(cssSelector);
			}
		});
	}
	
	static NonogramState createGame(WebDriver driver) {
		List<List<Integer>> verticalNums = getVerticalNums(driver);
		List<List<Integer>> horizontalNums = getHorizontalNums(driver);
		System.out.println(verticalNums);
		System.out.println(horizontalNums);
		
		NonogramState state = new NonogramState(
				horizontalNums.size(), 
				verticalNums.size(), 
				verticalNums, 
				horizontalNums);
		
		return state;
	}
	
	public static void main(String[] args) throws IOException {
		WebDriver driver = new ChromeDriver();
		try {
			driver.get("https://www.nonograms.org/nonograms/i/4353");
			
			NonogramState gameState = createGame(driver);
			
			NonogramSolver.solve(gameState);
			VisualisationUtils.showState(gameState);
			
			
			
			System.out.println("Solving finished! Press any key to quit the browser...");
			System.in.read();
		} finally {
			driver.quit();
		}
	}
}
