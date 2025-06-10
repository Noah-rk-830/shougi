package com.example.shougi.data;

import lombok.Data;

@Data
public class Kifu {
    private int id;
    private String p1;
    private String p2;
    private String time;
    private int win;
    private boolean bookmark;
}
