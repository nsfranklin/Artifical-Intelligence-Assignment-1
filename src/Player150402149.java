import java.awt.*;
import java.nio.file.OpenOption;
import java.util.*;



public class Player150402149 extends GomokuPlayer{
    public final int MAXDEPTH = 5;
    public  int UTILITY = 2;
    private int finalStatesVisited;
    private int statesVisited;
    public  final int[][][] leftToRightDiag =
            {{{0,3},{1,4},{2,5},{3,6},{4,7}},
             {{0,2},{1,3},{2,4},{3,5},{4,6},{5,7}},
             {{0,1},{1,2},{2,3},{3,4},{4,5},{5,6},{6,7}},
             {{0,0},{1,1},{2,2},{3,3},{4,4},{5,5},{6,6},{7,7}},
             {{1,0},{2,1},{3,2},{4,3},{5,4},{6,5},{7,6}},
             {{2,0},{3,1},{4,2},{5,3},{6,4},{7,5}},
             {{3,0},{4,1},{5,2},{6,3},{7,4}}};
    public final int[][][] rightToLeftDiag =
            {{{0,4},{1,3},{2,2},{3,1},{4,0}},
             {{0,5},{1,4},{2,3},{3,2},{4,1},{5,0}},
             {{0,6},{1,5},{2,4},{3,3},{4,2},{5,1},{6,0}},
             {{0,7},{1,6},{2,5},{3,4},{4,3},{5,2},{6,1},{7,0}},
             {{1,7},{2,6},{3,5},{4,4},{4,3},{5,2},{6,1}},
             {{2,7},{3,6},{4,5},{5,4},{6,3},{7,2}},
             {{3,7},{4,6},{5,5},{6,4},{7,3}}};
    public int[] DiagSize = {5, 6, 7, 8, 7, 6, 5};

    public final int[][] AvaliableActionHeuristic = {{3,3},{3,4},{4,3},{4,4},{2,3},{2,4},{3,2},{3,5},
                                                    {4,2},{4,5},{5,3},{5,4},{2,2},{2,5},{5,2},{5,5},
                                                    {1,3},{1,4},{3,1},{3,6},{4,1},{4,6},{6,3},{6,4},
                                                    {2,1},{1,2},{1,5},{2,6},{5,1},{6,2},{5,6},{6,5},
                                                    {1,1},{1,6},{6,1},{6,6},{0,1},{0,2},{0,3},{0,4},
                                                    {0,5},{0,6},{1,0},{2,0},{3,0},{4,0},{5,0},{6,0},
                                                    {1,7},{2,7},{3,7},{4,7},{5,7},{6,7},{7,1},{7,2},
                                                    {7,3},{7,4},{7,5},{7,6},{0,0},{0,7},{7,0},{7,7}};


    public Move chooseMove(Color[][] board, Color me) {
        finalStatesVisited = 0;
        statesVisited = 0;
        try {
            Integer[] MiniMaxResult = AlphaBetaMaxPlayer(board, Integer.MIN_VALUE, Integer.MAX_VALUE, MAXDEPTH, me);
            System.out.println(me.toString() + MiniMaxResult[0]+","+MiniMaxResult[1]);
            //System.out.println("Total Visited:" + finalStatesVisited);
            return new Move(MiniMaxResult[0],MiniMaxResult[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Integer[] AlphaBetaMaxPlayer(Color[][] state, int min, int max, int depth, Color player){
        finalStatesVisited++;
        Color nextPlayerColor = nextPlayer(player);

        ArrayList<Integer[]> actionsAvailable = populateAvailableActions(state);
        //System.out.println("Number of Actions" + actionsAvailable.size());

        if (CutOffTest(state, player, depth, actionsAvailable)) {
            Integer[] returned = utilityFunctionMax(state, actionsAvailable, player);
            System.out.println(returned[UTILITY]);
            return returned;
        }
        Integer[] best = new Integer[]{null, null, Integer.MIN_VALUE}; // [x,y,Utility]
        Integer[] actionAndReturnedUtility = new Integer[3];

        for (int i = 0; i < actionsAvailable.size(); i++) {
            actionAndReturnedUtility = AlphaBetaMinPlayer(makeMove(state, actionsAvailable.get(i), nextPlayerColor), min, max, depth - 1, nextPlayerColor);
            if (actionAndReturnedUtility[UTILITY] > best[2]) {
                best = new Integer[]{actionsAvailable.get(i)[0],actionsAvailable.get(i)[1], actionAndReturnedUtility[UTILITY]};
            }
            if (actionAndReturnedUtility[UTILITY] >= max) {
                return new Integer[]{null, null, actionAndReturnedUtility[UTILITY]};
            }
            if (actionAndReturnedUtility[UTILITY] > min) {
                min = actionAndReturnedUtility[UTILITY];
            }
        }
        return best;
    }
    public Integer[] AlphaBetaMinPlayer(Color[][] state, int min, int max,int depth, Color player) {
        finalStatesVisited++;

        Color nextPlayerColor = nextPlayer(player);
        ArrayList<Integer[]> actionsAvailable = populateAvailableActions(state);
        //System.out.println("Min" + actionsAvailable.size());

        if (CutOffTest(state, player, depth, actionsAvailable)) {
            Integer[] returned = utilityFunctionMin(state, actionsAvailable, player);
            //System.out.println(returned[UTILITY]);
            return returned;
        }
        Integer[] best = new Integer[]{null, null, Integer.MAX_VALUE}; // [x,y,Utility]
        Integer[] actionAndReturnedUtility = new Integer[3];

        for (int i = 0; i < actionsAvailable.size(); i++) {
            actionAndReturnedUtility = AlphaBetaMaxPlayer(makeMove(state, actionsAvailable.get(i), nextPlayerColor), min, max, depth - 1, nextPlayerColor);
            if (actionAndReturnedUtility[UTILITY] < best[2]) {
                best = new Integer[]{actionsAvailable.get(i)[0],actionsAvailable.get(i)[1], actionAndReturnedUtility[UTILITY]};
            }
            if (actionAndReturnedUtility[UTILITY] <= min) {
                return new Integer[]{null, null, actionAndReturnedUtility[UTILITY]};
            }
            if (actionAndReturnedUtility[UTILITY] < max) {
                max = actionAndReturnedUtility[UTILITY];
            }
        }
        return best;
    }

    public ArrayList<Integer[]> populateAvailableActions(Color[][] state){
        ArrayList<Integer[]> moves = new ArrayList<Integer[]>();
        for(int i = 0 ; i < AvaliableActionHeuristic.length ; i++){
            if(state[AvaliableActionHeuristic[i][0]][AvaliableActionHeuristic[i][1]] == null){
                moves.add(new Integer[]{AvaliableActionHeuristic[i][0],AvaliableActionHeuristic[i][1]});
            }
        }
        return moves;
    }

    public boolean CutOffTest(Color[][] state, Color player, int depth, ArrayList<Integer[]> a){  //terminal or max depth


    if(depth == 0 || a.size()==64){
        return true;
    }
    if(a.size()==63){

    }
    int count1 = 0;
    int count2 = 0;

        for (int i = 0; i < 8; i++) { //the verticle and horizontal
            for (int j = 0; j < 8; j++) {
                if(state[i][j] != null) {
                    if (state[i][j].equals(player)) {
                        count1++;
                    } else {
                        count1 = 0;
                    }
                }else{
                    count1 = 0;
                }

                if(state[j][i] != null) {
                    if(state[j][i].equals(player)) {
                        count2++;
                    } else {
                        count2 = 0;
                    }
                }else{
                    count2 = 0;
                }

                if (count1 == 5 || count2 == 5) {
                    return true;
                }
            }
        }
        int y = 0;
        count1 = 0;
        count2 = 0;
        for (int x = 0; x < 7; x++) { //the diagonals
            for (y = 0; y < DiagSize[x]; y++) {
                if (state[leftToRightDiag[x][y][0]][leftToRightDiag[x][y][1]] != null ) {
                    if (state[leftToRightDiag[x][y][0]][leftToRightDiag[x][y][1]].equals(player)) {
                        count1++;
                    } else {
                        count1 = 0;
                    }
                }else{
                    count1 = 0;
                }

                if(state[rightToLeftDiag[x][y][0]][rightToLeftDiag[x][y][1]] != null) {
                    if (state[rightToLeftDiag[x][y][0]][rightToLeftDiag[x][y][1]].equals(player)) {
                        count2++;
                    } else {
                        count2 = 0;
                    }
                }else{
                    count2 = 0;
                }

                if (count1 == 5 || count2 == 5) {
                    return true;
                }
            }
        }
        return false;
    }

    public Integer[] utilityFunctionMax(Color[][] state, ArrayList<Integer[]> a, Color me){
        if(a.size() == 64){
            return new Integer[]{3,3,Integer.MAX_VALUE};
        }
        int[] bestSoFar = new int[]{0,0,0}; //[x,y,lengthOfBest,nullbefore,nullafter]
        int[] worstSoFar = new int[]{0,0,0};

        int potential5 = 0;
        int enemyPotential5 = 0;
        int potential5AltDir = 0;
        int enemyPotential5AltDir = 0;
        int nullBefore = 0;
        int nullAfter = 0;


        return new Integer[]{0,0,0};for(int i = 0 ; i < 8; i++){
            for(int j = 0 ; j < 8 ; j++){
                if(state[i][j] == null){
                    nullBefore++;
                }else if(state[i][j].equals(me)){
                    potential5++;
                    enemyPotential5 = 0;
                }else{
                    enemyPotential5++;
                    potential5 = 0;
                }
                if(enemyPotential5 == 5){
                    return new Integer[]{i,j,100000};
                }
                if(potential5 == 5){
                    return new Integer[]{i,j,100000};
                }

                if(state[j][i] == null){
                    nullBefore++;
                }else if(state[j][i].equals(me)){
                    potential5AltDir++;
                    enemyPotential5AltDir = 0;
                }else{
                    enemyPotential5AltDir++;
                    potential5AltDir = 0;
                }
                if(enemyPotential5AltDir == 5){
                    return new Integer[]{j,i,100000};
                }
                if(potential5AltDir == 5){
                    return new Integer[]{j,i,100000};
                }
            }
        }
    }

    public Integer[] utilityFunctionMin(Color[][] state, ArrayList<Integer[]> a, Color me){

        int potential5 = 0;
        int enemyPotential5 = 0;
        int potential5AltDir = 0;
        int enemyPotential5AltDir = 0;
        int nullGapCount = 0;
        int[] bestSoFar = new int[]{0,0,0,0};
        int[] worstSoFar = new int[]{0,0,0,0};

        for(int i = 0 ; i < 8; i++){
            for(int j = 0 ; j < 8 ; j++){
                if(state[i][j] == null){
                    nullGapCount++;
                }else if(state[i][j].equals(me)){
                    potential5++;
                    enemyPotential5 = 0;
                }else{
                    enemyPotential5++;
                    potential5 = 0;
                }
                if(enemyPotential5 == 5){
                    return new Integer[]{i,j,-100000};
                }
                if(potential5 == 5){
                    return new Integer[]{i,j,-100000};
                }
                if(state[j][i] == null){
                    nullGapCount++;
                }else if(state[j][i].equals(me)){
                    potential5AltDir++;
                    enemyPotential5AltDir = 0;
                }else{
                    enemyPotential5AltDir++;
                    potential5AltDir = 0;
                }
                if(enemyPotential5AltDir == 5){
                    return new Integer[]{j,i,-100};
                }
                if(potential5AltDir == 5){
                    return new Integer[]{j,i,-100};
                }
            }
        }
        return new Integer[]{0,0,0};
    }
    public int huristicScoreMax(int runLength, int openEnds){
        if(runLength > 5){
            runLength = 5;
        }else if(openEnds == 0){
            return 0;
        }
        switch(runLength){
            case 5:
                switch(openEnds){
                    case 1:
                        return Integer.MAX_VALUE;
                    case 2:
                        return Integer.MAX_VALUE;
                }
            case 4:
                switch(openEnds){
                    case 1:
                        return 10000;
                    case 2:
                        return 1000000;
                }
            case 3:
                switch(openEnds){
                    case 1:
                        return 8;
                    case 2:
                        return 100;
                }
            case 2:
                switch(openEnds){
                    case 1:
                        return 4;
                    case 2:
                        return 7;
                }
            case 1:
                switch(openEnds){
                    case 1:
                        return 1;
                    case 2:
                        return 2;
                }
        }
        return 0;
    }

    public Color[][] makeMove(Color[][] state, Integer[] move, Color player){
        Color[][] newState = new Color[8][8];
        for(int i = 0 ; i < 8 ; i++){
            for(int j = 0 ; j < 8 ; j++){
                newState[i][j] = state[i][j];
            }
        }
        newState[move[0]][move[1]] = player;
        return newState;
    }

    public Color nextPlayer(Color player){
        if(player.equals(Color.black)) {
            return Color.white;
        }else{
            return Color.black;
        }
    }
    /*
    public boolean CutOffTestHighlyOptimised(Color[][] state, int[] best, int depth, Color player){  //terminal or max depth

        int count1 = 0;
        int count2 = 0;

        int[][][] leftToRightDiag = {{{0,3},{1,4},{2,5},{3,6},{4,7}},
                {{0,2},{1,3},{2,4},{3,5},{4,6},{5,7}},
                {{0,1},{1,2},{2,3},{3,4},{4,5},{5,6},{6,7}},
                {{0,0},{1,1},{2,2},{3,3},{4,4},{5,5},{6,6},{7,7}}};

        int[][][] rightToLeftDiag = {{{0,4},{1,3},{2,2}},
                {{0,5},{1,4},{2,3}},
                {{0,6},{1,5},{2,4},{3,3}},
                {{0,7},{1,6},{2,5},{3,4}},
                {{1,7}},
                {{2,7}},
                {{3,7}}};
        int[] rightDiagSize = {5};


        for(int i = 0 ; i < 8 ; i++ ){ //the verticle and horizontal
            for(int j = 0 ; j < 8 ; j++){
                if(state[i][j].equals(player)){
                    count1++;
                }else{
                    count1 = 0;
                }
                if(state[j][i].equals(player)){
                    count2++;
                }else{
                    count2 = 0;
                }
                if(count1 == 5 || count2 == 5){
                    return true;
                }
            }
        }
        int y = 0;
        count1 = 0;
        count2 = 0;
        for(int x = 0 ; x < 7 ; x++){ //the diagonals
            for(y = 0 ; y < ){

            }
        }

        return false;
    }
    */
}