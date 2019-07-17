package gdma.networkVoronoi;

import org.neo4j.graphdb.Node;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class NetworkVoronoiFinder {
    public static Map<Node, Node> voronoi(List<Node> voronoiCenters, String cost)
    {
        Map<Node, Node> voronoi = new HashMap<>();
        for (Node it : voronoiCenters) {
            voronoi.putIfAbsent(it, it);
        }
        return voronoi;
    }
}
