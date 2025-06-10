package com.example.shougi.service;

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
    
}
