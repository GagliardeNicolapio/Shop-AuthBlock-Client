package com.example.shop;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class AuthBlockClientAPI {
    private String keyMAC;
    private String algorithm = "HmacSHA256";
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    public AuthBlockClientAPI(String keyMAC){
        this.keyMAC = keyMAC;
    }


    public class Access{
        private String oraLogin, oraLogout, username, userAgent, ipAddress, ethereumWebSite, ethereumUser, urlWebSite;
        private String oraLoginMAC, oraLogoutMAC, usernameMAC, userAgentMAC, ipAddressMAC, ethereumWebSiteMAC, ethereumUserMAC, urlWebSiteMAC;
        public Access(){}

        public Access setOraLogin(String oraLogin)throws Exception{
            System.out.println("login");
            this.oraLogin = rsa(oraLogin);
            this.oraLoginMAC = rsa(hmac(oraLogin));
            return this;
        }
        public Access setOraLogout(String oraLogout)throws Exception{
            System.out.println("logout");

            this.oraLogout = rsa(oraLogout);
            this.oraLogoutMAC = rsa(hmac(oraLogout));
            return this;
        }
        public Access setUsername(String username)throws Exception{
            System.out.println("userna");

            this.username = rsa(username);
            this.usernameMAC = rsa(hmac(username));
            return this;
        }
        public Access setUserAgent(String userAgent)throws Exception{
            System.out.println("agent");

            this.userAgent = rsa(userAgent);
            this.userAgentMAC = rsa(hmac(userAgent));
            return this;
        }
        public Access setIpAddress(String ipAddress)throws Exception{
            System.out.println("ip");

            this.ipAddress = rsa(ipAddress);
            this.ipAddressMAC = rsa(hmac(ipAddress));
            return this;
        }
        public Access setEthereumWebSite(String ethereumWebSite)throws Exception{
            System.out.println("eth esite");

            this.ethereumWebSite = rsa(ethereumWebSite);
            this.ethereumWebSiteMAC = rsa(hmac(ethereumWebSite));
            return this;
        }
        public Access setEthereumUser(String ethereumUser)throws Exception{
            System.out.println("eth user");

            this.ethereumUser = rsa(ethereumUser);
            this.ethereumUserMAC = rsa(hmac(ethereumUser));
            return this;
        }
        public Access setUrlWebSite(String urlWebSite)throws Exception{
            System.out.println("url");

            this.urlWebSite = rsa(urlWebSite);
            this.urlWebSiteMAC = rsa(hmac(urlWebSite));
            return this;
        }

        private String getData(){
            return "[{ethSite:\""+ethereumWebSite+"\",ethUser:\""+ethereumUser+"\"},{ethSite:\""+ethereumWebSiteMAC+"\",ethUser:\""+ethereumUserMAC+"\"}," +
                    "{oraLogin:\""+oraLogin+"\",oraLogout:\""+
                    oraLogout+"\",username:\""+username+"\",userAgent:\""+userAgent+"\",ipAddress:\""+ipAddress+"\",url:\""+urlWebSite+"\"}," +
                    "{oraLogin:\""+oraLoginMAC+"\",oraLogout:\""+oraLogoutMAC+"\"," +
                    "username:\""+usernameMAC+"\",userAgent:\""+userAgentMAC+"\",ipAddress:\""+ipAddressMAC+"\",url:\""+urlWebSiteMAC+"\"}]";
        }
    }

    public void send(Access access) throws IOException, InterruptedException {
        System.out.println(access.getData());
       HttpClient client = HttpClient.newHttpClient();
       HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api"))
                .POST(HttpRequest.BodyPublishers.ofString(access.getData()))
               .header("Content-Type","application/json")
                .build();

       HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
       if(response.statusCode() != 200 || !response.body().equals("{result:ok}"))
           throw new RuntimeException("send failed. Status code: "+response.statusCode()+" Result: "+response.body());
    }

    private String rsa(String data) throws Exception {
        File publicKeyFile = new File("public.key");
        byte[] publicKeyBytes = Files.readAllBytes(publicKeyFile.toPath());
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

        Cipher encryptCipher = Cipher.getInstance("RSA");
        encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
        System.out.println(dataBytes.length);
        String bho = Base64.getEncoder().encodeToString(encryptCipher.doFinal(dataBytes));
        System.out.println(bho);
        System.out.println(bho.length());

        return bho;
    }

    private String hmac(String data) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyMAC.getBytes(), algorithm);
        Mac mac = Mac.getInstance(algorithm);
        mac.init(secretKeySpec);
        return bytesToHex(mac.doFinal(data.getBytes()));
    }

    private String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }
}
