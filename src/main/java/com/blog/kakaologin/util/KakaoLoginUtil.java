package com.blog.kakaologin.util;

import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class KakaoLoginUtil {


    //Kakao REST API 키와 Redirect URI
    private static final String CLIENT_ID = "6f3be945da8d84a01dbe6bba5cfe7757";
    private static final String REDIRECT_URI = "http://localhost:8282/login/kakao/callback";



    public static String getAccessToken(String code) throws Exception {

        String requestUrl = "https://kauth.kakao.com/oauth/token";
        URL url = new URL(requestUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        String params = "grant_type=authorization_code"
            + "&client_id=" + CLIENT_ID
            + "&redirect_uri=" + REDIRECT_URI
            + "&code=" + code;

        //요청 바디 전송
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
        writer.write(params);
        writer.flush();
        writer.close();

        //응답 받기
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }

        reader.close();
    conn.disconnect();

    //디버깅용... 개발 끝나면 지울게요 ㅠ
    System.out.println("💡 카카오 인가 코드: " + code);
    System.out.println("💬 카카오 토큰 응답: " + response);

    //JSON 파싱해서 토큰 추출
    JSONObject obj = new JSONObject(response.toString());

        if (obj.has("error")) {
            throw new RuntimeException("카카오 토큰 요청 실패: " + obj.getString("error_description"));
        }

    return obj.getString("access_token");
    }



    public static JSONObject getUserInfo(String accessToken) throws Exception {

        String requestUrl = "https://kapi.kakao.com/v2/user/me";
        URL url = new URL(requestUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();


        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer " + accessToken); // ← 헤더는 여기서 설정!


        //응답 읽기
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }

        reader.close();
        conn.disconnect();

        JSONObject json = new JSONObject(response.toString());

        Long kakaoId = json.getLong("id");

        JSONObject kakaoAccount = json.getJSONObject("kakao_account");
        JSONObject profile = kakaoAccount.getJSONObject("profile");
        JSONObject result = new JSONObject();

        if (kakaoAccount.has("email")) {
            result.put("email", kakaoAccount.getString("email"));
        } else {
            result.put("email", "no-email");
        }

        result.put("id", kakaoId);
        result.put("nickname", profile.getString("nickname"));

        return result;
    }









}//class
