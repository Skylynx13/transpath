package com.skylynx13.transpath.utils;

public class ProgressParam {
    private long sizeTotal;
    private long countTotal;
    private long timeStart;
    private long sizeNow;
    private long countNow;

    public ProgressParam(){
    }

    public void reset(long tSize, long tCount) {
        sizeTotal = tSize;
        countTotal = tCount;
        sizeNow = 0;
        countNow = 0;
        timeStart = System.currentTimeMillis();
    }

    public void update(long nSize) {
        addSize(nSize);
        incCount();
    }

    private void addSize(long nSize) {
        sizeNow += nSize;
    }

    private void incCount() {
        countNow++;
    }

    public int calcProgressSize() {
        return (int)(100 * sizeNow / sizeTotal);
    }

    int calcProgressCount() {
        return (int)(100 * countNow / countTotal);
    }

    String reportOfSize() {
        return String.format("%,d of %,d bytes.", sizeNow, sizeTotal);
    }

    public String reportOfCount() {
        return String.format("%,d of %,d files.", countNow, countTotal);
    }

    private long calcTimeLeftBySize() {
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
        return String.format("%d seconds left. ", calcTimeLeftBySize());
    }

    long calcTimeLeftByCount() {
        if (countNow == 0) {
            return 0;
        }
        long timeNow = System.currentTimeMillis();
        long timeLeft = (timeNow - timeStart) * countTotal / countNow - timeNow + timeStart;
        return timeLeft / 1000;
    }
}
