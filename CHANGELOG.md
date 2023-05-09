# Commits details and the impact of each change:

- Changed the project packages structure, now the packages declaration
matches the source files location.  
 Impact: `low`  
  **Improved code readability**: With the packages structured according to the source file location, it becomes easier 
to navigate the code and understand its organization. Developers can more quickly find the code they need to modify 
or review.
- Switched `new Random()` to `SecureRandom.getInstanceStrong()` for generating random numbers.  
 Impact: `low`  
  **Improved security**: The `SecureRandom` class  provides a strong algorithm that is suitable for generating secure
  random numbers. By using SecureRandom instead of
Random, you are less likely to generate predictable or reproducible random numbers, which could be exploited by game
cheaters.
  