package Game.card;

import javax.swing.*;
import java.util.Queue;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.LinkedList;
import java.awt.image.BufferedImage;

public class CardUI extends JPanel implements Serializable {
    private static final long serialVersionUID = 1L;

    private Card card;
    private transient Image cardImage;
    private transient Font customFont;
    private Queue<Point> animationQueue = new LinkedList<Point>();
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
                dialog.setSize(new Dimension(315, 432));
                dialog.setLocationRelativeTo(null);

                // 확대된 카드 이미지
                Image scaledImage = cardImage.getScaledInstance(300, 400, Image.SCALE_SMOOTH);
                JLabel cardImageLabel = new JLabel(new ImageIcon(scaledImage));
                cardImageLabel.setBounds(0, 0, 300, 400); // 이미지의 위치와 크기 설정

                // JLayeredPane 사용하여 이미지와 라벨들을 층으로 쌓기
                JLayeredPane layeredPane = new JLayeredPane();
                layeredPane.setPreferredSize(new Dimension(300, 400));

                // 카드 이미지 추가 (가장 뒤에)
                cardImageLabel.setBounds(0, 0, 300, 400); 
                layeredPane.add(cardImageLabel, Integer.valueOf(0)); // 이미지가 뒤쪽

                // 카드 정보 라벨 설정 및 위치 지정
                JLabel costLabel = new JLabel("" + card.getCost());
                costLabel.setFont(customFont.deriveFont(30f));
                costLabel.setForeground(Color.BLACK);
                costLabel.setBounds(35, 30, 100, 50);

                String nameWithBreaks = "<html>" + card.getName().replace("\n", "<br>") + "</html>";
                nameLabel = new JLabel(nameWithBreaks);
                nameLabel.setFont(customFont.deriveFont(24f));
                nameLabel.setForeground(Color.BLACK);
                nameLabel.setBounds(50, 100, 280, 80);

                JLabel attackLabel = new JLabel("" + card.getAttack());
                attackLabel.setFont(customFont.deriveFont(30f));
                attackLabel.setForeground(Color.BLACK);
                attackLabel.setBounds(35, 330, 100, 50);

                JLabel healthLabel = new JLabel("" + card.getHealth());
                healthLabel.setFont(customFont.deriveFont(30f));
                healthLabel.setForeground(Color.BLACK);
                healthLabel.setBounds(215, 330, 100, 50);

                String tagWithBreaks = "<html>" + card.getTag().replace("\n", "<br>") + "</html>";
                JLabel tagLabel = new JLabel(tagWithBreaks);
                tagLabel.setFont(customFont.deriveFont(25f));
                tagLabel.setForeground(Color.BLACK);
                tagLabel.setBounds(125, 310, 200, 50);

                String WithBreaks = "<html>" + card.getDescription().replace("\n", "<br>") + "</html>";
                JLabel descriptionLabel = new JLabel(WithBreaks);
                descriptionLabel.setFont(customFont.deriveFont(16f));
                descriptionLabel.setForeground(Color.BLACK);
                descriptionLabel.setBounds(30, 170, 280, 140);

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
    	// 카드 이름 라벨: n <br>로 바꾸고 HTML 태그로 감싸기
        String nameWithBreaks = "<html>" + card.getName().replace("\n", "<br>") + "</html>";
        nameLabel = new JLabel(nameWithBreaks, SwingConstants.CENTER);
        nameLabel.setForeground(Color.BLACK);
        nameLabel.setFont(customFont.deriveFont(13f));
        nameLabel.setBounds(10, 25, CARD_WIDTH- 20, 80);

        // 카드 태그 라벨
        tagLabel = new JLabel(card.getTag(), SwingConstants.CENTER);
        tagLabel.setForeground(Color.BLACK);
        tagLabel.setFont(customFont.deriveFont(25f));
        tagLabel.setBounds(0, 115, CARD_WIDTH, 30);

        // 카드 공격력 라벨
        attackLabel = new JLabel(String.valueOf(card.getAttack()));
        attackLabel.setForeground(Color.BLACK);
        attackLabel.setFont(customFont.deriveFont(16f));
        attackLabel.setBounds(15, CARD_HEIGHT - 40, 50, 30);

        // 카드 체력 라벨
        healthLabel = new JLabel(String.valueOf(card.getHealth()));
        healthLabel.setForeground(Color.BLACK);
        healthLabel.setFont(customFont.deriveFont(16f));
        healthLabel.setBounds(105, CARD_HEIGHT - 40, 50, 30);
        
        //코스트
        costLabel = new JLabel(String.valueOf(card.getCost()));
        costLabel.setForeground(Color.BLACK);
        costLabel.setFont(customFont.deriveFont(18f));
        costLabel.setBounds(15, 10, 50, 30);
        
        
    }
    
    public void resetFont() {
        nameLabel.setFont(customFont.deriveFont(13f));
        tagLabel.setFont(customFont.deriveFont(25f));
        attackLabel.setFont(customFont.deriveFont(16f));
        healthLabel.setFont(customFont.deriveFont(16f));
        costLabel.setFont(customFont.deriveFont(18f));

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

    public void addAnimation(Point point) {
        animationQueue.add(point);
    }

    public Queue<Point> getAnimationQueue() {
        return animationQueue;
    }
    
    public void updateCardState(Card updatedCard) {
        this.card = updatedCard;
        healthLabel.setText("" + card.getHealth()); // 체력 업데이트
        revalidate();
        repaint();
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
