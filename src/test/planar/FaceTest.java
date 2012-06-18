package test.planar;

import org.junit.Test;

import planar.Face;
import planar.Path;
import planar.Vertex;

public class FaceTest {
	
	@Test
	public void startToEndSplit() {
		Face face = new Face(7);
		face.add(0, 1);
		face.add(1, 2);
		face.add(2, 3);
		face.add(3, 4);
		face.add(4, 5);
		face.add(5, 6);
		face.add(6, 0);
		
		Vertex start = face.getVertex(3);
		Vertex end   = face.getVertex(6);

		Path path = new Path(2);
		path.add(start, end);
		Face splitFace = face.getStartToEndFace(start, end, path);
		System.out.println(splitFace);
	}
	
	@Test
	public void endToStartSplit() {
		Face face = new Face(7);
		face.add(0, 1);
		face.add(1, 2);
		face.add(2, 3);
		face.add(3, 4);
		face.add(4, 5);
		face.add(5, 6);
		face.add(6, 0);
		
		Vertex start = face.getVertex(3);
		Vertex end   = face.getVertex(6);

		Path path = new Path(2);
		path.add(start, end);
		Face splitFace = face.getEndToStartFace(start, end, path);
		System.out.println(splitFace);
	}

}
