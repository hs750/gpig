echo "Spawning 6 detection drones"
for i in {1..6} 
do
    ( ./gradlew detection-drone:run -Pconf=config & )
 done