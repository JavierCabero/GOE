package goe.maths;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2f;
import goe.errorhandling.GameOfLifeException;
import goe.graphics.TextureFactory;
import goe.graphics.gui.Button;
import goe.graphics.gui.Charset;
import goe.graphics.gui.DoubleB;
import goe.graphics.gui.Label;
import goe.graphics.gui.Listenable;
import goe.graphics.gui.Listener;
import goe.main.Main;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class Matrix implements Listenable {

	private static final int CELL_SIZE = 5;
	private static final int HEADER_SIZE = 64;
	private Cell[][] currentGeneration;
	private int xIndex;
	private int yIndex;
	private final int STATE_LIMIT = 4;
	private final int NEIGHBOURS_MIN = 0;
	private Listener listener;
	private long updateInterval = 100000000;
	private int stepsToDo = 0;
	private boolean spaceWasPressed = false;
	private Button stepB;
	private DoubleB playPauseB;
	private long before = System.nanoTime();
	private Charset charset;
	private Label stateL;
	private Button resetB;

	public Matrix(int screenWidth, int screenHeight) {
		if (screenHeight < 10 || screenWidth < 5) {
			throw new GameOfLifeException("Unsupported window size.");
		}

		// Buttons
		stepB = new Button(64, 16, 32, 32, TextureFactory.loadTexture("step"));
		List<Button> bl = new ArrayList<Button>();
		bl.add(new Button(16, 16, 32, 32, TextureFactory.loadTexture("play")));
		bl.add(new Button(16, 16, 32, 32, TextureFactory.loadTexture("pause")));
		playPauseB = new DoubleB(bl);
		// Calculate matrix size
		xIndex = screenWidth / CELL_SIZE;
		yIndex = (screenHeight - HEADER_SIZE) / CELL_SIZE;
		currentGeneration = new Cell[xIndex][yIndex];
		charset = new Charset(TextureFactory.loadTexture("charset"));
		stateL = new Label(112, 12, 80, 40, TextureFactory.loadTexture("state"));
		resetB = new Button(Main.SCREEN_WIDTH - 96, 12, 80, 40, TextureFactory.loadTexture("reset"));

		// Initialize matrix giving position to cells
		int i_ = 0;
		int j_ = HEADER_SIZE; // Space for the header
		for (int i = 0; i < xIndex; i++) {
			for (int j = 0; j < yIndex; j++) {
				// int x = 0; random.nextInt(STATE_LIMIT + 1);
				currentGeneration[i][j] = new Cell(i_, j_, 0); // x
				j_ += CELL_SIZE;
			}
			i_ += CELL_SIZE;
			j_ = HEADER_SIZE;
		}

		reset();

	}

	@Override
	public void setListener(Listener l) {
		listener = l;
	}

	private int applyRules(int i, int j) {
		int neighbours = nFactor(i, j);
		int cellState = currentGeneration[i][j].getState();

		int nextState = 0;
		if (neighbours < 2) {
			nextState = cellState - 1;
		} else if (neighbours == 2) {
			nextState = cellState;
		} else if (neighbours == 3) {
			nextState = cellState + 1;
		} else {
			nextState = cellState - (neighbours - 4);
		}

		if (nextState < 0) {
			return 0;
		} else if (STATE_LIMIT < nextState) {
			return STATE_LIMIT;
		} else {
			return nextState;
		}
	}

	private int nFactor(int i, int j) {
		int neighbours = 0;

		// On side left
		if (0 < i) {
			// Left top
			if (0 < j) {
				if (NEIGHBOURS_MIN < currentGeneration[i - 1][j - 1].getState()) {
					neighbours += currentGeneration[i - 1][j - 1].getState() - NEIGHBOURS_MIN;
				}
			}
			// Left middle
			if (NEIGHBOURS_MIN < currentGeneration[i - 1][j].getState()) {
				neighbours += currentGeneration[i - 1][j].getState() - NEIGHBOURS_MIN;
			}
			// Left bottom
			if (j < yIndex - 1) {
				if (NEIGHBOURS_MIN < currentGeneration[i - 1][j + 1].getState()) {
					neighbours += currentGeneration[i - 1][j + 1].getState() - NEIGHBOURS_MIN;
				}
			}
		}
		// Top middle
		if (0 < j) {
			if (NEIGHBOURS_MIN < currentGeneration[i][j - 1].getState()) {
				neighbours += currentGeneration[i][j - 1].getState() - NEIGHBOURS_MIN;
			}
		}
		// Top bottom
		if (j < yIndex - 1) {
			if (NEIGHBOURS_MIN < currentGeneration[i][j + 1].getState()) {
				neighbours += currentGeneration[i][j + 1].getState() - NEIGHBOURS_MIN;
			}
		}
		if (i < xIndex - 1) {
			// Right top
			if (0 < j) {
				if (NEIGHBOURS_MIN < currentGeneration[i + 1][j - 1].getState()) {
					neighbours += currentGeneration[i + 1][j - 1].getState() - NEIGHBOURS_MIN;
				}
			}
			// Right middle
			if (NEIGHBOURS_MIN < currentGeneration[i + 1][j].getState()) {
				neighbours += currentGeneration[i + 1][j].getState() - NEIGHBOURS_MIN;
			}
			// Right bottom
			if (j < yIndex - 1) {
				if (NEIGHBOURS_MIN < currentGeneration[i + 1][j + 1].getState()) {
					neighbours += currentGeneration[i + 1][j + 1].getState() - NEIGHBOURS_MIN;
				}
			}
		}
		return neighbours;
	}

	@Override
	public void input() {
		// Buttons input
		playPauseB.input();
		stepB.input();
		resetB.input();

		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE) && !spaceWasPressed) {
			playPauseB.activate();
			spaceWasPressed = true;
		} else if (!Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			spaceWasPressed = false;
		}

		if (playPauseB.isActive()) {
			if (playPauseB.getLastIndex() == 0) {
				stepsToDo = -1;
			} else {
				stepsToDo = 0;
			}
		}
		if (stepB.isActive()) {
			stepsToDo = 1;
		}

		if (stepsToDo == 0) {
			if (Mouse.isButtonDown(0)) {
				int iX = Mouse.getX() / CELL_SIZE;
				int iY = (Main.SCREEN_HEIGHT - Mouse.getY() - HEADER_SIZE) / CELL_SIZE;
				if (0 <= iX && iX < xIndex && 0 <= iY && iY < yIndex) {
					currentGeneration[iX][iY].state = 3;
				}
			} else if (Mouse.isButtonDown(1)) {
				int iX = Mouse.getX() / CELL_SIZE;
				int iY = (Main.SCREEN_HEIGHT - Mouse.getY() - HEADER_SIZE) / CELL_SIZE;
				if (0 <= iX && iX < xIndex && 0 <= iY && iY < yIndex) {
					currentGeneration[iX][iY].state = 0;
				}
			}
		}
		if (resetB.isActive() || Keyboard.isKeyDown(Keyboard.KEY_RETURN)) {
			reset();
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			listener.sendMessage("MainMenu");
		}

	}

	@Override
	public void logic() {

		long now = System.nanoTime();
		if (stepsToDo != 0 && now - before >= updateInterval) {
			// Update matrix
			Cell[][] nextGeneration = new Cell[xIndex][yIndex];
			int auxState = 0;
			for (int i = 0; i < xIndex; i++) {
				for (int j = 0; j < yIndex; j++) {
					/* Calculate next state */
					auxState = applyRules(i, j);
					nextGeneration[i][j] = new Cell(currentGeneration[i][j].getX(), currentGeneration[i][j].getY(),
							auxState);
				}
			}
			currentGeneration = nextGeneration;
			if (0 < stepsToDo) {
				stepsToDo--;
			}
			before = now;
		}
	}

	@Override
	public void render() {
		// Render matrix
		for (Cell[] row : currentGeneration) {
			for (Cell c : row) {
				c.render();
			}
		}

		// Render mouse
		if (stepsToDo == 0) {
			int state = -1;
			int iX = Mouse.getX() / CELL_SIZE;
			int iY = (Main.SCREEN_HEIGHT - Mouse.getY() - HEADER_SIZE) / CELL_SIZE;
			if (0 < iX && iX < xIndex && 0 < iY && iY < yIndex) {
				int x = currentGeneration[iX][iY].getX();
				int y = currentGeneration[iX][iY].getY();
				state = currentGeneration[iX][iY].getState();
				stateL.render();
				charset.renderNumbers(200, 16, 32, 32, String.valueOf(state));
				glBegin(GL_QUADS);
				glVertex2f(x, y);
				glVertex2f(x + CELL_SIZE, y);
				glVertex2f(x + CELL_SIZE, y + CELL_SIZE);
				glVertex2f(x, y + CELL_SIZE);
				glEnd();
			}
		}
		playPauseB.render();
		stepB.render();
		resetB.render();
	}

	private void reset() {
		for (int i = 0; i < xIndex; i++) {
			for (int j = 0; j < yIndex; j++) {
				currentGeneration[i][j].setState(0);
			}
		}
		if(playPauseB.getLastIndex() == 0){
			playPauseB.activate();
		}
	}

	private class Cell {
		private final int x;
		private final int y;
		private int state;

		public Cell(int x, int y, int state) {
			this.x = x;
			this.y = y;
			this.state = state;
		}

		public void render() {

			glDisable(GL_TEXTURE_2D);
			if (0 < state) {
				float cF = 1f - (1f / (1f + state)*2);
				glColor3f(cF, .9f, cF);
			} else {
				glColor3f(0, 0, 0);
			}

			glBegin(GL_QUADS);
			glVertex2f(x, y);
			glVertex2f(x + Matrix.CELL_SIZE, y);
			glVertex2f(x + Matrix.CELL_SIZE, y + Matrix.CELL_SIZE);
			glVertex2f(x, y + Matrix.CELL_SIZE);
			glEnd();
			glColor3f(1f, 1f, 1f);
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

		public int getState() {
			return state;
		}

		@Override
		public String toString() {
			return "Cell (" + x + "," + y + ") State:" + state;
		}

		public void setState(int state) {
			this.state = state;

		}
	}
}
