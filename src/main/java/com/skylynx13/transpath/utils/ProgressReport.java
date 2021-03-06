package com.skylynx13.transpath.utils;

/**
 * @author skylynx
 */
public class ProgressReport {
    private final int progress;
    private final String reportLine;

    ProgressReport(int progress, String reportLine) {
        this.progress = progress;
        this.reportLine = reportLine;
    }

    public int getProgress() {
        return progress;
    }

    public String getReportLine() {
        return reportLine;
    }
}
