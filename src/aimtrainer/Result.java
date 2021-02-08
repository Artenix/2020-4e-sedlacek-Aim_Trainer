/*

 */
package aimtrainer;

/**
 *
 * @author kubaj
 */
public class Result implements Comparable<Result>{
    private int hit;
    private int missed;
    private double accuracy;
    private int score;
    private int highestCombo;
    private long timeAchieved;

    public Result(int hit, int missed, int score, int highestCombo, long timeAchieved) {
        this.hit = hit;
        this.missed = missed;
        this.accuracy = Math.round(((double) hit) / (hit + missed) * 10000) / 10000.0;
        this.score = score;
        this.highestCombo = highestCombo;
        this.timeAchieved = timeAchieved;
    }
    
    @Override
    public int compareTo(Result other) {
        return Integer.compare(this.score, other.score);
    }

    @Override
    public String toString() {
        return hit + "," + missed + "," + score + "," + highestCombo + "," + timeAchieved;
    }

    public int getHit() {
        return hit;
    }

    public int getMissed() {
        return missed;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public int getScore() {
        return score;
    }

    public int getHighestCombo() {
        return highestCombo;
    }

    public long getTimeAchieved() {
        return timeAchieved;
    }

    public void setTimeAchieved(long timeAchieved) {
        this.timeAchieved = timeAchieved;
    }
    
    
}
