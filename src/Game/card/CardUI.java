package Game.card;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.URL;
import java.awt.image.BufferedImage;

public class CardUI extends JPanel implements Serializable {
    private static final long serialVersionUID = 1L;

    private Card card;
    private transient Image cardImage;
    private transient Font customFont;
    private JLabel nameLabel;
    private JLabel tagLabel;
    private JLabel attackLabel;
    private JLabel healthLabel;
    private JLabel costLabel;
    private static final int CARD_WIDTH = 150;
    private static final int CARD_HEIGHT = 200;
    private void addClickListenerForZoom() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 팝업 창 생성
                JDialog dialog = new JDialog();
                dialog.setTitle(card.getName() + " - 카드 확대 보기");
                dialog.setModal(true);
                dialog.setSize(new Dimension(400, 600));
                dialog.setLocationRelativeTo(null);

                // 확대된 카드 이미지
                Image scaledImage = cardImage.getScaledInstance(400, 600, Image.SCALE_SMOOTH);
                JLabel cardImageLabel = new JLabel(new ImageIcon(scaledImage));
                cardImageLabel.setBounds(0, 0, 400, 600); // 이미지의 위치와 크기 설정

                // JLayeredPane 사용하여 이미지와 라벨들을 층으로 쌓기
                JLayeredPane layeredPane = new JLayeredPane();
                layeredPane.setPreferredSize(new Dimension(400, 600));

                // 카드 이미지 추가 (가장 뒤에)
                cardImageLabel.setBounds(0, 0, 400, 600); 
                layeredPane.add(cardImageLabel, Integer.valueOf(0)); // 이미지가 뒤쪽

                // 카드 정보 라벨 설정 및 위치 지정
                JLabel costLabel = new JLabel("" + card.getCost(), SwingConstants.LEFT);
                costLabel.setFont(customFont.deriveFont(20f));
                costLabel.setForeground(Color.BLACK);
                costLabel.setBounds(10, 10, 100, 30); // 코스트는 왼쪽 상단에 위치

                JLabel nameLabel = new JLabel("" + card.getName(), SwingConstants.RIGHT);
                nameLabel.setFont(customFont.deriveFont(20f));
                nameLabel.setForeground(Color.BLACK);
                nameLabel.setBounds(220, 10, 170, 30); // 이름은 오른쪽 상단에 위치

                JLabel attackLabel = new JLabel("" + card.getAttack(), SwingConstants.LEFT);
                attackLabel.setFont(customFont.deriveFont(20f));
                attackLabel.setForeground(Color.BLACK);
                attackLabel.setBounds(10, 560, 100, 30); // 공격력은 왼쪽 하단에 위치

                JLabel healthLabel = new JLabel("" + card.getHealth(), SwingConstants.RIGHT);
                healthLabel.setFont(customFont.deriveFont(20f));
                healthLabel.setForeground(Color.BLACK);
                healthLabel.setBounds(290, 560, 100, 30); // 체력은 오른쪽 하단에 위치

                JLabel tagLabel = new JLabel("" + card.getTag(), SwingConstants.CENTER);
                tagLabel.setFont(customFont.deriveFont(18f));
                tagLabel.setForeground(Color.BLACK);
                tagLabel.setBounds(100, 500, 200, 30); // 태그는 중앙 아랫쪽에 위치

                JLabel descriptionLabel = new JLabel("" + card.getDescription() + "", SwingConstants.CENTER);
                descriptionLabel.setFont(customFont.deriveFont(16f));
                descriptionLabel.setForeground(Color.BLACK);
                descriptionLabel.setBounds(20, 350, 360, 140); // 설명은 태그보다 위에 위치하며 문장이 전부 나오도록 설정

                // 정보 라벨들을 layeredPane에 추가 (이미지 위에 위치)
                layeredPane.add(costLabel, Integer.valueOf(1));
                layeredPane.add(nameLabel, Integer.valueOf(1));
                layeredPane.add(attackLabel, Integer.valueOf(1));
                layeredPane.add(healthLabel, Integer.valueOf(1));
                layeredPane.add(tagLabel, Integer.valueOf(1));
                layeredPane.add(descriptionLabel, Integer.valueOf(1));

                // 다이얼로그에 layeredPane 추가
                dialog.add(layeredPane);
                dialog.setVisible(true);
            }
        });
    }


    public CardUI(Card card) {
        this.card = card;
        setLayout(null);  // 절대 위치 레이아웃 설정

        // 사용자 지정 폰트 로드
        loadCustomFont();

        // 카드 이미지 로드
        loadCardImage();

        // 패널 크기 설정
        setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));

        // 카드 이미지 표시를 위한 JLabel 생성
        Image scaledImage = cardImage.getScaledInstance(CARD_WIDTH, CARD_HEIGHT, Image.SCALE_SMOOTH);
        JLabel cardImageLabel = new JLabel(new ImageIcon(scaledImage));
        cardImageLabel.setBounds(0, 0, CARD_WIDTH, CARD_HEIGHT);
        add(cardImageLabel);

        // 텍스트 라벨들 설정
        setupLabels();

        // 라벨들을 패널에 추가
        add(nameLabel);
        add(tagLabel);
        add(attackLabel);
        add(healthLabel);
        add(costLabel);

        
        add(cardImageLabel, Integer.valueOf(0));

        // 라벨들의 배경을 투명하게 설정
        nameLabel.setOpaque(false);
        tagLabel.setOpaque(false);
        attackLabel.setOpaque(false);
        healthLabel.setOpaque(false);
        costLabel.setOpaque(false);

        
        addClickListenerForZoom();

    }

    private void loadCardImage() {
        String imagePath = card.getImage();
        URL imageUrl = getClass().getResource(imagePath);

        if (imageUrl != null) {
            cardImage = new ImageIcon(imageUrl).getImage();
            System.out.println("카드 이미지 로드 성공: " + imagePath);
        } else {
            System.out.println("카드 이미지 로드 실패: " + imagePath);
            // 기본 이미지로 대체
            cardImage = new BufferedImage(CARD_WIDTH, CARD_HEIGHT, BufferedImage.TYPE_INT_ARGB);
            Graphics g = cardImage.getGraphics();
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(0, 0, CARD_WIDTH, CARD_HEIGHT);
            g.dispose();
        }
    }

    private void loadCustomFont() {
        try {
            InputStream fontStream = getClass()
                .getResourceAsStream("/resources/fonts/Galmuri7.ttf");
            if (fontStream != null) {
                customFont = Font.createFont(Font.TRUETYPE_FONT,
                                             fontStream);
                System.out.println("폰트 로드 성공");
            } else {
                System.out.println("폰트 파일을 찾을 수 없습니다.");
                customFont = new Font("Serif", Font.PLAIN, 12);
            }
        } catch (Exception e) {
            e.printStackTrace();
            customFont = new Font("Serif", Font.PLAIN, 12);
        }
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        // 직렬화에서 제외된 필드를 다시 로드
        loadCardImage();
        loadCustomFont();
    }

    private void setupLabels() {
        // 카드 이름 라벨
        nameLabel = new JLabel(card.getName(), SwingConstants.CENTER);
        nameLabel.setForeground(Color.BLACK);
        nameLabel.setFont(customFont.deriveFont(14f));
        nameLabel.setBounds(0, 10, CARD_WIDTH, 20);

        // 카드 태그 라벨
        tagLabel = new JLabel(card.getTag(), SwingConstants.CENTER);
        tagLabel.setForeground(Color.BLACK);
        tagLabel.setFont(customFont.deriveFont(14f));
        tagLabel.setBounds(0, CARD_HEIGHT / 2 + 40, CARD_WIDTH, 20);

        // 카드 공격력 라벨
        attackLabel = new JLabel(String.valueOf(card.getAttack()));
        attackLabel.setForeground(Color.BLACK);
        attackLabel.setFont(customFont.deriveFont(16f));
        attackLabel.setBounds(15, CARD_HEIGHT - 40, 50, 30);

        // 카드 체력 라벨
        healthLabel = new JLabel(String.valueOf(card.getHealth()));
        healthLabel.setForeground(Color.BLACK);
        healthLabel.setFont(customFont.deriveFont(16f));
        healthLabel.setBounds(CARD_WIDTH - 50, CARD_HEIGHT - 40, 50, 30);
        
        costLabel = new JLabel(String.valueOf(card.getCost()));
        costLabel.setForeground(Color.BLACK);
        costLabel.setFont(customFont.deriveFont(16f));
        costLabel.setBounds(10, 10, 50, 30);
        
        
    }
    
    public void resetFont() {
        nameLabel.setFont(customFont.deriveFont(14f));
        tagLabel.setFont(customFont.deriveFont(14f));
        attackLabel.setFont(customFont.deriveFont(16f));
        healthLabel.setFont(customFont.deriveFont(16f));
        costLabel.setFont(customFont.deriveFont(16f));

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // 카드 이미지를 그리지 않아도 JLabel로 표시되므로 필요 없음
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(CARD_WIDTH, CARD_HEIGHT);

    }

    // 체력 업데이트 메서드
    public void updateHealth(int newHealth) {
        healthLabel.setText(String.valueOf(newHealth));
    }

    // 공격력 업데이트 메서드
    public void updateAttack(int newAttack) {
        attackLabel.setText(String.valueOf(newAttack));
    }
    public Card getCard() {
        return card;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CardUI cardUI = (CardUI) obj;
        return card.equals(cardUI.card);
    }

    @Override
    public int hashCode() {
        return card.hashCode();
    }
}
