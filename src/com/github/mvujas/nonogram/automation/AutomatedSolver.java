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
import com.github.mvujas.nonogram.TileState;
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
	
	static WebElement getWebElementByBoxCoordinates(
			WebDriver driver, int x, int y) {
		return driver.findElement(By.id(String.format("nmf%d_%d", x, y)));
	}
	
	static void fillBoard(WebDriver driver, NonogramState game) {
		for(int y = 0; y < game.getHeight(); y++) {
			for(int x = 0; x < game.getWidth(); x++) {
				TileState tile = game.getTile(x, y);
				if(tile == TileState.FULL) {
					WebElement boxEl = 
							getWebElementByBoxCoordinates(driver, x, y);
					boxEl.click();
				}
			}
		}
	}
	
	static WebElement[][] getClickableBoxes(WebDriver driver, NonogramState state) {
		WebElement[][] boxes = new WebElement[state.getHeight()][state.getWidth()];
		for(int y = 0; y < state.getHeight(); y++) {
			for(int x = 0; x < state.getWidth(); x++) {
				if(state.getTile(x, y) == TileState.FULL) {
					WebElement boxEl = 
							getWebElementByBoxCoordinates(driver, x, y);
					boxes[y][x] = boxEl;
				}
			}
		}
		return boxes;
	}
	
	static void fillBoardWithWebElements(WebElement[][] elements, NonogramState state) {
		for(int y = 0; y < state.getHeight(); y++) {
			for(int x = 0; x < state.getWidth(); x++) {
				TileState tile = state.getTile(x, y);
				if(tile == TileState.FULL) {
					WebElement box = elements[y][x];
					box.click();
				}
			}
		}
	}
	
	public static void main(String[] args) throws IOException {
		WebDriver driver = new ChromeDriver();
		try {
			driver.get("https://www.nonograms.org/nonograms/i/31875");
			
			NonogramState gameState = createGame(driver);
			
			NonogramSolver.solve(gameState);
			VisualisationUtils.showState(gameState);
			
			//fillBoard(driver, gameState);
			
			// Minimizes time on the website compared to the former solution (fillBoard)
			fillBoardWithWebElements(getClickableBoxes(driver, gameState), gameState);
			
			System.out.println("Solving finished! Press any key to quit the browser...");
			System.in.read();
		} finally {
			//	driver.quit();
		}
	}
}
