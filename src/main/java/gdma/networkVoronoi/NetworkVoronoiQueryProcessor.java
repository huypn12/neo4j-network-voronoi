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
            "CALL gdma.networkVoronoi.stream([n1,n2], relationshipName, costPropertyName)" +
            "YIELD nodeId, cell" +
            "RETURN nodeId, cell" +
            " - Description: Find (inward) Voronoi diagram, expand to a certain Relationship and Cost property"
        )
    public Stream<ParallelDijsktraResult> stream(
            @Name("voronoiCells") List<Node> voronoiCenters,
            @Name("relationshipName") String relationshipName,
            @Name("costPropertyName") String costPropertyName
    ) {
        return ParallelDijsktraResult.streamNetworkVoronoiResult(voronoiCenters, relationshipName, costPropertyName);
    }

}
