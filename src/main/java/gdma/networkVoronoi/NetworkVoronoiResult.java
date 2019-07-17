package gdma.networkVoronoi;

import org.neo4j.graphdb.Node;

import java.util.stream.StreamSupport;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;


public class NetworkVoronoiResult {
    public Node nodeId;
    public Node cell;

    public NetworkVoronoiResult(Map.Entry<Node, Node> result) {
        this.nodeId = result.getKey();
        this.cell = result.getValue();
    }

    public static Stream<NetworkVoronoiResult> streamNetworkVoronoiResult(List<Node> voronoiNodes, String cost) {
        Map<Node, Node> voronoi = NetworkVoronoiFinder.voronoi(voronoiNodes, cost);
        return StreamSupport.stream(voronoi.entrySet().spliterator(), false).map(NetworkVoronoiResult::new);
    }
}
