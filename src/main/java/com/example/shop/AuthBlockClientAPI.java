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

    public AuthBlockClientAPI(String key){
        this.keyMAC = key;
    }


    public void send(String oraLogin, String oraLogout, String username, String userAgent, String ipAddress) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, IOException, InvalidKeySpecException, BadPaddingException, InterruptedException {
        String hmac = hmac(username);
        String data = "[\""+oraLogin+"\",\""+oraLogout+"\",\""+username+"\",\""+userAgent+"\",\""+ipAddress+"\",\""+hmac+"\"]";

        String dataEncrypted = rsa(data);
       System.out.println(dataEncrypted.length());
       HttpClient client = HttpClient.newHttpClient();
       HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api"))
                .POST(HttpRequest.BodyPublishers.ofString("{data:"+dataEncrypted+"}"))
                .build();
       System.out.println("{data:"+dataEncrypted+"}");
       HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
       if(response.statusCode() != 200 || !response.body().equals("{result:ok}"))
           throw new RuntimeException("send failed. Status code: "+response.statusCode()+" Result: "+response.body());
    }

    private String rsa(String data) throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, InvalidKeySpecException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        File publicKeyFile = new File("public.key");
        byte[] publicKeyBytes = Files.readAllBytes(publicKeyFile.toPath());
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

        Cipher encryptCipher = Cipher.getInstance("RSA");
        encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
        return Base64.getEncoder().encodeToString(encryptCipher.doFinal(dataBytes));
    }

    private String hmac(String data) throws NoSuchAlgorithmException, InvalidKeyException {
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
