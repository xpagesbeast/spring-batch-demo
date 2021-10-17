package com.collabsphere.springbatchdemo.model.ui;

public class RunJob {
    private String unid; //unique job id
    private String batchJobName; //the batch job to run

    public String getUnid() {
        return unid;
    }

    public void setUnid(String unid) {
        this.unid = unid;
    }

    public String getBatchJobName() {
        return batchJobName;
    }

    public void setBatchJobName(String batchJobName) {
        this.batchJobName = batchJobName;
    }
}
