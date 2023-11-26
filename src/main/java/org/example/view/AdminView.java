package org.example.view;

import org.example.mapper.MenuAdapter;
import org.example.mapper.MenuLoader;
import org.example.model.Menu;
import org.example.model.MenuTableModel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class AdminView extends JFrame {
    private final List<Menu> menus;
    private final JTable table;
    private final KioskView kioskView;

    public AdminView(KioskView kioskView) {
        super("관리자 페이지");

        menus = MenuLoader.loadMenuFromExcel();

        // 테이블 모델 생성
        MenuTableModel model = new MenuTableModel(menus);

        this.kioskView = kioskView;

        // 테이블 생성
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        JButton exitButton = new JButton("이전");
        exitButton.addActionListener(e -> {
            this.dispose();
        });

        JButton addMenuButton = new JButton("메뉴 추가");
        addMenuButton.addActionListener(e -> {
            try {
                addMenu();
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });

        JButton updateImageButton = new JButton("이미지 수정");
        updateImageButton.addActionListener(e -> updateImage());

        JButton updateButton = new JButton("정보 업데이트");
        updateButton.addActionListener(e -> {
            try {
                updateMenuInfo();
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });

        JButton removeMenuButton = new JButton("메뉴 제거");
        removeMenuButton.addActionListener(e -> {
            try {
                removeMenu();
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });

        // 버튼 패널 생성 및 버튼 추가
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(exitButton);
        buttonPanel.add(addMenuButton);
        buttonPanel.add(updateImageButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(removeMenuButton);

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setSize(550, 500);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void addMenu() throws FileNotFoundException {
        // 사용자로부터 새 메뉴 정보를 입력받기
        String name = JOptionPane.showInputDialog(this, "새 메뉴 이름:");
        if (name == null) {
            return; // 사용자가 취소를 누르면 무시
        }

        String priceStr = JOptionPane.showInputDialog(this, "가격:");
        if (priceStr == null) {
            return;
        }
        int price;
        try {
            price = Integer.parseInt(priceStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "가격은 숫자로 입력하세요.", "오류", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String description = JOptionPane.showInputDialog(this, "설명:");
        if (description == null) {
            return;
        }

        String category = JOptionPane.showInputDialog(this, "카테고리:");
        if (category == null) {
            return;
        }

        // 실제로 메뉴를 추가
        Menu newMenu = new Menu(name, price, description, category, null); // 이미지는 null로 설정
        menus.add(newMenu);

        // MenuTableModel 업데이트
        MenuTableModel model = (MenuTableModel) table.getModel();
        model.fireTableDataChanged();
    }

    private void updateImage() {
        int selectedRow = table.getSelectedRow();

        if (selectedRow != -1) {
            Menu menu = menus.get(selectedRow);

            // 이미지 업데이트 로직
            try {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("이미지 선택");
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

                int result = fileChooser.showOpenDialog(this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    Image newImage = ImageIO.read(selectedFile);

                    menu.setImage(newImage);

                    // MenuTableModel 업데이트
                    MenuTableModel model = (MenuTableModel) table.getModel();
                    model.fireTableDataChanged();

                    MenuAdapter.updateMenuInfo(menus); // 엑셀에 정보 업데이트

                    // KioskView에서 변경 사항 반영
                    kioskView.menus = MenuLoader.loadMenuFromExcel();
                    kioskView.updateMenuButtons();
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "이미지를 업데이트하는 데 오류가 발생했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "메뉴를 선택하세요.", "경고", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void updateMenuInfo() throws FileNotFoundException {
        int selectedRow = table.getSelectedRow();

        if (selectedRow != -1) {
            Menu menu = menus.get(selectedRow);

            Menu updatedMenu = getMenuInfoFromUser();

            if (updatedMenu != null) {
                menu.setName(updatedMenu.getName());
                menu.setPrice(updatedMenu.getPrice());
                menu.setDescription(updatedMenu.getDescription());
                menu.setCategory(updatedMenu.getCategory());

                MenuAdapter.updateMenuInfo(menus); // 엑셀에 정보 업데이트

                kioskView.menus = MenuLoader.loadMenuFromExcel();
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

    private void removeMenu() throws FileNotFoundException {
        int selectedRow = table.getSelectedRow();

        if (selectedRow != -1) {
            int option = JOptionPane.showConfirmDialog(
                    this,
                    "선택한 메뉴를 제거하시겠습니까?",
                    "메뉴 제거",
                    JOptionPane.YES_NO_OPTION);

            if (option == JOptionPane.YES_OPTION) {
                menus.remove(selectedRow);

                // MenuTableModel 업데이트
                MenuTableModel model = (MenuTableModel) table.getModel();
                model.fireTableDataChanged();

                MenuAdapter.updateMenuInfo(menus); // 엑셀에 정보 업데이트

                // KioskView에서 변경 사항 반영
                kioskView.menus = MenuLoader.loadMenuFromExcel();
                kioskView.updateMenuButtons();
            }
        } else {
            JOptionPane.showMessageDialog(this, "메뉴를 선택하세요.", "경고", JOptionPane.WARNING_MESSAGE);
        }
    }
}
