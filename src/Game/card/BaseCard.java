package Game.card;

public abstract class BaseCard {
    private String name;
    private int attack;
    private int health;
    private int cost;
    private String description;
    private String tag;
    private String image;

    public BaseCard(String name, int attack, int health, int cost, String description, String tag, String image) {
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

    // 공통 행동 정의
    public void takeDamage(int damage) {
        // 체력 감소 로직
        this.health -= damage;
        if (this.health < 0) {
            this.health = 0;
        }
        System.out.println(name + " 카드가 " + damage + " 데미지를 입었습니다. 남은 체력: " + health);
    }

    public boolean carddie() {
        // 카드가 소멸되었는지 확인
        return this.health <= 0;
    }

    public void attackCard(BaseCard opponentCard) {
        // 상대 카드를 공격하는 로직
        System.out.println(name + "가 " + opponentCard.getName() + "을(를) 공격합니다.");
        opponentCard.takeDamage(attack);
    }

    public void performAction() {
        // 카드를 냈을 때 수행할 기본 행동
        System.out.println(name + " 카드가 수행 중입니다.");
    }

    public abstract void onDeath(); // 카드가 소멸될 때 특수 행동 (하위 클래스에서 구현 필요)

    @Override
    public String toString() {
        return "Card{name='" + name + "', attack=" + attack + ", health=" + health + ", cost=" + cost + ", description='" + description + "', tag='" + tag + "', image='" + image + "'}";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        BaseCard card = (BaseCard) obj;
        return name.equals(card.name); // 카드 이름이 같으면 동일한 카드로 인식
    }

    @Override
    public int hashCode() {
        return name.hashCode(); // 카드 이름을 기준으로 해시코드 생성
    }
}
