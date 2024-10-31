package Game.card;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Deck {
    Map<String, List<Card>> minion;
    Map<String, List<Card>> skill;
    Map<String, List<Card>> power;
    
    public Map<String, List<Card>> getMinion() {
        return minion;
    }

    public Map<String, List<Card>> getSkill() {
        return skill;
    }

    public Map<String, List<Card>> getPower() {
        return power;
    }


    // 모든 카드를 하나의 리스트로 묶어 반환하는 메서드
    public List<Card> getAllCards() {
        List<Card> allCards = new ArrayList<>();

        // 미니언 카드 추가
        if (minion != null) {
            for (List<Card> minionList : minion.values()) {
                allCards.addAll(minionList);
            }
        }

        // 스킬 카드 추가
        if (skill != null) {
            for (List<Card> skillList : skill.values()) {
                allCards.addAll(skillList);
            }
        }

        // 파워 카드 추가
        if (power != null) {
            for (List<Card> powerList : power.values()) {
                allCards.addAll(powerList);
            }
        }
        Collections.shuffle(allCards);
        
        return allCards;
    }
    
    
    public List<Card> getPlayerDeck() {
        return getAllCards();
    }

    // 적 덱을 반환하는 메서드
    public List<Card> getEnemyDeck() {
        return getAllCards();
    }

}
