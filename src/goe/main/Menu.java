package goe.main;

import static org.lwjgl.opengl.GL11.*;
import goe.graphics.gui.Listenable;
import goe.graphics.gui.Listener;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.opengl.Texture;

public class Menu implements Listenable {

	private Texture background;
	private Listener listener;
	private boolean escapeWasPressed = true;

	public Menu(Texture background) {
		// Load menu texture
		this.background = background;
		escapeWasPressed = true;
	}

	public void setListener(Listener listener) {
		this.listener = listener;
	}

	public void render() {
		// Render background

		glColor3f(1f, 1f, 1f);
		background.bind();

		glBegin(GL_QUADS);
		glTexCoord2f(0, 0);
		glVertex2f(0, 0);
		glTexCoord2f(background.getWidth(), 0);
		glVertex2f(Main.SCREEN_WIDTH, 0);
		glTexCoord2f(background.getWidth(), background.getHeight());
		glVertex2f(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
		glTexCoord2f(0, background.getHeight());
		glVertex2f(0, Main.SCREEN_HEIGHT);
		glEnd();
	}

	public void input() {
		// Check for buttons event
		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			escapeWasPressed = true;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_RETURN)) {
			listener.sendMessage("StartGame");
		} else if (!escapeWasPressed && Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			listener.sendMessage("Exit");
		} else if (!Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			escapeWasPressed = false;
		}
	}

	public void logic() {

	}

}
