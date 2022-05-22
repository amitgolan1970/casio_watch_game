package org.golanamit.casiowatchgameretro;

import java.util.Random;

public class Board {
    private static final int[] CYCLES = {30, 40, 50};
    private static int GAME_ITERATIONS = CYCLES[1];
    public static final int ARR_SIZE = 10;
    public static final String BONUS = "~";
    public static final String SPACE = " ";
    private int count = 0;
    private String[] places = new String[ARR_SIZE];
    private Random rnd = new Random();

    private String userNumber = "0";

    private int score = 0;

    public void setEasyLevelIterations()
    {
        GAME_ITERATIONS = CYCLES[GameLevel.EASY.ordinal()];
    }

    public void setMediumLevelIterations() {
        GAME_ITERATIONS = CYCLES[GameLevel.MEDIUM.ordinal()];
    }

    public void setHardLevelIterations() {
        GAME_ITERATIONS = CYCLES[GameLevel.HARD.ordinal()];
    }

    public int getScore() {
        return this.score;
    }
    public void increaseScoreBy(int points) {
        this.score += points;
    }

    public void decreaseScoreBy(int points) {
        this.score -= points;
        if(this.score < 0)
            resetScore();
    }

    public void resetScore() {
        this.score = 0;
    }

    ///////////////////////////////////////////////////////////////////////////////

    public String getUserNum() {
        return this.userNumber;
    }

    public void fillArrElemRnd() {
        count++;
        if(count > GAME_ITERATIONS)
        {   //  failsafe in case "win / loose" evaluations are wrong
            resetArray();
            System.err.println("game over");
            return;
        }
        if(count > (GAME_ITERATIONS - ARR_SIZE)) {
            places[0] = SPACE;
            return;
        }
        int random = rnd.nextInt(10), bonusRandom = rnd.nextInt(10);
        String numStr = String.valueOf((char) (random + 48));
        if(count == 0) {
            places[0] = numStr;
            if (bonusRandom == 5 || bonusRandom == 7)
                places[0] = BONUS;
        } else {
            numStr = places[1];
            while (numStr.equals(places[1])) {
                random = rnd.nextInt(10);
                numStr = String.valueOf((char) (random + 48));
                places[0] = numStr;
                if (bonusRandom == 5 || bonusRandom == 7)
                    places[0] = BONUS;
            }
        }
    }

    public void makeStepForward() {
        for(int i = places.length-1; i>0; i--) {
            places[i] = places[i-1];
        }
    }

    public void resetArray() {
        for(int i = 0; i < places.length; i++) {
            places[i] = SPACE;
        }
    }

    public boolean matchHit(String now) {
        int currIndex = returnLastFilledElemIndex();
        if(now.equals(places[currIndex])) {
            elapseByIndex(currIndex);
            int baseScore = now.equals(BONUS) ? 50 : 20;
            int indScoreCalc = (places.length - currIndex) * 10;
            increaseScoreBy(baseScore + indScoreCalc);
            return true;
        } else {
            decreaseScoreBy(20);
            return false;
        }
    }

    public String returnArrayElementByIndex(int i) {
        return places[i];
    }

    public void printArray() {
        System.out.print("{");
        for(int i = 0; i < places.length; i++) {
            System.out.print(places[i] + SPACE);
        }
        System.out.println("}");
    }

    public boolean didWeLoose() {
        return !places[places.length-1].equals(SPACE);
    }

    public boolean didWeWeen() {
        return count > (GAME_ITERATIONS - ARR_SIZE) && isAllArrayResetted();
    }

    public int returnLastFilledElemIndex() {
        int ind = 0;
        for(int i = 0; i < places.length-1; i++) {
            if (!places[i].equals(SPACE))
                ind = i;
        }
        return ind;
    }

    private void elapseByIndex(int currIndex) {
        places[currIndex] = SPACE;
    }

    private boolean isAllArrayResetted() {
        for(int i = 0; i < places.length; i++) {
            if(!places[i].equals(SPACE))
                return false;
        }
        return true;
    }

    ///////////////////////////////////////////////////////////////////////////////

    public void resetUserNumber() {
        this.userNumber = "0";
    }

    public void printUserNumber() {
        System.out.println("{" + userNumber + "}");
    }

    public void decreaseUserNumber() {
        if (userNumber.equals(BONUS)) {
            userNumber = "9";
            return;
        }
        if(userNumber.equals("0")) {
            userNumber = BONUS;
            return;
        }
        int userNumInt = -1;
        try {
            userNumInt = Integer.parseInt(userNumber);
        } catch (Exception e) {
            System.err.println("bad formation");
            return;
        }
        userNumInt--;
        userNumber = String.valueOf(userNumInt);
    }

    public void increaseUserNumber() {
        if (userNumber.equals(BONUS)) {
            resetUserNumber();
            return;
        }
        int userNumInt = -1;
        try {
            userNumInt = Integer.parseInt(userNumber);
        } catch (Exception e) {
            System.err.println("bad formation, returning");
            return;
        }
        userNumInt++;
        if (userNumInt == 10) {
            userNumber = BONUS;
            return;
        }
        userNumber = String.valueOf(userNumInt);
    }

    public void setGameLevel(GameLevel level) {
        switch (level) {
            case EASY:
                setEasyLevelIterations();
                break;
            case MEDIUM:
                setMediumLevelIterations();
                break;
            case HARD:
                setHardLevelIterations();
                break;
            default:
                System.err.println("Should never reach here");
        }
    }
}
