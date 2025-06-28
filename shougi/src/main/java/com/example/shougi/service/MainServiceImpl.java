package com.example.shougi.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.shougi.data.Kifu;
import com.example.shougi.data.MainData;
import com.example.shougi.model.BdForm;
import com.example.shougi.model.KifuForm;
import com.example.shougi.model.NewgameModel;

@Service
public class MainServiceImpl implements MainService{
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
    public BdForm setNewGame(NewgameModel config,MainData mainData){
        BdForm bdForm=new BdForm();
        mainData.setAi(config.getVs()==1);
        String[]sfenList={"lnsgkgsnl/1r5b1/ppppppppp/9/9/9/PPPPPPPPP/1B5R1/LNSGKGSNL b - 1",
                    "1nsgkgsn1/1r5b1/ppppppppp/9/9/9/PPPPPPPPP/1B5R1/LNSGKGSNL b - 1",
                    "lnsgkgsnl/1r7/ppppppppp/9/9/9/PPPPPPPPP/1B5R1/LNSGKGSNL b - 1",
                    "lnsgkgsnl/7b1/ppppppppp/9/9/9/PPPPPPPPP/1B5R1/LNSGKGSNL b - 1",
                    "lnsgkgsn1/7b1/ppppppppp/9/9/9/PPPPPPPPP/1B5R1/LNSGKGSNL b - 1",
                    "lnsgkgsnl/9/ppppppppp/9/9/9/PPPPPPPPP/1B5R1/LNSGKGSNL b - 1",
                    "1nsgkgsn1/9/ppppppppp/9/9/9/PPPPPPPPP/1B5R1/LNSGKGSNL b - 1",
                    "2sgkgs2/9/ppppppppp/9/9/9/PPPPPPPPP/1B5R1/LNSGKGSNL b - 1",
                    "lnsgk2n+B/6g2/p1pppp1pp/1r4p2/9/2P6/PP1PPPP1P/2G6/LNS1KGSN1 b RBS2L2P 1"};
        String[]sfenListR={"lnsgkgsnl/1r5b1/ppppppppp/9/9/9/PPPPPPPPP/1B5R1/LNSGKGSNL b - 1",
                    "lnsgkgsnl/1r5b1/ppppppppp/9/9/9/PPPPPPPPP/1B5R1/1NSGKGSN1 b - 1",
                    "lnsgkgsnl/1r5b1/ppppppppp/9/9/9/PPPPPPPPP/7R1/LNSGKGSNL b - 1",
                    "lnsgkgsnl/1r5b1/ppppppppp/9/9/9/PPPPPPPPP/1B7/LNSGKGSNL b - 1",
                    "lnsgkgsnl/1r5b1/ppppppppp/9/9/9/PPPPPPPPP/1B7/1NSGKGSNL b - 1",
                    "lnsgkgsnl/1r5b1/ppppppppp/9/9/9/PPPPPPPPP/9/LNSGKGSNL b - 1",
                    "lnsgkgsnl/1r5b1/ppppppppp/9/9/9/PPPPPPPPP/9/1NSGKGSN1 b - 1",
                    "lnsgkgsnl/1r5b1/ppppppppp/9/9/9/PPPPPPPPP/9/2SGKGS2 b - 1",
                    "1nsgk1snl/6g2/p1pppp1pp/6p2/9/2P4R1/PP1PPPP1P/2G6/+bN2KGSNL b rbs2l2p 1"};
        String sfen=sfenList[config.getDif()];
        if(config.getVs()==1){
            if(config.getDif()==0||config.getDif()==8){
                mainData.setP1S(config.getSg()==0);
                sfen=sfenListR[config.getDif()];
            }else{
                mainData.setP1S(config.getDifR()==0);
                sfen=sfenListR[config.getDif()];
            }
            mainData.setP2Name("AI");
        }else{
            mainData.setP2Name("p2");
        }
        mainData.setP1Name("p1");
        mainData.sfen2Bd(sfen);
        bdForm=convertToBdForm(mainData);
        return bdForm;
    }
    @Override
    // game
    public BdForm convertToBdForm(MainData mainData) {
        BdForm bdForm = new BdForm();
    
        bdForm.setReverse(!mainData.isP1S()); // p1が先手 → reverse=false
        if(bdForm.isReverse()){
            bdForm.setP2Name(mainData.getP1Name());
            bdForm.setP1Name(mainData.getP2Name());
            bdForm.setP2M(new ArrayList<>(mainData.getP1M())); // ディープコピー推奨
            bdForm.setP1M(new ArrayList<>(mainData.getP2M()));
            bdForm.setBd(mainData.getReversedBoard()); // 反転させる
        }else{
            bdForm.setP1Name(mainData.getP1Name());
            bdForm.setP2Name(mainData.getP2Name());
            bdForm.setP1M(new ArrayList<>(mainData.getP1M())); // ディープコピー推奨
            bdForm.setP2M(new ArrayList<>(mainData.getP2M()));
            bdForm.setBd(mainData.getBd()); // bdはそのまま参照渡しでも可（必要ならコピー）
        }
        bdForm.setKansuji();
        bdForm.pM2pMochi();
        return bdForm;
    }
    @Override
    public BdForm selectKoma(BdForm bdForm,MainData mainData){
        List<List<Integer>>posList=getMoveList(bdForm.getSelectedRow(),
            bdForm.getSelectedCol(),mainData);
        BdForm tmp=convertToBdForm(mainData);
        tmp.setSelectedRow(bdForm.getSelectedRow());
        tmp.setSelectedCol(bdForm.getSelectedCol());
        tmp.setMoveList(posList);
        return tmp;
    }
    @Override
    // game AI
    public List<List<Integer>> getMoveList(int selectedRow, int selectedCol, MainData data){
        List<List<Integer>> moveList = new ArrayList<>();
    
        // 盤面上のマス番号を生成（例："7g" など）
        String[] files = {"9", "8", "7", "6", "5", "4", "3", "2", "1"};
        String[] ranks = {"a", "b", "c", "d", "e", "f", "g", "h", "i"};
    
        // 盤面の向きに応じてrow/colを変換
        int row = selectedRow;
        int col = selectedCol;
        if (!data.isP1S()) {
            row = 8 - selectedRow;
            col = 8 - selectedCol;
        }
    
        String fromSquare = files[col] + ranks[row];
        String fromSquarePromoted = "+" + fromSquare;
        // legalMoves を取得
        Map<String, List<String>> legalMoves = getLegalMoves(data);
    
        List<String> destinations = new ArrayList<>();
        boolean hasPromoted = false;
        boolean hasUnpromoted = false;
    
        if (legalMoves.containsKey(fromSquare)) {
            destinations.addAll(legalMoves.get(fromSquare));
            hasUnpromoted = true;
        }
        if (legalMoves.containsKey(fromSquarePromoted)) {
            destinations.addAll(legalMoves.get(fromSquarePromoted));
            hasPromoted = true;
        }
    
        int option = 0;
        if (hasPromoted && hasUnpromoted) {
            option = 2;
        } else if (hasPromoted) {
            option = 1;
        }
    
        for (String toSquare : destinations) {
            int toCol = Arrays.asList(files).indexOf(String.valueOf(toSquare.charAt(0)));
            int toRow = Arrays.asList(ranks).indexOf(String.valueOf(toSquare.charAt(1)));
    
            if (!data.isP1S()) {
                toRow = 8 - toRow;
                toCol = 8 - toCol;
            }
    
            moveList.add(Arrays.asList(toRow, toCol, option));
        }

        return moveList;
    }
    @Override
    // 動ける場所を取得
    public Map<String, List<String>>getLegalMoves(MainData data){
        CallAi callAi=new CallAi();
        String sfen=callAi.toSfen(data);
        Map<String, List<String>>ret=callAi.getLegalMoves(sfen);
        return ret;
    }

    // @Override
    // public BdForm moveKoma(MainData mainData,BdForm bdForm){
    //     return bdForm;
    // }
}
