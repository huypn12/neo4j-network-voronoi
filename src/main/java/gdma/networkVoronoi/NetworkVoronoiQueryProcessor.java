package gdma.networkVoronoi;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.procedure.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.neo4j.graphalgo.CommonEvaluators.doubleCostEvaluator;

public class NetworkVoronoiQueryProcessor {
    @Context
    public GraphDatabaseService db;

    private Stream<NetworkVoronoiResult> nop() {
        ArrayList<Node> nodes = new ArrayList<Node>();
        return NetworkVoronoiResult.streamNetworkVoronoiResult(nodes);
    }

    @Procedure(mode = Mode.READ)
    @Description("MATCH (n1 {name:’n1'}), (n2 {name:’n2’})" +
            "CALL gdma.networkVoronoi.stream([n1,n2])" +
            "YIELD nodeId, cell\n" +
            "RETURN nodeId, cell")
    public Stream<NetworkVoronoiResult> stream(
            @Name("voronoiCenters") List<Node> voronoiCenters,
            @Name("cost") String cost
    ) {
        System.out.println("fuck this shit");
        for (Node it : voronoiCenters) {
            System.out.println(it.getId());
        }
        return nop();
    }

}
