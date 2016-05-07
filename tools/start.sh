DOMAINE=/apps/domoticz2mongo
nohup java -jar $DOMAINE/bin/domoticz2mongo.jar --spring.config.location=$DOMAINE/bin/application.properties --logging.config=$DOMAINE/bin/logback.xml &
echo $! > $DOMAINE/domoticz2mongo.pid
