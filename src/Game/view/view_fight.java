package Game.view;

import Game.button_manager.Default_button;
import Game.card.Card;
import Game.card.CardUI;
import Game.logic.basicfight;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.*;
import java.util.List;
import java.util.Queue;
import java.util.*;
import java.util.concurrent.ExecutionException;


public class view_fight extends JPanel implements basicfight.GameEventListener {
    private MainFrame mainFrame;
    private basicfight battleManager;
    private boolean isDragging = false;
    private static final long serialVersionUID = 1L;
    private JPanel cardPanel;
    private JPanel playerBattlefieldPanel;
    private JPanel enemyBattlefieldPanel;
    private Image backgroundImage;
    private JPanel[] playerFieldSlots = new JPanel[5];
    private JPanel[] enemyFieldSlots = new JPanel[5];
    private JButton endTurnButton;
    private Queue<Runnable> animationQueue = new LinkedList<>();

    public view_fight(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.battleManager = new basicfight(); // 게임 로직 초기화
        this.battleManager.setGameEventListener(this); // 이벤트 리스너 설정

        setLayout(null);
        setPreferredSize(new Dimension(1300, 800));
        setSize(new Dimension(1300, 800));

        // 배경 이미지 로드
        backgroundImage = new ImageIcon(getClass().getResource("/resources/background/fightEX.png")).getImage();
        if (backgroundImage == null) {
            System.out.println("배경 이미지 로드 실패");
        } else {
            System.out.println("배경 이미지 로드 성공");
        }

        // UI 초기화
        initUI();

        if (battleManager.isPlayerTurn()) {
            startPlayerTurn();
        } else {
            startEnemyTurn();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, 1300, 800, this);
        } else {
            System.out.println("배경 이미지가 없습니다!");
        }
    }

    private void initUI() {
        setLayout(null);

        // 적 배틀필드 패널 설정
        enemyBattlefieldPanel = new JPanel(new GridLayout(1, 5));
        enemyBattlefieldPanel.setOpaque(false);
        enemyBattlefieldPanel.setBounds(48, 75, 750, 200);
        add(enemyBattlefieldPanel);
        for (int i = 0; i < 5; i++) {
            JPanel slotPanel = new JPanel(null);
            slotPanel.setOpaque(false);
            slotPanel.setBorder(null); // 테두리 제거
            enemyBattlefieldPanel.add(slotPanel);
            enemyFieldSlots[i] = slotPanel;
        }

        // 플레이어 배틀필드 패널 설정
        playerBattlefieldPanel = new JPanel(new GridLayout(1, 5));
        playerBattlefieldPanel.setOpaque(false);
        playerBattlefieldPanel.setBounds(48, 275, 750, 200);
        add(playerBattlefieldPanel);
        for (int i = 0; i < 5; i++) {
            JPanel slotPanel = new JPanel(null);
            slotPanel.setOpaque(false);
            slotPanel.setBorder(null); // 테두리 제거
            playerBattlefieldPanel.add(slotPanel);
            playerFieldSlots[i] = slotPanel;

            final int slotIndex = i;
            slotPanel.setDropTarget(new DropTarget() {
                @Override
                public synchronized void drop(DropTargetDropEvent dtde) {
                    if (isDragging) {
                        isDragging = false;
                    }
                    try {
                        if (!battleManager.canPlaceCardInPlayerField(slotIndex)) {
                            dtde.rejectDrop();
                            return;
                        }
                        dtde.acceptDrop(DnDConstants.ACTION_MOVE);
                        Transferable transferable = dtde.getTransferable();
                        CardUI droppedCardUI = (CardUI) transferable.getTransferData(new DataFlavor(CardUI.class, "CardUI"));
                        Card droppedCard = droppedCardUI.getCard();

                        if (battleManager.placePlayerCard(droppedCard, slotIndex)) {
                            slotPanel.add(droppedCardUI);
                            droppedCardUI.setBounds(0, 0, droppedCardUI.getPreferredSize().width, droppedCardUI.getPreferredSize().height);
                            droppedCardUI.resetFont();
                            displayPlayerCards(); // 손패에서 카드가 사라졌으므로 다시 그리기
                            updateAfterBattle();
                        } else {
                            System.out.println("카드를 손패에서 찾을 수 없습니다.");
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    } finally {
                        isDragging = false;
                    }
                }
            });
        }

        Default_button buttonManager = new Default_button();
        endTurnButton = buttonManager.createImageButton("턴 종료");
        endTurnButton.setBounds(1000, 461, 200, 100);
        endTurnButton.setLayout(null);
        endTurnButton.setVisible(true); // 첫 턴이 플레이어의 턴이므로 버튼을 보이도록 설정
        add(endTurnButton);

        endTurnButton.addActionListener(e -> {
            endTurnButton.setVisible(false); // 플레이어 턴 종료 시 버튼 숨김
            battleManager.endPlayerTurn(); // 게임 로직에 턴 종료 요청
        });

        // 카드 패널 설정 (하단에 배치)
        cardPanel = new JPanel();
        cardPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        cardPanel.setOpaque(false);
        cardPanel.setBounds(0, 550, 1000, 500);
        add(cardPanel);

        displayPlayerCards();
    }

    private void startPlayerTurn() {
        System.out.println("플레이어 턴 시작");
        endTurnButton.setVisible(true);
        displayPlayerCards();
        updateAfterBattle();
    }

    private void startEnemyTurn() {
        System.out.println("적 턴 시작");
        endTurnButton.setVisible(false);
        updateEnemyCards();
        updateAfterBattle();
    }

    private void displayPlayerCards() {
        cardPanel.removeAll();
        cardPanel.setLayout(null);

        int cardWidth = 150;
        int cardHeight = 200;
        int maxVisibleWidth = cardPanel.getWidth();
        List<Card> handCards = battleManager.getPlayerHand();
        int cardCount = handCards.size();

        int spacing;

        if (cardCount * cardWidth <= maxVisibleWidth) {
            spacing = (maxVisibleWidth - cardWidth) / Math.max(1, cardCount - 1);
            spacing = Math.min(spacing, cardWidth);
        } else {
            int maxOverlap = 20;
            spacing = (maxVisibleWidth - cardWidth) / (cardCount - 1);
            spacing = Math.max(spacing, maxOverlap);
        }

        for (int i = 0; i < cardCount; i++) {
            CardUI cardUI = new CardUI(handCards.get(i));
            int xPosition = Math.min(i * spacing, maxVisibleWidth - cardWidth);
            cardUI.setBounds(xPosition, 0, cardWidth, cardHeight);
            enableCardDrag(cardUI);
            cardPanel.add(cardUI);
        }

        cardPanel.revalidate();
        cardPanel.repaint();
    }

    private void updateEnemyCards() {
        updateFieldUI(enemyFieldSlots, battleManager.getEnemyField());
    }

    private void updateFieldUI(JPanel[] fieldSlots, List<Card> updatedField) {
        for (int i = 0; i < fieldSlots.length; i++) {
            Card updatedCard = updatedField.get(i);
            if (updatedCard != null) {
                if (fieldSlots[i].getComponentCount() == 0) {
                    // 슬롯에 CardUI가 없으면 새로 생성
                    CardUI cardUI = new CardUI(updatedCard);
                    fieldSlots[i].setLayout(null);
                    cardUI.setBounds(0, 0, 150, 200);
                    cardUI.resetFont();
                    fieldSlots[i].add(cardUI);
                } else {
                    // 기존 CardUI 재사용 및 카드 상태 업데이트
                    CardUI existingCardUI = (CardUI) fieldSlots[i].getComponent(0);
                    existingCardUI.updateCardState(updatedCard); // 카드 상태 업데이트
                }
            } else {
                // 슬롯에 카드가 없으면 CardUI 제거
                if (fieldSlots[i].getComponentCount() > 0) {
                    fieldSlots[i].removeAll();
                }
            }
            fieldSlots[i].revalidate();
            fieldSlots[i].repaint();
        }
    }



    private void updateAfterBattle() {
        updateFieldUI(playerFieldSlots, battleManager.getPlayerField());
        updateFieldUI(enemyFieldSlots, battleManager.getEnemyField());
    }

    private void enableCardDrag(CardUI cardUI) {
        DragSource ds = new DragSource();
        ds.createDefaultDragGestureRecognizer(cardUI, DnDConstants.ACTION_MOVE, dge -> {
            if (!isDragging) {
                isDragging = true;
                Transferable transferable = new TransferableCard(cardUI);
                try {
                    ds.startDrag(dge, DragSource.DefaultMoveDrop, transferable, new DragSourceListener() {
                        @Override
                        public void dragEnter(DragSourceDragEvent dsde) {}

                        @Override
                        public void dragOver(DragSourceDragEvent dsde) {}

                        @Override
                        public void dropActionChanged(DragSourceDragEvent dsde) {}

                        @Override
                        public void dragExit(DragSourceEvent dse) {}

                        @Override
                        public void dragDropEnd(DragSourceDropEvent dsde) {
                            isDragging = false;
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    isDragging = false;
                }
            }
        });
    }

    // 공격 애니메이션 실행
    private void moveCardAnimation(CardUI cardUI, int offsetX, int offsetY, Runnable onComplete) {
        animationQueue.add(() -> startAnimation(cardUI, offsetX, offsetY, onComplete));
        
        // 큐에 첫 번째 작업이라면 즉시 실행
        if (animationQueue.size() == 1) {
            System.out.println("애니메이션 시작: 새로운 작업 실행");
            animationQueue.peek().run(); // 직접 실행
        }
    }


    private void startAnimation(CardUI cardUI, int offsetX, int offsetY, Runnable onComplete) {
        SwingWorker<Void, Point> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                int duration = 300;
                int steps = 25;
                int delay = duration / steps;

                Point originalLocation = cardUI.getLocation();
                int originalX = originalLocation.x;
                int originalY = originalLocation.y;

                int targetX = originalX + offsetX;
                int targetY = originalY + offsetY;

                // 목표 위치로 이동
                for (int i = 0; i < steps; i++) {
                    int currentX = originalX + (int) ((targetX - originalX) * (i / (float) steps));
                    int currentY = originalY + (int) ((targetY - originalY) * (i / (float) steps));
                    publish(new Point(currentX, currentY));
                    Thread.sleep(delay);
                }

                // 다시 원래 위치로 돌아옴
                for (int i = steps; i >= 0; i--) {
                    int currentX = originalX + (int) ((targetX - originalX) * (i / (float) steps));
                    int currentY = originalY + (int) ((targetY - originalY) * (i / (float) steps));
                    publish(new Point(currentX, currentY));
                    Thread.sleep(delay);
                }

                return null;
            }

            @Override
            protected void process(List<Point> chunks) {
                if (cardUI.getParent() == null) {
                    JPanel correctParent = findCorrectParentForCard(cardUI);
                    if (correctParent != null) {
                        correctParent.setLayout(null); // 레이아웃을 null로 설정
                        correctParent.add(cardUI);
                        correctParent.revalidate();
                        correctParent.repaint();
                    } else {
                        System.out.println("올바른 부모를 찾을 수 없습니다.");
                        return;
                    }
                }
                Point point = chunks.get(chunks.size() - 1);
                cardUI.setLocation(point);
                cardUI.getParent().revalidate();
                cardUI.getParent().repaint();
            }


            @Override
            protected void done() {
                try {
                    get(); // 예외가 발생했는지 확인
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                } finally {
                    if (onComplete != null) {
                        SwingUtilities.invokeLater(() -> {
                            onComplete.run(); // 애니메이션 완료 후 작업 수행
                            updateAfterBattle(); // 애니메이션 완료 후 필드 업데이트
                        });
                    }
                    animationQueue.poll(); // 현재 애니메이션 완료
                    if (!animationQueue.isEmpty()) {
                        animationQueue.peek().run(); // 다음 애니메이션 실행
                    }
                }
            }

        };

        worker.execute();
    }

    // 카드를 포함해야 할 올바른 부모 컨테이너를 찾는 메서드
    private JPanel findCorrectParentForCard(CardUI cardUI) {
        // 플레이어 필드 검사
        for (int i = 0; i < playerFieldSlots.length; i++) {
            if (playerFieldSlots[i].getComponentCount() > 0) {
                CardUI ui = (CardUI) playerFieldSlots[i].getComponent(0);
                if (ui.getCard().equals(cardUI.getCard())) {
                    return playerFieldSlots[i];
                }
            }
        }
        // 적 필드 검사
        for (int i = 0; i < enemyFieldSlots.length; i++) {
            if (enemyFieldSlots[i].getComponentCount() > 0) {
                CardUI ui = (CardUI) enemyFieldSlots[i].getComponent(0);
                if (ui.getCard().equals(cardUI.getCard())) {
                    return enemyFieldSlots[i];
                }
            }
        }
        return null; // 올바른 부모를 찾지 못한 경우
    }




    private static class TransferableCard implements Transferable {
        private final CardUI cardUI;
        private final DataFlavor[] flavors = {new DataFlavor(CardUI.class, "CardUI")};

        public TransferableCard(CardUI cardUI) {
            this.cardUI = cardUI;
        }

        @Override
        public Object getTransferData(DataFlavor flavor) {
            if (flavor.equals(flavors[0])) {
                return cardUI;
            } else {
                return null;
            }
        }

        @Override
        public DataFlavor[] getTransferDataFlavors() {
            return flavors;
        }

        @Override
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return flavor.equals(flavors[0]);
        }
    }

    // GameEventListener 인터페이스 구현
    @Override
    public void onAttack(int attackerIndex, int defenderIndex, boolean isPlayerAttacker, Runnable performAttack) {
        SwingUtilities.invokeLater(() -> {
            CardUI attackerCardUI = getCardUI(isPlayerAttacker, attackerIndex);
            int moveDistanceY = 80; // 이동할 거리

            if (attackerCardUI == null) {
                System.out.println("공격 애니메이션 실행 실패: 공격자 카드 UI가 없습니다.");
                performAttack.run(); // 애니메이션 없이 공격 수행
                updateAfterBattle();
            } else {
                System.out.println("공격 애니메이션 시작: 카드 UI가 존재합니다.");

                Runnable afterAnimation = () -> {
                    performAttack.run(); // 공격 로직 실행
                    SwingUtilities.invokeLater(this::updateAfterBattle); // 애니메이션 완료 후 UI 업데이트
                };


                int targetY = isPlayerAttacker ? -moveDistanceY : moveDistanceY;
                moveCardAnimation(attackerCardUI, 0, targetY, afterAnimation);
            }
        });
    }

    
    private CardUI getCardUI(boolean isPlayer, int index) {
        JPanel[] fieldSlots = isPlayer ? playerFieldSlots : enemyFieldSlots;
        if (index >= 0 && index < fieldSlots.length) {
            if (fieldSlots[index].getComponentCount() > 0) {
                return (CardUI) fieldSlots[index].getComponent(0);
            }
        }
        return null;
    }




    @Override
    public void onTurnChange(boolean isPlayerTurn) {
        SwingUtilities.invokeLater(() -> {
            if (isPlayerTurn) {
                startPlayerTurn();
            } else {
                startEnemyTurn();
            }
        });
    }

    @Override
    public void onGameOver(boolean playerWon) {
        SwingUtilities.invokeLater(() -> {
            if (playerWon) {
                System.out.println("게임 승리!");
                // 승리 화면 표시 로직 추가
            } else {
                System.out.println("게임 패배...");
                // 패배 화면 표시 로직 추가
            }
        });
    }

    @Override
    public void onCardDrawn(Card drawnCard) {
        SwingUtilities.invokeLater(this::displayPlayerCards);
    }

    @Override
    public void onFieldUpdate() {
        SwingUtilities.invokeLater(this::updateAfterBattle);
    }
}
