package Game.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;


public class start_intro extends JPanel {

    private static final long serialVersionUID = 1L;
    private JLabel dialogueLabel;
    private List<String> dialogues;
    private int currentDialogueIndex = 0;
    private boolean isFullTextDisplayed = false;
    private Timer timer;
    private String currentDialogue;
    private int charIndex = 0;
    private Image backgroundImage;
    private MainFrame mainFrame;

    private JButton nextButton;

    public start_intro(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

        // 배경 이미지 로드
        backgroundImage = new ImageIcon(getClass().getResource("/resources/background/black.jpg")).getImage();

        // 레이아웃 설정
        setLayout(null);

        // UI 초기화
        initUI();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // 패널 크기에 맞게 이미지 그리기
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }

    private void initUI() {

        // 대사 리스트 설정
        dialogues = new ArrayList<>();
        dialogues.add("하아... 이번 시험도 망쳤어...");
        dialogues.add("...");
        dialogues.add("이번에도 망치면 진짜 학사경고야...");
        dialogues.add("어떡하지...하아");
        dialogues.add("??? : 임동현군 ?");
        dialogues.add("하아...");
        dialogues.add("??? : 임동현군 맞나요 ?");
        dialogues.add("헉! 교,교수님?");
        dialogues.add("(교수님들이 왜 여기에...?)");
        dialogues.add("교수진 : 다름이 아니고 임동현군의 성적이 처참해서...");
        dialogues.add("네...?");
        dialogues.add("교수진 : 그래서 내부회의를 통해 동현군에게 기회를 주기로 했어요");
        dialogues.add("네...????");
        dialogues.add("교수진 : 우리랑 카드게임을 해서 승리하면, 성적을 올려주기로 했네");
        dialogues.add("(나에게 이런 기회가...?)");
        dialogues.add("(꿀꺽,)교수님들...! 제가 완승한다면 A, A로 주세욧...! 혹시, A+도?");
        dialogues.add("교수진 : 그래 알겠네, ...뭐? A+ ?");
        dialogues.add("그럼 제가 교수님들 마음에 탕!탕! 카드게임..! 하러 갑시다!");
        dialogues.add("(하아..근데 무슨 수로 교수님들을 이기지...)");

        // 대화 라벨 설정
        dialogueLabel = new JLabel();
        dialogueLabel.setForeground(Color.WHITE);
        dialogueLabel.setVerticalAlignment(SwingConstants.BOTTOM);
        dialogueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        dialogueLabel.setBounds(100, 500, 1200, 200); // 위치와 크기 설정

        // 사용자 지정 폰트 로드
        try {
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/resources/fonts/Galmuri14.ttf")).deriveFont(24f);
            dialogueLabel.setFont(customFont);
        } catch (Exception e) {
            e.printStackTrace();
            dialogueLabel.setFont(new Font("Serif", Font.PLAIN, 30));
        }

        // 버튼 설정
        nextButton = new JButton("다음으로");
        nextButton.setBounds(1100, 650, 150, 50); // 위치와 크기 설정
        nextButton.setVisible(false); // 초기에는 보이지 않도록 설정

        // 버튼 클릭 시 view_fight로 전환
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.showView("fight");
            }
        });

        // 컴포넌트 추가
        add(dialogueLabel);
        add(nextButton);

        // 패널에 마우스 클릭 이벤트 추가
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!isFullTextDisplayed) {
                    timer.stop();
                    dialogueLabel.setText(currentDialogue);
                    isFullTextDisplayed = true;
                } else {
                    currentDialogueIndex++;
                    if (currentDialogueIndex < dialogues.size()) {
                        startDialogue();
                    } else {
                        // 대사가 모두 끝나면 버튼을 보이게 함
                        nextButton.setVisible(true);
                    }
                }
            }
        });

        // 첫 번째 대화 시작
        startDialogue();
    }

    private void startDialogue() {
        currentDialogue = dialogues.get(currentDialogueIndex);
        charIndex = 0;
        isFullTextDisplayed = false;
        dialogueLabel.setText("");

        timer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (charIndex < currentDialogue.length()) {
                    dialogueLabel.setText(dialogueLabel.getText() + currentDialogue.charAt(charIndex));
                    charIndex++;
                } else {
                    timer.stop();
                    isFullTextDisplayed = true;
                }
            }
        });
        timer.start();
    }
}
