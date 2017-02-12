package goe.graphics;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

/**
 * Convenience class for loading textures.
 *
 * @author Javier Cabero Guerra
 */
public class TextureFactory {

	/** Loads PNG textures from the res folder. The fileName must not have extension. */
	public static Texture loadTexture(String fileName) {
		try {
			return TextureLoader.getTexture("PNG", new FileInputStream(new File("res/" + fileName + ".png")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
