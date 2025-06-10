package com.example.shougi.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.shougi.data.Kifu;
import com.example.shougi.data.MainData;
import com.example.shougi.model.BdForm;
import com.example.shougi.model.KifuForm;

@Service
public class MainServoceImpl implements MainService{
    // Kif
    @Override
    public KifuForm setDummyKifu(MainData mainData){
        // Kifuのダミーデータ入れる
        Kifu dummy1=new Kifu();
        dummy1.setId(0);
        dummy1.setP1("p1");
        dummy1.setP2("p2");
        LocalDateTime now=LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        dummy1.setTime(now.format(formatter));
        dummy1.setWin(1);
        dummy1.setBookmark(false);
        Kifu dummy2=new Kifu();
        dummy2.setId(1);
        dummy2.setP1("p3");
        dummy2.setP2("p2");
        dummy2.setTime(now.plusDays(1).plusHours(11).plusMinutes(30).plusSeconds(-35).format(formatter));
        dummy2.setWin(1);
        dummy2.setBookmark(true);

        List<Kifu>kifuList=new ArrayList<>();
        kifuList.add(dummy1);
        kifuList.add(dummy2);
        mainData.setKifuList(kifuList);
        KifuForm form=new KifuForm();
        form.setKifuList(kifuList);
        return form;
    }
    @Override
    // retunbykihuList
    public void setKifu2Form(MainData mainData,KifuForm kifuForm){
        List<Kifu>form=kifuForm.getKifuList();
        List<Kifu>data=mainData.getKifuList();
        List<Kifu>list=new ArrayList<>();
        for(int i=0;i<form.size();i++){
            Kifu kf=data.get(i);
            kf.setBookmark(form.get(i).isBookmark());
            list.add(kf);
        }
        mainData.setKifuList(list);
    }

    @Override
        // game.test
    public void setDummyBdForm(BdForm bdForm){
        List<Integer>p1M=new ArrayList<>();
        List<Integer>p2M=new ArrayList<>();
        int[]samplearray={0,1,2,3,4,5,6};
        for(int val:samplearray){
            p1M.add(val);
            p2M.add(10-val);
        }
        bdForm.setP1M(p1M);
        bdForm.setP2M(p2M);

        bdForm.setP1Name("p1");
        bdForm.setP2Name("p2");
    }
}
