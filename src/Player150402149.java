import java.awt.*;
import java.util.*;
import java.util.List;


public class Player150402149 extends GomokuPlayer{
    public final int MAXDEPTH = 2;
    public  final int[][][] leftToRightDiag = {{{0,3},{1,4},{2,5},{3,6},{4,7}},
            {{0,2},{1,3},{2,4},{3,5},{4,6},{5,7}},
            {{0,1},{1,2},{2,3},{3,4},{4,5},{5,6},{6,7}},
            {{0,0},{1,1},{2,2},{3,3},{4,4},{5,5},{6,6},{7,7}},
            {{1,0},{2,1},{3,2},{4,3},{5,4},{6,5},{7,6}},
            {{2,0},{3,1},{4,2},{5,3},{6,4},{7,5}},
            {{3,0},{4,1},{5,2},{6,3},{7,4}}};
    public final int[][][] rightToLeftDiag = {{{0,4},{1,3},{2,2},{3,1},{4,0}},
            {{0,5},{1,4},{2,3},{3,2},{4,1},{5,0}},
            {{0,6},{1,5},{2,4},{3,3},{4,2},{5,1},{6,0}},
            {{0,7},{1,6},{2,5},{3,4},{4,3},{5,2},{6,1},{7,0}},
            {{1,7},{2,6},{3,5},{4,4},{4,3},{5,2},{6,1}},
            {{2,7},{3,6},{4,5},{5,4},{6,3},{7,2}},
            {{3,7},{4,6},{5,5},{6,4},{7,3}}};
    public int[] DiagSize = {5, 6, 7, 8, 7, 6, 5};


    public Move chooseMove(Color[][] board, Color me) {
        Integer[] MiniMaxResult = AlphaBetaMaxPlayer(board, Integer.MIN_VALUE, Integer.MAX_VALUE, MAXDEPTH, me);
        return new Move(MiniMaxResult[0],MiniMaxResult[1]);
    }

    public Integer[] AlphaBetaMaxPlayer(Color[][] state, int min, int max, int depth, Color player){
        Color nextPlayerColor = nextPlayer(player);
        ArrayList<Integer[]> actionsAvailable = populateAvailableActions(state);
        Collections.shuffle(actionsAvailable);

        if(CutOffTest(state, player, depth)){
            Integer[] returned =  {null,null,utilityFunctionMax(state, actionsAvailable)};
            return returned;
        }
        Integer[] best = new Integer[3]; // [x,y,Utility]
        Integer[] actionAndReturnedUtility = new Integer[3];
        for(int i = 0 ; i < actionsAvailable.size(); i++){
            actionAndReturnedUtility = AlphaBetaMinPlayer(makeMove(state, actionsAvailable.get(i), nextPlayerColor), min , max , depth - 1 , nextPlayerColor );
             if(actionAndReturnedUtility[2] > best[2]){
                best = actionAndReturnedUtility;
             }else if(actionAndReturnedUtility[2] >= max){
                 return new Integer[]{null, null, actionAndReturnedUtility[2]};
             }else if(actionAndReturnedUtility[2] > min){
                min = actionAndReturnedUtility[2];
             }
        }
        return best;
    }
    public Integer[] AlphaBetaMinPlayer(Color[][] state, int min, int max,int depth, Color player){
        Color nextPlayerColor = nextPlayer(player);
        ArrayList<Integer[]> actionsAvailable = populateAvailableActions(state);
        Collections.shuffle(actionsAvailable);

        if(CutOffTest(state, player, depth)){
            Integer[] returned =  {null,null,utilityFunctionMin(state, actionsAvailable)};
            return returned;
        }
        Integer[] best = new Integer[3]; // [x,y,Utility]
        Integer[] actionAndReturnedUtility = new Integer[3];
        for(int i = 0 ; i < actionsAvailable.size(); i++){
            actionAndReturnedUtility = AlphaBetaMaxPlayer(makeMove(state, actionsAvailable.get(i), nextPlayerColor), min , max , depth - 1 , nextPlayerColor );
            if(actionAndReturnedUtility[2] < best[2]){
                best = actionAndReturnedUtility;
            }else if(actionAndReturnedUtility[2] <= min){
                return new Integer[]{null,null,actionAndReturnedUtility[2]};
            }else if(actionAndReturnedUtility[2] < max){
                max = actionAndReturnedUtility[2];
            }
        }
        return best;
    }

    public ArrayList<Integer[]> populateAvailableActions(Color[][] state){
        ArrayList<Integer[]> moves = new ArrayList<Integer[]>();
        for(int i = 0 ; i < 7 ; i++){
            for(int j = 0 ; j < 7 ; j++){
                if(state[i][j] == null){
                    moves.add(new Integer[]{i,j,null});
                }
            }
        }
        return moves;
    }

    public boolean CutOffTest(Color[][] state, Color player, int depth){  //terminal or max depth
        if(depth == 0){
            return true;
        }
        int count1 = 0;
        int count2 = 0;
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
            for(y = 0 ; y < DiagSize[x] ; y++){
                if(state[leftToRightDiag[x][y][0]][leftToRightDiag[x][y][1]].equals(player)){
                    count1++;
                }else{
                    count1 = 0;
                }
                if(state[rightToLeftDiag[x][y][0]][rightToLeftDiag[x][y][1]].equals(player)){
                    count2++;
                }else{
                    count2 = 0;
                }
                if(count1 == 5 || count2 == 5){
                    return true;
                }
            }
        }
        return false;
    }

    public int utilityFunctionMax(Color[][] state, ArrayList<Integer[]> a){
        if(a.size() == 64){

        }

        return new Random().nextInt();
    }

    public int utilityFunctionMin(Color[][] state, ArrayList<Integer[]> a){
        return new Random().nextInt();
    }

    public int bestblock(Color[][] state, ArrayList<Integer[]> options){
        return 1;
    }

    public Color[][] makeMove(Color[][] state, Integer[] move, Color player){
        Color[][] newState = state;
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