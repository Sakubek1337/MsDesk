package sample;

public class Levels extends Postgre{
    public Integer defLevel(int score){
        int lvl = 1;
        lvl += score / 2000;
        return lvl;
    }

    public Double per(int score){
        double n;
        score -= (score / 2000) * 2000;
        score = score * 100 / 2000;
        n = (double)score / 100;

        return n;
    }
}
