package Game.card;
import javax.swing.*;
import java.awt.*;
import java.io.FileReader;
import java.io.IOException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;

public class card_loder {

    public List<Card> loadCards(String jsonFilePath) {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(jsonFilePath)) {
            Type cardListType = new TypeToken<List<Card>>() {}.getType();
            return gson.fromJson(reader, cardListType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class Card {
        String name;
        int attack;
        int health;
        int cost;
        String description;
        String image;  // 이미지 경로 추가

        // 이미지 표시
        public ImageIcon getImage() {
            return new ImageIcon(image);
        }
    }

    // 카드 GUI 만들기
    public void displayCard(Card card) {
        JFrame frame = new JFrame(card.name);
        frame.setSize(300, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // 이미지와 텍스트 추가
        JLabel cardImageLabel = new JLabel(card.getImage());
        JLabel cardStatsLabel = new JLabel("" + card.attack + "" + card.health);

        frame.add(cardImageLabel, BorderLayout.CENTER);
        frame.add(cardStatsLabel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        card_loder loader = new card_loder();
        List<Card> cards = loader.loadCards("src/Game/assets/cards/cards.json");

        // 첫 번째 카드 화면에 표시
        if (cards != null && !cards.isEmpty()) {
            loader.displayCard(cards.get(0));
        }
    }
}
