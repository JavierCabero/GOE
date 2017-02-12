package goe.graphics.gui;

import static org.lwjgl.opengl.GL11.*;
import goe.main.GameObject;
import goe.main.Main;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.opengl.Texture;

public class DoubleTextureButton implements GameObject {

	/* Variables */
	private int x;
	private int y;
	private int width;
	private int height;

	private Texture currentTexture;
	private Texture texture1;
	private Texture texture2;
	
	private boolean pressed = false;
	private int state = 1;
	
	/* Constructor */
	public DoubleTextureButton(int x, int y, int width, int height, Texture texture1, Texture texture2) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.texture1 = texture1;
		this.currentTexture = texture1;
		this.texture2 = texture2;
		
	}

	/* Methods */
	public void input() {
		boolean inbounds = inBounds();
		if (!pressed && inbounds) {
			if (Mouse.isButtonDown(0)) {
				pressed = true;
			}
		} else if (!Mouse.isButtonDown(0)) {
			pressed = false;
			if(inbounds){
				if(currentTexture.equals(texture1)){
					currentTexture = texture2;
					state = 2;
				} else {
					currentTexture = texture1;
					state = 1;
				}
			}
		}
	}

	private boolean inBounds() {
		int mouseX = Mouse.getX();
		int mouseY = Mouse.getY();
		return x < mouseX && mouseX < x + width && Main.SCREEN_HEIGHT - y - height < mouseY && mouseY < Main.SCREEN_HEIGHT - y;
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
		
		currentTexture.bind();

		// TODO: Check if this works
		glBegin(GL_QUADS);
		glTexCoord2f(0, 0);
		glVertex2f(x, y);
		glTexCoord2f(currentTexture.getWidth(), 0);
		glVertex2f(x + width, y);
		glTexCoord2f(currentTexture.getWidth(), currentTexture.getHeight());
		glVertex2f(x + width, y + height);
		glTexCoord2f(0, currentTexture.getHeight());
		glVertex2f(x, y + height);
		glEnd();

		glColor3f(1f, 1f, 1f);
		glDisable(GL_TEXTURE_2D);
	}

	public int getState() {
		return state;
	}

}
