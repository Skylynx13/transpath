package com.skylynx13.transpath.utils;

public class ProgressParam {
    long sizeTotal;
    long countTotal;
    long timeStart;
    long sizeNow;
    long countNow;

    public ProgressParam(){

    }

    ProgressParam(long tSize, long tCount) {
        reset(tSize, tCount);
    }

    public void reset(long tSize, long tCount) {
        sizeTotal = tSize;
        countTotal = tCount;
        sizeNow = 0;
        countNow = 0;
        timeStart = System.currentTimeMillis();
    }

    long getCountTotal() {
        return countTotal;
    }

    long getCountNow() {
        return countNow;
    }

    public void addSize(long nSize) {
        sizeNow += nSize;
    }

    public void incCount() {
        countNow++;
    }

    public int calcProgressSize() {
        return (int)(100 * sizeNow / sizeTotal);
    }

    int calcProgressCount() {
        return (int)(100 * countNow / countTotal);
    }

    String reportOfSize() {
        return "" + sizeNow + " of " + sizeTotal + " bytes";
    }

    public String reportOfCount() {
        return "" + countNow + " of " + countTotal + " files";
    }

    long calcTimeLeftBySize() {
        if (sizeNow == 0) {
            return 0;
        }
        long timeNow = System.currentTimeMillis();
        long timeLeft = (timeNow - timeStart) * sizeTotal / sizeNow - timeNow + timeStart;
        return timeLeft / 1000;
    }

    public String reportTimeLeftBySize() {
        if (sizeNow == 0) {
            return "Estimating time left...";
        }
        return "" + calcTimeLeftBySize() + " seconds left.";
    }

    long calcTimeLeftByCount() {
        long timeNow = System.currentTimeMillis();
        long timeLeft = 0;
        if (countNow != 0) {
            timeLeft = (timeNow - timeStart) * countTotal / countNow - timeNow + timeStart;
        }
        return timeLeft / 1000;
    }
}
