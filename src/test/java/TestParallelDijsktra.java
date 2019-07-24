import gdma.networkVoronoi.NetworkVoronoiQueryProcessor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.neo4j.graphdb.GraphDatabaseService;

public class TestParallelDijsktra {

    private static final String SETUP_SAMPLE_GRAPH= "CREATE\n" +
            "(n0 {name:\"n0\"}), " +
            "(n1 {name:\"n1\"}), " +
            "(n2 {name:\"n2\"}), " +
            "(n3 {name:\"n3\"}), " +
            "(n4 {name:\"n4\"}), " +
            "(n5 {name:\"n5\"}), " +
            "(n6 {name:\"n6\"}), " +
            "(n0)-[:WAY { weight:6 }]->(n1), " +
            "(n0)-[:WAY { weight:4 }]->(n2), " +
            "(n0)-[:WAY { weight:3 }]->(n3), " +
            "(n1)-[:WAY { weight:6 }]->(n0), " +
            "(n1)-[:WAY { weight:2 }]->(n3), " +
            "(n1)-[:WAY { weight:6 }]->(n6), " +
            "(n2)-[:WAY { weight:4 }]->(n0), " +
            "(n2)-[:WAY { weight:3 }]->(n3), " +
            "(n2)-[:WAY { weight:5 }]->(n4), " +
            "(n3)-[:WAY { weight:3 }]->(n0), " +
            "(n3)-[:WAY { weight:2 }]->(n1), " +
            "(n3)-[:WAY { weight:3 }]->(n2), " +
            "(n3)-[:WAY { weight:5 }]->(n4), " +
            "(n3)-[:WAY { weight:3 }]->(n5), " +
            "(n4)-[:WAY { weight:5 }]->(n2), " +
            "(n4)-[:WAY { weight:5 }]->(n3), " +
            "(n4)-[:WAY { weight:1 }]->(n5), " +
            "(n4)-[:WAY { weight:2 }]->(n6), " +
            "(n5)-[:WAY { weight:3 }]->(n3), " +
            "(n5)-[:WAY { weight:1 }]->(n4), " +
            "(n5)-[:WAY { weight:2 }]->(n6), " +
            "(n6)-[:WAY { weight:6 }]->(n1), " +
            "(n6)-[:WAY { weight:2 }]->(n4), " +
            "(n6)-[:WAY { weight:2 }]->(n5)";

    private static final String ERWIG_TESTCASE= "CREATE\n" +
            "(n0 {name:\"n0\"}), " +
            "(n1 {name:\"n1\"}), " +
            "(n2 {name:\"n2\"}), " +
            "(n3 {name:\"n3\"}), " +
            "(n4 {name:\"n4\"}), " +
            "(n5 {name:\"n5\"}), " +
            "(n6 {name:\"n6\"}), " +
            "(n7 {name:\"n7\"}), " +
            "(n0)-[:WAY { weight:3 }]->(n3), " +
            "(n1)-[:WAY { weight:3 }]->(n2), " +
            "(n1)-[:WAY { weight:3 }]->(n3), " +
            "(n3)-[:WAY { weight:4 }]->(n1), " +
            "(n3)-[:WAY { weight:2 }]->(n5), " +
            "(n4)-[:WAY { weight:5 }]->(n1), " +
            "(n4)-[:WAY { weight:6 }]->(n2), " +
            "(n4)-[:WAY { weight:5 }]->(n6), " +
            "(n4)-[:WAY { weight:6 }]->(n7), " +
            "(n5)-[:WAY { weight:3 }]->(n4), " +
            "(n5)-[:WAY { weight:2 }]->(n6), " +
            "(n6)-[:WAY { weight:3 }]->(n7), " +
            "(n7)-[:WAY { weight:3 }]->(n6)";


    private static GraphDatabaseService db;

    @BeforeAll
    public static void setUp() {
        db = TestDatabase.getTestDatabase();
        TestDatabase.registerProcedure(db, NetworkVoronoiQueryProcessor.class);
        db.execute(ERWIG_TESTCASE).close();
    }

    @AfterAll
    public static void tearDown() {
        db.shutdown();
    }

    @Test
    public void testValidity() {

    }

    @Test
    public void testNetworkVoronoi() {
        TestDatabase.testNetworkVoronoiCall(db,
                "MATCH (n3 {name:'n3'}), (n4 {name:'n4'})" +
                        "CALL gdma.networkVoronoi.stream([n3,n4], 'WAY', 'weight')" +
                        "YIELD nodeId, cell\n" +
                        "RETURN nodeId, cell" ,
                row ->  {
                    System.out.println(row.get("nodeId").toString() +
                            " in " +
                            row.get("cell").toString());
                }
        );
    }
}