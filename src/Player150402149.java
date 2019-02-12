import java.awt.*;

public class Player150402149 {

    public Move chooseMove(Color[][] board, Color me) {
        AlphaBetaMaxPlayer(board, Integer.MIN_VALUE, Integer.MAX_VALUE);
        return new Move(1,1);
    } // chooseMove(

    public int[] AlphaBetaMaxPlayer(Color[][] state, int min, int max){

        int[] best = new int[3]; // [x,y,Utility]

        if(){

        }
        for(){

        }
    }

    public int[] AlphaBetaMinPlayer(Color[][] state, int min, int max){

        int[] best = new int[3]; // [x,y,Utility]

        if(terminatingTest(state, best)){


            return ()
        }
        for(){
            if(){

            }

        }
    }

    public boolean terminatingTest(Color[][] state, int[] best){

    }
}