package com.example.Tucasdesk.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChamadoUpdateParcialRequest {

    private Integer statusId;
    private Integer prioridadeId;

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public Integer getPrioridadeId() {
        return prioridadeId;
    }

    public void setPrioridadeId(Integer prioridadeId) {
        this.prioridadeId = prioridadeId;
    }
}
