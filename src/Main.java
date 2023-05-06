
import java.util.Scanner;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Main {
    public static void main(String[] args) throws IOException, ParseException {

        BufferedReader br = new BufferedReader(new FileReader("src/secret.txt"));

        final String NAVER_CLIENT_ID = br.readLine();
        final String NAVER_CLIENT_SECRET = br.readLine();

        Scanner scanner = new Scanner(System.in);

        System.out.print("번역할 문장 : ");
        String src = scanner.nextLine();

        String url = "https://openapi.naver.com/v1/papago/n2mt";
        String data = "source=ko&target=en&text=" + src;

        String res = post(url, data, NAVER_CLIENT_ID, NAVER_CLIENT_SECRET);

        System.out.println(res);

        JSONParser parser = new JSONParser();

        JSONObject messageObject = (JSONObject) parser.parse(res);
        String message = messageObject.get("message").toString();

        JSONObject resultObject = (JSONObject) parser.parse(message);
        String result = resultObject.get("result").toString();

        JSONObject translatedTextObject = (JSONObject) parser.parse(result);
        String translatedText = translatedTextObject.get("translatedText").toString();

        System.out.println(translatedText);
    }

    public static String post(String _url, String postData, String id, String secret) throws IOException {
        URL url = new URL(_url);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setDoOutput(true);
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8"); // 요청 헤더
        con.setRequestProperty("X-Naver-Client-Id", id); // 요청 헤더
        con.setRequestProperty("X-Naver-Client-Secret", secret); // 요청 헤더

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
        bw.write(postData);
        bw.flush();
        bw.close();

        int responseCode = con.getResponseCode(); // HTTP 응답 코드
        System.out.println("HTTP 응답 코드: " + responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) response.append(inputLine);
        in.close();

        return response.toString();

    }

}