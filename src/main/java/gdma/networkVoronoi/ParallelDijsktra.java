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
    protected Map<Node, Node> voronoiCells = new HashMap<>();

    /**
     * Find network voronoi (a map v:N->N, from each node to its Voronoi cell
     * @see Dijkstra
     */
    public ParallelDijsktra(List<Node> startNodes, CostType startCost,
                            CostAccumulator<CostType> costAccumulator,
                            CostEvaluator<CostType> costEvaluator,
                            Comparator<CostType> costComparator,
                            RelationshipType costRelationType,
                            Direction voronoiDirection)
    {
        parallelDijsktraIterator = new ParallelDijsktraIterator(startNodes, startCost,
                costAccumulator, costEvaluator, costComparator, costRelationType, voronoiDirection);
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
        protected Map<Node, Boolean> marked;
        protected Map<Node, CostType> distance;
        protected DijkstraPriorityQueue<CostType> queue;
        protected Direction voronoiDirection;

        public ParallelDijsktraIterator(List<Node> startNodes, CostType startCost,
                                        CostAccumulator<CostType> costAccumulator,
                                        CostEvaluator<CostType> costEvaluator,
                                        Comparator<CostType> costComparator,
                                        RelationshipType costRelationType,
                                        Direction voronoiDirection)
        {
            this.startNodes = startNodes;
            this.startCost = startCost;
            this.costAccumulator = costAccumulator;
            this.costEvaluator = costEvaluator;
            this.costComparator = costComparator;
            this.costRelationType = costRelationType;
            this.distance = new HashMap<>();
            this.marked = new HashMap<>();
            this.voronoiDirection = voronoiDirection;
            InitQueue();
        }

        protected void InitQueue() {
            queue = new DijkstraPriorityQueueFibonacciImpl<>( costComparator );
            for (Node startNode : startNodes)
            {
                // init()
                distance.put(startNode, startCost);
                queue.insertValue(startNode, startCost);
                voronoiCells.put(startNode, startNode);
                marked.put(startNode, true);
            }
        }

        private void insertOrReplaceVoronoiCenter(Node targetNode, Node currentNode) {
            Node center = voronoiCells.get(currentNode);
            if (!voronoiCells.containsKey(targetNode)) {
                voronoiCells.put(targetNode, center);
            } else {
                voronoiCells.replace(targetNode, center);
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
            marked.putIfAbsent(currentNode, true);
            // expandnext()
            CostType currentDistance = distance.get(currentNode);
            ResourceIterable<Relationship> relationships = Iterables.asResourceIterable(
                    currentNode.getRelationships(costRelationType, voronoiDirection));
            try (ResourceIterator<Relationship> iterator = relationships.iterator()) {
                while (iterator.hasNext()) {
                    // scansuc()
                    Relationship relationship = iterator.next();
                    Node targetNode = relationship.getOtherNode(currentNode);
                    if (marked.containsKey(targetNode)) {
                        continue;
                    }
                    CostType edgeCost = costEvaluator.getCost(relationship, voronoiDirection);
                    CostType delta = costAccumulator.addCosts(currentDistance, edgeCost);
                    if (!distance.containsKey(targetNode)) {
                        distance.put(targetNode, delta);
                        insertOrReplaceVoronoiCenter(targetNode, currentNode);
                        queue.insertValue(targetNode, delta);
                    } else  {
                        CostType targetDistance = distance.get(targetNode);
                        if (costComparator.compare(delta, targetDistance) < 0) {
                            insertOrReplaceVoronoiCenter(targetNode, currentNode);
                            queue.decreaseValue(targetNode, delta);
                            distance.replace(targetNode, delta);
                        }
                    }
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
