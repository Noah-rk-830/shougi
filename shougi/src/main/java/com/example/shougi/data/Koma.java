package com.example.shougi.data;

import lombok.Data;

@Data
public class Koma {
    private int id;
    private int kNo;//駒番号:駒名識別番号 成無視,0~7
    private boolean nari;
    private int user;//どちらの駒か
    private String pos;//AI表記に合わせる
    private String disp;//表記用 △歩,▽成香
    private String imgPath;//imgのpath /image/koma/?
    private String code;//sfen形式用 P,+r

    public void setEmpty(){
        this.setId(-1);
        this.setDisp("");
        this.setCode("");
    }

    public void setImgPath(){
        String[]path1={"hu","kyou","kei","gin","kin","kaku","hisha","gyoku"};
        String[]path2={"tokin","narikyou","narikei","narigin","","ryouma","ryuuou",""};
        String path="/image/koma/";
        if(nari){
            path+=path2[kNo];
        }else{
            path+=path1[kNo];
        }
        this.imgPath=path+".png";
    }

    public void values2Disp(){
        String disp="";
        if(pos.startsWith("hand_")){
            this.disp=disp;
        }else{
            if(user==1){
                disp+="△";
            }else{
                disp+="▽";
            }
            String[]komaName={"歩","香","桂","銀","金","角","飛","玉"};
            String[]narigomaName={"と","成香","成桂","成銀","","馬","龍",""};
            if(nari){
                disp+=narigomaName[kNo];
            }else{
                disp+=komaName[kNo];
            }
            this.disp=disp;
            setImgPath();
        }
    }
    public void code2Values(boolean p1S){//p1が先手か
        String code=this.code;
        String[]codeAB={"p","l","n","s","g","b","r","k"};
        if(code.charAt(0)=='+'){
            this.nari=true;
            code=code.substring(1);
        }else{
            this.nari=false;
        }
        for(int i=0;i<codeAB.length;i++){
            if(code.toLowerCase().equals(codeAB[i])){
                this.kNo=i;
                if(code.equals(codeAB[i])){
                    if(p1S){
                        this.user=1;
                    }else{
                        this.user=2;
                    }
                }else{
                    if(p1S){
                        this.user=2;
                    }else{
                        this.user=1;
                    }
                }
            }
        }
    }
    public void values2Code(boolean p1S){
        String[] codeAB = {"P", "L", "N", "S", "G", "B", "R", "K"};
        String base = codeAB[this.kNo];
        if (!this.isSente(p1S)) {
            base = base.toLowerCase();
        }
        String code=(this.nari ? "+" : "") + base;
        this.code=code;
    }
    public boolean isSente(boolean p1S) {
        return (p1S && user == 1) || (!p1S && user == 2); // 先手かどうかを判定
    }
    public void setUchigoma(int kNo,int row,int col,boolean p1S) {
        this.id=100+kNo; // 仮のID設定
        this.kNo=kNo;
        this.nari=false; // 持ち駒は成っていない状態から打たれる
        this.user=1;     // 持ち駒を打つのは常に先手（プレイヤー1）と仮定

        // posの計算 (rowは1-9, colは'a'-'i'に変換)
        char colChar=(char)('a'+col);
        this.pos=(row+1)+String.valueOf(colChar);

        // その他のメンバを既存のメソッドで設定
        values2Disp(); // dispとimgPathを設定
        values2Code(p1S);
    }
}
