package Game.button_manager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import Game.view.view_main;
import Game.view.MainFrame;

public class Mainmenu_button {
    private view_main view;
    private MainFrame mainFrame;

    public Mainmenu_button(view_main view, MainFrame mainFrame) {
        this.view = view;
        this.mainFrame = mainFrame;
    }

    public void initController() {
        setupButton(view.getButton1(), "intro");      // 시작하기
        setupButton(view.getButton2(), "continue");   // 이어하기
        setupButton(view.getButton3(), "manual");     // 설명서
    }

    private void setupButton(JButton button, String viewName) {
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (viewName) {
                    case "intro":
                        mainFrame.showView("intro");  // start_intro로 전환
                        break;
                    case "continue":
                        mainFrame.showView("continue");  // 이어하기 기능 구현
                        break;
                    case "manual":
                        mainFrame.showView("manual");  // view_manual로 전환
                        break;
                    default:
                        System.out.println("알 수 없는 뷰 이름: " + viewName);
                }
            }
        });
    }
}
