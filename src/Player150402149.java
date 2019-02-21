import java.awt.*;
import java.util.*;

import static java.util.Collections.shuffle;


@SuppressWarnings({"ALL", "SpellCheckingInspection"})
class Player150402149 extends GomokuPlayer{
    private int MAXDEPTH = 5;
    private final int UTILITY = 2;
    private int finalStatesVisited;
    private final int[][][] leftToRightDiag =
            {{{0,3},{1,4},{2,5},{3,6},{4,7}},
                    {{0,2},{1,3},{2,4},{3,5},{4,6},{5,7}},
                    {{0,1},{1,2},{2,3},{3,4},{4,5},{5,6},{6,7}},
                    {{0,0},{1,1},{2,2},{3,3},{4,4},{5,5},{6,6},{7,7}},
                    {{1,0},{2,1},{3,2},{4,3},{5,4},{6,5},{7,6}},
                    {{2,0},{3,1},{4,2},{5,3},{6,4},{7,5}},
                    {{3,0},{4,1},{5,2},{6,3},{7,4}}};
    private final int[][][] rightToLeftDiag =
            {{{0,4},{1,3},{2,2},{3,1},{4,0}},
                    {{0,5},{1,4},{2,3},{3,2},{4,1},{5,0}},
                    {{0,6},{1,5},{2,4},{3,3},{4,2},{5,1},{6,0}},
                    {{0,7},{1,6},{2,5},{3,4},{4,3},{5,2},{6,1},{7,0}},
                    {{1,7},{2,6},{3,5},{4,4},{5,3},{6,2},{7,1}},
                    {{2,7},{3,6},{4,5},{5,4},{6,3},{7,2}},
                    {{3,7},{4,6},{5,5},{6,4},{7,3}}};
    private final int[] DiagSize = {5, 6, 7, 8, 7, 6, 5};


    private final int[][] AvaliableActionHeuristic = {{3,3},{3,4},{4,3},{4,4},{2,3},{2,4},{3,2},{3,5},
            {4,2},{4,5},{5,3},{5,4},{2,2},{2,5},{5,2},{5,5},
            {1,3},{1,4},{3,1},{3,6},{4,1},{4,6},{6,3},{6,4},
            {2,1},{1,2},{1,5},{2,6},{5,1},{6,2},{5,6},{6,5},
            {1,1},{1,6},{6,1},{6,6},{0,1},{0,2},{0,3},{0,4},
            {0,5},{0,6},{1,0},{2,0},{3,0},{4,0},{5,0},{6,0},
            {1,7},{2,7},{3,7},{4,7},{5,7},{6,7},{7,1},{7,2},
            {7,3},{7,4},{7,5},{7,6},{0,0},{0,7},{7,0},{7,7}};

    private long startTime;

    public Move chooseMove(Color[][] board, Color me) {
        startTime = System.currentTimeMillis();
        finalStatesVisited = 0;
        try {
            ArrayList<Integer[]> actions = populateActions(board);

            if(actions.size() < 12){
                MAXDEPTH = 11;
                System.out.println("Max Depth Set to " + MAXDEPTH);
            }else if(actions.size() < 56){
                //MAXDEPTH = 5;
                System.out.println("Max Depth Set to " + MAXDEPTH);
            }
            Integer[] MiniMaxResult = AlphaBetaMaxPlayer(board, Integer.MIN_VALUE, Integer.MAX_VALUE, MAXDEPTH, me, me);


            System.out.println(findColour(me)+ " " + MiniMaxResult[0]+","+MiniMaxResult[1]+","+MiniMaxResult[2]);
            System.out.println("Total Visited:" + finalStatesVisited+ " Time: " + (System.currentTimeMillis()-startTime));

            if(MiniMaxResult[0] == null || MiniMaxResult[1] == null){
                ArrayList<Integer[]> actions2 = populateActions(board);
                shuffle(actions2);
                return(new Move(actions2.get(0)[0], actions2.get(0)[1]));
            }

            return new Move(MiniMaxResult[0],MiniMaxResult[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Integer[] AlphaBetaMaxPlayer(Color[][] state, int alpha, int beta, int depth, Color player, Color me){
        int min = alpha;
        int max = beta;

        finalStatesVisited++;
        Color nextPlayerColor = nextPlayer(player);

        ArrayList<Integer[]> actionsAvailable = populateAvailableActions(state);

        if (CutOffTest(state, depth, actionsAvailable)) {
            return utilityFunctionMax(state, actionsAvailable, me, depth);
        }
        Integer[] best = new Integer[]{null, null, Integer.MIN_VALUE}; // [x,y,Utility]
        Integer[] value;

        for (int i = 0 ; i < actionsAvailable.size() ; i++) {
            value = AlphaBetaMinPlayer(makeMove(state, actionsAvailable.get(i), nextPlayerColor), min, max, depth - 1, nextPlayerColor, me);
            if (value[UTILITY] > best[UTILITY]) {
                best = new Integer[]{actionsAvailable.get(i)[0], actionsAvailable.get(i)[1], value[UTILITY]};
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
    private Integer[] AlphaBetaMinPlayer(Color[][] state, int alpha, int beta, int depth, Color player, Color me) {
        int min = alpha;
        int max = beta;



        finalStatesVisited++;

        Color nextPlayerColor = nextPlayer(player);
        ArrayList<Integer[]> actionsAvailable = populateAvailableActions(state);

        if (CutOffTest(state, depth, actionsAvailable)) {
            return utilityFunctionMin(state, me, depth);
        }
        Integer[] best = new Integer[]{null, null, Integer.MAX_VALUE}; // [x,y,Utility]
        Integer[] value;

        for (int i = 0 ; i < actionsAvailable.size() ; i++) {
            value = AlphaBetaMaxPlayer(makeMove(state, actionsAvailable.get(i), nextPlayerColor), min, max, depth - 1, nextPlayerColor, me);

            if(depth == 0 && actionsAvailable.size()- i <3 ){
                System.out.println(min+ "," +max);
            }
            if (value[UTILITY] < best[UTILITY]) {
                best = new Integer[]{actionsAvailable.get(i)[0], actionsAvailable.get(i)[1], value[UTILITY]};
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

    private int huristicScore(int runLength, int openEnds, boolean isMax, Color runColor, Color me, boolean atThetop){

        if(runLength <= 0){
            return 0;
        }
        if(runLength > 5){
            runLength = 5;
        }else if(openEnds < 1){
            return 0;
        }else if(openEnds > 2){
            openEnds = 2;
        }

        int score = 0;
        switch(runLength){
            case 4:
                switch (openEnds) {
                    case 1:
                        if (runColor.equals(me)) {
                            score = 2000;
                        } else {
                            score = 2000000;
                        }
                        break;
                    case 2:
                        if (runColor.equals(me)) {
                            score = 200000;
                        } else {
                            score = 2000000;
                        }
                        break;
                }
                break;
            case 3:
                switch(openEnds){
                    case 1: score =  100;
                        break;
                    case 2: score =  200;
                        break;
                }
                break;
            case 2:
                switch(openEnds){
                    case 1: score =  3;
                        break;
                    case 2: score =  5;
                        break;
                }
                break;
            case 1:
                switch(openEnds){
                    case 1: score =  1;
                        break;
                    case 2: score =  2;
                        break;
                }
                break;
            default: score = Integer.MAX_VALUE/4-1;
                break;
        }

        if(isMax){
            return score;
        }
        return score*-1;
    }

    private boolean CutOffTest(Color[][] state, int depth, ArrayList<Integer[]> a){  //terminal or max depth
        if(depth == 0 || a.size()==64 || System.currentTimeMillis()-startTime > 9980){
            return true;
        }
        Color player = Color.white;

        int count1 = 0;
        int countEnemy1 = 0;
        int count2 = 0;
        int countEnemy2 = 0;


        for (int i = 0; i < 8; i++) { //the verticle and horizontal
            for (int j = 0; j < 8; j++) {
                if(state[i][j] != null) {
                    if (state[i][j].equals(player)) {
                        count1++;
                        countEnemy1 = 0;
                    } else {
                        count1 = 0;
                        countEnemy1++;
                    }
                }else{
                    count1 = 0;
                    countEnemy1 = 0;
                }

                if(state[j][i] != null) {
                    if(state[j][i].equals(player)) {
                        count2++;
                        countEnemy1 = 0;
                    } else {
                        count2 = 0;
                        countEnemy2++;
                    }
                }else{
                    count2 = 0;
                    countEnemy2++;
                }

                if (count1 >= 5 || count2 >= 5 || countEnemy1 >=5 || countEnemy2 >= 5) {
                    return true;
                }
                if(((7 - j + count1) < 5) && ((7 + count2 - j) < 5)){ //length of diag - y = number of spaces left. that plus count < 5 there aren't enough spaces left
                    break;
                }
            }
            count1 = 0;
            count2 = 0;
            countEnemy1 = 0;
            countEnemy2 = 0;
        }
        count1 = 0;
        count2 = 0;
        for (int x = 0; x < 7; x++) { //the diagonals
            for (int y = 0; y < DiagSize[x]; y++) {
                if (state[leftToRightDiag[x][y][0]][leftToRightDiag[x][y][1]] != null ) {
                    if (state[leftToRightDiag[x][y][0]][leftToRightDiag[x][y][1]].equals(player)) {
                        count1++;
                        countEnemy1 = 0;
                    } else {
                        count1 = 0;
                        countEnemy1++;
                    }
                }else{
                    count1 = 0;
                    countEnemy1 = 0;
                }

                if(state[rightToLeftDiag[x][y][0]][rightToLeftDiag[x][y][1]] != null) {
                    if (state[rightToLeftDiag[x][y][0]][rightToLeftDiag[x][y][1]].equals(player)) {
                        count2++;
                        countEnemy2 = 0;
                    } else {
                        count2 = 0;
                        countEnemy2++;
                    }
                }else{
                    count2 = 0;
                    countEnemy2 = 0;
                }

                if (count1 >= 5 || count2 >= 5 || countEnemy1 >=5 || countEnemy2 >= 5) {
                    return true;
                }
                if(((DiagSize[x] - y + count1 -1) < 5) && ((DiagSize[x] + count2 - y -1) < 5)){ //length of diag - y = number of spaces left. that plus count < 5 there aren't enough spaces left
                    break;
                }
            }
            count1 = 0;
            count2 = 0;
            countEnemy1 = 0;
            countEnemy2 = 0;
        }
        return false;
    }

    private Integer[] utilityFunctionMax(Color[][] state, ArrayList<Integer[]> a, Color me, int depth){
        if(a.size() == 64){
            return new Integer[]{3,3,Integer.MAX_VALUE};
        }
        int scoreVerticleAndHorizontal = 0;
        int scoreDiag = 0;

        scoreVerticleAndHorizontal = findHorizontalAndVerticle(state, true, me, depth);
        scoreDiag = findDiag(state, true, me, depth);

        if(scoreDiag > scoreVerticleAndHorizontal){
            return new Integer[]{null,null,scoreDiag};
        }else{
            return new Integer[]{null, null, scoreVerticleAndHorizontal};
        }
    }

    private Integer[] utilityFunctionMin(Color[][] state, Color me, int depth){

        int scoreVerticleAndHorizontal = 0;
        int scoreDiag = 0;

        scoreVerticleAndHorizontal = findHorizontalAndVerticle(state, true, me, depth);
        scoreDiag = findDiag(state, true, me, depth);

        if(scoreDiag < scoreVerticleAndHorizontal){
            return new Integer[]{null,null,scoreDiag};
        }else{
            return new Integer[]{null, null, scoreVerticleAndHorizontal};
        }
    }


    private ArrayList<Integer[]> populateAvailableActions(Color[][] state) {
        ArrayList<Integer[]> moves = new ArrayList<>();
        for (int[] aAvaliableActionHeuristic : AvaliableActionHeuristic) {
            if (state[aAvaliableActionHeuristic[0]][aAvaliableActionHeuristic[1]] == null) {
                moves.add(new Integer[]{aAvaliableActionHeuristic[0], aAvaliableActionHeuristic[1]});
            }
        }
        return moves;
    }
    private ArrayList<Integer[]> populateActions(Color[][] state){

        ArrayList<Integer[]> moves = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++ ){
                if(state[i][j]==null) {
                    moves.add(new Integer[]{i, j});
                }
            }
        }
        return moves;
    }
    private int findHorizontalAndVerticle(Color[][] state, boolean isMax, Color me, int depth){
        int tempScore = 0;
        int tempScore2 = 0;
        int tempScore3 = 0;
        int returnedScore = 0;
        int nullBefore = 0;
        int nullBefore2 = 0;
        int nullAfter;
        int nullAfter2;
        int runLength = 0;
        int runLength2 = 0;
        Color runColor = me;
        Color runColor2 = me;

        for(int i = 0; i < 8 ; i++){
            for(int j = 0 ; j < 8 ; j++){
                if(state[i][j] == null){
                    if(runLength > 0){
                        nullAfter = 1;
                        tempScore = huristicScore(runLength, nullAfter+nullBefore, isMax, runColor, me, depth==MAXDEPTH);
                        runLength = 0;
                        nullBefore = 1;
                    }else{
                        nullBefore = 1;
                    }
                }else if(state[i][j].equals(runColor)){
                    runLength++;
                }else if(state[i][j].equals(nextPlayer(runColor))){
                    tempScore = huristicScore(runLength, nullBefore, isMax, runColor, me, depth==MAXDEPTH);
                    runLength = 1;
                    nullBefore = 0;
                    runColor = nextPlayer(runColor);
                }

                if(state[j][i] == null){
                    if(runLength2 > 0){
                        nullAfter2 = 1;
                        tempScore2 = huristicScore(runLength2, nullAfter2+nullBefore2, isMax, runColor2, me, depth==MAXDEPTH);
                        runLength2 = 0;
                        nullBefore2 = 1;
                    }else{
                        nullBefore2 =1;
                    }
                }else if(state[j][i].equals(runColor2)){
                    runLength2++;
                }else{
                    tempScore2 = huristicScore(runLength2, nullBefore2, isMax, runColor2, me, depth==MAXDEPTH);
                    runLength2 = 1;
                    nullBefore2 = 0;
                    runColor2 = nextPlayer(runColor2);
                }
            }
            if(runLength > 0){
                tempScore = huristicScore(runLength, nullBefore, isMax, runColor, me, depth==MAXDEPTH);
            }
            if(runLength2 > 0){
                tempScore2 = huristicScore(runLength2, nullBefore2, isMax, runColor2, me, depth==MAXDEPTH);
            }
            if(isMax){
                if(tempScore2 > tempScore){
                    tempScore3 = tempScore2;
                }else{
                    tempScore3 = tempScore;
                }
            }else{
                if(tempScore2 < tempScore){
                    tempScore3 = tempScore2;
                }else{
                    tempScore3 = tempScore;
                }
            }
            if(isMax){
                if(tempScore3 > returnedScore){
                    returnedScore = tempScore3;
                }
            }else{
                if(tempScore3 < returnedScore){
                    returnedScore = tempScore3;
                }
            }
            runLength=0;
            nullBefore = 0;
            runLength2= 0;
            nullBefore2 = 0;
        }
        return returnedScore;
    }
    private int findDiag(Color[][] state, boolean isMax, Color me, int depth){
        int tempScore = 0;
        int tempScore2 = 0;
        int tempScore3 = 0;
        int returnedScore = 0;
        int nullBefore = 0;
        int nullBefore2 = 0;
        int nullAfter;
        int nullAfter2;
        int runLength = 0;
        int runLength2 = 0;
        Color runColor = me;
        Color runColor2 = me;
        boolean currentTurn = false;

        for(int i = 0; i < 7 ; i++){
            for(int j = 0 ; j < DiagSize[i] ; j++){
                if(state[leftToRightDiag[i][j][0]][leftToRightDiag[i][j][1]] == null){
                    if(runLength > 0){
                        nullAfter = 1;
                        tempScore = huristicScore(runLength, nullAfter+nullBefore, isMax, runColor, me, depth==MAXDEPTH);
                        runLength = 0;
                        nullBefore = 1;
                    }else{
                        nullBefore = 1;
                    }
                }else if(state[leftToRightDiag[i][j][0]][leftToRightDiag[i][j][1]].equals(runColor)){
                    runLength++;
                }else if(state[leftToRightDiag[i][j][0]][leftToRightDiag[i][j][1]].equals(nextPlayer(runColor))){
                    tempScore = huristicScore(runLength, nullBefore, isMax, runColor, me, depth==MAXDEPTH);
                    runLength = 1;
                    nullBefore = 0;
                    runColor = nextPlayer(runColor);
                }
                if(state[rightToLeftDiag[i][j][0]][rightToLeftDiag[i][j][1]] == null){
                    if(runLength2 > 0){
                        nullAfter2 = 1;
                        tempScore2 = huristicScore(runLength2, nullAfter2+nullBefore2, isMax, runColor2, me, depth==MAXDEPTH);
                        runLength2 = 0;
                        nullBefore2 = 1;
                    }else{
                        nullBefore2 = 1;
                    }
                }else if(state[rightToLeftDiag[i][j][0]][rightToLeftDiag[i][j][1]].equals(runColor2)){
                    runLength2++;
                }else if(state[rightToLeftDiag[i][j][0]][rightToLeftDiag[i][j][1]].equals(nextPlayer(runColor2))){
                    tempScore2 = huristicScore(runLength2, nullBefore2, isMax, runColor2, me, depth==MAXDEPTH);
                    runLength2 = 1;
                    nullBefore2 = 0;
                    runColor2 = nextPlayer(runColor2);
                }
            }
            if(runLength > 0){
                tempScore = huristicScore(runLength, nullBefore, isMax, runColor, me, depth==MAXDEPTH);
            }
            if(runLength2 > 0){
                tempScore2 = huristicScore(runLength2, nullBefore2, isMax, runColor2, me, depth==MAXDEPTH);
            }

            if(isMax){
                if(tempScore2 > tempScore){
                    tempScore3 = tempScore2;
                }else{
                    tempScore3 = tempScore;
                }
            }else{
                if(tempScore2 < tempScore){
                    tempScore3 = tempScore2;
                }else{
                    tempScore3 = tempScore;
                }
            }
            if(isMax){
                if(tempScore3 > returnedScore){
                    returnedScore = tempScore3;
                }
            }else{
                if(tempScore3 < returnedScore){
                    returnedScore = tempScore3;
                }
            }
            runLength=0;
            nullBefore = 0;
            runLength2 = 0;
            nullBefore2 = 0;
        }
        return returnedScore;
    }
    public String findColour(Color c){
        if(c.equals(Color.black)){
            return "Black";
        }
        else{
            return "White";
        }
    }

    private Color[][] makeMove(Color[][] state, Integer[] move, Color player){
        Color[][] newState = new Color[8][8];
        for(int i = 0 ; i < 8 ; i++){
            for(int j = 0 ; j < 8 ; j++){
                newState[i][j] = state[i][j];
            }
        }
        newState[move[0]][move[1]] = player;
        return newState;
    }
    private Color nextPlayer(Color player){
        if(player.equals(Color.black)) {
            return Color.white;
        }else{
            return Color.black;
        }
    }
}