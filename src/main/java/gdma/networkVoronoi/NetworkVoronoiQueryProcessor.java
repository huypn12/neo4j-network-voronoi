package gdma.networkVoronoi;

import static org.neo4j.graphdb.Direction.INCOMING;

import org.neo4j.graphdb.*;
import org.neo4j.procedure.*;

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
    public Stream<ParallelDijsktraResult> stream(
            @Name("voronoiCenters") List<Node> voronoiCenters,
            @Name("cost") String cost
    ) {
        return ParallelDijsktraResult.streamNetworkVoronoiResult(voronoiCenters, cost);
    }

    private PathExpander<Object> buildPathExpander(String cost) {
        PathExpanderBuilder builder = PathExpanderBuilder.empty();
        builder.add(RelationshipType.withName(cost), INCOMING);
        return builder.build();
    }

}
