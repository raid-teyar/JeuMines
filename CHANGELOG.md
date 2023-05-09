# Commits details and the impact of each change:

- Changed the project packages structure, now the packages declaration
matches the source files location.  
 Impact: `medium`  
  **Improved code readability**: With the packages structured according to the source file location, it becomes easier 
to navigate the code and understand its organization. Developers can more quickly find the code they need to modify 
or review.
- Switched `new Random()` to `SecureRandom.getInstanceStrong()` for generating random numbers.  
 Impact: `medium`  
  **Improved security**: The `SecureRandom` class  provides a strong algorithm that is suitable for generating secure
  random numbers. By using SecureRandom instead of
Random, you are less likely to generate predictable or reproducible random numbers, which could be exploited by game
cheaters.
- Merged collapsible if statements.  
 Impact: `low`  
  **Improved code readability and maintainability**: Collapsing if statements makes the code more readable and easier to understand.
- Changed `private final` variables to `private static final`  
Impact: `low`  
  **Improved Performance**: By making the variable static, there will only be one copy of the variable that is shared 
across all instances of the class, which can save memory and reduce the overhead of creating and managing new objects.
- Changed local variables and methods names to match the Java naming conventions camelCase  
Impact: `low`  
  **Improved code readability**: By following the Java naming conventions, the code becomes more readable and easier
    to understand.
- Made the image field transient.
 Impact: `meduim`  
  **Improved Compatibility**: If the Serializable class contains a non-serializable field, the serialization process 
 will fail, and an exception will be thrown. Therefore, making the image field either serializable or transient will ensure that the entire
 object can be serialized without errors. which means now we can transfer the object between different JVMs, and we ensured
 that our class is compatible with other libraries.
- Refactored the `newGame()` method to reduce the cognitive complexity.  
 Impact: `meduim`  
  **Improved code readability and maintainability**: The impact of this refactoring is that the cognitive complexity of the method has been 
significantly reduced from 49 to 15, making it easier to understand and maintain. The method has been broken down into
smaller, more manageable parts, and the logic for finding adjacent cells has been moved to a separate method.
Additionally, the `Arrays.fill` method has been used to initialize the `field` array with `COVER_FOR_CELL` values, rather
than using a for loop. Overall, these changes should make the code more readable and easier to modify in the future.
- Replaced `random.nextDouble()` with `random.nextInt()`
    Impact: `meduim`  
    **Improved Performance**: The `nextInt()` method is faster than `nextDouble()` because it does not have to convert the
    result to a double value. This change should improve the performance of the game.
- Added `@Override` to overriding and implementing methods
    Impact: `low`  
    **Improved code readability**: The `@Override` annotation makes it clear that the method is overriding a superclass
    method or implementing an interface method. This makes the code easier to understand and maintain.
- Replaced standard console output with `Logger` class  
 
    Impact: `low`  
    **Improved code readability**: The `Logger` class provides a more flexible and powerful way to log messages than
    standard console output. It allows you to specify the level of the message, which makes it easier to filter out
    messages that are not relevant to the current task. It also allows you to specify the source of the message, which
    makes it easier to identify where the message came from.
- Refactored  `findEmptyCells()` method to reduce the cognitive complexity.   
  This refactoring reduces the cognitive complexity of the findEmptyCells method by extracting the repeated code into
  the checkCell helper method. The helper method takes care of checking if the cell is within bounds, if it is not a mine
  cell, and if it is an empty cell. If these conditions are met, it marks the cell as uncovered and recursively calls
  findEmptyCells for the current cell.  
 Impact: `meduim`  
  **Improved code readability and maintainability**: The impact of this refactoring is that the findEmptyCells method 
is now easier to read and understand, which makes it less error-prone and easier to maintain. Additionally, the code 
is now more modular, which makes it easier to modify and test. The extracted helper method can be reused in other parts
of the codebase, which reduces code duplication and makes the code more maintainable.


