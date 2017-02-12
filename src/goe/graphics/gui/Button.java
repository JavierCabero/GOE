package goe.graphics.gui;

import static org.lwjgl.opengl.GL11.*;
import goe.main.GameObject;
import goe.main.Main;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.opengl.Texture;

public class Button implements GameObject {

	/* Variables */
	private int x;
	private int y;
	private int width;
	private int height;

	private Texture texture;
	private boolean pressed = false;
	private boolean active = false;

	/* Constructor */
	public Button(int x, int y, int width, int height, Texture texture) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.texture = texture;
	}

	/* Methods */
	public void input() {
		active = false;
		
		boolean inbounds = inBounds();
		if (inbounds && !pressed) {
			if (Mouse.isButtonDown(0)) {
				pressed = true;
			}
		} else if (!Mouse.isButtonDown(0)) {
			pressed = false;
			if(inbounds){
				active = true;
			}
		}
	}

	public boolean inBounds() {
		int mouseX = Mouse.getX();
		int mouseY = Mouse.getY();
		return x < mouseX && mouseX < x + width && Main.SCREEN_HEIGHT - y - height < mouseY
				&& mouseY < Main.SCREEN_HEIGHT - y;
	}

	public void logic() {
	}

	public boolean isPressed() {
		return pressed;
	}

	public void render() {
		
		glEnable(GL_TEXTURE_2D);
		
		if (pressed)
			glColor3f(.8f, .8f, .8f);
		else
			glColor3f(1f, 1f, 1f);
		texture.bind();

		glBegin(GL_QUADS);
		glTexCoord2f(0, 0);
		glVertex2f(x, y);
		glTexCoord2f(texture.getWidth(), 0);
		glVertex2f(x + width, y);
		glTexCoord2f(texture.getWidth(), texture.getHeight());
		glVertex2f(x + width, y + height);
		glTexCoord2f(0, texture.getHeight());
		glVertex2f(x, y + height);
		glEnd();

		glColor3f(1f, 1f, 1f);
		glDisable(GL_TEXTURE_2D);
	}

	public boolean isActive() {
		return active;
	}

	public void activate() {
		active = true;
	}

}
