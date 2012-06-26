package eu.tomylobo.lwjgltest;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector;
import org.lwjgl.util.vector.Vector3f;

import static java.lang.Math.*;
import static org.lwjgl.opengl.GL11.*;
import static eu.tomylobo.lwjgltest.GL.*;

public class Main {
	public static void main(String[] args) {
		int targetWidth = 640;
		int targetHeight = 640;

		try {
			DisplayMode chosenMode = new DisplayMode(targetWidth,targetHeight);

			Display.setDisplayMode(chosenMode);
			Display.setTitle("Example Maven Natives");
			Display.setFullscreen(false);
			Display.create();
			Display.setResizable(true);
		} catch (LWJGLException e) {
			Sys.alert("Error","Unable to create display.");
			System.exit(0);
		}

		initializeGL();

		resizeGL();

		int FRAMERATE = 60;

		while (true) {
			paintGL();

			Display.update();
			Display.sync(FRAMERATE);

			if (Display.isCloseRequested()) {
				Display.destroy();
				break;
			}

			if (Display.wasResized()) {
				resizeGL();
			}
		}
	}

	private static void initializeGL() {
		glEnable(GL_BLEND);
		glEnable(GL_POINT_SMOOTH);
		glEnable(GL_LINE_SMOOTH);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		GL11.glClearColor(1f, 1f, 1f, 0);
		glMatrixMode(GL_MODELVIEW);
		final double factor = 0.1;
		glScaled(factor, factor, factor);
	}

	private static void resizeGL() {
		final int width = Display.getWidth()|1;
		final int height = Display.getHeight()|1;
		GL11.glViewport(0, 0, width, height);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		double aspX = Math.max(1, (double)width/height);
		double aspY = Math.max(1, (double)height/width);
		glOrtho(-aspX, aspX, -aspY, aspY, 0, 1);
	}

	private static void paintGL() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_STENCIL_BUFFER_BIT);
		glColor3f(0,0,0);

		for (float x = -10; x <= 10; ++x) {
			for (float y = -10; y <= 10; ++y) {
				final Vector3f position = new Vector3f(x, y, 0);
				final Vector3f orientation = position.normalise(null);
				orientation.scale(-1/position.lengthSquared());
				gltArrow(position, new Vector3f(x*-.2f, 1, 0).normalise(null));
			}
		}
	}
}
