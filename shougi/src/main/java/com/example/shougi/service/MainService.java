package com.example.shougi.service;

import java.util.List;
import java.util.Map;

import com.example.shougi.data.MainData;
import com.example.shougi.model.BdForm;
import com.example.shougi.model.KifuForm;

public interface MainService {
    // Kif
    public KifuForm setDummyKifu(MainData mainData);
    // retunbykihuList
    public void setKifu2Form(MainData mainData,KifuForm kifuForm);

    // game.test
    public void setDummyBdForm(BdForm bdForm);
    public BdForm setDummyBdForm1(MainData mainData);
    // game
    public BdForm convertToBdForm(MainData mainData);
    // game AI
    // 動ける場所を取得
    public Map<String, List<String>>getLegalMoves(MainData data);
    
}
