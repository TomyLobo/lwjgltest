package eu.tomylobo.lwjgltest;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Color;
import org.lwjgl.util.vector.Vector3f;

import static java.lang.Math.*;
import static org.lwjgl.opengl.GL11.*;
import static eu.tomylobo.lwjgltest.GL.*;

public class Hexagon {
	public static void main(String[] args) {
		int targetWidth = 640;
		int targetHeight = 480;

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
		final double factor = sqrt(1.0/6.0);//0.25;
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

		Vector3f xd = new Vector3f(3, 0, 0);
		final float radiusInternal = (float) (sqrt(3.0)/2.0);
		Vector3f yd = new Vector3f(1.5f, radiusInternal, 0);
		for (int y = 10; y >= -10; --y) {
			for (int x = -10; x <= 10; ++x) {
				Vector3f cellPos = Vector3f.add((Vector3f)(new Vector3f(xd).scale(x)), (Vector3f)(new Vector3f(yd).scale(y)), null);
				cell(cellPos.getX(), cellPos.getY(), 1f);
			}
		}
	}

	private static final Color mainHexagonColor = new Color(0, 0, 0, 255);
	private static final int mainHexagonWidth = 5;
	private static final boolean mainHexagonAbbreviated = true;

	private static final Color subdividedHexagonColor = new Color(0, 128, 255, 255);
	private static final int subdividedHexagonWidth = 3;
	private static final double subdivedHexagonRadius = 0.4;
	private static final boolean subdivedHexagonAbbreviated = false;

	private static final Color mainTriangleColor = new Color(255, 0, 0, 255);
	private static final int mainTriangleWidth = 1;

	private static final Color subdividedTriangleColor = new Color(0, 255, 0, 255);
	private static final int subdividedTriangleWidth = 1;

	public static void cell(double x, double y, double radius) {
		final double radiusInternal = radius * (sqrt(3.0)/2.0);

		glColor(subdividedTriangleColor);
		glLineWidth(subdividedTriangleWidth);
		glBegin(GL_LINES);
		for (int i = 0; i < 6; ++i) {
			double angle1 = (i+0.5)/3.0*Math.PI;
			double angle2 = (i+2.5)/3.0*Math.PI;
			glVertex3d(x + cos(angle1)*radiusInternal, y + sin(angle1)*radiusInternal, 0);
			glVertex3d(x + cos(angle2)*radiusInternal, y + sin(angle2)*radiusInternal, 0);
		}
		glEnd();

		glColor(mainTriangleColor);
		glLineWidth(mainTriangleWidth);
		glBegin(GL_LINES);
		for (int i = 0; i < 6; i += 2) {
			double angle1 = i/3.0*Math.PI;
			double angle2 = (i+3)/3.0*Math.PI;
			glVertex3d(x + cos(angle1)*radius, y + sin(angle1)*radius, 0);
			glVertex3d(x + cos(angle2)*radius, y + sin(angle2)*radius, 0);
		}
		glEnd();

		glColor(subdividedHexagonColor);
		final double subdividedRadius = radius * subdivedHexagonRadius;
		hexagon(x, y, subdividedRadius, subdividedHexagonWidth, subdivedHexagonAbbreviated);

		for (int i = 0; i < 3; ++i) {
			double angle = (i+0.5)/3.0*Math.PI;
			hexagon(x + cos(angle)*radiusInternal, y + sin(angle)*radiusInternal, subdividedRadius, subdividedHexagonWidth, subdivedHexagonAbbreviated);
		}

		glColor(mainHexagonColor);

		hexagon(x, y, radius, mainHexagonWidth, mainHexagonAbbreviated);
	}

	private static void hexagon(double x, double y, double radius, int width, boolean abbreviated) {
		if (abbreviated) {
			glLineWidth(width);
			glBegin(GL_LINE_STRIP);
			hexagonVertices(x, y, radius, true);
			glEnd();
		}
		else {
			glLineWidth(width);
			glBegin(GL_LINE_LOOP);
			hexagonVertices(x, y, radius, false);
			glEnd();

			glPointSize(width);
			glBegin(GL_POINTS);
			hexagonVertices(x, y, radius, false);
			glEnd();
		}
	}

	public static void hexagonVertices(double x, double y, double radius, boolean abbreviated) {
		int nVertices = abbreviated ? 4 : 6;
		for (int i = 0; i < nVertices; ++i) {
			double angle = i/3.0*Math.PI;
			glVertex3d(x + cos(angle)*radius, y + sin(angle)*radius, 0);
		}
	}

}
