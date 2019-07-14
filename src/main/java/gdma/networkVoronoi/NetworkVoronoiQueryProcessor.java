package gdma.networkVoronoi;

import org.neo4j.graphalgo.GraphAlgoFactory;
import org.neo4j.graphalgo.PathFinder;
import org.neo4j.graphalgo.WeightedPath;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.procedure.*;

import java.util.List;
import java.util.stream.Stream;

import static org.neo4j.graphalgo.CommonEvaluators.doubleCostEvaluator;

public class NetworkVoronoiQueryProcessor {
    @Context
    public GraphDatabaseService db;

    @Procedure(mode = Mode.READ)
    @Description("gdma.query.networkVoronoi() YIELD path, weight" +
            "- query network voronoi clustering using parallel dijsktra")
    public Stream<NetworkVoronoiResult> dijkstra(
            @Name("vcells") List<Node> vcells
    ) {
        NetworkVoronoiFactory voronoiFactory = 
        return NetworkVoronoiResult.streamNetworkVoronoiResult();
    }

}
