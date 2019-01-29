package com.skylynx13.transpath.store;

import com.skylynx13.transpath.utils.ProgressData;

import javax.swing.*;
import java.util.List;

public class StoreCombiner extends SwingWorker<StringBuilder, ProgressData> {
    private boolean updateList;
    private final static String REGEX_STORE_PATH = "A\\d{4}B\\d{4}(-\\d{1,4})?(,(A\\d{4})?B\\d{4}(-\\d{1,4})?)*";

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
