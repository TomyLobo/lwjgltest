package eu.tomylobo.lwjgltest;

import org.lwjgl.util.ReadableColor;
import org.lwjgl.util.vector.ReadableVector3f;
import org.lwjgl.util.vector.Vector3f;

import static org.lwjgl.opengl.GL11.*;

public class GL {
	public static void glColor(ReadableColor color) {
		glColor4ub(color.getRedByte(), color.getGreenByte(), color.getBlueByte(), color.getAlphaByte());
	}

	public static void glVertex(ReadableVector3f color) {
		glVertex3f(color.getX(), color.getY(), color.getZ());
	}

	public static void gltArrow(Vector3f position, Vector3f orientation) {
		float arrowHeadSize = 0.1f;

		orientation = new Vector3f(orientation);
		orientation.scale(0.5f);

		Vector3f p1 = Vector3f.sub(position, orientation, null);
		Vector3f p2 = Vector3f.add(position, orientation, null);
		// Compute the vector along the arrow direction
		Vector3f v = orientation.normalise(null);

		// Compute two perpendicular vectors to v
		Vector3f vPerp1 = new Vector3f(-v.y, v.x, 0);
		Vector3f vPerp2 = new Vector3f(v.y, -v.x, 0);

		// Compute two half-way vectors
		Vector3f v1 = Vector3f.add(v, vPerp1, null).normalise(null);
		Vector3f v2 = Vector3f.add(v, vPerp2, null).normalise(null);

		v1.scale(arrowHeadSize);
		v2.scale(arrowHeadSize);

		glBegin(GL_LINE_STRIP);
		glVertex(Vector3f.sub(p2, v1, null));
		glVertex(p2);
		glVertex(Vector3f.sub(p2, v2, null));
		glEnd();

		glBegin(GL_LINES);
		glVertex(p1);
		glVertex(p2);
		glEnd();
	}
}
