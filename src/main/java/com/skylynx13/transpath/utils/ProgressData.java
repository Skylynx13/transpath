package com.skylynx13.transpath.utils;

public class ProgressData {
    private int progress;
    private String line;

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public ProgressData(int pProgress, String pLine) {
        progress = pProgress;
        line = pLine;
    }
}