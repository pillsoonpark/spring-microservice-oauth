# To Build
mvn clean package 

# To Run
java -Doauth.db.url="<DATABASE URL>" -Doauth.db.driver-class-name="<DATABASE DRIVER>" -jar target/*.jar
