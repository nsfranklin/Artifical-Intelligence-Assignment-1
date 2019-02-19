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
             {{1,7},{2,6},{3,5},{4,4},{5,3},{6,2},{7,1}},
             {{2,7},{3,6},{4,5},{5,4},{6,3},{7,2}},
             {{3,7},{4,6},{5,5},{6,4},{7,3}}};
    public int[] DiagSize = {5, 6, 7, 8, 7, 6, 5};

    /*
    public final int[][] AvaliableActionHeuristic = {{3,3},{3,4},{4,3},{4,4},{2,3},{2,4},{3,2},{3,5},
                                                    {4,2},{4,5},{5,3},{5,4},{2,2},{2,5},{5,2},{5,5},
                                                    {1,3},{1,4},{3,1},{3,6},{4,1},{4,6},{6,3},{6,4},
                                                    {2,1},{1,2},{1,5},{2,6},{5,1},{6,2},{5,6},{6,5},
                                                    {1,1},{1,6},{6,1},{6,6},{0,1},{0,2},{0,3},{0,4},
                                                    {0,5},{0,6},{1,0},{2,0},{3,0},{4,0},{5,0},{6,0},
                                                    {1,7},{2,7},{3,7},{4,7},{5,7},{6,7},{7,1},{7,2},
                                                    {7,3},{7,4},{7,5},{7,6},{0,0},{0,7},{7,0},{7,7}};
    */

    public Move chooseMove(Color[][] board, Color me) {
        finalStatesVisited = 0;
        statesVisited = 0;
        try {
            Integer[] MiniMaxResult = AlphaBetaMaxPlayer(board, Integer.MIN_VALUE, Integer.MAX_VALUE, MAXDEPTH, me);
            System.out.println(me.toString() + MiniMaxResult[0]+","+MiniMaxResult[1]+","+MiniMaxResult[2]);
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
            Integer[] returned = utilityFunctionMax(state, actionsAvailable, player, depth);
            return returned;
        }
        Integer[] best = new Integer[]{null, null, Integer.MIN_VALUE}; // [x,y,Utility]
        Integer[] value = new Integer[3];

        for (int i = 0; i < actionsAvailable.size(); i++) {
            value = AlphaBetaMinPlayer(makeMove(state, actionsAvailable.get(i), nextPlayerColor), min, max, depth - 1, nextPlayerColor);
            if (value[UTILITY] > best[UTILITY]) {
                best = new Integer[]{actionsAvailable.get(i)[0],actionsAvailable.get(i)[1], value[UTILITY]};
            }
            if (value[UTILITY] >= max) {
                return new Integer[]{null, null, value[UTILITY]};
            }
            if (value[UTILITY] > min) {
                min = value[UTILITY];
            }
        }
        return best;
    }
    public Integer[] AlphaBetaMinPlayer(Color[][] state, int min, int max ,int depth, Color player) {
        finalStatesVisited++;

        Color nextPlayerColor = nextPlayer(player);
        ArrayList<Integer[]> actionsAvailable = populateAvailableActions(state);

        if (CutOffTest(state, player, depth, actionsAvailable)) {
            
            Integer[] returned = utilityFunctionMin(state, actionsAvailable, player, depth);
            return returned;
        }
        Integer[] best = new Integer[]{null, null, Integer.MAX_VALUE}; // [x,y,Utility]
        Integer[] value = new Integer[3];

        for (int i = 0; i < actionsAvailable.size(); i++) {
            value = AlphaBetaMaxPlayer(makeMove(state, actionsAvailable.get(i), nextPlayerColor), min, max, depth - 1, nextPlayerColor);
            if (value[UTILITY] < best[2]) {
                best = new Integer[]{actionsAvailable.get(i)[0],actionsAvailable.get(i)[1], value[UTILITY]};
            }
            if (value[UTILITY] <= min) {
                return new Integer[]{null, null, value[UTILITY]};
            }
            if (value[UTILITY] < max) {
                max = value[UTILITY];
            }
        }
        return best;
    }

    public ArrayList<Integer[]> populateAvailableActions(Color[][] state) {
        /*
        ArrayList<Integer[]> moves = new ArrayList<Integer[]>();
        for(int i = 0 ; i < AvaliableActionHeuristic.length ; i++){
            if(state[AvaliableActionHeuristic[i][0]][AvaliableActionHeuristic[i][1]] == null){
                moves.add(new Integer[]{AvaliableActionHeuristic[i][0],AvaliableActionHeuristic[i][1]});
            }
        }
        return moves;
        */
        ArrayList<Integer[]> moves = new ArrayList<Integer[]>();
        for (int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++ ){
                if(state[i][j]==null) {
                    moves.add(new Integer[]{i, j});
                }
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

    public Integer[] utilityFunctionMax(Color[][] state, ArrayList<Integer[]> a, Color me, int depth){
        if(a.size() == 64){
            return new Integer[]{3,3,Integer.MAX_VALUE};
        }
        int score = 0;

        score = score + findHorizontal(state, true, me, depth);
        score = score + findVerticle(state, true, me, depth);
        score = score + findDiagLeftToRight(state, true, me, depth);
        score = score + findDiagRightToLeft(state, true, me, depth);

        return new Integer[]{null,null,score};
    }

    public Integer[] utilityFunctionMin(Color[][] state, ArrayList<Integer[]> a, Color me, int depth){

        int score = 0;

        score = score + findHorizontal(state, false, me, depth);
        score = score + findVerticle(state, false, me, depth);
        score = score + findDiagLeftToRight(state, false, me, depth);
        score = score + findDiagRightToLeft(state, false, me, depth);

        return new Integer[]{null,null,score};
    }
    public int huristicScore(int runLength, int openEnds, boolean isMax, Color runColor, Color me, boolean currentTurn){
        if(runLength > 4){
            runLength = 4;
        }else if(openEnds == 0){
            return 0;
        }else if(openEnds >2){
            openEnds = 2;
        }
        int score = 0;
        switch(runLength){
            /*
            case 5:
                switch(openEnds){
                    case 1:
                        score =  Integer.MAX_VALUE/2;
                        break;
                    case 2:
                        score =  Integer.MAX_VALUE/2;
                }
                break;
                */
            case 4:
                switch(openEnds){
                    case 1:
                        if(currentTurn){
                            score =  500000;
                        }else{
                            score = 5000;
                        }
                        break;
                    case 2:
                        if(currentTurn) {
                            score = 50000000;
                        }else{
                            score = 50000;
                        }
                        break;
                }
                break;
            case 3:
                switch(openEnds){
                    case 1:
                        score =  100;
                        break;
                    case 2:
                        score =  5000;
                        break;
                }
                break;
            case 2:
                switch(openEnds){
                    case 1:
                        score =  4;
                        break;
                    case 2:
                        score =  10;
                        break;
                }
                break;
            case 1:
                switch(openEnds){
                    case 1:
                        score =  1;
                        break;
                    case 2:
                        score =  1;
                        break;
                }
                break;
            default: return 0;
    }
        if(isMax){
            if(score > 10000){
                System.out.println(score);
            }
            return score;
        }else{
            if(score > 10000){
                System.out.println(score*-1);
            }
            return score*-1;
        }
    }

    public int findHorizontal(Color[][] state, boolean isMax, Color me, int depth){
        int score = 0;
        int nullBefore = 0;
        int nullAfter = 0;
        int runLength = 0;
        Color runColor = me;
        boolean currentTurn = false;
        if(MAXDEPTH == depth){
            currentTurn = true;
        }

        for(int i = 0; i < 8 ; i++){
            for(int j = 0 ; j < 8 ; j++){
                if(state[i][j] == null){
                    if(runLength > 0){
                        nullAfter++;
                        score = huristicScore(runLength, nullAfter+nullBefore, isMax, runColor, me, currentTurn);
                        runLength = 0;
                        nullBefore = nullAfter;
                        nullAfter = 0;
                    }else if(nullBefore >1){
                    nullBefore = 1;
                    nullAfter = 0;
                }else{
                    nullBefore++;
                }
                }else if(state[i][j].equals(runColor)){
                    runLength++;
                }else if(state[i][j].equals(nextPlayer(runColor))){
                    score = huristicScore(runLength, nullBefore, isMax, runColor, me, currentTurn);
                    runLength = 1;
                    nullBefore = 0;
                    runColor = nextPlayer(runColor);
                }
            }
            if(runLength > 0){
                score = huristicScore(runLength, nullBefore, isMax, runColor, me, currentTurn);
            }
            runLength=0;
            nullAfter = 0;
            nullBefore = 0;
        }

        return score;
    }
    public int findVerticle(Color[][] state, boolean isMax, Color me, int depth){
        int score = 0;
        int nullBefore = 0;
        int nullAfter = 0;
        int runLength = 0;
        Color runColor = me;
        boolean currentTurn = false;
        if(MAXDEPTH == depth){
            currentTurn = true;
        }
        for(int i = 0; i < 8 ; i++){
            for(int j = 0 ; j < 8 ; j++){
                if(state[j][i] == null){
                    if(runLength > 0){
                        nullAfter++;
                        score = huristicScore(runLength, nullAfter+nullBefore, isMax, runColor, me, currentTurn);
                        runLength = 0;
                        nullBefore = nullAfter;
                        nullAfter = 0;
                    }else if(nullBefore >1){
                        nullBefore =1;
                        nullAfter = 0;
                    }else{
                        nullBefore++;
                    }
                }else if(state[j][i].equals(runColor)){
                    runLength++;
                }else{
                    score = huristicScore(runLength, nullBefore, isMax, runColor, me, currentTurn);
                    runLength = 1;
                    nullBefore = 0;
                    runColor = nextPlayer(runColor);
                }
            }
            if(runLength > 0){
                score = huristicScore(runLength, nullBefore, isMax, runColor, me,currentTurn);
            }
            runLength=0;
            nullAfter = 0;
            nullBefore = 0;
        }

        return score;
    }
    public int findDiagLeftToRight(Color[][] state, boolean isMax, Color me, int depth){
        int score = 0;
        int nullBefore = 0;
        int nullAfter = 0;
        int runLength = 0;
        Color runColor = me;
        boolean currentTurn = false;
        if(MAXDEPTH == depth){
            currentTurn = true;
        }else
        
        for(int i = 0; i < 7 ; i++){
            for(int j = 0 ; j < DiagSize[i] ; j++){
                if(state[leftToRightDiag[i][j][0]][leftToRightDiag[i][j][1]] == null){
                    if(runLength > 0){
                        nullAfter++;
                        score = huristicScore(runLength, nullAfter+nullBefore, isMax, runColor, me, currentTurn);
                        runLength = 0;
                        nullBefore = nullAfter;
                        nullAfter = 0;
                    }else if(nullBefore >1){
                        nullBefore =1;
                        nullAfter = 0;
                    }else{
                        nullBefore++;
                    }
                }else if(state[leftToRightDiag[i][j][0]][leftToRightDiag[i][j][1]].equals(runColor)){
                    runLength++;
                }else if(state[leftToRightDiag[i][j][0]][leftToRightDiag[i][j][1]].equals(nextPlayer(runColor))){
                    score = huristicScore(runLength, nullBefore, isMax, runColor, me, currentTurn);
                    runLength = 1;
                    nullBefore = 0;
                    runColor = nextPlayer(runColor);
                }
            }
            if(runLength > 0){
                score = huristicScore(runLength, nullBefore, isMax, runColor, me, currentTurn);
            }
            runLength=0;
            nullAfter = 0;
            nullBefore = 0;
        }
        return score;
    }
    public int findDiagRightToLeft(Color[][] state, boolean isMax, Color me, int depth){
        int score = 0;
        int nullBefore = 0;
        int nullAfter = 0;
        int runLength = 0;
        Color runColor = me;
        boolean currentTurn = false;
        if(MAXDEPTH == depth){
            currentTurn = true;
        }

        for(int i = 0; i < 7 ; i++){
            for(int j = 0 ; j < DiagSize[i] ; j++){
                if(state[rightToLeftDiag[i][j][0]][rightToLeftDiag[i][j][1]] == null){
                    if(runLength > 0){
                        nullAfter++;
                        score = huristicScore(runLength, nullAfter+nullBefore, isMax, runColor, me, currentTurn);
                        runLength = 0;
                        nullBefore = nullAfter;
                        nullAfter = 0;
                    }else if(nullBefore >1){
                        nullBefore =1;
                        nullAfter = 0;
                    }else{
                        nullBefore++;
                    }
                }else if(state[rightToLeftDiag[i][j][0]][rightToLeftDiag[i][j][1]].equals(runColor)){
                    runLength++;
                }else if(state[rightToLeftDiag[i][j][0]][rightToLeftDiag[i][j][1]].equals(nextPlayer(runColor))){
                    score = huristicScore(runLength, nullBefore, isMax, runColor, me, currentTurn);
                    runLength = 1;
                    nullBefore = 0;
                    runColor = nextPlayer(runColor);
                }
            }
            if(runLength > 0){
                score = huristicScore(runLength, nullBefore, isMax, runColor, me, currentTurn);
            }
            runLength=0;
            nullAfter = 0;
            nullBefore = 0;
        }
        return score;
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