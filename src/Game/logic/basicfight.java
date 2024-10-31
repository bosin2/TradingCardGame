package Game.logic;

import Game.card.Card;
import Game.card.Deck;
import Game.card.card_loder;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.Timer;

public class basicfight {
    // 게임 상태 변수들
    private List<Card> playerField;
    private List<Card> enemyField;
    private List<Card> playerHand;
    private List<Card> playerDeck;
    private List<Card> enemyDeck;
    private int playerHealth;
    private int enemyHealth;
    private int playerCost;
    private final int MAX_COST = 5;
    private boolean isPlayerTurn;
    private boolean isGameOver;

    // 이벤트 리스너 인터페이스
    public interface GameEventListener {
        void onAttack(int attackerIndex, int defenderIndex, boolean isPlayerAttacker, Runnable performAttack);
        void onTurnChange(boolean isPlayerTurn);
        void onGameOver(boolean playerWon);
        void onCardDrawn(Card drawnCard);
        void onFieldUpdate();
    }

    private GameEventListener eventListener;

    // 게임 로직 메서드들
    public basicfight() {
        this.playerField = new ArrayList<>(Collections.nCopies(5, null));
        this.enemyField = new ArrayList<>(Collections.nCopies(5, null));
        this.playerHand = new ArrayList<>();
        this.playerDeck = new ArrayList<>();
        this.enemyDeck = new ArrayList<>();
        this.playerHealth = 50;
        this.enemyHealth = 50;
        this.playerCost = MAX_COST;
        this.isPlayerTurn = true;
        this.isGameOver = false;

        loadCards();

        // 초기 손패 구성
        if (playerDeck.size() >= 5) {
            Collections.shuffle(playerDeck);
            for (int i = 0; i < 5; i++) {
                this.playerHand.add(playerDeck.remove(0));
            }
        }

        System.out.println("초기 손패 카드 수: " + playerHand.size());
    }

    private void loadCards() {
        System.out.println("loadCards() 메서드 시작됨");

        card_loder loader = new card_loder();

        // 플레이어 카드 로드
        InputStream playerInputStream = getClass().getResourceAsStream("/resources/data/StartDeck.json");
        if (playerInputStream != null) {
            InputStreamReader reader = new InputStreamReader(playerInputStream);
            Deck playerDeckData = loader.loadDeck(reader);
            if (playerDeckData != null) {
                playerDeck = playerDeckData.getPlayerDeck();
                System.out.println("로딩된 플레이어 카드 수: " + playerDeck.size());
            } else {
                System.out.println("플레이어 덱을 불러오지 못했습니다.");
            }
        } else {
            System.out.println("StartDeck 파일을 찾을 수 없습니다.");
        }

        // 적 카드 로드
        InputStream enemyInputStream = getClass().getResourceAsStream("/resources/data/Pro1Deck.json");
        if (enemyInputStream != null) {
            InputStreamReader reader = new InputStreamReader(enemyInputStream);
            Deck enemyDeckData = loader.loadDeck(reader);
            if (enemyDeckData != null) {
                enemyDeck = enemyDeckData.getEnemyDeck();
                System.out.println("로딩된 적 카드 수: " + enemyDeck.size());
            } else {
                System.out.println("적 덱을 불러오지 못했습니다.");
            }
        } else {
            System.out.println("Pro1Deck 파일을 찾을 수 없습니다.");
        }
    }

    // 플레이어 턴 시작
    public void startPlayerTurn() {
        if (isGameOver) return;
        isPlayerTurn = true;
        playerCost = MAX_COST;
        drawCard();
        triggerTurnChange();
    }

 // 플레이어 턴 종료
    public void endPlayerTurn() {
        if (isGameOver) return;

        // 플레이어의 공격
        playerAttackPhase(() -> {
            // 플레이어 공격이 끝나면 적의 반격
            enemyAttackPhase(() -> {
                checkWinCondition();
                if (!isGameOver) {
                    startEnemyTurn();
                }
            });
        });
    }

    public void startEnemyTurn() {
        if (isGameOver) return;

        isPlayerTurn = false;
        enemyPlaceCards(); // 적이 카드를 배치
        triggerTurnChange();

        // 적이 카드를 낸 후 1초 지연
        new Timer(1000, e -> {
            ((Timer)e.getSource()).stop(); // 타이머를 한번만 실행하도록 정지
            // 적이 선공
            enemyAttackPhase(() -> {
                // 플레이어가 후공으로 반격
                playerAttackPhase(() -> {
                    checkWinCondition();
                    if (!isGameOver) {
                        startPlayerTurn();
                    }
                });
            });
        }).start();
    }

    // 플레이어가 카드를 배치할 때 호출하는 메서드
    public boolean placePlayerCard(Card card, int position) {
        if (playerCost >= card.getCost() && position >= 0 && position < playerField.size() && playerField.get(position) == null) {
            playerField.set(position, card);
            playerCost -= card.getCost();
            boolean removed = playerHand.remove(card); // 손패에서 카드 제거
            if (removed) {
                System.out.println("손패에서 카드 제거 성공: " + card.getName());
                triggerFieldUpdate();
            } else {
                System.out.println("손패에서 카드 제거 실패: " + card.getName());
            }
            return true;
        }
        return false;
    }

    // 플레이어가 카드 배치를 시도할 수 있는지 확인하는 메서드
    public boolean canPlaceCardInPlayerField(int position) {
        return position >= 0 && position < playerField.size() && playerField.get(position) == null;
    }

    // 적이 랜덤으로 카드를 배치하는 메서드
    private void enemyPlaceCards() {
        Random rand = new Random();
        int numCardsToPlace = rand.nextInt(3) + 1;

        int[] cardsPlaced = {0}; // 카드 배치 수를 배열로 관리 (내부 클래스에서 접근 가능)
        
        Timer timer = new Timer(300, e -> {
            int position = findEmptySlot(enemyField); // 무작위 빈 슬롯 찾기
            if (position != -1 && !enemyDeck.isEmpty()) {
                Card card = enemyDeck.remove(0);
                enemyField.set(position, card);
                cardsPlaced[0]++; // 배치된 카드 수 증가
                triggerFieldUpdate(); // 필드 업데이트 호출
            }

            if (cardsPlaced[0] >= numCardsToPlace || position == -1) {
                ((Timer) e.getSource()).stop(); // 모든 카드를 배치했거나 빈 슬롯이 없으면 타이머 중지
            }
        });

        timer.setInitialDelay(0); // 첫 카드는 즉시 배치
        timer.start(); // 타이머 시작
    }

    // 공격 단계
 // 공격 단계
    public void attackPhase(boolean isPlayerAttacker, Runnable afterAnimation) {
        List<Card> attackerField = isPlayerAttacker ? playerField : enemyField;
        List<Card> defenderField = isPlayerAttacker ? enemyField : playerField;

        AtomicInteger animationsRemaining = new AtomicInteger(0);

        for (int index = 0; index < attackerField.size(); index++) {
            final int idx = index;
            final Card attackerCard = attackerField.get(idx);

            if (attackerCard != null) {
                animationsRemaining.incrementAndGet();

                Runnable performAttack = () -> {
                    // performAttack 실행 시점에 카드의 상태를 다시 확인
                    Card currentAttackerCard = attackerField.get(idx);
                    Card currentDefenderCard = defenderField.get(idx);

                    if (currentAttackerCard == null || currentAttackerCard.isDestroyed()) {
                        if (animationsRemaining.decrementAndGet() == 0 && afterAnimation != null) {
                            afterAnimation.run();
                        }
                        return;
                    }

                    // 공격자가 수비자를 공격
                    if (currentDefenderCard != null && !currentDefenderCard.isDestroyed()) {
                        currentDefenderCard.takeDamage(currentAttackerCard.getAttack());
                        System.out.println(currentAttackerCard.getName() + "가 " + currentDefenderCard.getName() + "를 공격함");

                        if (currentDefenderCard.isDestroyed()) {
                            defenderField.set(idx, null);
                            System.out.println(currentDefenderCard.getName() + "가 파괴되었습니다.");
                        }
                    } else {
                        // 수비자가 없으면 본체 공격
                        if (isPlayerAttacker) {
                            enemyHealth -= currentAttackerCard.getAttack();
                            System.out.println("적 본체에 " + currentAttackerCard.getAttack() + " 데미지를 입힘. 남은 체력: " + enemyHealth);
                        } else {
                            playerHealth -= currentAttackerCard.getAttack();
                            System.out.println("플레이어 본체에 " + currentAttackerCard.getAttack() + " 데미지를 입힘. 남은 체력: " + playerHealth);
                        }
                    }

                    // 애니메이션 카운터 감소 및 체크
                    if (animationsRemaining.decrementAndGet() == 0 && afterAnimation != null) {
                        afterAnimation.run();
                    }
                };

                triggerAttackEvent(idx, defenderField.get(idx) != null ? idx : -1, isPlayerAttacker, performAttack);
            }
        }

        if (animationsRemaining.get() == 0 && afterAnimation != null) {
            afterAnimation.run();
        }
    }


    // 플레이어 공격 단계
    private void playerAttackPhase(Runnable afterAnimation) {
        attackPhase(true, afterAnimation);
    }

    // 적 공격 단계
    private void enemyAttackPhase(Runnable afterAnimation) {
        attackPhase(false, afterAnimation);
    }

    // 카드 드로우
    private void drawCard() {
        if (!playerDeck.isEmpty()) {
            Card drawnCard = playerDeck.remove(0);
            playerHand.add(drawnCard);
            System.out.println("카드 뽑음: " + drawnCard.getName());
            triggerCardDrawn(drawnCard);
        } else {
            System.out.println("더 이상 뽑을 카드가 없습니다.");
        }
    }

    // 승리 조건 체크
    private void checkWinCondition() {
        if (enemyHealth <= 0) {
            System.out.println("플레이어 승리!");
            isGameOver = true;
            triggerGameOver(true);
        } else if (playerHealth <= 0) {
            System.out.println("게임 오버");
            isGameOver = true;
            triggerGameOver(false);
        }
    }

    // 빈 슬롯 찾기
    private int findEmptySlot(List<Card> field) {
        List<Integer> emptySlots = new ArrayList<>();

        // 빈 슬롯 수집
        for (int i = 0; i < field.size(); i++) {
            if (field.get(i) == null) {
                emptySlots.add(i); // 빈 슬롯 인덱스 추가
            }
        }

        // 빈 슬롯이 없다면 -1 반환
        if (emptySlots.isEmpty()) {
            return -1;
        }

        // 빈 슬롯 중 무작위로 하나 선택
        Random rand = new Random();
        return emptySlots.get(rand.nextInt(emptySlots.size()));
    }


    // 이벤트 리스너 설정 메서드
    public void setGameEventListener(GameEventListener listener) {
        this.eventListener = listener;
    }

    // 이벤트 발생 시 리스너 호출 메서드들
    private void triggerAttackEvent(int attackerIndex, int defenderIndex, boolean isPlayerAttacker, Runnable performAttack) {
        if (eventListener != null) {
            eventListener.onAttack(attackerIndex, defenderIndex, isPlayerAttacker, performAttack);
        } else {
            performAttack.run();
        }
    }

    private void triggerTurnChange() {
        if (eventListener != null) {
            eventListener.onTurnChange(isPlayerTurn);
        }
    }

    private void triggerGameOver(boolean playerWon) {
        if (eventListener != null) {
            eventListener.onGameOver(playerWon);
        }
    }

    private void triggerCardDrawn(Card drawnCard) {
        if (eventListener != null) {
            eventListener.onCardDrawn(drawnCard);
        }
    }

    private void triggerFieldUpdate() {
        if (eventListener != null) {
            eventListener.onFieldUpdate();
        }
    }

    // Getter 메서드들
    public List<Card> getPlayerField() {
        return playerField;
    }

    public List<Card> getEnemyField() {
        return enemyField;
    }

    public List<Card> getPlayerHand() {
        return playerHand;
    }

    public int getPlayerHealth() {
        return playerHealth;
    }

    public int getEnemyHealth() {
        return enemyHealth;
    }

    public boolean isPlayerTurn() {
        return isPlayerTurn;
    }

    public boolean isGameOver() {
        return isGameOver;
    }
}
