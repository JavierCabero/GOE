package goe.main;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import goe.graphics.TextureFactory;
import goe.graphics.gui.Listenable;
import goe.graphics.gui.Listener;
import goe.maths.Matrix;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.opengl.Texture;

public class Main implements Listener {

	public static final int SCREEN_WIDTH = 400;
	public static final int SCREEN_HEIGHT = 400;

	public Listenable state;

	private String menuBackgroundName = "menuBackground";
	private boolean isCloseRequested = false;

	public Main() {
		try {
			Display.setDisplayMode(new DisplayMode(SCREEN_WIDTH, SCREEN_HEIGHT));
			Display.setTitle("Game of Evolution");
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}

		// Set up configuration
		setUpOpenGL();

		// Create menu
		Texture menuBackground = TextureFactory.loadTexture(menuBackgroundName);
		state = new Menu(menuBackground);
		state.setListener(this);

		while (!Display.isCloseRequested()) {
			// Render

			glClear(GL_COLOR_BUFFER_BIT);

			if(isCloseRequested ){
				break;
			}
			state.input();
			state.logic();
			state.render();

			Display.update();
			Display.sync(60);

		}

		Display.destroy();
	}

	private void setUpOpenGL() {
		// Initialization code OpenGL
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, SCREEN_WIDTH, SCREEN_HEIGHT, 0, 1, -1); // (0,0) on
															// left-bottom
		glMatrixMode(GL_MODELVIEW);

		glEnable(GL_TEXTURE_2D);
		// Enable transparency
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}

	@Override
	public void sendMessage(String msg) {
		switch (msg) {
		case "StartGame":
			state = new Matrix(SCREEN_WIDTH, SCREEN_HEIGHT);
			state.setListener(this);
			break;
		case "MainMenu":
			System.out.println("LOADING MENU");
			Texture menuBackground = TextureFactory.loadTexture(menuBackgroundName);
			state = new Menu(menuBackground);
			state.setListener(this);
		case "Exit":
			isCloseRequested = true;
			break;
		}
	}

	public static void main(String[] args) {
		new Main();
	}

}
