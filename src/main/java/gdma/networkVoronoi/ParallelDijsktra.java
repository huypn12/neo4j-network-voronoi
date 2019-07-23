package gdma.networkVoronoi;

import org.neo4j.graphalgo.CostAccumulator;
import org.neo4j.graphalgo.CostEvaluator;
import org.neo4j.graphalgo.impl.shortestpath.Dijkstra;
import org.neo4j.graphalgo.impl.shortestpath.DijkstraPriorityQueue;
import org.neo4j.graphalgo.impl.shortestpath.DijkstraPriorityQueueFibonacciImpl;
import org.neo4j.graphdb.*;
import org.neo4j.helpers.collection.Iterables;

import java.util.*;

public class ParallelDijsktra<CostType>
{

    ParallelDijsktraIterator parallelDijsktraIterator;



    // Result
    protected Map<Node, Node> voronoiCells;

    /**
     * Find network voronoi (a map v:N->N, from each node to its Voronoi cell
     * @see Dijkstra
     */
    public ParallelDijsktra(List<Node> startNodes, CostType startCost,
                            CostAccumulator<CostType> costAccumulator,
                            CostEvaluator<CostType> costEvaluator,
                            Comparator<CostType> costComparator,
                            RelationshipType costRelationType)
    {
        parallelDijsktraIterator = new ParallelDijsktraIterator(startNodes, startCost,
                costAccumulator, costEvaluator, costComparator, costRelationType);
        voronoiCells = new HashMap<>();
        reset();
    }

    public void reset()
    {
        voronoiCells = new HashMap<>();
    }


    protected class ParallelDijsktraIterator implements Iterator<Node> {
        protected List<Node> startNodes;
        protected CostType startCost; // Start cost is 0 for all voronoi centers
        protected CostAccumulator<CostType> costAccumulator;
        protected CostEvaluator<CostType> costEvaluator;
        protected Comparator<CostType> costComparator;
        protected RelationshipType costRelationType;
        protected Map<Node, CostType> distance;
        protected DijkstraPriorityQueue<CostType> queue;

        public ParallelDijsktraIterator(List<Node> startNodes, CostType startCost,
                                        CostAccumulator<CostType> costAccumulator,
                                        CostEvaluator<CostType> costEvaluator,
                                        Comparator<CostType> costComparator,
                                        RelationshipType costRelationType)
        {
            this.startNodes = startNodes;
            this.startCost = startCost;
            this.costAccumulator = costAccumulator;
            this.costEvaluator = costEvaluator;
            this.costComparator = costComparator;
            this.costRelationType = costRelationType;
            this.distance = new HashMap<>();
            InitQueue();
        }

        protected void InitQueue() {
            queue = new DijkstraPriorityQueueFibonacciImpl<>( costComparator );
            for (Node startNode : startNodes)
            {
                queue.insertValue(startNode, startCost);
            }

        }

        @Override
        public Node next()
        {
            if ( !hasNext() )
            {
                throw new NoSuchElementException();
            }

            Node currentNode = queue.extractMin();
            CostType currentDistance = distance.get(currentNode);

            ResourceIterable<Relationship> relationships = Iterables.asResourceIterable(
                    currentNode.getRelationships(costRelationType, Direction.INCOMING));
            try (ResourceIterator<Relationship> iterator = relationships.iterator()) {
                while (iterator.hasNext()) {
                    Relationship relationship = iterator.next();


                    //TODO: complete this part, this is only dummy
                    Node targetNode = relationship.getOtherNode(currentNode);
                    CostType targetCost = costEvaluator.getCost(relationship, Direction.INCOMING);
                    queue.insertValue(targetNode, targetCost);
                    voronoiCells.putIfAbsent(currentNode, targetNode);
                }
            }
            return currentNode;
        }

        @Override
        public boolean hasNext()
        {
            return !queue.isEmpty();
        }
    }


    public boolean calculate()
    {
        while (parallelDijsktraIterator.hasNext())
        {
            parallelDijsktraIterator.next();
        }
        return true;
    }

    public Iterable<Map.Entry<Node, Node>> getVoronoiCells()
    {
        return voronoiCells.entrySet();
    }
}
