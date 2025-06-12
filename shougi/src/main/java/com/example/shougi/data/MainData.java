package com.example.shougi.data;

import java.util.ArrayList;
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
                        empty.setId(-1);
                        empty.setDisp("");
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
        p1M = new ArrayList<>();
        p2M = new ArrayList<>();
        String hands = parts[2];
        if (!hands.equals("-")) {
            int i = 0;
            while (i < hands.length()) {
                int count = 1;
                if (Character.isDigit(hands.charAt(i))) {
                    int start = i;
                    while (i < hands.length() && Character.isDigit(hands.charAt(i))) i++;
                    count = Integer.parseInt(hands.substring(start, i));
                }
                char ch = hands.charAt(i);
                String code = String.valueOf(ch);
                Koma koma = new Koma();
                koma.setCode(code);
                koma.code2Values(p1S);
                int kNo = koma.getKNo();
                for (int j = 0; j < count; j++) {
                    if (koma.getUser() == (p1S ? 1 : 2)) {
                        p1M.add(kNo);
                    } else {
                        p2M.add(kNo);
                    }
                }
                i++;
            }
        }
        // --- 手数 ---
        tesu = Integer.parseInt(parts[3]);
    }    

}
