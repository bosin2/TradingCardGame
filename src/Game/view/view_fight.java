package Game.view;

import Game.button_manager.Default_button;
import Game.card.Card;
import Game.card.CardUI;
import Game.logic.basicfight;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

public class view_fight extends JPanel {

    private MainFrame mainFrame;
    private boolean isDragging = false;
    private static final long serialVersionUID = 1L;
    private JPanel cardPanel;
    private JPanel playerBattlefieldPanel;
    private JPanel enemyBattlefieldPanel;
    private Image backgroundImage;
    private basicfight battleManager; // 전투 매니저
    private JPanel[] playerFieldSlots = new JPanel[5];
    private JPanel[] enemyFieldSlots = new JPanel[5];

    public view_fight(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.battleManager = new basicfight(); // 전투 매니저 초기화
        
        setLayout(null);
        setPreferredSize(new Dimension(1300, 800));
        setSize(new Dimension(1300, 800));

        battleManager.setAttackListener((attackerIndex, defenderIndex, isPlayerAttacker) -> {
            CardUI attackerCardUI = null;
            int targetX, targetY;

            if (isPlayerAttacker) {
                attackerCardUI = (CardUI) playerFieldSlots[attackerIndex].getComponent(0);
                if (defenderIndex == -1) {
                    // 적 본체를 공격하는 경우
                    targetX = enemyBattlefieldPanel.getX() + enemyBattlefieldPanel.getWidth() / 2;
                    targetY = enemyBattlefieldPanel.getY();
                } else {
                    // 적 카드 공격
                    targetX = enemyFieldSlots[defenderIndex].getX();
                    targetY = enemyFieldSlots[defenderIndex].getY();
                }
            } else {
                attackerCardUI = (CardUI) enemyFieldSlots[attackerIndex].getComponent(0);
                if (defenderIndex == -1) {
                    // 플레이어 본체를 공격하는 경우
                    targetX = playerBattlefieldPanel.getX() + playerBattlefieldPanel.getWidth() / 2;
                    targetY = playerBattlefieldPanel.getY();
                } else {
                    // 플레이어 카드 공격
                    targetX = playerFieldSlots[defenderIndex].getX();
                    targetY = playerFieldSlots[defenderIndex].getY();
                }
            }

            if (attackerCardUI != null) {
                System.out.println("공격 애니메이션 시작: 카드 UI가 존재합니다.");
                // 공격 애니메이션 추가
                moveCardAnimation(attackerCardUI, targetX, targetY);
            } else {
                System.out.println("공격 애니메이션 실행 실패: 카드 UI가 없습니다.");
            }
        });

        
        // 배경 이미지 로드
        backgroundImage = new ImageIcon(getClass().getResource("/resources/background/fightEX.png")).getImage();
        if (backgroundImage == null) {
            System.out.println("배경 이미지 로드 실패");
        } else {
            System.out.println("배경 이미지 로드 성공");
        }

        // UI 초기화
        initUI();

        System.out.println("view_fight 패널 생성 완료");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            System.out.println("배경 이미지가 없습니다!");
        }
    }

    private void initUI() {
        setLayout(null);

        // 적 배틀필드 패널 설정
        enemyBattlefieldPanel = new JPanel(new GridLayout(1, 5));
        enemyBattlefieldPanel.setOpaque(false);
        enemyBattlefieldPanel.setBounds(50, 50, 770, 230);
        add(enemyBattlefieldPanel);
        for (int i = 0; i < 5; i++) {
            JPanel slotPanel = new JPanel(null);
            slotPanel.setOpaque(false);
            slotPanel.setBorder(BorderFactory.createLineBorder(Color.RED));
            enemyBattlefieldPanel.add(slotPanel);
            enemyFieldSlots[i] = slotPanel;
        }

        // 플레이어 배틀필드 패널 설정
        playerBattlefieldPanel = new JPanel(new GridLayout(1, 5));
        playerBattlefieldPanel.setOpaque(false);
        playerBattlefieldPanel.setBounds(50, 300, 770, 230);
        add(playerBattlefieldPanel);
        for (int i = 0; i < 5; i++) {
            JPanel slotPanel = new JPanel(null);
            slotPanel.setOpaque(false);
            slotPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
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
                    } catch (UnsupportedFlavorException | IOException ex) {
                        ex.printStackTrace();
                    } finally {
                        isDragging = false;
                    }
                }
            });
        }

        Default_button buttonManager = new Default_button();
        JButton endTurnButton = buttonManager.createImageButton("턴 종료");
        endTurnButton.setBounds(1000, 461, 200, 100);
        endTurnButton.setLayout(null);
        endTurnButton.addActionListener(e -> {
            if (battleManager.isPlayerTurn()) {
                battleManager.playerEndTurn();
            } else {
                battleManager.enemyEndTurn();
            }
            battleManager.endCurrentTurn();
            battleManager.drawCard(); // 턴 종료 시 카드 드로우 추가
            displayPlayerCards(); // 턴 종료 후 손패 UI 업데이트
            updateAfterBattle();
            changeTurn();
        });

        add(endTurnButton);

        // 카드 패널 설정 (하단에 배치)
        cardPanel = new JPanel();
        cardPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        cardPanel.setOpaque(false);
        cardPanel.setBounds(0, 550, 1000, 500);
        add(cardPanel);

        displayPlayerCards();
    }

    private void changeTurn() {
        if (battleManager.isPlayerTurn()) {
            updateEnemyCards();
        }
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
        battleManager.enemyPlaceCards();
        updateFieldUI(enemyFieldSlots, battleManager.getEnemyField());
    }

    private void updateFieldUI(JPanel[] fieldSlots, List<Card> updatedField) {
        for (int i = 0; i < fieldSlots.length; i++) {
            fieldSlots[i].removeAll();
            Card updatedCard = updatedField.get(i);
            if (updatedCard != null) {
                CardUI cardUI = new CardUI(updatedCard);
                fieldSlots[i].add(cardUI);
                cardUI.setBounds(0, 0, 150, 200);
                cardUI.resetFont();
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
                } catch (InvalidDnDOperationException e) {
                    e.printStackTrace();
                    isDragging = false;
                }
            }
        });
    }
    
    
    private void moveCardAnimation(CardUI cardUI, int targetX, int targetY) {
        new Thread(() -> {
            try {
                SwingUtilities.invokeAndWait(() -> {
                    if (cardUI.getParent() == null) {
                        System.err.println("카드 UI의 부모가 없습니다. 애니메이션을 중지합니다.");
                        return;
                    }
                });

                Point startPoint = cardUI.getLocation();
                int startX = startPoint.x;
                int startY = startPoint.y;

                int duration = 1000; // 애니메이션 총 시간 (밀리초)
                int steps = 50;      // 애니메이션 단계 수
                int delay = duration / steps;

                for (int i = 0; i < steps; i++) {
                    if (cardUI.getParent() == null) {
                        System.err.println("애니메이션 도중 부모가 제거되었습니다. 애니메이션 중지.");
                        return;
                    }

                    int currentX = startX + (int) ((targetX - startX) * (i / (float) steps));
                    int currentY = startY + (int) ((targetY - startY) * (i / (float) steps));

                    SwingUtilities.invokeLater(() -> {
                        cardUI.setLocation(currentX, currentY);
                        cardUI.getParent().revalidate();
                        cardUI.getParent().repaint();
                    });

                    Thread.sleep(delay);
                }

                SwingUtilities.invokeLater(() -> {
                    if (cardUI.getParent() != null) {
                        cardUI.setLocation(targetX, targetY);
                        cardUI.getParent().revalidate();
                        cardUI.getParent().repaint();
                        System.out.println("카드 최종 위치 설정 완료: x=" + targetX + ", y=" + targetY);
                    } else {
                        System.err.println("카드 최종 위치 설정 시 부모가 존재하지 않습니다.");
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }




    private static class TransferableCard implements Transferable {
        private final CardUI cardUI;
        private final DataFlavor[] flavors = {new DataFlavor(CardUI.class, "CardUI")};

        public TransferableCard(CardUI cardUI) {
            this.cardUI = cardUI;
        }

        @Override
        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
            if (flavor.equals(flavors[0])) {
                return cardUI;
            } else {
                throw new UnsupportedFlavorException(flavor);
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
}