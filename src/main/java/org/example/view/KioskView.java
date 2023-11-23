package org.example.view;

import org.example.mapper.MenuLoader;
import org.example.model.Category;
import org.example.model.Menu;
import org.example.model.OrderManager;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static org.example.model.Category.CATEGORY_1;
import static org.example.model.Category.CATEGORY_2;

public class KioskView extends JFrame {
    private boolean isMenuSizeLarge = false;
    public boolean isTakeOutChecked = false;
    public boolean isTakeOut = false;
    private String category = CATEGORY_1.getName();
    private final Font buttonFont = new Font("맑은 고딕", Font.BOLD, 16);
    private final OrderManager orderManager = new OrderManager();
    private final List<Menu> menus = MenuLoader.loadMenuFromExcel("src/main/resources/menu.xlsx");

    public KioskView() {
        super("Kiosk");
        setLayout(null);

        Container c = getContentPane();
        c.setLayout(null);
        setLocationRelativeTo(null);

        Dimension buttonSize = new Dimension(180, 60);
        Font buttonFont = new Font("맑은 고딕", Font.BOLD, 20);

        JPanel header = new JPanel();
        header.setSize(1200, 100);
        header.setLayout(null);
        header.setVisible(true);
        header.setBackground(new Color(0x7F6AFF));
        c.add(header);

        JButton adminButton = new JButton("관리자 설정");
        adminButton.setSize(100, 50);
        adminButton.setLocation(1075, 25);
        adminButton.setFont(new Font("맑은 고딕", Font.BOLD, 15));

        adminButton.setVisible(true);
        header.add(adminButton);

        JPanel menuBody = new JPanel();
        menuBody.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        menuBody.setSize(1200, 710);
        menuBody.setLocation(0, 170);
        menuBody.setVisible(true);
        menuBody.setBackground(new Color(0xefefef));

        JPanel categoryPanel = new JPanel();
        categoryPanel.setSize(1200, 70);
        categoryPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        categoryPanel.setLocation(0, 100);
        categoryPanel.setBackground(new Color(0xB9AAF5));
        categoryPanel.setVisible(true);
        JButton categoryButton1 = getCategoryButton(CATEGORY_1, menuBody);
        categoryPanel.add(categoryButton1);

        JButton categoryButton2 = getCategoryButton(CATEGORY_2, menuBody);
        categoryPanel.add(categoryButton2);

        c.add(categoryPanel);

        displayMenuButtons(menuBody);

        JScrollPane scrollPane = new JScrollPane(menuBody);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setSize(1200, 710);
        scrollPane.setLocation(0, 170);
        scrollPane.setVisible(true);

        c.add(scrollPane);

        JPanel footer = new JPanel();
        footer.setSize(1200, 120);
        footer.setLayout(new FlowLayout(FlowLayout.CENTER, 110, 20));
        footer.setLocation(0, 880);
        footer.setVisible(true);
        footer.setBackground(new Color(0xB9AAF5));
        c.add(footer);

        JButton callButton = new JButton("직원 호출");
        callButton.setPreferredSize(buttonSize);
        callButton.setFont(buttonFont);
        callButton.setBackground(new Color(0xECE9FF));
        callButton.setVisible(true);
        footer.add(callButton);

        callButton.addActionListener(e -> {
            JDialog dialog = new JDialog(this, "직원 호출", true);
            dialog.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 30));

            JButton callButton1 = new JButton("직원 호출");
            callButton1.setPreferredSize(new Dimension(200, 200));
            callButton1.setFont(new Font("맑은 고딕", Font.BOLD, 30));
            JButton cancelButton = new JButton("취소");
            cancelButton.setPreferredSize(new Dimension(200, 200));
            cancelButton.setFont(new Font("맑은 고딕", Font.BOLD, 30));

            callButton1.addActionListener(e1 -> {
                dialog.dispose(); // 다이얼로그 닫기
                JDialog dialog1 = new JDialog(this, "직원 호출", true);
                dialog1.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 30));

                JLabel label = new JLabel("직원이 호출되었습니다.");
                label.setFont(new Font("맑은 고딕", Font.BOLD, 30));
                dialog1.add(label);

                dialog1.setSize(400, 140);
                dialog1.setLocationRelativeTo(this);
                dialog1.setVisible(true);

                // 10초 뒤 사라짐
                Timer timer = new Timer(8000, e2 -> {
                    dialog1.dispose();
                });
                timer.setRepeats(false);
                timer.start();
            });

            cancelButton.addActionListener(e1 -> {
                dialog.dispose(); // 다이얼로그 닫기
            });

            dialog.add(callButton1);
            dialog.add(cancelButton);

            // 다이얼로그 크기 및 위치 설정
            dialog.setSize(500, 350);
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
        });

        JButton menuSizeButton = new JButton("메뉴 크게 보기");
        menuSizeButton.setPreferredSize(buttonSize);
        menuSizeButton.setFont(buttonFont);
        menuSizeButton.setVisible(true);
        footer.add(menuSizeButton);

        menuSizeButton.addActionListener(e -> {
            isMenuSizeLarge = !isMenuSizeLarge;
            updateMenuSizeButtonText(menuSizeButton);
            displayMenuButtons(menuBody);
        });

        setSize(1200, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private JButton getCategoryButton(Category category, JPanel menuBody) {
        JButton categoryButton = new JButton(category.getName());
        categoryButton.setPreferredSize(new Dimension(180, 55));
        categoryButton.setFont(buttonFont);
        categoryButton.setVisible(true);

        categoryButton.addActionListener(e -> {
            changeCategory(category.getName());
            displayMenuButtons(menuBody);
        });

        return categoryButton;
    }

    private JButton createMenuButton(Menu menu, OrderManager orderManager) {
        // 이미지 로드 및 크기 조정
        ImageIcon image;
        if (isMenuSizeLarge) {
            image = new ImageIcon(menu.getImage().getScaledInstance(300, 400, Image.SCALE_SMOOTH));
        } else {
            image = new ImageIcon(menu.getImage().getScaledInstance(120, 170, Image.SCALE_SMOOTH));
        }

        JButton menuButton = new JButton("<html><b>" + menu.getName() + "</b><br>" +
                "<font size='4'>" + "</font><br>" + menu.getPrice() + "원<br>" + menu.getDescription() + "</html>");

        // 이미지 아이콘 설정
        menuButton.setIcon(image);

        if (isMenuSizeLarge) {
            menuButton.setFont(new Font("맑은 고딕", Font.BOLD, 30));
            menuButton.setPreferredSize(new Dimension(540, 650));
        } else {
            menuButton.setFont(new Font("맑은 고딕", Font.BOLD, 20));
            menuButton.setPreferredSize(new Dimension(380, 305));
        }

        menuButton.setFont(buttonFont);

        menuButton.addActionListener(e -> {
            if (!isTakeOutChecked) {
                JDialog dialog = new JDialog(this, "포장 여부 선택", true);
                dialog.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 30));

                JButton takeOutButton = new JButton("포장");
                takeOutButton.setPreferredSize(new Dimension(200, 200));
                takeOutButton.setFont(new Font("맑은 고딕", Font.BOLD, 30));
                JButton eatInButton = new JButton("매장");
                eatInButton.setPreferredSize(new Dimension(200, 200));
                eatInButton.setFont(new Font("맑은 고딕", Font.BOLD, 30));

                takeOutButton.addActionListener(e1 -> {
                    isTakeOutChecked = true;
                    isTakeOut = true;
                    dialog.dispose(); // 다이얼로그 닫기
                    OrderView orderView = new OrderView(menu, orderManager);
                });

                eatInButton.addActionListener(e1 -> {
                    isTakeOutChecked = true;
                    isTakeOut = false;
                    dialog.dispose(); // 다이얼로그 닫기
                    OrderView orderView = new OrderView(menu, orderManager);
                });

                dialog.add(takeOutButton);
                dialog.add(eatInButton);

                // 다이얼로그 크기 및 위치 설정
                dialog.setSize(500, 350);
                dialog.setLocationRelativeTo(this);
                dialog.setVisible(true);
            } else {
                // 이미 포장 여부가 선택되었으면 바로 주문 화면 열기
                OrderView orderView = new OrderView(menu, orderManager);
            }
        });

        return menuButton;
    }

    private void displayMenuButtons(JPanel body) {
        body.removeAll();
        if (isMenuSizeLarge) {
            // 크게 보기 상태인 경우, FlowLayout 사용
            body.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
            for (Menu menu : menus) {
                if (menu.getCategory().equals(category)) {
                    JButton menuButton = createMenuButton(menu, orderManager);
                    body.add(menuButton);
                }
            }
        } else {
            // 작게 보기 상태인 경우, GridLayout 사용하여 2행으로 표시
            int rows = 2;
            int cols = (menus.size() + 1) / rows;  // 예외 처리 추가
            body.setLayout(new GridLayout(rows, cols, 20, 20));

            for (Menu menu : menus) {
                if (menu.getCategory().equals(category)) {
                    JButton menuButton = createMenuButton(menu, orderManager);
                    body.add(menuButton);
                }
            }
        }

        body.revalidate();
        body.repaint();
    }

    private void updateMenuSizeButtonText(JButton menuSizeButton) {
        if (isMenuSizeLarge) {
            menuSizeButton.setText("메뉴 작게 보기");
        } else {
            menuSizeButton.setText("메뉴 크게 보기");
        }
    }

    private void changeCategory(String category) {
        this.category = category;
    }
}
