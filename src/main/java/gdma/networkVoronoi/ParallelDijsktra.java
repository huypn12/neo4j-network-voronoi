package gdma.networkVoronoi;

import org.neo4j.graphalgo.CostAccumulator;
import org.neo4j.graphalgo.CostEvaluator;
import org.neo4j.graphalgo.impl.shortestpath.Dijkstra;
import org.neo4j.graphalgo.impl.shortestpath.SingleSourceShortestPath;
import org.neo4j.graphdb.*;

import java.util.*;

public class ParallelDijsktra<CostType> extends Dijkstra<CostType> implements SingleSourceShortestPath<CostType>
{
    private Map<Node, Node> voronoiCells;

    DijkstraIterator dijstraIterator;

    /**
     * Find network voronoi (a map v:N->N, from each node to its Voronoi cell
     * @see Dijkstra
     */
    public ParallelDijsktra(CostType startCost, Node startNode,
                            CostEvaluator<CostType> costEvaluator,
                            CostAccumulator<CostType> costAccumulator,
                            Comparator<CostType> costComparator,
                            Direction relationDirection, RelationshipType... costRelationTypes )
    {
        super( startCost, startNode, null, costEvaluator, costAccumulator,
                costComparator, relationDirection, costRelationTypes );
        reset();
    }

    @Override
    public void reset()
    {
        super.reset();
        voronoiCells = new HashMap<>();
    }


    protected class ParallelDijsktraIterator extends Dijkstra.DijkstraIterator {
        public ParallelDijsktraIterator(Node startNode,
                                        HashMap<Node, List<Relationship>> predecessors,
                                        HashMap<Node, CostType> mySeen,
                                        HashMap<Node, CostType> otherSeen,
                                        HashMap<Node, CostType> myDistances,
                                        HashMap<Node, CostType> otherDistances, boolean backwards )
        {
            super(startNode, predecessors, mySeen, otherSeen, myDistances, otherDistances, backwards);
        }


        @Override
        public Node next()
        {
            return null;
        }
    }

    public Node getVoronoiCenter(Node node)
    {
        return null;
    }

    // These overrides should be copied from singlesoureshortestpathdijsktra

    @Override
    public List<PropertyContainer> getPath(Node node) {
        return null;
    }

    @Override
    public List<Node> getPathAsNodes(Node node) {
        return null;
    }

    @Override
    public List<Relationship> getPathAsRelationships(Node node) {
        return null;
    }

    @Override
    public List<List<PropertyContainer>> getPaths(Node node) {
        return null;
    }

    @Override
    public List<List<Node>> getPathsAsNodes(Node node) {
        return null;
    }

    @Override
    public List<List<Relationship>> getPathsAsRelationships(Node node) {
        return null;
    }

    @Override
    public CostType getCost(Node node) {
        return null;
    }

    @Override
    public List<Node> getPredecessorNodes(Node node) {
        return null;
    }

    @Override
    public Map<Node, List<Relationship>> getPredecessors() {
        return null;
    }
}
