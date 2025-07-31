package com.example.shougi.service;

import java.util.List;
import java.util.Map;

import com.example.shougi.data.MainData;
import com.example.shougi.model.BdForm;
import com.example.shougi.model.KifuForm;
import com.example.shougi.model.NewgameModel;

public interface MainService {
    // Kif
    public KifuForm setDummyKifu(MainData mainData);
    // retunbykihuList
    public void setKifu2Form(MainData mainData,KifuForm kifuForm);
    // game
    public BdForm setNewGame(NewgameModel config,MainData mainData);
    public BdForm convertToBdForm(MainData mainData);
    // game AI
    public BdForm selectKoma(BdForm bdForm,MainData mainData);
    public List<List<Integer>> getMoveList(int selectedRow, int selectedCol, MainData data);
    // 動ける場所を取得
    public Map<String, List<String>>getLegalMoves(MainData data);
    public List<List<Integer>> getDropList(int selectedMochigomaKNo, MainData data);
    // 駒を動かす
    public MainData moveKoma(BdForm bdForm,MainData mainData);
}
