echo "Spawning 6 delivery drones"
for i in {1..6} 
do
    ( ./gradlew delivery-drone:run -Pconf=config & )
 done