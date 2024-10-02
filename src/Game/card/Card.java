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

    @Override
    public String toString() {
        return "Card{name='" + name + "', attack=" + attack + ", health=" + health + ", cost=" + cost + ", description='" + description + "', tag='" + tag + "', image='" + image + "'}";
    }
}
