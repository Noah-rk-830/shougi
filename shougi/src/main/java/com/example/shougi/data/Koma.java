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
    private String code;//sfen形式用 P,+r

    public void setDisp2Values(){
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
        }
    }
    public void setValues2Code(boolean p1S){//p1が先手か
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
                        this.user=2;
                    }else{
                        this.user=1;
                    }
                }else{
                    if(p1S){
                        this.user=1;
                    }else{
                        this.user=2;
                    }
                }
            }
        }
    }
}
