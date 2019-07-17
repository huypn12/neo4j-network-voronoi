package gdma.networkVoronoi;

import org.neo4j.graphdb.Node;

import java.util.stream.StreamSupport;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Stream;


public class NetworkVoronoiResult {
    public Node nodeId;
    public Node cell;

    public NetworkVoronoiResult(Map.Entry<Node, Node> result) {
        this.nodeId = result.getKey();
        this.cell = result.getValue();
    }

    public static Stream<NetworkVoronoiResult> streamNetworkVoronoiResult(ArrayList<Node> voronoiNodes) {
        Map<Node, Node> vcells = NetworkVoronoiFinder.generate();
        return StreamSupport.stream(vcells.entrySet().spliterator(), false).map(NetworkVoronoiResult::new);
    }
}
