package Game.control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Game.view.view_main;
import java.awt.CardLayout;


public class Control {
    private view_main view;

    public Control(view_main view) {
        this.view = view;
    }

    public void initController() {
        view.getButton1().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.showstartintroView();
            }
        });

        view.getButton2().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("이어하기 버튼이 눌렸습니다.");
            }
        });

        view.getButton3().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.showManualView();

            }
        });
    }
}
