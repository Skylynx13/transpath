package com.skylynx13.transpath.store;

import com.skylynx13.transpath.utils.ProgressData;

import javax.swing.*;
import java.util.List;

public class StoreCombiner extends SwingWorker<StringBuilder, ProgressData> {
    private boolean updateList;

    public StoreCombiner(boolean updateList) {
        this.updateList = updateList;
    }

    @Override
    protected StringBuilder doInBackground() throws Exception {
        return null;
    }

    @Override
    protected void process(List<ProgressData> progressData) {

    }

    @Override
    protected void done() {

    }

    public boolean isUpdateList() {
        return updateList;
    }

    public void setUpdateList(boolean updateList) {
        this.updateList = updateList;
    }
}
