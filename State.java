import java.util.*;
/**
 * Name : Param Mohapatra(pxm417)
 * 
 * State represents a particular State of the 8 Puzzle
 * A state keeps trap of the matrix which it holds, its parent, and it's f value
 * zeroI,zeroJ just keep track of the position of 0 value in the matrix.
 **/
public class State implements Comparable{
  //Matrix represnting the State. 
  private int[][] matrix;
  //Parent of the State
  private State parent;
  //f(n) value of the State
  private int f; 
  public int zeroI;
  public int zeroJ;
  
  //Constructor that initializes the state to "000 000 000"
  public State(){
    this.matrix = new int[3][3];
    this.parent = null;
  }
  //Constructor that can initialize the state to particular input State.
  public State(String s){
    //Calls the first constructor to initialize a state with just zeros
    this();
    //Sets the State to a particular input 
    setStateHelper(s);
  }
  //Constructor that initializes the state to a particular input State and input f value
  public State(String s, int f){
    //Calls the first constructor to initialize a state with just zeros
    this();
    //Sets the State to a particular input 
    setStateHelper(s);
    //Sets the f value to a particular input 
    this.f = f;
  }
  //This constructor helps copy a State and also makes a move in a particular direction. Pretty much constructs a child state.
  //We only use this constructor in the A-star search when a move is valid
  private State(State parent, String direction){
    this(parent.toString(), parent.getF() + 1);
    this.parent = parent;
    this.zeroI = parent.zeroI;
    this.zeroJ = parent.zeroJ;
    moveHelper(direction);
  }
  
  
  
  //Sets the state to particular input
  //Required Method
  public void setStateHelper(String state){
    int[][] k = new int[3][3];
    int a = 0;
    for(int i = 0 ; i<3 ; i++){
      for(int j = 0 ; j<3 ; j++){
        if(state.charAt(a)=='b'){
          k[i][j] = 0;
          zeroI=i;
          zeroJ=j;
        }
        if(state.charAt(a)!='b' && state.charAt(a)!=' ')
          k[i][j] = (int)state.charAt(a) - 48;
        if(state.charAt(a)==' ')
          j--;
        a++;
      }
    }
    
    this.setMatrix(k);
    this.setParent(null);
    this.setF(0);
    
    
  }
  
  //Static method that prints the currentState of the Puzzle
  //Required Method 
  public static void printStateHelper(State input){
    String s = "";
    for(int i = 0; i < 3; i++){
      for(int j = 0; j < 3; j++){
        if(input.getMatrix()[i][j] == 0){
          if(j==0 && i!=0)
            s = s + ' ';
          s = s + 'b';
        }
        else{
          if(j==0 && i!=0)
            s = s + ' ';
          s = s + input.getMatrix()[i][j];
        }
      }
    }
    System.out.println(s);
  }
  
  //Moves a State in a particular direction 
  //Required Method 
  public void moveHelper(String direction){
    if(direction.equals("up")){
      if(zeroI == 0)
        ;
      else{
        int temp = this.matrix[zeroI-1][zeroJ];
        this.matrix[zeroI-1][zeroJ] = 0;
        this.matrix[zeroI][zeroJ]=temp;
        zeroI = zeroI - 1;
      }
      
    }
    
    if(direction.equals("down")){
      if(zeroI == 2)
        ;
      else{
        int temp = this.matrix[zeroI+1][zeroJ];
        this.matrix[zeroI+1][zeroJ] = 0;
        this.matrix[zeroI][zeroJ]=temp;
        zeroI = zeroI + 1;
      }
      
    }
    
    if(direction.equals("right")){
      if(zeroJ == 2)
        ;
      else{
        int temp = this.matrix[zeroI][zeroJ + 1];
        this.matrix[zeroI][zeroJ + 1] = 0;
        this.matrix[zeroI][zeroJ]=temp;
        zeroJ = zeroJ + 1;
      }
    }
    
    if(direction.equals("left")){
      if(zeroJ == 0)
        ;
      else{
        int temp = this.matrix[zeroI][zeroJ - 1];
        this.matrix[zeroI][zeroJ - 1] = 0;
        this.matrix[zeroI][zeroJ]=temp;
        zeroJ = zeroJ - 1;
      }
      
    }
  }
  
  public void randomizeStateHelper(int n){
    for(int i = 0;i<n;i++){
      double k = Math.random();
      if(k<0.25)
        moveHelper("up");
      if(k>=0.25 && k<0.5)
        moveHelper("down");
      if(k>=0.5 && k<0.75)
        moveHelper("left");
      if(k>=0.75 && k<=1)
        moveHelper("right");
      }
  }
  
  
  
  //Helper that sets the matrix of a particular state.
  public void setMatrix(int[][] matrixValues){
    this.matrix =  matrixValues;
  }
  
  //Helper that gets the matrix of a particular state
  public int[][] getMatrix(){
    return matrix;
  }
  
  //Helper that gets the F value of a particular State
  public int getF(){
    return f;
  }
  
  //Hellper that sets the F value of a particular State
  public void setF(int fValue){
    this.f = fValue; 
  }
  
  //Hellper that sets the Parent of a particular State
  public void setParent(State parentState){
    this.parent =  parentState;
  }
  
  //Hellper that gets the Parent of a particular State
  public State getParent(){
    return parent;
  }
  
  
  //Helper method that checks if a move is valid
  public boolean validMove(String direction){
    if(direction.equals("up")){
      if(zeroI == 0)
        return false;
    }
    if(direction.equals("down")){
      if(zeroI == 2)
        return false ;
    }
    if(direction.equals("left")){
      if(zeroJ == 0)
        return false;
    }
    if(direction.equals("right")){
      if(zeroJ == 2)
        return false;
    }
    
    return true;
    
  }
  
  
  
  //Helper method that gets all possible children of a State
  private String[] DIRECTIONS = new String[]{"up", "down", "left", "right"};
  public List<State> getExpandedStates(){
    
    List<State> expandedStates = new ArrayList<State>();
    
    for(String direction: DIRECTIONS){
      if(validMove(direction)){
        State expansion = new State(this, direction);
        expandedStates.add(expansion);
      }
    }
    
    return expandedStates;
  }
  
  
  //Need to override the compareTo method so that it compares the 2 states as per the Heuristic. 
  @Override
  public int compareTo(Object another) {
    int Hanother = Puzzle.calculateH1((State)another);
    int Fanother = ((State)another).getF();
    
    int HState = Puzzle.calculateH1(this); 
    int FState = this.f;
    if(FState + HState>Fanother + Hanother)
      return 1;
    else if(FState + HState<Fanother + Hanother)
      return -1;
    else
      return 0;
  }
  
  //Overrides the equals method so that it compares the matrix values at each index of the matrix.
  @Override 
  public boolean equals(Object input){
    for(int i = 0; i < 3; i++){
      for(int j = 0; j < 3; j++){
        if(((State)input).getMatrix()[i][j]!=this.getMatrix()[i][j])
          return false;
      }
    }
    return true;
  }
  
  //Overrides the toString method 
  @Override
  public String toString()
  {
    String s = "";
    for(int i = 0; i <3; i++)
    {
      for(int j = 0; j <3; j++)
        s = s + matrix[i][j];
      if(i != 2)
        s = s + " ";
    }
    return s;
  }
  
  //Calculate the hash-value of a State.
  //Helper
  @Override
  public int hashCode(){
    int code = 0;
    for(int i = 0; i < 3; i++)
      for(int j =0; j <3; j++)
      code += matrix[i][j]*i*j*j;
    return code;
  }
  
  
}