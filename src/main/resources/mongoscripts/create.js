use domoticz2mongo
db.createCollection("temperatures");
db.temperatures.createIndex({dateTime:-1});
db.temperatures.createIndex({idDevice:1});