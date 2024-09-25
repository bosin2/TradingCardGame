package Game.view;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.io.File; // File 클래스 import 추가


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

    public start_intro() {
        backgroundImage = new ImageIcon("src/Game/assets/background/black.jpg").getImage(); // 이미지 경로 수정
        initUI();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this); // 패널 크기에 맞게 이미지 그리기
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
        dialogues.add("(나에게이런기회가...?)");
        dialogues.add("(꿀꺽,)교수님들...! 제가 완승한다면 A, A로 주세욧...! 혹시, A+도?");
        dialogues.add("교수진 : 그래 알겠네, ...뭐? A+ ? ");
        dialogues.add("그럼 제가 교수님들 마음에 탕!탕! 카드게임..! 하러 갑시다!");


        // UI 요소 설정
        dialogueLabel = new JLabel();
     // 폰트를 불러오는 코드 추가
        try {
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/Game/assets/fonts/Galmuri14.ttf")).deriveFont(24f); // 24pt 크기
            dialogueLabel.setFont(customFont);
        } catch (Exception e) {
            e.printStackTrace(); // 폰트를 불러오지 못했을 경우
            dialogueLabel.setFont(new Font("Serif", Font.PLAIN, 30)); // 기본 폰트 사용
        }

     // 패널 및 레이아웃 설정
        setLayout(new BorderLayout());
        dialogueLabel.setBorder(BorderFactory.createEmptyBorder(20, 100, 150, 20));
        setLayout(new BorderLayout());
        add(dialogueLabel, BorderLayout.SOUTH);
        dialogueLabel.setForeground(Color.WHITE); // 텍스트 색상 설정

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
                        dialogueLabel.setText("대화가 끝났습니다.");
                    }
                }
            }
        });

        // 첫 대사 출력 시작
        startDialogue();
    }

    private void startDialogue() {
        currentDialogue = dialogues.get(currentDialogueIndex);
        charIndex = 0;
        isFullTextDisplayed = false;
        dialogueLabel.setText("");

        timer = new Timer(100, e -> {
            if (charIndex < currentDialogue.length()) {
                dialogueLabel.setText(dialogueLabel.getText() + currentDialogue.charAt(charIndex));
                charIndex++;
            } else {
                timer.stop();
                isFullTextDisplayed = true;
            }
        });
        timer.start();
    }
}
