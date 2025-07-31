package com.example.shougi.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
    private boolean ai;//対AI=true
    private Kifu kifu;

    // 駒コードの順番（歩, 香, 桂, 銀, 金, 角, 飛）
    private final List<String> komaOrder = List.of("P", "L", "N", "S", "G", "B", "R");


    public void setTesuD(){
        if(p1S){
            tesu=1;
            teban=true;
        }else{
            tesu=2;
            teban=true;
        }
    }
    public void setTeban(){
        if(this.p1S){
            this.teban=(this.tesu%2==1);
        }else{
            this.teban=(this.tesu%2==0);
        }
    }
    public void setNextTurnSub(){
        this.tesu++;
        this.teban=!this.teban;
    }

    public List<List<Koma>> getReversedBoard() {
        if(bd == null){return null;}
        List<List<Koma>> reversed = new ArrayList<>();
        for (int i = bd.size() - 1; i >= 0; i--) {
            List<Koma> originalRow = bd.get(i);
            List<Koma> newRow = new ArrayList<>(originalRow);
            Collections.reverse(newRow);
            reversed.add(newRow);
        }
        return reversed;
    }    

    public String bd2Sfen() {
        StringBuilder sfen = new StringBuilder();
        // --- 盤面 ---
        for (int row = 0; row < 9; row++) {
            int empty = 0;
            for (int col = 0; col < 9; col++) {
                Koma koma = bd.get(row).get(col);
                if (koma.getId() == -1) {
                    empty++;
                } else {
                    if (empty > 0) {
                        sfen.append(empty);
                        empty = 0;
                    }
                    koma.values2Code(p1S); // kNo, user, nari から code を生成
                    sfen.append(koma.getCode());
                }
            }
            if (empty > 0) {
                sfen.append(empty);
            }
            if (row < 8) sfen.append("/");
        }
        // --- 手番 ---
        sfen.append(" ").append(teban ? "b" : "w");
        // --- 持ち駒 ---
        Map<String, Integer> handMap = new LinkedHashMap<>();
        for (int kNo : p1M) {
            Koma koma = new Koma();
            koma.setKNo(kNo);
            koma.setUser(p1S ? 1 : 2);
            koma.setNari(false);
            koma.values2Code(p1S);
            String code = koma.getCode();
            handMap.put(code, handMap.getOrDefault(code, 0) + 1);
        }
        for (int kNo : p2M) {
            Koma koma = new Koma();
            koma.setKNo(kNo);
            koma.setUser(p1S ? 2 : 1);
            koma.setNari(false);
            koma.values2Code(p1S);
            String code = koma.getCode();
            handMap.put(code, handMap.getOrDefault(code, 0) + 1);
        }
        if (handMap.isEmpty()) {
            sfen.append(" -");
        } else {
            sfen.append(" ");
            for (Map.Entry<String, Integer> entry : handMap.entrySet()) {
                if (entry.getValue() > 1) sfen.append(entry.getValue());
                sfen.append(entry.getKey());
            }
        }
        // --- 手数 ---
        sfen.append(" ").append(tesu);
        return sfen.toString();
    }    

    public void sfen2Bd(String sfen) {
        String[] parts = sfen.trim().split(" ");
        if (parts.length != 4) throw new IllegalArgumentException("SFEN形式が不正");
        // --- 盤面構築 ---
        bd = new ArrayList<>();
        String[] rows = parts[0].split("/");
        int rowIdx = 0; // 行インデックス（0〜8）
        for (String rowStr : rows) {
            List<Koma> row = new ArrayList<>();
            int colIdx = 0; // 列インデックス（0〜8）
            for (int i = 0; i < rowStr.length(); i++) {
                char ch = rowStr.charAt(i);
                if (Character.isDigit(ch)) {
                    int count = ch - '0';
                    for (int j = 0; j < count; j++) {
                        Koma empty = new Koma();
                        empty.setEmpty();
                        row.add(empty);
                        colIdx++;
                    }
                } else {
                    boolean promoted = false;
                    if (ch == '+') {
                        promoted = true;
                        i++;
                        ch = rowStr.charAt(i);
                    }
                    String code = (promoted ? "+" : "") + ch;
                    Koma koma = new Koma();
                    koma.setCode(code);
                    koma.code2Values(p1S); // code から kNo, nari, user を設定
    
                    // --- 位置情報セット（dlshougi形式: "1g"など） ---
                    int file = 9 - colIdx; // 筋（左から右が9〜1）
                    char rank = (char) ('a' + rowIdx); // 段（上から下がa〜i）
                    String pos = file + String.valueOf(rank);
                    koma.setPos(pos);
    
                    koma.values2Disp(); // 表示用文字列生成
                    row.add(koma);
                    colIdx++;
                }
            }
            bd.add(row);
            rowIdx++;
        }
        // --- 手番 ---
        teban = parts[1].equals("b");
        // --- 持ち駒 ---
        String hands = parts[2];
        // 各プレイヤーの持ち駒を初期化（全て0で始まる）
        p1M = new ArrayList<>(Collections.nCopies(komaOrder.size(), 0));
        p2M = new ArrayList<>(Collections.nCopies(komaOrder.size(), 0));

        if (!hands.equals("-")) {
            int i = 0;
            while (i < hands.length()) {
                int count = 1;

                // 数字（個数）がある場合、読み取る
                if (Character.isDigit(hands.charAt(i))) {
                    int start = i;
                    while (i < hands.length() && Character.isDigit(hands.charAt(i))) i++;
                    count = Integer.parseInt(hands.substring(start, i));
                }

                if (i >= hands.length()) break; // 安全対策
                char ch = hands.charAt(i);
                boolean isP1 = Character.isUpperCase(ch); // 大文字 → 先手
                String code = String.valueOf(Character.toUpperCase(ch)); // 駒コードを大文字化

                int kNo = komaOrder.indexOf(code);
                if (kNo == -1) {
                    throw new IllegalArgumentException("未知の駒コード: " + code);
                }

                // 持ち駒を加算
                if (isP1) {
                    p1M.set(kNo, p1M.get(kNo) + count);
                } else {
                    p2M.set(kNo, p2M.get(kNo) + count);
                }

                i++;
            }
        }
        // --- 手数 ---
        tesu = Integer.parseInt(parts[3]);

        // debug
        System.out.println(hands);
        System.out.print("p1m:");
        for(int cnt:p1M){
            System.out.print(cnt+",");
        }
        System.out.println();
        System.out.print("p2m:");
        for(int cnt:p2M){
            System.out.print(cnt+",");
        }
        System.out.println();
    }

}
