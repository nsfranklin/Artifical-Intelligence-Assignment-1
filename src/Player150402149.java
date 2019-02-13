import java.awt.*;

public class Player150402149 extends GomokuPlayer{
    public final int MAXDEPTH = 3;

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
            {{3,7},{4,6},{5,5},{6,4},{7,3}}}; //read only therefore less memory used if global.

    public int[] DiagSize = {5, 6, 7, 8, 7, 6, 5};


    public Move chooseMove(Color[][] board, Color me) {
        Integer[] MiniMaxResult = AlphaBetaMaxPlayer(board, Integer.MIN_VALUE, Integer.MAX_VALUE, MAXDEPTH, me);
        return new Move(MiniMaxResult[0],MiniMaxResult[1]);
    }

    public Integer[] AlphaBetaMaxPlayer(Color[][] state, int min, int max, int depth, Color player){

        if(CutOffTest(state, player)){
            Integer[] returned =  {null,null,utilityFunction(state)};
            return returned;
        }
        int[] best = new int[3]; // [x,y,Utility]
        for(){

             if(){

             }else if(){

             }else if(){

             }
        }

        return best;
    }

    public Integer[] AlphaBetaMinPlayer(Color[][] state, int min, int max,int depth, Color player){

        Integer[] best = new Integer[3]; // [x,y,Utility]

        if(CutOffTest(state, player)){


            return ()
        }
        for(){
            if(){

            }

        }

        return best;
    }

    public boolean CutOffTest(Color[][] state, Color player){  //terminal or max depth

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


    public int utilityFunction(Color[][] state){

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