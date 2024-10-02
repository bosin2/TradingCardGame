package Game.control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Game.view.view_main;
import Game.view.MainFrame;

public class Control {
    private view_main view;
    private MainFrame mainFrame;

    public Control(view_main view, MainFrame mainFrame) {
        this.view = view;
        this.mainFrame = mainFrame;
    }

    public void initController() {
        view.getButton1().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.showView("intro"); // start_intro로 전환
            }
        });

        view.getButton2().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("이어하기 버튼이 눌렸습니다.");
                // 이어하기 기능 구현 필요
            }
        });

        view.getButton3().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.showView("manual"); // view_manual로 전환
            }
        });
    }
}
