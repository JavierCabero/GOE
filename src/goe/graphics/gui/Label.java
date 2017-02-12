package goe.graphics.gui;

import static org.lwjgl.opengl.GL11.*;

import org.newdawn.slick.opengl.Texture;

public class Label {

	private int x, y, width, height;
	private Texture texture;

	public Label(int x, int y, int width, int height, Texture texture) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.texture = texture;
	}

	public void render() {

		glColor3f(1f, 1f, 1f);
		glEnable(GL_TEXTURE_2D);
		
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

		glDisable(GL_TEXTURE_2D);
	}
}
