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
    public String home() throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, IOException, InvalidKeySpecException, BadPaddingException {
        AuthBlockClientAPI authBlockClientAPI = new AuthBlockClientAPI("chiave");
        System.out.println(authBlockClientAPI.send("","","pippo","",""));
        System.out.println("fine");
        return "index";
    }
}
