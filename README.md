# Scratch Game
The game of chance compiled and build with java 1.8
The following dependencies were utilized:
  - Jackson Databind: Serialize/Deserialize json.
  - Lombok: code cleanliness.
  - Log4j: Logging application steps and show json result with result message.

## Build/Execution Notes
**"mvn clean install shade:shade"**: Please use this command in order to build and execute the application. 
It packages the dependencies above into our jar file. 
"mvn clean install" doesn't include external dependencies as deafult. **NoClassDefFoundError** can be thrown Without shade.

After building the jar file with **"mvn clean install shade:shade"**. You can run the application in bash via following command:   
**"java -jar <jar_directory>\ScratchGame-1.0-SNAPSHOT.jar --config <config_path>\<config_file_name>.json --betting-amount 100"**

Example: "java -jar ScratchGame-1.0-SNAPSHOT.jar --config <path_to_project>\Cyberspeed\src\main\resources\config.json --betting-amount 10"


## Business Logic Notes
The current calculation logic of the application for single symbol:
  - Reward of symbol
  - The reward of the highest reward from the same_symbol win combinations
  - The reward of the highest reward from the linear_symbol win combinations
  - Bet amount.
The logic has been inspired from output format example in the assignment. The A symbol also matches with same_symbol_3_times and same_symbol_4_times.
But only the highest (same_symbol_5_times) was included in the calculation.

The output format and examples inside assignment have been covered in unit tests.
