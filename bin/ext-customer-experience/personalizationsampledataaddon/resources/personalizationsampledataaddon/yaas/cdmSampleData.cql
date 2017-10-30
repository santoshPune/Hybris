LOAD CSV WITH HEADERS FROM "file:///Users/i303776/Work/env/cxm/sources/cxm/personalizationsampledataaddon/resources/personalizationsampledataaddon/yaas/customers.csv"
AS line
CREATE (:Identity{id:line.id,name:line.name,email:line.email}) ;

