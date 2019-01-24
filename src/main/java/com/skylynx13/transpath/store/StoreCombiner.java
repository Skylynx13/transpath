package com.skylynx13.transpath.store;

import com.skylynx13.transpath.utils.ProgressData;

import javax.swing.*;

public class StoreCombiner extends SwingWorker<StringBuilder, ProgressData> {
    private boolean update;

    public StoreCombiner(boolean update) {
        this.update = update;
    }

    @Override
    protected StringBuilder doInBackground() throws Exception {
        return null;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }
}
