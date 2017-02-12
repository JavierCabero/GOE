package goe.graphics.gui;

import org.newdawn.slick.opengl.Texture;
import static org.lwjgl.opengl.GL11.*;

public class Charset {

	private Texture texture;

	public Charset(Texture charset) {
		this.texture = charset;
	}

	public void renderNumbers(int x, int y, int width, int height, String text) {
		glEnable(GL_TEXTURE_2D);

		int nextPos = 0;

		texture.bind();

		for (int i = 0; i < text.length(); i++) {
			/* Get codification of next char */
			float cod = getCodOf(text.charAt(i));

			/* Render char */
			float textureX = ((cod * 8f) / 80f) * texture.getWidth();
			glBegin(GL_QUADS);
			glTexCoord2f(textureX, 0);
			glVertex2f(x + nextPos, y);
			glTexCoord2f(textureX + ((texture.getWidth() * 7) / 64), 0);
			glVertex2f(x + width + nextPos, y);
			glTexCoord2f(textureX + ((texture.getWidth() * 7) / 64), texture.getHeight());
			glVertex2f(x + width + nextPos, y + height);
			glTexCoord2f(textureX, texture.getHeight());
			glVertex2f(x + nextPos, y + height);
			glEnd();

			nextPos += width - 1;
		}

		glDisable(GL_TEXTURE_2D);

	}

	private int getCodOf(char c) {
		switch (c) {
		case '0':
			return 0;
		case '1':
			return 1;
		case '2':
			return 2;
		case '3':
			return 3;
		case '4':
			return 4;
		case '5':
			return 5;
		case '6':
			return 6;
		case '7':
			return 7;
		case '8':
			return 8;
		case '9':
			return 9;
		default:
			throw new RuntimeException("Invalid char or charset.");
		}
	}
}
