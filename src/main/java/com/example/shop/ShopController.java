package com.example.shop;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class ShopController {
    @GetMapping("/")
    public String home() throws Exception {
        AuthBlockClientAPI authBlockClientAPI = new AuthBlockClientAPI("chiave");

        AuthBlockClientAPI.Access access = authBlockClientAPI.new Access()
                .setEthereumUser("x")
                .setEthereumWebSite("y")
                .setUrlWebSite("youtube.com")
                .setOraLogin("23:44").setOraLogout("23:59")
                .setUsername("nik").setUserAgent("chrome").setIpAddress("8.8.8.8");

        authBlockClientAPI.send(access);
        System.out.println("fine");
        return "index";
    }
}
