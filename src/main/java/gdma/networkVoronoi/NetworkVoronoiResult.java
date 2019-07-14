package gdma.networkVoronoi;

import org.neo4j.graphdb.Node;

import java.util.stream.StreamSupport;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Stream;


public class NetworkVoronoiResult {
    private Node node;
    private Node vcell;

    public NetworkVoronoiResult(Map.Entry<Node, Node> result) {
        this.node = result.getKey();
        this.vcell = result.getValue();
    }

    public static Stream<NetworkVoronoiResult> streamNetworkVoronoiResult(ArrayList<Node> voronoiNodes) {
        Map<Node, Node> vcells = NetworkVoronoiFactory.voronoi();
        StreamSupport.stream(vcells.entrySet().spliterator(), false).map(NetworkVoronoiResult::new);
    }
}
