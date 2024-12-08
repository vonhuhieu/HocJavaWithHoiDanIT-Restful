package com.example.demo.Domain.DTO;

import org.springframework.stereotype.Component;

@Component
public class ResultPaginationDTO {
    private Meta meta;
    private Object result;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
