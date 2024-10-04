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
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class view_fight extends JPanel {
	
    private MainFrame mainFrame;
    private boolean isDragging = false; // 드래그 앤 드롭 상태 플래그 추가
    private card_loder loader = new card_loder();
    private static final long serialVersionUID = 1L;
    private JPanel cardPanel;
    private JPanel battlefieldPanel;
    private List<Card> playerCards = new ArrayList<>();
    private List<Card> allCards = new ArrayList<>();
    private Image backgroundImage;
    private JPanel[] fieldSlots = new JPanel[5]; // 5개의 슬롯 패널
    private Card[] fieldCards = new Card[5]; // 각 슬롯에 배치된 카드


    public view_fight(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(null);
        setPreferredSize(new Dimension(1300, 800));
        setSize(new Dimension(1300, 800)); // 크기를 명시적으로 지정



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
        
        setLayout(null); // 절대 위치 사용

     // 배틀필드 패널 설정
        battlefieldPanel = new JPanel(new GridLayout(1, 5)); // 1행 5열의 그리드 레이아웃
        battlefieldPanel.setOpaque(false);
        battlefieldPanel.setBounds(50, 300, 770, 230); // 위치와 크기 설정
        add(battlefieldPanel);
                
        for (int i = 0; i < 5; i++) {
            JPanel slotPanel = new JPanel(null); // 슬롯마다 null 레이아웃 사용
            slotPanel.setOpaque(false);
            slotPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE)); // 슬롯 구분을 위한 테두리
            battlefieldPanel.add(slotPanel);
            fieldSlots[i] = slotPanel;
            
            final int slotIndex = i;
            slotPanel.setDropTarget(new DropTarget() {
                @Override
                public synchronized void drop(DropTargetDropEvent dtde) {
                    if (isDragging) {
                        // 드래그 앤 드롭 중복 방지
                        isDragging = false;
                    }
                    try {
                        // 슬롯에 이미 카드가 있으면 드롭 불가
                        if (fieldCards[slotIndex] != null) {
                            dtde.rejectDrop();
                            return;
                        }

                        dtde.acceptDrop(DnDConstants.ACTION_MOVE);
                        Transferable transferable = dtde.getTransferable();
                        CardUI droppedCardUI = (CardUI) transferable.getTransferData(new DataFlavor(CardUI.class, "CardUI"));

                        // 손패에서 해당 카드 제거
                        Card droppedCard = droppedCardUI.getCard();
                        boolean removed = playerCards.remove(droppedCard);
                        if (removed) {
                            System.out.println("카드손패에서제거성공: " + droppedCard.getName());
                            displayPlayerCards();
                        } else {
                            System.out.println("카드를 손패에서 찾을 수 없습니다.");
                        }

                        // 슬롯에 카드 추가
                        slotPanel.add(droppedCardUI);
                        droppedCardUI.setBounds(0, 0, droppedCardUI.getPreferredSize().width, droppedCardUI.getPreferredSize().height);
                        droppedCardUI.resetFont();

                        // 슬롯에 배치된 카드 기록
                        fieldCards[slotIndex] = droppedCard;

                        // 패널들 다시 그리기
                        slotPanel.revalidate();
                        slotPanel.repaint();

                    } catch (UnsupportedFlavorException | IOException ex) {
                        ex.printStackTrace();
                    } finally {
                        // 드래그 앤 드롭 상태 해제
                        isDragging = false;
                    }
                }
            });
        }

        
        JButton endTurnButton = new JButton("턴 종료");
        endTurnButton.setBounds(1000, 461, 100, 30);
        endTurnButton.setLayout(null); // 절대 위치 사용
        endTurnButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		drawCard();
        	}
        });
        add(endTurnButton);
        


        // 카드 패널 설정 (하단에 배치)
        cardPanel = new JPanel();
        cardPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        cardPanel.setOpaque(false);
        cardPanel.setBounds(0, 550, 1000, 500); // 위치와 크기 설정
        add(cardPanel);
        
        
                

        // 손패 카드 보여주기
        displayPlayerCards();
        System.out.println("카드보여주기완료");

        System.out.println("UI 불러오기 완료");
        revalidate();
        repaint();

    }

    private void displayPlayerCards() {
        cardPanel.removeAll();
        cardPanel.setLayout(null); // 패널 레이아웃을 null로 설정해서 절대 위치를 사용

        int cardWidth = 150; // 카드의 너비 (픽셀)
        int cardHeight = 200; // 카드의 높이 (픽셀)
        int maxVisibleWidth = cardPanel.getWidth(); // 카드 패널의 너비
        int cardCount = playerCards.size(); // 손패의 카드 수

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
            CardUI cardUI = new CardUI(playerCards.get(i));
            int xPosition = Math.min(i * spacing, maxVisibleWidth - cardWidth); // 카드가 잘리지 않도록 위치 조정
            cardUI.setBounds(xPosition, 0, cardWidth, cardHeight); // 카드 위치 설정
            enableCardDrag(cardUI);
            cardPanel.add(cardUI);
            System.out.println("손패에 카드 추가됨: " + playerCards.get(i).getName());

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
                            isDragging = false; // 드래그가 끝나면 플래그 해제
                        }
                    });
                } catch (InvalidDnDOperationException e) {
                    e.printStackTrace();
                    isDragging = false; // 예외 발생 시 플래그 해제
                }
            }
        });
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
