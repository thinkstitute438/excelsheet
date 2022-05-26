// Meghana Pula
// Period 1
// Text Excel Project

import java.io.*;
import java.util.*;
/**
 * The Grid class will hold all the cells. It allows access to the cells via the
 * public methods. It will create a display String for the whole grid and process
 * many commands that update the cells. These command will include
 * sorting a range of cells and saving the grid contents to a file.
 *
 */
 
//inherits from gridBase
public class Grid extends GridBase {
   //the constructor creates a matrix
   public Grid(){
      createMatrix();
   }
   
   //this method to create a matrix to make cells and input values into grid
   public void createMatrix(){
      //1. create 2D array
      matrix=new Cell[rowCount][colCount];
      //2. fill the elements with a cell object
      for(int row=0; row< matrix.length;row++){
         for(int col=0; col< colCount; col++){
            Cell number=new Cell();
            matrix[row][col]=number;
         } 
      }
   }
   
   // These are called instance fields.
   // They are scoped to the instance of this Grid object.
   // Use them to keep track of the count of columns, rows and cell width.
   // They are initialized to the prescribed default values.
   // Notice that there is no "static" here.
   public int colCount = 7;
   public int rowCount = 10;
   public int cellWidth = 9;
   public String[] alphabet={"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
   private Cell[][] matrix;
   public String[] months={"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    /**
    * This processes a user command.
    * <p>
    * Checkpoint #1 commands are:
    * <ul>
    * <li><b>[cell] = [expression]</b> : "set the cell's expression, which may simply
    *  be a value such as 5, or it may more complicated such as: 'a3 * b3'."</li>
    * <li><b>[cell]</b> : "get the cell's expression, NOT the cell's value"</li>
    * <li><b>value [cell]</b> : "get the cell value"</li>
    * <li><b>print</b> : "render a text based version of the matrix"</li>
    * <li><b>clear</b> : "empty out the entire matrix"</li>
    * <li><b>clear [cell]</b> : "empty out a single cell"</li>
    * <li><b>sorta [range]</b> : "sort the range in ascending order"</li>
    * <li><b>sortd [range]</b> : "sort the range in descending order"</li>
    * <li><b>width = [value]</b> : "set the cell width"</li>
    * <li><b>width</b> : "get the cell width"</li>
    * <li><b>rows = [value]</b> : "set the row count"</li>
    * <li><b>cols = [value]</b> : "set the column count"</li>
    * <li><b>rows</b> : "get the row count"</li>
    * <li><b>cols</b> : "get the column count"</li>
    * </ul>
    * 
    * @param command The command to be processed.
    * @return The results of the command as a string.
    */
   
   //processes the command and it calls methods to perform command
   public String processCommand(String command) {
      String result=null;
      //if the command contains rows, cols, or width than it is chnaging the gridsize or
      //or cell width so it is sent into a method where they can be further processed to see
      //which one is being altered
      if(command.contains("rows") || command.contains("cols") || command.contains("width")){
         result = gridSizeCommands(result, command);
      //if the command contains print, it is to go into the methid that prints the grid
      //using the rowcount and colCount and cellWidth
      }else if(command.equals("print")){
         result= renderMatrix();
      //if the command contains clear with a space it means it is clearing a specific cell
      //and calls the method to clear value of cell
      }else if(command.contains("clear ")){
         result = clearCellLocation(command);
      //if the command is just clear it reset all the cells to empty themselves but the rows and cols do
      //not reset to row or col or width
      }else if(command.equals("clear")){
         createMatrix();
         result="";
      //if it had save and .txt it means that the command was to save previous commands in a
      //text file
      }else if(command.contains("save") && command.contains(".txt")){
         result=saveToFile(command);
      //if the command contains parentheses it means its a value that needs to be put in the
      //grid
      }else if(command.contains("(")){
         //the command is split with the celllocation and the value/expression
         String[] tokens=command.split("=");
         String cellLocation=tokens[0];
         cellLocation=cellLocation.replace(" ","");
         //you need to switch letter to number for the column the value should go in
         int colNum=findCellLocation(cellLocation.substring(0,1));
         int rowNum=Integer.parseInt(cellLocation.substring(1))-1;
         //sends the row and colum and commad(to find value) to be printed in 
         //text,date, or number cells
         addText(colNum, rowNum, command);
         result="";
      //any other commands are likely to be expr or display or value and a1
      //which are sent to processcommand to be further sectioned
      }else{
         result=processCellCommand(command);
      }
      //if none of the commands above went through then the result stays null
      if (result == null){
         //informs user that it is unknown
         result = "unknown or malformed command: " + command;
      }
      return result;
   }
   
   //this methid is used to find the number version of the colnum bc its
   //in letter form and it cant be used in matrix[rowNum][colNum] unless a number
   public int findCellLocation(String colLetter){
      int colNum=0;
      //loops through the entire alphabet
      for(int i=0; i<alphabet.length; i++){
         //checks to see which letter matches and takes the index and 
         //assigns to colNum
         if(alphabet[i].equals(colLetter.toUpperCase())){
            colNum=i;;
         }
      }
      return colNum;
   }

   //this method is used for clearing a specific cellLocation given by the command
   public String clearCellLocation(String command){
      //splits the location that needs to be cleared and the value
      String[] words = command.split(" ");
      //next three lines find the rownum and colNum using the location
      int rowNum=Integer.parseInt(words[1].substring(1));
      String colLetter=words[1].substring(0,1);
      int colNum=findCellLocation(colLetter);
      //use the rownum(you subtract 0 because in the matrix it starts at 0)
      // and colNum(you dont subtract zero bc the column of number of the rows counts as one)
      // to find the cell in the matrix then make a new cell which automically clears it
      matrix[rowNum-1][colNum]=new Cell();
      return "";
   }
   
   //this method is used to save previous commands into a text file
   public String saveToFile(String command){
      //split command to easily find the value for row col or width later
      String[] textFile=command.split(" ");
      PrintStream writer;
      //use try catch to catch the fileNotFoundException when you make a file
      try{
         //make a file and make a printstream to able to add print/save commands 
         //to the file
         writer = new PrintStream(new File(textFile[1]));
         //saves the rows, cols, and width and their values
         writer.println("rows = "+rowCount);
         writer.println("cols = "+colCount);
         writer.println("width = "+cellWidth);
         //loops through all the cells in the grid
         for(int i=0; i<rowCount; i++){
            for(int j=0; j<colCount; j++){
               //checks if the value(getExpression gives the value) at the 
               //specific cell is a string or empty
               if(!matrix[i][j].getExpression().equals("")){
                  //if its a string than its adds that command to the file
                  writer.println(alphabet[j]+(i+1)+" = " + matrix[i][j].getExpression());
               }
            }
         }
      //if you catch FileNotFoundException than return cannot find file
      }catch(FileNotFoundException e){
         return "Cannot Find File";
      }
      return "File Saved";
   }
   
   //this method is used to find cellLocation and send to cellCommands 
   public String processCellCommand(String command){
      String result=null;
      //to split the command into command:expr, display, value and location
      String[] words=command.split(" ");
      for(int eachWord=0; eachWord<words.length; eachWord++){
         //to check if the command is just the cellLocation or cellCmmand with cellLocation
         String word= words[eachWord];
         String letter=word.substring(0,1);
         Scanner number=new Scanner(word.replace(letter,""));
         //to check if the command is just the cellLocation or cellCmmand with cellLocation
         if(number.hasNextInt()){
         //find rowNum
            int rowNum=number.nextInt();
            for(int colNum=0; colNum<colCount; colNum++){
               //find colnum
               if(alphabet[colNum].equals(letter.toUpperCase())){
                  //pass the rownum(you subtract 0 because in the matrix it starts at 0)
                  // and colNum(you dont subtract zero bc the column of number of the rows counts as one)
                  // and command to see whether expr or display or value
                  result=cellCommands(colNum, rowNum-1, command);
               }
            }
         }
      }
      return result;
   }
   
   //this method differentiates the cells commands expr, value and display, and no cellComannd
   public String cellCommands(int colNum, int rowNum, String command){
      String result="";
      //if this is a value being added to a cellLocation it is automatically sent to cellLocation
      if(command.contains("=")){
         addText(colNum, rowNum, command);
         result="";
      //this is passed to a method that returns exactly what the user input
      }else if(command.startsWith("expr")){
         result=expression(colNum, rowNum);
      //this is passed to a method that returns 0 if string because string has no value other it returns the decimal form 
      }else if(command.startsWith("value")){
         result=value(colNum, rowNum);
      //this is passed to a method that returns the value in the cell
      }else if(command.startsWith("display")){
         result=display(colNum, rowNum);
      //means that there was no coimmand and the output should be same as expression
      }else{
         result=expression(colNum, rowNum);
      }
      return result;
   }
   
   //this method returns exactly what the user put into the command
   public String expression(int colNum, int rowNum){
      //matrix[rowNum][colNum] is the cellLocation and getExpression gets the input
      //at that specific location
      String display=matrix[rowNum][colNum].getExpression();
      return display;
   }
   
   //this method returns the value of the cell
   public String value(int colNum, int rowNum){
      //you get the display at the cell using matrix[rowNum][colNum]
      String display=matrix[rowNum][colNum].toString();
      Scanner phrase=new Scanner(display);
      //if its a number than you return the number as a double
      if(phrase.hasNextDouble()){
         return phrase.nextDouble()+"";
      //strings have no value therefore you return 0
      }else{
         return "0.0";
      }
   }

   //this method just returns what is in the cell at that cellLocation
   public String display(int colNum, int rowNum){
      //you get the display at the cell using matrix[rowNum][colNum]
      String display=matrix[rowNum][colNum].toString();
      return display;
   }
   
   //this method is what adds the text, numbers, values, dates to the cell
   public void addText(int colNum, int rowNum, String command){
      //you already have been given the cellLocation so you must find what is after the = sign because that
      //is the expression you are entering to the cell
      String phrase=command.substring(command.indexOf("=")+2);
      Scanner phrase2=new Scanner(phrase);
      System.out.println(phrase);
      //if it contains a / and no parenthese than you know it is a date
      if(phrase.contains("/") && !phrase.contains("(")){
         //create a datecell object
         DateCell date=new DateCell();
         //sets date to the datecell
         String expr=phrase2.nextLine();
         date.setExpression(expr);
         //enter the text into the cell using the cellLocation
         matrix[rowNum][colNum]=date;
      }else{
         //if it contains a quote it is a text cell
         if(phrase.contains("\"")){
            //creates a textcell object
            TextCell sentence=new TextCell();
            //sets the sentence to the textcell
            String expr=phrase2.nextLine();
            sentence.setExpression(expr);
             //enter the text into the cell using the cellLocation
            matrix[rowNum][colNum]=sentence;;
         }else{
            //any other would be a number or expression that needs to be added
            //creates numbercell object
            NumberCell number =new NumberCell();
            //sets the number or expression to numbercell
            number.setExpression(phrase);
            //enter the text into the cell using the cellLocation
            matrix[rowNum][colNum]=number;
         }
      }
   }

   //this method is used to resize the grid according to users commands
   public String gridSizeCommands(String result, String command){
      //if the command has "rows = "
      if(command.contains("rows = ")){
         //you get the number that rows equals to 
         Scanner num=new Scanner(command);
         num.next();
         num.next();
         //change rowCount
         rowCount=Integer.valueOf(num.nextInt());
         //create a matrix with new size
         createMatrix();
         result =rowCount+"";
      }else if(command.equals("rows")){
         //to return new rowCount
         result=rowCount+"";
      }
      //if the command has "cols = "
      if(command.contains("cols = ")){
         //you get the number that cols equals to 
         Scanner num=new Scanner(command);
         num.next();
         num.next();
         //change colCount
         colCount=Integer.valueOf(num.nextInt());
         //create Matrix with new size
         createMatrix();
         //return new colCount
         result =colCount+"";
      }else if(command.equals("cols")){
         //return the colCount bc no chnage
         result=colCount+"";
      }
      //if the command has "width = "
      if(command.contains("width = ")){
         //you get the number that width equals to
         Scanner num=new Scanner(command);
         num.next();
         num.next();
         //change width
         cellWidth=Integer.valueOf(num.nextInt());
         //return new width
         result =cellWidth+"";
      }else if(command.equals("width")){
         //return cellWidth because no chnage
         result=cellWidth+"";
      }
      return result;
   }
   
   //this method is used to build the grid
   public String renderMatrix(){
      String grid="";
      //prints the header line first
      grid+=headerLine();
      //for every row in rowCount it prints a separation line and a empty row
      for(int row=0; row<rowCount; row++){
         //prints sepration line
         grid+=separationLine();
         //empty row
         grid+=insideRow(row);
      }
      //final separation(bottom line)
      grid+=separationLine();
      return grid;
   }
   
   //this method is used to prints rows
   public String insideRow(int row){
      String grid="";
      //2 spaces in the front of every row
      int newWidth=2;
      //if it is a double digit number in the rows
      //cellWidth goes down one
      if(row>=9){
         newWidth-=1;
      }
      //prints the spaces before the number
      for(int i=0; i<newWidth; i++){
         grid+=" ";
      }
      //prints the number with a space
      int newRow=row+1;
      grid+=newRow+" ";
      //at every column theres a bar
      for(int col=0; col<colCount; col++){
         grid+="|";
         //finds the value of the cell
         String display=matrix[row][col].toString();
         //if the length of the number is more than the cell, it is suppsoed to get cut off
         if(display.length()>cellWidth){
            display=display.substring(0, cellWidth);
         //otherwise spaces are added
         }else{
            for(int spaces=0; spaces<cellWidth-display.length();spaces++){
               grid+=" ";
            }
         }
         //prints the value
         grid+=display;
      }
      //prints the bar at the end of every cell
      grid+="|";
      return grid;
   }
   
   //this method is used to print the separation lines
   public String separationLine(){
      String grid="";
      //starts new line
      grid+="\n";
      //prints the dashes during the row numbers
      for(int i=0; i<4; i++){
         grid+="-";
      }
      //prints the +-+-+-+-+-+-+-+- during the cell parts 
      for(int i=0; i<colCount; i++){
         grid+="+";
         for(int k=0; k<cellWidth; k++){
            grid+="-";
         }
      }
      //starts new line
      grid+="+\n";
      return grid;
   
   }
   
   //this method is used to print the columns header(A, B, C etc)
   public String headerLine(){
      //spaces because theres empty corner box
      String grid="    |";
      //spaces before the colNumber
      for(int col=0; col<colCount; col++){
         for(int j=0; j<cellWidth/2; j++){
            grid+=" ";
         }
         //prints the column Number
         grid+=alphabet[col];
         int newWidth=cellWidth;
         //if the number of spaces were divisibly by 2
         //the newWidth decreases
         if(newWidth%2==0){
            newWidth-=1;  
            //prints the spaces after the col number       
            for(int k=0; k<newWidth/2; k++){
               grid+=" ";
            
            }
         //the number of spaces were odd
         }else{
            //prints the spaces after the col number
            for(int k=0; k<cellWidth/2; k++){
               grid+=" ";
            }
         }
         //bars at the end of every cell
         grid+="|";
         //reset the newWidth
         newWidth=cellWidth;
      }
      return grid;
   }
}
