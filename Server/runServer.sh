javac -sourcepath ./src -d bin src/server/csv/*.java
javac -sourcepath ./src -d bin -classpath ./bin:./gson-2.8.5.jar src/server/russel/*.java
java -classpath ./bin:./lib/gson-2.8.5.jar server.russel.Server 8000
