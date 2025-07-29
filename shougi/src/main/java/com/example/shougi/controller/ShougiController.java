package com.example.shougi.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.shougi.data.MainData;
import com.example.shougi.model.BdForm;
import com.example.shougi.model.KifuForm;
import com.example.shougi.model.NewgameModel;
import com.example.shougi.service.MainService;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequestMapping("/shogi")
public class ShougiController {
    private final MainService mainService;
    public ShougiController(MainService mainService){
        this.mainService=mainService;
    }
    @Autowired
    HttpSession session;
    

    @GetMapping("/title")
    public String title(){
        return "title";
    }

    @GetMapping("/newgame")
    public String newgame(Model model){
        model.addAttribute("config",new NewgameModel());
        return "newgame";
    }

    @PostMapping("/totitle")
    public String totitle(){
        return "redirect:/shogi/title";
    }
    @GetMapping("/kif")
    public String kif(Model model){
        KifuForm kifuform;
        MainData mainData=(MainData)session.getAttribute("mainData");
        if(mainData==null){
            mainData=new MainData();
            kifuform=mainService.setDummyKifu(mainData);
            session.setAttribute("mainData",mainData);
        }else{
            kifuform=new KifuForm();
            kifuform.setKifuList(mainData.getKifuList());
        }
        model.addAttribute("kifuForm",kifuform);
        return "kifuList";
    }
    @PostMapping("/returnbykihuList")
    public String returnbykihuList(@ModelAttribute("kifuForm")KifuForm kifuForm){
        MainData mainData=(MainData) session.getAttribute("mainData");
        mainService.setKifu2Form(mainData,kifuForm);
        session.setAttribute("mainData",mainData);
        return "redirect:/shogi/title";
    }
    @PostMapping("/game")
    public String game(@ModelAttribute("config")NewgameModel config,Model model){
        MainData mainData=(MainData)session.getAttribute("mainData");
        if(mainData==null){
            mainData=new MainData();
            session.setAttribute("mainData",mainData);
        }
        BdForm bdForm=mainService.setNewGame(config,mainData);
        model.addAttribute("bdForm",bdForm);
        session.setAttribute("mainData",mainData);

        return "game";
    }
    @PostMapping("/gameret")
    public String gameret(@ModelAttribute("bdForm")BdForm bdForm,Model model){
        MainData mainData=(MainData)session.getAttribute("mainData");
        if(mainData==null){
            mainData=new MainData();
            session.setAttribute("mainData",mainData);
        }
        bdForm=mainService.convertToBdForm(mainData);
        model.addAttribute("bdForm",bdForm);
        session.setAttribute("mainData",mainData);

        return "game";
    }
    @PostMapping("/game1")
    public String game1(@ModelAttribute("bdForm")BdForm bdForm,Model model){
        MainData mainData=(MainData)session.getAttribute("mainData");
        bdForm=mainService.selectKoma(bdForm,mainData);
        model.addAttribute("bdForm",bdForm);
        
        return "game1";
    }
    @PostMapping("/game2")
    public String game2(@ModelAttribute("bdForm")BdForm bdForm,Model model){
        MainData mainData=(MainData)session.getAttribute("mainData");
        // bdForm=mainService.selectKoma(bdForm,mainData);駒を動かさせる
        model.addAttribute("bdForm",bdForm);
        
        return "title";
    }
}
