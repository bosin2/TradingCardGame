package Game.view;

import Game.card.Card;
import Game.card.CardUI;
import Game.card.Deck;

import javax.swing.*;
import java.awt.*;
import Game.card.card_loder; // 카드 로더 클래스 임포트
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class view_fight extends JPanel {

	private card_loder loader = new card_loder();
    private static final long serialVersionUID = 1L;
    private JPanel cardPanel;
    private JPanel battlefieldPanel;
    private List<Card>playerCards = new ArrayList<>();
    private List<Card>allCards = new ArrayList<>();
    private JButton endTurnButton;

    public view_fight() {
    	System.out.println("loadCards() 메서드 시작됨"); // 메서드 시작 로그
        loadCards();

        if (allCards.size() >= 5) {
            Collections.shuffle(allCards); // 카드를 무작위로 섞음
            for (int i = 0; i < 5; i++) {
                this.playerCards.add(allCards.remove(0)); // 5장을 뽑아 손패에 추가
            }
        }

        System.out.println("초기 손패 카드 수: " + playerCards.size()); // 초기 손패 카드 수 출력
        initUI();
    }

    private void loadCards() {
    	System.out.println("loadCards() 메서드 시작됨"); // 메서드 시작 로그
        InputStream inputStream = getClass().getResourceAsStream("/resources/data/StartDeck.json");
        if (inputStream != null) {
            InputStreamReader reader = new InputStreamReader(inputStream);
            Deck deck = loader.loadDeck(reader);
            if (deck != null) {
                allCards = deck.getAllCards(); // 전체 카드 로드
                System.out.println("로딩된 카드 수: " + allCards.size()); // 카드 수 출력
            } else {
                System.out.println("덱을 불러오지 못했습니다.");
            }
        } else {
            System.out.println("Deck 파일을 찾을 수 없습니다.");
        }
    }


    private void initUI() {
    	System.out.println("배경불러오는중");
        setLayout(new BorderLayout());
        
        // 배경 패널 설정
        JPanel backgroundPanel = new JPanel() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Image backgroundImage = new ImageIcon(getClass().getResource("/resources/background/fightEX.png")).getImage();
                try {
                    ImageIcon icon = new ImageIcon(getClass().getResource("/resources/background/fightEX.png"));
                    if (icon.getImageLoadStatus() == MediaTracker.ERRORED) {
                        System.out.println("배경 이미지 로드 중 에러 발생");
                    }
                } catch (Exception e) {
                    System.out.println("배경 이미지 로드 중 예외 발생: " + e.getMessage());
                }

                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(new BorderLayout());
        add(backgroundPanel, BorderLayout.CENTER);

        // 카드 패널 설정 (하단에 카드 배치)
        cardPanel = new JPanel();
        cardPanel.setLayout(new FlowLayout());
        backgroundPanel.add(cardPanel, BorderLayout.SOUTH);

        // 배틀필드 패널 설정 (카드를 드래그할 공간)
        battlefieldPanel = new JPanel();
        battlefieldPanel.setLayout(null);
        battlefieldPanel.setOpaque(false);
        backgroundPanel.add(battlefieldPanel, BorderLayout.CENTER);

        // 손패 카드 보여주기
        displayPlayerCards();

        // 턴 종료 버튼 추가
        endTurnButton = new JButton("턴 종료");
        endTurnButton.addActionListener(e -> drawCard());
        backgroundPanel.add(endTurnButton, BorderLayout.NORTH);
    }

    private void displayPlayerCards() {
        cardPanel.removeAll(); // 이전 손패 삭제 후 새롭게 추가
        for (Card card : playerCards) {
            CardUI cardUI = new CardUI(card);
            cardUI.setPreferredSize(new Dimension(100, 150));
            enableCardDrag(cardUI);
            cardPanel.add(cardUI);
            System.out.println("손패에 카드 추가됨: " + card.getName()); // 추가된 카드 이름 출력
        }
        cardPanel.revalidate();
        cardPanel.repaint();
    }


    // 턴 종료 시 카드를 한 장 뽑아서 손패에 추가하는 로직
    private void drawCard() {
        if (!allCards.isEmpty()) {
            Card drawnCard = allCards.remove(0); // allCards에서 한 장 뽑음
            playerCards.add(drawnCard); // 손패에 추가
            System.out.println("카드 뽑음: " + drawnCard.getName());
            displayPlayerCards(); // 손패를 다시 보여줌
        } else {
            System.out.println("더 이상 뽑을 카드가 없습니다.");
        }
    }

    private void enableCardDrag(CardUI cardUI) {
        DragSource ds = new DragSource();
        ds.createDefaultDragGestureRecognizer(cardUI, DnDConstants.ACTION_MOVE, new DragGestureListener() {
            @Override
            public void dragGestureRecognized(DragGestureEvent dge) {
                Transferable transferable = new TransferableCard(cardUI);
                ds.startDrag(dge, DragSource.DefaultMoveDrop, transferable, null);
            }
        });

        battlefieldPanel.setDropTarget(new DropTarget() {
            @Override
            public synchronized void drop(DropTargetDropEvent dtde) {
                try {
                    dtde.acceptDrop(DnDConstants.ACTION_MOVE);
                    Transferable transferable = dtde.getTransferable();
                    CardUI droppedCard = (CardUI) transferable.getTransferData(new DataFlavor(CardUI.class, "Card UI"));

                    Point dropPoint = dtde.getLocation();
                    battlefieldPanel.add(droppedCard);
                    droppedCard.setBounds(dropPoint.x - 50, dropPoint.y - 75, droppedCard.getWidth(), droppedCard.getHeight());
                    battlefieldPanel.revalidate();
                    battlefieldPanel.repaint();
                } catch (UnsupportedFlavorException | IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private static class TransferableCard implements Transferable {
        private final CardUI cardUI;

        public TransferableCard(CardUI cardUI) {
            this.cardUI = cardUI;
        }

        @Override
        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
            if (flavor.equals(new DataFlavor(CardUI.class, "Card UI"))) {
                return cardUI;
            } else {
                throw new UnsupportedFlavorException(flavor);
            }
        }

        @Override
        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[]{new DataFlavor(CardUI.class, "Card UI")};
        }

        @Override
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return flavor.equals(new DataFlavor(CardUI.class, "Card UI"));
        }
    }
}
