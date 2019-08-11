package com.shmigel.todo.utils;

import lombok.Data;

@Data
public class Tuple<F, S> {
    private F first;
    private S second;

    public Tuple(F first, S second) {
        this.first = first;
        this.second = second;
    }

    public F _1() {
        return first;
    }

    public S _s() {
        return second;
    }
}
