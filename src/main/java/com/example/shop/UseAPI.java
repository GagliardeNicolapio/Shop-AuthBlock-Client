package com.example.shop;

public class UseAPI {


    public String home() throws Exception {
        AuthBlockClientAPI authBlockClientAPI = new AuthBlockClientAPI("chiave");

        AuthBlockClientAPI.Access access = authBlockClientAPI.new Access()
                .setEthereumUser("0xf05fb83e4CceEd1c54BdA10a23BfA3e78A94C981")
                .setEthereumWebSite("0x1879086e634ef05B18FB66455e90d1353a128B5f")
                .setUrlWebSite("youtube.com")
                .setOraLogin("23:44").setOraLogout("23:59")
                .setUsername("nik").setUserAgent("chrome").setIpAddress("8.8.8.8");

        authBlockClientAPI.send(access);
        System.out.println("fine");
        return "index";
    }
}
