package com.byrybdyk.lb1.model.util;

public class LabWorkDeleteMessage {
    private Long labWorkId;
    private String type;
    public LabWorkDeleteMessage() {}

    public LabWorkDeleteMessage(Long labWorkId) {
        this.labWorkId = labWorkId;
    }

    public Long getLabWorkId() {
        return labWorkId;
    }

    public void setLabWorkId(Long labWorkId) {
        this.labWorkId = labWorkId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

