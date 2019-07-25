CREATE
(n0:NODE {name:"n0"}),
(n1:NODE {name:"n1"}),
(n2:NODE {name:"n2"}),
(n3:NODE {name:"n3"}),
(n4:NODE {name:"n4"}),
(n5:NODE {name:"n5"}),
(n6:NODE {name:"n6"}),
(n7:NODE {name:"n7"}),
(n0)-[:WAY { weight:3 }]->(n3),
(n1)-[:WAY { weight:3 }]->(n2),
(n1)-[:WAY { weight:3 }]->(n3),
(n3)-[:WAY { weight:4 }]->(n1),
(n3)-[:WAY { weight:2 }]->(n5),
(n4)-[:WAY { weight:5 }]->(n1),
(n4)-[:WAY { weight:6 }]->(n2),
(n4)-[:WAY { weight:5 }]->(n6),
(n4)-[:WAY { weight:6 }]->(n7),
(n5)-[:WAY { weight:3 }]->(n4),
(n5)-[:WAY { weight:2 }]->(n6),
(n6)-[:WAY { weight:3 }]->(n7),
(n7)-[:WAY { weight:3 }]->(n6);

MATCH (n3 {name:'n3'}), (n4 {name:'n4'})
CALL gdma.networkVoronoi.stream([n3,n4], 'WAY', 'weight')
YIELD nodeId, cell
RETURN nodeId.name AS node, cell.name AS center;
