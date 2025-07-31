package com.example.shougi.data;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Kifu {
    private int id;
    private String p1;
    private String p2;
    private String time;
    private int win;
    private boolean bookmark;

    private List<String>kifuData=new ArrayList<>();


    public Boolean addKifuData(String sfen){
        // 半角スペースで分割
        String[] parts = sfen.split(" ");
        // 分割された文字列の数が4つ未満の場合は、不正なSFEN形式としてnullを返すか、例外をスローするなど適切に処理する
        if (parts.length < 3) {
            // 例: 不正な形式
            System.out.println("不正な形式のsfen");
            return null;//エラー
        }
        // 盤面情報と持ち駒を結合して返す
        // 例: "lnsgk2n+B/6g2/p1pppp1pp/1r4p2/9/2P6/PP1PPPP1P/2G6/LNS1KGSN1 RBS2L2P"
        sfen=parts[0]+" "+parts[2];
        int cnt=1;
        for(String data:this.kifuData){
            if(sfen.equals(data)){
                cnt++;
            }
            if(cnt>=4){
                return true;
            }
        }
        return false;
    }
}
