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
    private List<Koma>p1Mochi;
    private List<Integer>p2M;
    private List<Koma>p2Mochi;
    private boolean reverse;//p1が先手=false
    private List<List<Koma>>bd;//Komaの二重リスト
    private List<String>kansuji;//漢数字

    private Integer selectedRow;            // 盤面クリック時の行（0〜8）
    private Integer selectedCol;            // 盤面クリック時の列（0〜8）
    private Integer selectedMochigomaKNo;   // p1の持ち駒クリック時の駒番号（0〜7）
    private List<List<Integer>>moveList;
    private Integer moveToRow;
    private Integer moveToCol;

    /**
     * 指定された行と列のペアが moveList に含まれているかをチェックするヘルパーメソッド
     * @param row チェック対象の行インデックス
     * @param col チェック対象の列インデックス
     * @return 含まれていれば true、そうでなければ false
     */
    public boolean isInMoveList(int row, int col) {
        if (this.moveList == null || this.moveList.isEmpty()) {
            return false;
        }
        for (List<Integer> move : this.moveList) {
            // move が [row, col] の形式であると仮定
            if (move != null && move.size() >= 2 && move.get(0) == row && move.get(1) == col) {
                return true;
            }
        }
        return false;
    }

    public void pM2pMochi(){
        p1Mochi=new ArrayList<>();
        p2Mochi=new ArrayList<>();
        for(int i=0;i<7;i++){
            for(int j=0;j<p1M.get(i);j++){
                Koma km=new Koma();
                km.setKNo(i);
                km.setNari(false);
                km.setImgPath();
                km.setUser(1);
                p1Mochi.add(km);
            }
            for(int j=0;j<p2M.get(i);j++){
                Koma km=new Koma();
                km.setKNo(i);
                km.setNari(false);
                km.setImgPath();
                km.setUser(2);
                p2Mochi.add(km);
            }
        }
    }
    public void setKansuji(){
        ArrayList<String>kansuji=new ArrayList<>();
        String[]vals={"一","二","三","四","五","六","七","八","九"};
        String[]valsR={"九","八","七","六","五","四","三","二","一"};
        if(reverse){
            for(String val:valsR){
                kansuji.add(val);
            }
        }else{
            for(String val:vals){
                kansuji.add(val);
            }
        }
        this.kansuji=kansuji;
    }

    // game1から呼び出す:成
    public boolean getCanNari(int row,int col){
        if(selectedRow==null){//null=持駒からなら成れない
            return false;
        }else if(reverse){
            Koma km=bd.get(selectedRow).get(selectedCol);
            if(km.isNari()){//既に成っているなら成れない
                return false;
            }else if(km.getKNo()==4||km.getKNo()==7){//成れない駒
                return false;
            }
            if(selectedRow>=6){
                return true;
            }else if(row>=6){
                return true;
            }
        }else{
            Koma km=bd.get(selectedRow).get(selectedCol);
            if(km.isNari()){//既に成っているなら成れない
                return false;
            }else if(km.getKNo()==4||km.getKNo()==7){//成れない駒
                return false;
            }
            if(selectedRow<=2){
                return true;
            }else if(row<=2){
                return true;
            }
        }
        return false;
    }
}
