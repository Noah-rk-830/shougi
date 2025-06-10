package com.example.shougi.data;

import java.util.List;

import lombok.Data;

@Data
public class MainData {
    private List<Kifu>kifuList;

    // game
    private String p1Name;
    private String p2Name;
    private List<Integer>p1M;
    private List<Integer>p2M;
    private boolean p1S;//p1が先手か
    private List<List<Koma>>bd;//Komaの二重リスト
    private boolean teban;//先手番:true
    private int tesu=1;//手数
}
