package com.example.shougi.controller;

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
            System.out.println("dummy");
            session.setAttribute("mainData",mainData);
        }else{
            kifuform=new KifuForm();
            kifuform.setKifuList(mainData.getKifuList());
        }
        model.addAttribute("kifuForm",kifuform);
        return "kifuList";
    }
    @PostMapping("/retunbykihuList")
    public String retunbykihuList(@ModelAttribute("kifuForm")KifuForm kifuForm){
        MainData mainData=(MainData) session.getAttribute("mainData");
        mainService.setKifu2Form(mainData,kifuForm);
        session.setAttribute("mainData",mainData);
        return "redirect:/shogi/title";
    }

    @GetMapping("/test")
    public String test(Model model){
        // game.html
        MainData mainData=new MainData();
        BdForm bdForm=mainService.setDummyBdForm1(mainData);
        model.addAttribute("bdForm",bdForm);
        
        return "game";
    }
}
