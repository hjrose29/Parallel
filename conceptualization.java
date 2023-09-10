public class conceptualization {
 
    public static void main(String[] args){
        String[][] floor = createFloor(7, 7);
        printMatrix(floor);
    }

    public static String[][] createFloor(int x, int y){
        if(x <= 0 || y <= 0){
            return null;
        }
        String[][] floor = new String[x][y];
        for(int i = 0; i < floor.length; i++){
            for(int j = 0; j < floor[0].length; j++){
                floor[i][j] = "□";
            }
        }
        return floor;
    }

    public static void printMatrix(Object[][] in){
        for(int i = 0; i < in.length; i++){
            for(int j = 0; j < in[0].length; j++){
                System.out.print(in[i][j]);
            }
            if(i < in.length - 1){
                System.out.print("\n");
            }
        }


    }

}
