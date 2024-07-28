package com.skylynx13.transpath.utils;

/**
 * @author skylynx
 */
public class ProgressTracer {
    private long sizeTotal;
    private long countTotal;
    private long sizeNow;
    private long countNow;
    private long timeStart;
    private String reportHeader;

    public ProgressTracer(){
    }

    public void reset(long tSize, long tCount, String header) {
        sizeTotal = tSize;
        countTotal = tCount;
        sizeNow = 0;
        countNow = 0;
        timeStart = System.currentTimeMillis();
        reportHeader = header;
    }

    public void update(long nSize) {
        sizeNow += nSize;
        countNow ++;
    }

    public void updateCurrent(long cSize, long cCount) {
        sizeNow = cSize;
        countNow = cCount;
    }

    private int calcSizePercentage() {
        return (int)(100 * sizeNow / sizeTotal);
    }

    private long calcTimeLeftBySize() {
        if (sizeNow == 0) {
            return 0;
        }
        long timeNow = System.currentTimeMillis();
        long timeLeft = (timeNow - timeStart) * sizeTotal / sizeNow - timeNow + timeStart;
        return timeLeft / 1000;
    }

    public ProgressReport report() {
        return new ProgressReport(calcSizePercentage(), buildReportLine());
    }

    private String buildReportLine() {
        StringBuilder line = new StringBuilder(reportHeader);
        line.append(String.format(": Count %,d/%,d. ", countNow, countTotal));
        if (sizeNow != 0) {
            line.append(String.format("    >>>>>>>>     %,d seconds left. ", calcTimeLeftBySize()));
        }
        return line.toString();
    }
}
