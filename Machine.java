import java.util.ArrayList;

public class Machine extends Thread{
    String type;
    int base;
    int likesMultiplier, dislikesMultiplier;
    String likes, dislikes;
    int x, y;
    
    Machine(String type, Machine[][] floor, int x, int y){
        this.x = x; this.y = y; this.type = type;
        floor[x][y] = this;
        if(type.equals("0")){
            this.base = 0;
            this.likes = "0";
            this.dislikes = "0";
            this.likesMultiplier = 0;
            this.dislikesMultiplier = 0;
        }
        else if(type.equals("x")){
            this.base = 3;
            this.likes = "y";
            this.dislikes = "z";
            this.likesMultiplier = 2;
            this.dislikesMultiplier = 2;
        }
        
        else if(type.equals("y")){
            this.base = 2;
            this.likes = "z";
            this.dislikes = "x";
            this.likesMultiplier = 3;
            this.dislikesMultiplier = 3;
        }
        else if(type.equals("z")){
            this.base = 1;
            this.likes = "x";
            this.dislikes = "z";
            this.likesMultiplier = 4;
            this.dislikesMultiplier = 1;
        }
    }
    public int getBase(){
        return base;
    }
    public String getType(){
        return type;
    }
    public String getLikes(){
        return likes;
    }
    public String getDislikes(){
        return dislikes;
    }
    public int getLikesMultiplier(){
        return likesMultiplier;
    }
    public int getDislikesMultiplier(){
        return dislikesMultiplier;
    }
    public Machine[] getAdjacencyList(Machine[][] floor){
        ArrayList<Machine> adjacentAL = new ArrayList<Machine>();
        

        Machine temp;
        if(x+1 < floor[0].length){
            temp = floor[this.x+1][this.y];
            if(!temp.type.equals("0")) adjacentAL.add(temp);
        }

        if(y+1 < floor.length){ 
            temp = floor[this.x][this.y+1];
            if(!temp.type.equals("0")) adjacentAL.add(temp);
        }

        if(y+1 < floor.length && x+1 < floor[0].length){
            temp = floor[this.x+1][this.y+1];
            if(!temp.type.equals("0")) adjacentAL.add(temp);
        }

        if(x-1 >= 0){
            temp = floor[this.x-1][this.y];
            if(!temp.type.equals("0")) adjacentAL.add(temp);
        }
        
        if(y-1 >= 0){
            temp = floor[this.x][this.y-1];
            if(!temp.type.equals("0")) adjacentAL.add(temp);
        }
        
        if(y-1 >= 0 && x-1>=0){
            temp = floor[this.x - 1][this.y - 1];
            if(!temp.type.equals("0")) adjacentAL.add(temp);
        }

        if(x-1 >= 0 && y+1 < floor.length){
            temp = floor[this.x - 1][this.y + 1];
            if(!temp.type.equals("0")) adjacentAL.add(temp);
        }

        if(y-1 >= 0 && x+1 < floor[0].length){
            temp = floor[this.x + 1][this.y - 1];
            if(!temp.type.equals("0")) adjacentAL.add(temp);
        }

        Machine[] out = new Machine[adjacentAL.size()];
        for(int i = 0; i < adjacentAL.size(); i++){
            out[i] = adjacentAL.get(i);
        }
        return out;
    }

    public int getMachineAffinity(Machine[] adjList){
        int affinity = 0;
        for(int i = 0; i < adjList.length;i++){
            affinity += base;
            if(this.likes.equals(adjList[i].type)){
                affinity += this.likesMultiplier;
            }
            else if(this.dislikes.equals(adjList[i].type)){
                affinity -= this.dislikesMultiplier;
            }
        }
        return affinity;
    }
}
