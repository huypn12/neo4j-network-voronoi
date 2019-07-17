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


    @Procedure(mode = Mode.READ)
    @Description("MATCH (n1 {name:’n1'}), (n2 {name:’n2’})" +
            "CALL gdma.networkVoronoi.stream([n1,n2])" +
            "YIELD nodeId, cell" +
            "RETURN nodeId, cell")
    public Stream<NetworkVoronoiResult> stream(
            @Name("voronoiCenters") List<Node> voronoiCenters,
            @Name("cost") String cost
    ) {
        return NetworkVoronoiResult.streamNetworkVoronoiResult(voronoiCenters, cost);
    }

}
