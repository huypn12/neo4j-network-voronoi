import gdma.networkVoronoi.NetworkVoronoiQueryProcessor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

import java.util.HashMap;
import java.util.Map;

public class TestParallelDijsktra {

    // Graph described in Erwig2000
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
    public void testInwardVoronoi() {
        // expected, according to Wrwig
        Map<String, String> expected = new HashMap<>();
        expected.put("n0", "n3");
        expected.put("n1", "n3");
        expected.put("n3", "n3");
        expected.put("n4", "n4");
        expected.put("n5", "n4");

        TestDatabase.testNetworkVoronoiCall(db,
                "MATCH (n3 {name:'n3'}), (n4 {name:'n4'})" +
                        "CALL gdma.networkVoronoi.stream([n3,n4], 'WAY', 'weight')" +
                        "YIELD nodeId, cell\n" +
                        "RETURN nodeId.name AS node, cell.name AS center",
                row ->  {
                    String node = row.get("node").toString();
                    String actualCenter = row.get("center").toString();
                    String expectedCenter = expected.get(node);
                    Assertions.assertEquals(expectedCenter, actualCenter);
                    System.out.println(node + " in cell " + actualCenter);
                }
        );
    }

    /* Keep it here in case of another test
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

     */
}
