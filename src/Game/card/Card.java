package Game.card;
import java.io.Serializable;


public class Card implements Serializable {
    private String name;
    private int attack;
    private int health;
    private int cost;
    private String description;
    private String tag;
    private String image;  // 이미지 경로 추가
    

    public Card(String name, int attack, int health, int cost, String description, String tag, String image) {
        this.name = name;
        this.attack = attack;
        this.health = health;
        this.cost = cost;
        this.description = description;
        this.tag = tag;
        this.image = image;
    }

    // Getters
    public String getName() {
        return name;
    }

    public int getAttack() {
        return attack;
    }

    public int getHealth() {
        return health;
    }

    public int getCost() {
        return cost;
    }

    public String getDescription() {
        return description;
    }

    public String getTag() {
        return tag;
    }

    public String getImage() {
        return image;
    }
 // 체력 감소 처리 메서드
    public void takeDamage(int damage) {
        this.health -= damage;
        if (this.health < 0) {
            this.health = 0;
        }
    }

    // 카드가 소멸됐는지 확인하는 메서드
    public boolean isDestroyed() {
        return this.health <= 0;
    }

    @Override
    public String toString() {
        return "Card{name='" + name + "', attack=" + attack + ", health=" + health + ", cost=" + cost + ", description='" + description + "', tag='" + tag + "', image='" + image + "'}";
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Card card = (Card) obj;
        return name.equals(card.name); // 카드 이름이 같으면 동일한 카드로 인식
    }

    @Override
    public int hashCode() {
        return name.hashCode(); // 카드 이름을 기준으로 해시코드 생성
    }

}
