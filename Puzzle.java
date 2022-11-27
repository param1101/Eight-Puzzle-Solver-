import java.util.*;


/**
 * Name : Param Mohapatra(pxm417)
 * Puzzle class consists of the AStar method and LocalBeamSearch method.
 * Both of them return the Solved State alongwith the Path the algorithm followed to reach the goal state. 
 * 
 * NOTE: My write-up also involves a testing section which shows how to test the various methods included in this class
 */
public class Puzzle{
  
  private State currentState = new State("b12 345 678");
  //Initially maxNodes is set 100000
  private int maxNodes = 100000;
  
  //Initializes the Puzzle
  public Puzzle(){
  }
  
  //Sets the currentState to a particular input. 
  //Input of the form "xxx xxx xxx" each position is a distinct integer b/w 0 and 9 and 0 is represented by b.
  //Required method
  public void setState(String input){
    currentState.setStateHelper(input);
  }
  
  //Returns the current State 
  //Not a Required method 
  public State getCurrentState(){
    return currentState;
  }
  
  //Prints currentState of the Puzzle
  //Required method
  public void printState(){
    System.out.println("" + this.getCurrentState());
  }
  
  //Moves the 0 tile in the puzzle in a particular direction
  //Inputs must be of the form "up" "down" "right" "left"
  //Required method 
  public void move(String input){
    currentState.moveHelper(input);
  }
  
  //Makes n random moves from the CurrentState of the puzzle.
  //Required method 
  public void randomizeState(int n){
    currentState.randomizeStateHelper(n);
  }
  
  //Solves the Puzzle using A-Star Search 
  //Calls the helper AStar method defined later
  //Prints out the path to the goalState
  //Required Method
  //Note: It doesn't change the object currentState. It returns a path to the goalState from the CurrentState.
  //Note : Dr. Lewicki said it’s okay to print the path from the current State to the goal State instead of the sequence of moves. So I just did that
  public void solveAStar(String heuristic){
    this.AStar(currentState,heuristic);
  }
  
  //Solve the Puzzle using Local Beam Search 
  //Prints out the path to the goalState
  //Evalutation function is path cost + H1(misplaced tiles)
  //Required Method
  //Note: It doesn't change the object currentState. It returns a path to the goalState from the CurrentState.
  //Note : Dr. Lewicki said it’s okay to print the path from the current State to the goal State instead of the sequence of moves. So I just did that
   public void solveBeam(int k){
    this.SolveBeamHelper(k, currentState);
  }
   
   //Sets the maximum number of nodes to be considered during a search algorithm.
   public void setMaxNodes(int n){
     this.maxNodes = n;
   }
  
  /**
   * Helper
   * AStar take an initial state and the goal state is "b12 345 678"
   * Prints the optimal path to the goal state as per the given heuristic.
   * Input must be "h1" or "h2"
   * Initializing the sets and lists to lengths of 1000 but they can change if it exceeds that length 
   */
  public void AStar(State input, String Heuristic){
    
    //Initialize the goalState 
    State goalState =  new State();
    goalState.setMatrix(new int[][]{{0,1,2},{3,4,5},{6,7,8}});
    
    //Initialize the open list and open set. HashSet will be used to make fast comparisons. 
    HashSet<State> openSet= new HashSet<State>(1000);
    PriorityQueue<State> openList;
    if(Heuristic.equals("h2"))
      openList = new PriorityQueue<State>(1000,new ManhattanComparator());
    else
      openList = new PriorityQueue<State>(1000,new tilesComparator());
    
    //Add the starting state to the openSet and openList
    openSet.add(input);
    openList.add(input);
    
    //Initialize the closedSet
    HashSet<State> closedSet = new HashSet<State>(1000);;
    
    //finalState which will help us backtrack through the path followed in the end.
    State finalState = null;
    int NodesConsidered= 0 ;
    while(!openList.isEmpty()){
      //Break the loop if we exceeds maxNodes.
      NodesConsidered++;
      if(NodesConsidered>maxNodes){
        System.out.println("MaxNodes exceeded");
        break;
      }
      //Get the next state to expand
      //Choose the 'cheapest' state in the open list
      State toExpand = openList.poll();
      //We're expanding the goal state!
      if(toExpand.equals(goalState)) {
        finalState = toExpand;
        break;
      }
      openSet.remove(toExpand);
      closedSet.add(toExpand);
      
      List<State> expansions = toExpand.getExpandedStates();
      for(State e : expansions){
        if(!closedSet.contains(e) && !openSet.contains(e)){
          openList.add(e);
          openSet.add(e);
        }
      } 
    }
    System.out.println("" + calculateMoves(finalState) + " moves: " + (calculateMoves(finalState).size()-1));
  }
  
  /**
   * Helper
   * Solves the 8Puzzle by using Local Beam Search 
   */
  public State SolveBeamHelper(int k, State current){
    
    State goalState =  new State();
    goalState.setMatrix(new int[][]{{0,1,2},{3,4,5},{6,7,8}});
    
    //Initialize Sets and Lists
    HashSet<State> currentSet= new HashSet<State>(10000);
    PriorityQueue<State> currentList = new PriorityQueue<State>(k);
    
    currentList.add(current);
    currentSet.add(current);
    
    //We can at most consider 4 children of each State. So 4k should be an upper bound for the possibleStates.
    PriorityQueue<State> possibleList = new PriorityQueue<State>(4*k);
    int count = 0;
    int NodesConsidered = 0;
    //If we find the goalState then Stop.
    while(!currentSet.contains(goalState)){
      //If NodesConsidered exceeds maxNodes then break the loop 
      NodesConsidered++;
      if(NodesConsidered>maxNodes){
        System.out.println("MaxNodes exceeded");
        break;
      }
      //Keep track of the number of states in the current list.
      count = 0 ;
      for(State P : currentList){
        State toExpand = P;
        //Add all successors of the k-states
        for(State e : toExpand.getExpandedStates()){
          possibleList.add(e);
          count++;
        }
      }
      //Clear the current list 
      currentList.clear();
      //Add best k successors 
      for(int i = 0 ; i<Math.min(count,k);i++){
        State addition =  possibleList.poll();
        currentList.add(addition);
        currentSet.add(addition);
      }
    }
    
    //Find the goalState so you can backtrack and find the path.
    State finalState = null;
    for(State e : currentList){
      if(e.equals(goalState))
        finalState = e;
    }
    System.out.println("" + calculateMoves(finalState) + " moves: " + (calculateMoves(finalState).size()-1) );
    return finalState;
    
  }
  
  //Method that calculates the path to the goalState once the goalState has been found;
  private static List<State> calculateMoves(State input){
    State current =  input;
    List<State> moves = new ArrayList<State>();
    while(current!=null){
      moves.add(current);
      current = current.getParent();
    }
    
    Collections.reverse(moves);
    return moves;
  }
  
  
  //Comparators for the two heuristics.
  private static class ManhattanComparator implements Comparator<State>{
    public int compare(State a, State b){
      //Calculate the g values for each state
      int ga = a.getF() + manhattanHeuristic(a);
      int gb = b.getF() + manhattanHeuristic(b);
      
      
      //Compare the g values
      if(ga > gb)
        return 1;
      else if(ga < gb)
        return -1;
      return 0;
    }
    
    private int manhattanHeuristic(State state){
      int[][] k = new int[][]{{0,1,2},{3,4,5},{6,7,8}};
      int count = 0;
      for(int i = 0; i < 3; i++){
        for(int j = 0; j < 3; j++){
          count = count + Math.abs(state.getMatrix()[i][j] -  k[i][j]);
          
        }
      }
      return count; 
    }
  }
  
  private static class tilesComparator implements Comparator<State>{
    public int compare(State a, State b){
      //Calculate the g values for each state
      int ga = a.getF() + tilesHeuristic(a);
      int gb = b.getF() + tilesHeuristic(b);
      
      
      //Compare the g values
      if(ga > gb)
        return 1;
      else if(ga < gb)
        return -1;
      return 0;
    }
    
    private int tilesHeuristic(State state){
      int[][] k = new int[][]{{0,1,2},{3,4,5},{6,7,8}};
      int count = 0;
      for(int i = 0; i < 3; i++){
        for(int j = 0; j < 3; j++){
          if(state.getMatrix()[i][j]!=k[i][j])
            count++;
          
        }
      }
      return count; 
    }
  }
  
  /**
   * A function that calculates the heuristic value of the matrix based on the number of missing tiles.  
   * Used for overriding the compareTo method which in turn is used in LocalBeamSearch.
   */
  public static int calculateH1(State state){
    int[][] k = new int[][]{{0,1,2},{3,4,5},{6,7,8}};
    int count = 0;
    for(int i = 0; i < 3; i++){
      for(int j = 0; j < 3; j++){
        count = count + state.getMatrix()[i][j] -  k[i][j];
        
      }
    }
    return count; 
  } 
  
  
  
}


