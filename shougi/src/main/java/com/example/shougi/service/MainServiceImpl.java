package com.example.shougi.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.shougi.data.Kifu;
import com.example.shougi.data.Koma;
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
                    "lnsgk1snl/1r4g2/p1pppp1pp/6p2/9/2P6/PP1PPPP1P/1+bG4R1/LNS1KGSNL b Pbp 1"};
        String sfen=sfenList[config.getDif()];
        if(config.getVs()==1){
            if(config.getDif()==0||config.getDif()==8){
                mainData.setP1S(config.getSg()==0);
                if(config.getSg()==1){
                    sfen=sfenListR[config.getDif()];
                }
            }else{
                mainData.setP1S(config.getDifR()==0);
                if(config.getDifR()==0){
                    sfen=sfenListR[config.getDif()];
                }
            }
            mainData.setP2Name("AI");
        }else{
            mainData.setP2Name("p2");
        }
        mainData.setP1Name("p1");
        // debug
        System.out.println(sfen);
        //  
        mainData.sfen2Bd(sfen);
        mainData.setTeban();
        bdForm=convertToBdForm(mainData);
        // debug
        System.out.print("p1m:");
        for(int cnt:mainData.getP1M()){
            System.out.print(cnt+",");
        }
        System.out.println();
        System.out.print("p2m:");
        for(int cnt:mainData.getP2M()){
            System.out.print(cnt+",");
        }
        System.out.println();

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
        List<List<Integer>>posList;
        BdForm tmp=convertToBdForm(mainData);
        if(bdForm.isBoardPieceSelected()){
            // 盤上の駒が選択された場合
            posList=getMoveList(bdForm.getSelectedRow(),
                bdForm.getSelectedCol(),mainData);
            tmp.setSelectedRow(bdForm.getSelectedRow());
            tmp.setSelectedCol(bdForm.getSelectedCol());
            tmp.setMoveList(posList);
            return tmp;
        }else if(bdForm.isMochigomaSelected()){
            // 持ち駒が選択された場合
            posList=getDropList(bdForm.getSelectedMochigomaKNo(),mainData);
            tmp.setSelectedMochigomaKNo(bdForm.getSelectedMochigomaKNo());
            tmp.setMoveList(posList);
            return tmp;

        }else {
        // どちらも選択されていない、または不正な状態の場合
        // エラー処理、あるいは何もせず終了
        System.err.println("Error:selectKoma");
        return bdForm; // または適切なエラー処理
        }
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
    @Override
    public List<List<Integer>> getDropList(int selectedMochigomaKNo, MainData data) {
        List<List<Integer>> dropList = new ArrayList<>();
        // 盤面上のマス番号を生成（getMoveListと同じ）
        String[] files = {"9", "8", "7", "6", "5", "4", "3", "2", "1"};
        String[] ranks = {"a", "b", "c", "d", "e", "f", "g", "h", "i"};
        // selectedMochigomaKNo から駒コードを取得
        String pieceCode = data.getKomaOrder().get(selectedMochigomaKNo).toLowerCase();
        // 現在のプレイヤーが先手か後手かで、駒コードを調整
        // SFENのドロップは 'P*7g' のように大文字の駒コードを使用し、先後で異なる文字は使わない
        // String dropPieceSfenCode = data.isP1S() ? pieceCode.toUpperCase() : pieceCode.toLowerCase(); // 必要に応じて
        // legalMoves を取得 (これは盤面と持ち駒の両方からの合法手を含むと仮定)
        Map<String, List<String>> legalMoves = getLegalMoves(data);
        // 持ち駒を打つ手は通常、ソースが "P*" (歩を打つ) のような形式になる
        // 例: key = "P*", value = ["7g", "7f", ...]
        String dropKey = "hand_" +pieceCode;
        if (legalMoves.containsKey(dropKey)) {
            List<String> destinations = legalMoves.get(dropKey);
            for (String toSquare : destinations) {
                // toSquare は "7g" のような形式
                int toCol = Arrays.asList(files).indexOf(String.valueOf(toSquare.charAt(0)));
                int toRow = Arrays.asList(ranks).indexOf(String.valueOf(toSquare.charAt(1)));
                // 盤面の向きに応じてrow/colを変換
                if (!data.isP1S()) {
                    toRow = 8 - toRow;
                    toCol = 8 - toCol;
                }
                dropList.add(Arrays.asList(toRow, toCol, 0));
            }
        }
        // debug
        // List<List<Integer>>
        for(int key=0;key<dropList.size();key++){
            System.out.print(key+":");
            for(int val:dropList.get(key)){
                System.out.print(val+",");
            }
            System.out.println();
        }
        return dropList;
    }
    @Override
    // 駒を動かす
    public BdForm moveKoma(BdForm bdForm,MainData mainData){
        if(bdForm.getSelectedRow()!=null){
            List<List<Koma>>bd=mainData.getBd();
            Koma tmp=bd.get(bdForm.getMoveToCol()).get(bdForm.getMoveToRow());
            Koma move=bd.get(bdForm.getSelectedCol()).get(bdForm.getSelectedRow());
            if(bdForm.isPromoteMove()){//成
                move.setNari(true);
            }
            bd.get(bdForm.getMoveToCol()).set(bdForm.getMoveToRow(),move);
            Koma empty = new Koma();
            empty.setEmpty();
            bd.get(bdForm.getSelectedCol()).set(bdForm.getSelectedRow(),empty);
            if(tmp.getId()!=-1){
                int kNo=tmp.getKNo();
                mainData.getP1M().set(kNo,mainData.getP1M().get(kNo)+1);
            }
        }else if(bdForm.getSelectedMochigomaKNo()!=null){
            Koma km=new Koma();
            km.setUchigoma(bdForm.getSelectedMochigomaKNo(),bdForm.getMoveToRow(),
                bdForm.getMoveToCol(),mainData.isP1S());
            List<List<Koma>>bd=mainData.getBd();
            List<Integer>p1M=bdForm.getP1M();
            int num=p1M.get(bdForm.getSelectedMochigomaKNo());
            p1M.set(bdForm.getSelectedMochigomaKNo(),num-1);
            bd.get(bdForm.getMoveToCol()).set(bdForm.getMoveToRow(),km);
        }else{
            System.out.println("不正なデータ");
        }
        return bdForm;
    }
}
