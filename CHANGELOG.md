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
  **Improved code readability**: Collapsing if statements makes the code more readable and easier to understand.
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
