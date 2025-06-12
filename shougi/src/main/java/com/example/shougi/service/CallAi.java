package com.example.shougi.service;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import com.example.shougi.data.Koma;
import com.example.shougi.data.MainData;

import java.util.*;
import org.springframework.core.ParameterizedTypeReference;

public class CallAi {
    /**
     * MainData から SFEN 形式の文字列を生成する
     */
    public String toSfen(MainData data) {
        StringBuilder boardPart = new StringBuilder();

        List<List<Koma>> bd = data.getBd();

        for (int row = 0; row < 9; row++) {
            int emptyCount = 0;
            for (int col = 0; col < 9; col++) {
                Koma koma = bd.get(row).get(col);

                if (koma.getKNo() == -1) { // 空白
                    emptyCount++;
                } else {
                    if (emptyCount > 0) {
                        boardPart.append(emptyCount);
                        emptyCount = 0;
                    }
                    String code =koma.getCode();
                    boardPart.append(code);
                }
            }
            if (emptyCount > 0) {
                boardPart.append(emptyCount);
            }
            if (row < 8) {
                boardPart.append("/");
            }
        }

        // 手番
        String turn = "b";
        if(data.isTeban()){
            turn="b";
        }else{
            turn="w";
        }

        // 持ち駒
        String hands = getHands(data.getP2M(), false) + getHands(data.getP1M(), true);
        if (hands.isEmpty()) hands = "-";

        // 手数（省略もできるがここでは 1 固定）
        return boardPart + " " + turn + " " + hands + " "+data.getTesu();
    }

    /**
     * 持ち駒部分の SFEN 表現を生成する（user==0:先手, user==1:後手）
     */
    private static String getHands(List<Integer> pieces, boolean isSente) {
        StringBuilder sb = new StringBuilder();
        // 順序：P, L, N, S, G, B, R
        String[] codes = {"P", "L", "N", "S", "G", "B", "R"};

        for (int i = 0; i < pieces.size(); i++) {
            int count = pieces.get(i);
            if (count > 0) {
                String code = codes[i];
                if (!isSente) code = code.toLowerCase();
                if (count > 1) sb.append(count);
                sb.append(code);
            }
        }
        return sb.toString();
    }

    public Map<String, List<String>> getLegalMoves(String sfen){
        String url = "http://localhost:8000/legal_moves";
        RestTemplate restTemplate = new RestTemplate();
    
        // JSON送信内容
        Map<String, String> requestBody = Map.of("sfen", sfen);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);
    
        // 型情報を明示
        ResponseEntity<Map<String, List<String>>> response = restTemplate.exchange(
            url,
            HttpMethod.POST,
            request,
            new ParameterizedTypeReference<>() {}
        );
    
        return response.getBody();
    }
}
