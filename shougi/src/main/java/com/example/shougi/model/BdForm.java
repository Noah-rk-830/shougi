package com.example.shougi.model;

import java.util.List;

import lombok.Data;

@Data
public class BdForm {
    private String p1Name;
    private String p2Name;
    private List<Integer>p1M;
    private List<Integer>p2M;
}
