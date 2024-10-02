package Game.card;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.net.URL;
import java.awt.image.BufferedImage;


public class CardUI extends JPanel {
    private Card card;
    private Image cardImage;
    private Font customFont;
    private JLabel nameLabel;
    private JLabel tagLabel;
    private JLabel attackLabel;
    private JLabel healthLabel;
    private static final int CARD_WIDTH = 50;
    private static final int CARD_HEIGHT = 75;


    public CardUI(Card card) {
        this.card = card;
        setLayout(null);  // 절대 위치 레이아웃 설정

        // 사용자 지정 폰트 로드
        loadCustomFont();

        // 카드 이미지 로드
        loadCardImage();

        // 패널 크기 설정
     // 수정된 코드
        setPreferredSize(new Dimension(100, 150));

        // 카드 이미지 표시를 위한 JLabel 생성
        Image scaledImage = cardImage.getScaledInstance(100, 150, Image.SCALE_SMOOTH);
        JLabel cardImageLabel = new JLabel(new ImageIcon(scaledImage));
        cardImageLabel.setBounds(0, 0, cardImage.getWidth(this),
                                 cardImage.getHeight(this));
        add(cardImageLabel);

        // 텍스트 라벨들 설정
        setupLabels();

        // 라벨들을 패널에 추가
        add(nameLabel);
        add(tagLabel);
        add(attackLabel);
        add(healthLabel);

        // 라벨들의 배경을 투명하게 설정
        nameLabel.setOpaque(false);
        tagLabel.setOpaque(false);
        attackLabel.setOpaque(false);
        healthLabel.setOpaque(false);
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
            cardImage = new BufferedImage(100, 150,
                                          BufferedImage.TYPE_INT_ARGB);
            Graphics g = cardImage.getGraphics();
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(0, 0, 100, 150);
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
        attackLabel.setBounds(10, CARD_HEIGHT - 40, 50, 30);

        // 카드 체력 라벨
        healthLabel = new JLabel(String.valueOf(card.getHealth()));
        healthLabel.setForeground(Color.BLACK);
        healthLabel.setFont(customFont.deriveFont(16f));
        healthLabel.setBounds(CARD_WIDTH - 60, CARD_HEIGHT - 40, 50, 30);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // 카드 이미지를 그리지 않아도 JLabel로 표시되므로 필요 없음
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(100, 150); // 원하는 카드 크기

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

}

