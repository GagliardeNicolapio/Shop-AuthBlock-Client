package com.example.shop;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Timestamp;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping(value = "")
public class ShopController {
    @GetMapping("")
    public String home(){
        return "index";
    }

    @GetMapping("/registrazione")
    public String showRegistrazione(){return "registrazione";}

    @PostMapping("/registrazione")
    public String registrazione(@RequestParam("username") String username, @RequestParam("addressEth") String addressEth, HttpServletRequest request ) throws Exception {
        /*qui il sito salva il nuovo utente nel suo database*/


        /*qui il sito invia i dati ad AuthBlock*/
        AuthBlockClientAPI authBlockClientAPI = new AuthBlockClientAPI("chiave");

        AuthBlockClientAPI.Access access = authBlockClientAPI.new Access()
                .setEthereumUser(addressEth)
                .setEthereumWebSite("0xdCC1d026c131580791B318407d3C85c81Da22C24")
                .setUrlWebSite("www.roachforlife.com")
                .setOraLogin(new Timestamp(System.currentTimeMillis()).toString())
                .setOraLogout("")
                .setUsername(username)
                .setUserAgent(request.getHeader("User-Agent"))
                .setIpAddress(request.getRemoteAddr());

        authBlockClientAPI.send(access);
        return "areaPrivata";
    }

    @PostMapping("login")
    public String login(@RequestParam("address") String address, HttpServletRequest request) throws Exception {
        //qui il sito controlla se address è presente nel suo db

        //qui invia i dati a authblock
        AuthBlockClientAPI authBlockClientAPI = new AuthBlockClientAPI("chiave");

        AuthBlockClientAPI.Access access = authBlockClientAPI.new Access()
                .setEthereumUser(address)
                .setEthereumWebSite("0xdCC1d026c131580791B318407d3C85c81Da22C24")
                .setUrlWebSite("www.roachforlife.com")
                .setOraLogin(new Timestamp(System.currentTimeMillis()).toString())
                .setOraLogout("")
                .setUsername("username")
                .setUserAgent(request.getHeader("User-Agent"))
                .setIpAddress(request.getRemoteAddr());

        authBlockClientAPI.send(access);

        return "areaPrivata";
    }
}
