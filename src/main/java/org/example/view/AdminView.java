package org.example.view;

import org.example.mapper.MenuAdapter;
import org.example.mapper.MenuLoader;
import org.example.model.Menu;
import org.example.model.MenuTableModel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AdminView extends JFrame {
    private final List<Menu> menus;
    private final JTable table;
    private final KioskView kioskView;

    public AdminView(KioskView kioskView) {
        super("관리자 페이지");

        menus = MenuLoader.loadMenuFromExcel("src/main/resources/menu.xlsx");

        // 테이블 모델 생성
        MenuTableModel model = new MenuTableModel(menus);

        this.kioskView = kioskView;

        // 테이블 생성
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        JButton updateButton = new JButton("정보 업데이트");
        updateButton.addActionListener(e -> updateMenuInfo());

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(updateButton, BorderLayout.SOUTH);

        setSize(500, 500);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void updateMenuInfo() {
        int selectedRow = table.getSelectedRow();

        if (selectedRow != -1) {
            Menu menu = menus.get(selectedRow);

            // 예를 들어, 각 정보를 수정할 수 있는 다이얼로그를 띄우고 수정된 정보를 가져오는 로직을 추가하세요.
            // 여기서는 간단한 예제로 null을 반환하도록 합니다.
            Menu updatedMenu = getMenuInfoFromUser();

            if (updatedMenu != null) {
                menu.setName(updatedMenu.getName());
                menu.setPrice(updatedMenu.getPrice());
                menu.setDescription(updatedMenu.getDescription());
                menu.setCategory(updatedMenu.getCategory());

                MenuAdapter.updateMenuInfo(menus); // 엑셀에 정보 업데이트

                kioskView.menus = MenuLoader.loadMenuFromExcel("src/main/resources/menu.xlsx");
                kioskView.updateMenuButtons();
            }
        } else {
            JOptionPane.showMessageDialog(this, "메뉴를 선택하세요.", "경고", JOptionPane.WARNING_MESSAGE);
        }
    }

    private Menu getMenuInfoFromUser() {
        int selectedRow = table.getSelectedRow();

        if (selectedRow != -1) {
            Menu menu = menus.get(selectedRow);

            // 다이얼로그에서 수정할 정보를 입력받기
            String name = JOptionPane.showInputDialog(this, "메뉴 이름", menu.getName());
            if (name == null) {
                // 사용자가 취소를 누르면 null 반환
                return null;
            }

            String priceStr = JOptionPane.showInputDialog(this, "가격", menu.getPrice());
            if (priceStr == null) {
                return null;
            }
            int price;
            try {
                price = Integer.parseInt(priceStr);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "가격은 숫자로 입력하세요.", "오류", JOptionPane.ERROR_MESSAGE);
                return null;
            }

            String description = JOptionPane.showInputDialog(this, "메뉴 설명", menu.getDescription());
            if (description == null) {
                return null;
            }

            String category = JOptionPane.showInputDialog(this, "카테고리", menu.getCategory());
            if (category == null) {
                return null;
            }

            // 수정된 정보를 반환
            return new Menu(name, price, description, category, menu.getImage());
        } else {
            JOptionPane.showMessageDialog(this, "메뉴를 선택하세요.", "경고", JOptionPane.WARNING_MESSAGE);
            return null;
        }
    }
}
