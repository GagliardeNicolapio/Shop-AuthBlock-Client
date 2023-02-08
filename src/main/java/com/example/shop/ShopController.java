package com.example.shop;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.sql.Timestamp;


@Controller
@RequestMapping(value = "")
public class ShopController {
    private static final String url="www.roachforlife.com", chiaveHMAC = "chiave", indirizzoSito = "0x3B06eEb99C6dFc819A797348DB74CB38Ff004dAC";

    @GetMapping("")
    public String home(){
        return "index";
    }

    @GetMapping("/registrazione")
    public String showRegistrazione(){return "registrazione";}

    @PostMapping("/registrazione")
    public String registrazione(@RequestParam("username") String username, @RequestParam("addressEth") String addressEth, HttpServletRequest request, Model model) throws Exception {
        /*qui il sito invia i dati ad AuthBlock, quindi salva il nuovo utente e i dati della login*/
        AuthBlockClientAPI authBlockClientAPI = new AuthBlockClientAPI(chiaveHMAC, indirizzoSito, url);
        if(authBlockClientAPI.checkUser(addressEth)){
            model.addAttribute("utenteGiaPresente", true);
            return "index";
        }else{
            AuthBlockClientAPI.Access access = authBlockClientAPI.new Access()
                    .setEthereumUser(addressEth)
                    .setOraLogout("")
                    .setUsername(username)
                    .setUserAgent(request.getHeader("User-Agent"))
                    .setIpAddress(request.getRemoteAddr());

            authBlockClientAPI.insertNewUser(access);
            model.addAttribute("fraseBenvenuto", "Grazie per esserti registrato");
            model.addAttribute("address", addressEth);
            model.addAttribute("username", username);
            model.addAttribute("UserAgent", request.getHeader("User-Agent"));
            model.addAttribute("ip",request.getRemoteAddr());
            return "areaPrivata";
        }
    }

    @PostMapping("/login")
    public String login(@RequestParam("address") String address, HttpServletRequest request, Model model) throws Exception {
        //controlla se address è un utente registrato
        AuthBlockClientAPI authBlockClientAPI = new AuthBlockClientAPI(chiaveHMAC, indirizzoSito, url);
        if(authBlockClientAPI.checkUser(address)){
            AuthBlockClientAPI.Access access = authBlockClientAPI.new Access()
                    .setEthereumUser(address)
                    .setOraLogout("")
                    .setUserAgent(request.getHeader("User-Agent"))
                    .setIpAddress(request.getRemoteAddr());

            authBlockClientAPI.insertAccess(access);
            model.addAttribute("fraseBenvenuto", "Bentornato");
            model.addAttribute("address", address);
            model.addAttribute("UserAgent", request.getHeader("User-Agent"));
            model.addAttribute("ip",request.getRemoteAddr());
            return "areaPrivata";
        }else{
            model.addAttribute("flagUtenteTrovato", false);
            return "index";
        }
    }

    @PostMapping("/logout")
    public String logout(@RequestParam("address") String address) throws Exception {
        AuthBlockClientAPI authBlockClientAPI = new AuthBlockClientAPI(chiaveHMAC, indirizzoSito, url);
        authBlockClientAPI.sendLogout(address);
        return "index";
    }
}
