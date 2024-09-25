package Game.view;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class start_intro extends JPanel {

    private static final long serialVersionUID = 1L;
    private JLabel dialogueLabel;
    private List<String> dialogues;
    private int currentDialogueIndex = 0;
    private boolean isFullTextDisplayed = false; // 전체 대사 출력 여부
    private Timer timer;
    private String currentDialogue;
    private int charIndex = 0;

    public start_intro() {
        initUI();
    }

    private void initUI() {
        // 대사 리스트 설정
        dialogues = new ArrayList<>();
        dialogues.add("수뭉이: 안녕, 주인! 반가워.");
        dialogues.add("수뭉이: 오늘은 뭐할거냥?");
        dialogues.add("주인: 게임을 만들고 있어.");

        // UI 요소 설정
        dialogueLabel = new JLabel();

        // 패널 및 레이아웃 설정
        setLayout(new BorderLayout());
        add(dialogueLabel, BorderLayout.CENTER);

        // 패널에 마우스 클릭 이벤트 추가
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!isFullTextDisplayed) {
                    // 대사가 완전히 출력되지 않았다면, 나머지 전체 대사 출력
                    timer.stop();
                    dialogueLabel.setText(currentDialogue);
                    isFullTextDisplayed = true;
                } else {
                    // 다음 대사로 넘어가기
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
        // 대사 초기화
        currentDialogue = dialogues.get(currentDialogueIndex);
        charIndex = 0;
        isFullTextDisplayed = false;
        dialogueLabel.setText("");

        // 타이머로 한 글자씩 출력
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
