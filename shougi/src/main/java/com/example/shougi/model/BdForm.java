package com.example.shougi.model;

import java.util.ArrayList;
import java.util.List;

import com.example.shougi.data.Koma;

import lombok.Data;

@Data
public class BdForm {
    private String p1Name;
    private String p2Name;
    private List<Integer>p1M;
    private List<Integer>p2M;
    private boolean reverse;//p1が先手=false
    private List<List<Koma>>bd;//Komaの二重リスト
    private List<String>kansuji=setKansuji();//漢数字

    private Integer selectedRow;            // 盤面クリック時の行（0〜8）
    private Integer selectedCol;            // 盤面クリック時の列（0〜8）
    private Integer selectedMochigomaKNo;   // p1の持ち駒クリック時の駒番号（0〜7）

    public List<String> setKansuji(){
        ArrayList<String>kansuji=new ArrayList<>();
        String[]vals={"一","二","三","四","五","六","七","八","九"};
        for(String val:vals){
            kansuji.add(val);
        }
        return kansuji;
    }
}
