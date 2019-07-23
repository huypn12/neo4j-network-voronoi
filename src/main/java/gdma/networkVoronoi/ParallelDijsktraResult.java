package gdma.networkVoronoi;

import org.neo4j.graphdb.Node;

import java.util.stream.StreamSupport;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;


public class ParallelDijsktraResult {
    public Node nodeId;
    public Node cell;

    public ParallelDijsktraResult(Map.Entry<Node, Node> result) {
        this.nodeId = result.getKey();
        this.cell = result.getValue();
    }

    public static Stream<ParallelDijsktraResult> streamNetworkVoronoiResult(List<Node> voronoiNodes, String cost) {
        Map<Node, Node> voronoi = ParallelDijsktra.voronoi(voronoiNodes, cost);
        return StreamSupport.stream(voronoi.entrySet().spliterator(), false).map(ParallelDijsktraResult::new);
    }
}
