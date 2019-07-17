import gdma.networkVoronoi.NetworkVoronoiQueryProcessor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.neo4j.graphdb.GraphDatabaseService;

public class TestNetworkVoronoi {

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

    private static GraphDatabaseService db;

    @BeforeAll
    public static void setUp() {
        db = TestDatabase.getTestDatabase();
        TestDatabase.registerProcedure(db, NetworkVoronoiQueryProcessor.class);
        db.execute(SETUP_SAMPLE_GRAPH).close();
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
                "MATCH (n1 {name:'n1'}), (n2 {name:'n2'})" +
                        "CALL gdma.networkVoronoi.stream([n1,n2], 'WAY')" +
                        "YIELD nodeId, cell\n" +
                        "RETURN nodeId, cell" ,
                row ->  {
                    System.out.println(row.get("nodeId").toString());
                }
        );
    }
}
