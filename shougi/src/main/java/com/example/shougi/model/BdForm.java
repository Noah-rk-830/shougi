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
    private boolean promoteMove;

    // どちらが選択されているかを判別するヘルパーメソッド（オプション）
    public boolean isBoardPieceSelected() {
        return selectedRow != null && selectedCol != null;
    }

    public boolean isMochigomaSelected() {
        return selectedMochigomaKNo != null;
    }

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
        }
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
        return false;
    }
    /**
 * 選択した駒が指定した座標に移動する際に、強制的に成る必要があるか判定します。
 * （歩・香車が最奥段に、桂馬が2段目・1段目に移動する場合）
 *
 * @param row 移動先の行
 * @param col 移動先の列
 * @return 強制成りの必要がある場合はtrue、それ以外はfalse
 */
public boolean isForcePromotion(int row, int col) {
    if (selectedRow == null) { // 持駒からは強制成りの判定は不要
        return false;
    }
    Koma km = bd.get(selectedRow).get(selectedCol);
    if (km.isNari()) { // 既に成っている駒は強制成りの対象外
        return false;
    }
    // 後手番の場合
    if (reverse){
        // 歩 (KNo=1), 香車 (KNo=2) が最奥段 (1段目=row=0) に到達する場合
        if ((km.getKNo()==1||km.getKNo()==2)&&row==0){
            return true;
        }
        // 桂馬 (KNo=3) が2段目 (row=1) または1段目 (row=0) に到達する場合
        if (km.getKNo()==3&&(row==1||row==0)){
            return true;
        }
    }
    // 先手番の場合
    else {
        // 歩 (KNo=1), 香車 (KNo=2) が最奥段 (9段目=row=8) に到達する場合
        if ((km.getKNo()==1||km.getKNo()==2)&&row==8){
            return true;
        }
        // 桂馬 (KNo=3) が8段目 (row=7) または9段目 (row=8) に到達する場合
        if (km.getKNo()==3&&(row==7||row==8)){
            return true;
        }
    }
    return false;
}
}
