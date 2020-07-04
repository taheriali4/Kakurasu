# Kakurasu 
  This is a little project I wrote to play a puzzle game called Kakurasu.
  The rules are quite simple 
  1. The goal is to mark the correct squares on the grid.
  2. The marked cells on each row sum up to the number on the corresponding column to the right
  2. The marked cells on each column sum up to the number on the corresponding row on the bottom
  3. If a cell is first on a row/column its value is 1, if it is second the value is 2 etc.
   
# Running the program
  To run this game:
  Run the jar file in dist
 - On linux systems `java -jar /dist/lib/kakurasu-***.jar` should work.
 - If it doesn't work on your system you can recompile it by running `ant clean` and then `ant` in the top level folder.

 While the jar file runs, go to `localhost:8080` in your browser
 
 
