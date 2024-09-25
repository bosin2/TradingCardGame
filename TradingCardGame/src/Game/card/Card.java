package Game.card;

class Card {
    String name;
    int attack;
    int health;
    int cost;
    String description;
    String tag; // tag가 있을 경우

    @Override
    public String toString() {
        return "Card{name='" + name + "', attack=" + attack + ", health=" + health + ", cost=" + cost + ", description='" + description + "', tag='" + tag + "'}";
    }
}

