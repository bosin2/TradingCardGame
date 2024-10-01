package Game.card;

import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class card_loder {

    // InputStream을 사용하여 리소스 폴더에서 JSON 파일 로드
    public Deck loadDeck(InputStream inputStream) {
        Gson gson = new Gson();
        try (Reader reader = new InputStreamReader(inputStream)) {
            return gson.fromJson(reader, Deck.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
        // FileReader 대신 Reader 타입을 받는 메서드 추가
        public Deck loadDeck(Reader reader) {
            Gson gson = new Gson();
            try {
                return gson.fromJson(reader, Deck.class);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        // 기존의 String 경로로 로드하는 메서드
        public Deck loadDeck(String jsonFilePath) {
            Gson gson = new Gson();
            try (FileReader reader = new FileReader(jsonFilePath)) {
                return gson.fromJson(reader, Deck.class);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }


    // 여러 JSON 파일에서 카드를 로드하는 메서드
    public List<Card> loadAllCards(String[] jsonFiles) {
        List<Card> allCards = new ArrayList<>();
        for (String jsonFile : jsonFiles) {
            InputStream inputStream = getClass().getResourceAsStream(jsonFile);
            if (inputStream != null) {
                Deck deck = loadDeck(inputStream);
                if (deck != null) {
                    allCards.addAll(deck.getAllCards());
                } else {
                    System.out.println(jsonFile + "에서 덱을 로드하지 못했습니다.");
                }
            } else {
                System.out.println(jsonFile + " 파일을 찾을 수 없습니다.");
            }
        }
        return allCards;
    }
}
