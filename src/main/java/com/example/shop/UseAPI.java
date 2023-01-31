package com.example.shop;

public class UseAPI {


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
