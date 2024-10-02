package Game.view;

import Game.card.Card;
import Game.card.CardUI;
import Game.card.Deck;

import javax.swing.*;
import java.awt.*;
import Game.card.card_loder;
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
	
    private MainFrame mainFrame;
    private card_loder loader = new card_loder();
    private static final long serialVersionUID = 1L;
    private JPanel cardPanel;
    private JPanel battlefieldPanel;
    private List<Card> playerCards = new ArrayList<>();
    private List<Card> allCards = new ArrayList<>();
    private JButton endTurnButton;
    private Image backgroundImage;

    public view_fight(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(null);

        // 배경 이미지 로드
        backgroundImage = new ImageIcon(getClass().getResource("/resources/background/fightEX.png")).getImage();
        if (backgroundImage == null) {
            System.out.println("배경 이미지 로드 실패");
        } else {
            System.out.println("배경 이미지 로드 성공");
        }

        // 카드 로드
        loadCards();

        // 초기 손패 구성
        if (allCards.size() >= 5) {
            Collections.shuffle(allCards);
            for (int i = 0; i < 5; i++) {
                this.playerCards.add(allCards.remove(0));
            }
        }

        System.out.println("초기 손패 카드 수: " + playerCards.size());

        // UI 초기화
        initUI();
        System.out.println("view_fight 패널 생성 완료");
        System.out.println("view_fight 크기: " + getSize());
        System.out.println("battlefieldPanel 크기: " + battlefieldPanel.getSize());
        System.out.println("cardPanel 크기: " + cardPanel.getSize());

    }

    private void loadCards() {
        System.out.println("loadCards() 메서드 시작됨");
        InputStream inputStream = getClass().getResourceAsStream("/resources/data/StartDeck.json");
        if (inputStream != null) {
            InputStreamReader reader = new InputStreamReader(inputStream);
            Deck deck = loader.loadDeck(reader);
            if (deck != null) {
                allCards = deck.getAllCards();
                System.out.println("로딩된 카드 수: " + allCards.size());
            } else {
                System.out.println("덱을 불러오지 못했습니다.");
            }
        } else {
            System.out.println("Deck 파일을 찾을 수 없습니다.");
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            // System.out.println("배경 이미지 그리기 완료"); // 디버깅 메시지
        } else {
            System.out.println("배경 이미지가 없습니다!");
        }
    }

    private void initUI() {
        System.out.println("UI 초기화 중");
        
        setLayout(new BorderLayout());

        // 배틀필드 패널 설정 (센터에 배치)
        battlefieldPanel = new JPanel(null); // null 레이아웃 사용
        battlefieldPanel.setOpaque(false);
        add(battlefieldPanel, BorderLayout.CENTER);

        // 카드 패널 설정 (하단에 배치)
        cardPanel = new JPanel();
        cardPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        cardPanel.setOpaque(false);
        add(cardPanel, BorderLayout.SOUTH);

        // 손패 카드 보여주기
        displayPlayerCards();
        System.out.println("카드보여주기완료");

        // 턴 종료 버튼 추가 (상단에 배치)
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.setOpaque(false);
        endTurnButton = new JButton("턴 종료");
        endTurnButton.addActionListener(e -> drawCard());
        add(endTurnButton, BorderLayout.NORTH);

        System.out.println("UI 불러오기 완료");
    }

    private void displayPlayerCards() {
        cardPanel.removeAll();
        for (Card card : playerCards) {
            CardUI cardUI = new CardUI(card);
            cardUI.setPreferredSize(new Dimension(100, 150));
            enableCardDrag(cardUI);
            cardPanel.add(cardUI);
            System.out.println("손패에 카드 추가됨: " + card.getName());
        }
        cardPanel.revalidate();
        cardPanel.repaint();
    }

    private void drawCard() {
        if (!allCards.isEmpty()) {
            Card drawnCard = allCards.remove(0);
            playerCards.add(drawnCard);
            System.out.println("카드 뽑음: " + drawnCard.getName());
            displayPlayerCards();
        } else {
            System.out.println("더 이상 뽑을 카드가 없습니다.");
        }
    }

    private void enableCardDrag(CardUI cardUI) {
        DragSource ds = new DragSource();
        ds.createDefaultDragGestureRecognizer(cardUI, DnDConstants.ACTION_MOVE, dge -> {
            Transferable transferable = new TransferableCard(cardUI.getCard()); // Card 객체를 전달
            ds.startDrag(dge, DragSource.DefaultMoveDrop, transferable, null);
        });

        battlefieldPanel.setDropTarget(new DropTarget() {
            @Override
            public synchronized void drop(DropTargetDropEvent dtde) {
                try {
                    dtde.acceptDrop(DnDConstants.ACTION_MOVE);
                    Transferable transferable = dtde.getTransferable();
                    Card droppedCard = (Card) transferable.getTransferData(new DataFlavor(Card.class, "Card"));

                    // CardUI 생성
                    CardUI droppedCardUI = new CardUI(droppedCard);
                    Point dropPoint = dtde.getLocation();
                    battlefieldPanel.add(droppedCardUI);
                    droppedCardUI.setBounds(dropPoint.x - 50, dropPoint.y - 75, droppedCardUI.getPreferredSize().width, droppedCardUI.getPreferredSize().height);

                    battlefieldPanel.revalidate();
                    battlefieldPanel.repaint();

                    // 손패에서 제거
                    playerCards.remove(droppedCard);
                    displayPlayerCards();

                } catch (UnsupportedFlavorException | IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }


    private static class TransferableCard implements Transferable {
        private final Card card;
        private final DataFlavor[] flavors = {new DataFlavor(Card.class, "Card")};

        public TransferableCard(Card card) {
            this.card = card;
        }

        @Override
        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
            if (flavor.equals(flavors[0])) {
                return card;
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
