package com.stuypulse.frc2017.robot.cv;

import java.util.ArrayList;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import com.stuypulse.frc2017.util.Vector;

/**
 * CV Utility class used to draw an updatable array of Vectors using the GUI.
 * This image autoscales to fit a specific width and height, while keeping
 * all of the vectors in the frame.
 */

public class VectorDrawer {

	private int imageWidth, imageHeight;

	private ArrayList<Vector> vectors;

	public VectorDrawer(int imageWidth, int imageHeight) {
		this.imageWidth = imageWidth;
		this.imageHeight = imageHeight;
		vectors = new ArrayList<Vector>();
	}

	public void addVector(Vector vector) {
		vectors.add(vector);
	}

	public void addVectors(Vector[] vectors) {
		for(Vector vector : vectors) addVector(vector);
	}

	public Mat getImage() {
		// If there are no vectors, just return an empty tiny image.
		if (vectors.size() == 0) return new Mat( 0, 0, CvType.CV_8UC1 );

		Mat drawTo;
		// Everything is zero because we
		// want to include (0,0) (the origin) in the image
		double minX = 0;
		double minY = 0;
		double maxX = 0;
		double maxY = 0;

		for(Vector vector : vectors) {
			double x = vector.getX(),
				   y = vector.getY();
			if      (x < minX) minX = x;
			else if (x > maxX) maxX = x;
			if      (y < minY) minY = y;
			else if (y > maxY) maxY = y;
		}
		drawTo = new Mat((int)(maxX - minX), (int)(maxY - minY), CvType.CV_8UC1);
		
		// Start at the origin and draw out the vectors
		// Offset by minX and minY so we don't draw outside of the frame
		double offsetX = -1 * Math.min(0, minX),
			   offsetY = -1 * Math.min(0, minY);
		
		Point lastPoint = new Point(offsetX,offsetY);
		
		// Draw white lines connecting each vector
		for(Vector vector : vectors) {
			Point currentPoint = vector.getPoint();
			currentPoint.x += offsetX;
			currentPoint.y += offsetY;
			Imgproc.line(drawTo, lastPoint, currentPoint, new Scalar(255,255,255));
			lastPoint = currentPoint;
		}
		// Now we scale the image to fit into imageWidth and imageHeight
		Imgproc.resize(drawTo, drawTo, new Size(imageWidth, imageHeight));

		return drawTo;
	}

}
