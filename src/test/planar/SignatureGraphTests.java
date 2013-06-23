package test.planar;

import graph.model.Graph;

import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import layout.ConcentricCircularLayout;
import layout.PlestenjakRefiner;

import org.junit.Test;

import planar.BlockEmbedding;
import planar.PlanarBlockEmbedder;
import signature.simple.SimpleGraph;
import signature.simple.SimpleGraphFactory;
import draw.ParameterSet;
import draw.SignatureColorer;

public class SignatureGraphTests extends AbstractDrawingTest {
	
	public static final String OUTPUT_DIR = "output/planar/signature";

	@Override
	public String getOutputDir() {
		return OUTPUT_DIR;
	}
	
	public Graph convert(SimpleGraph graph) {
		Graph cGraph = new Graph();
		for (SimpleGraph.Edge edge : graph.edges) {
			cGraph.makeEdge(edge.a, edge.b);
		}
		return cGraph;
	}
	
	public void testLarge(Graph graph, String filename) throws IOException {
	    BlockEmbedding embedding = PlanarBlockEmbedder.embed(graph);
        ParameterSet params = new ParameterSet();
        params.set("lineWidth", 2);
        params.set("pointRadius", 4);
        draw(embedding, 600, 600, filename, 
                new SignatureColorer(),
                new ConcentricCircularLayout(),
                new PlestenjakRefiner(new Rectangle2D.Double(0,0,600,600)),
                params);
	}
	
	@Test
	public void testFullerene() throws IOException {
	    Graph graph = convert(SimpleGraphFactory.make26Fullerene());
	    testLarge(graph, "fullerene26_refined.png");
	}
	
	@Test
    public void testCuneane() throws IOException {
        Graph graph = convert(SimpleGraphFactory.makeCuneane());
        testLarge(graph, "cuneane_refined.png");
    }
	
	@Test
    public void testGrotschGraph() throws IOException {
        Graph graph = convert(SimpleGraphFactory.makeGrotschGraph());
        testLarge(graph, "grotsch_refined.png");
    }
	
	@Test
	public void test() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException {
		for (Method method : SimpleGraphFactory.class.getMethods()) {
			String name = method.getName();
			System.out.println("Method " + name);
			String outputName = name.substring(4);	// remove 'make' from beginning
			if (method.getParameterTypes().length > 0 
					|| method.getReturnType() != SimpleGraph.class) continue;
			SimpleGraph graph = (SimpleGraph) method.invoke(SimpleGraphFactory.class);
			try {
				BlockEmbedding embedding = PlanarBlockEmbedder.embed(convert(graph));
				if (embedding != null) {
					draw(embedding, 300, 300, outputName + ".png", new SignatureColorer());
				}
			} catch (Exception e) {
				// naughty, naughty
				e.printStackTrace();
			}
		}
	}

}
