package com.example.comicbook.models;

import java.util.List;

public class ResultModel<T> {

    private T results;

    public ResultModel(T results) {
        this.results = results;
    }

    public T getResults() {
        return results;
    }
}
