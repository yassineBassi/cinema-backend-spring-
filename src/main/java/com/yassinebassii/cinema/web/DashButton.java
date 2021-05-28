package com.yassinebassii.cinema.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data @ToString @AllArgsConstructor
public class DashButton {
    private String name;
    private String cssClass;
    private String icon;
    private String link;
}
