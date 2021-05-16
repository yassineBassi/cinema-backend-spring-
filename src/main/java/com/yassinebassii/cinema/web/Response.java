package com.yassinebassii.cinema.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class Response implements Serializable {
    private int status;
    private String message;
    private Object data;
}
