package Game.effects;

public interface CardAction {
    void attack(CardAction target);  // 공격 동작
    void chooseTarget();  // 공격 대상 선택
    boolean isDestroyed();  // 소멸 여부 체크
    int getAttack();
    int getHealth();
    String getKeyword(); // 카드의 키워드 반환
}
