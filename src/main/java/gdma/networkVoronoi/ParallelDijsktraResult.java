package gdma.networkVoronoi;

import org.neo4j.graphalgo.CostAccumulator;
import org.neo4j.graphalgo.CostEvaluator;
import org.neo4j.graphalgo.GraphAlgoFactory;
import org.neo4j.graphalgo.impl.shortestpath.SingleSourceShortestPath;
import org.neo4j.graphalgo.impl.shortestpath.SingleSourceShortestPathDijkstra;
import org.neo4j.graphalgo.impl.util.DoubleAdder;
import org.neo4j.graphdb.*;
import org.neo4j.kernel.impl.util.NoneStrictMath;

import static org.neo4j.graphalgo.CommonEvaluators.doubleCostEvaluator;

import java.util.Comparator;
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

    public static Stream<ParallelDijsktraResult> streamNetworkVoronoiResult(List<Node> startNodes,
                                                                            String relationshipName,
                                                                            String costPropertyName) {
        ParallelDijsktra<Double> algo = new ParallelDijsktra(
                startNodes, 0.0,
                new DoubleAdder(),
                doubleCostEvaluator(costPropertyName),
                new NoneStrictMath.CommonToleranceComparator(1e-9),
                RelationshipType.withName(relationshipName),
                Direction.INCOMING);
        algo.calculate();
        return StreamSupport.stream(algo.getVoronoiCells().spliterator(), false).map(ParallelDijsktraResult::new);
    }
}
