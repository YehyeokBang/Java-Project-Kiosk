package org.example.view;

import org.example.model.Menu;
import org.example.model.OrderManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class PayView extends JFrame {
    private JPanel menuPanel;
    private JLabel totalPriceLabel;
    private final OrderView orderView;
    private final OrderManager orderManager;

    public PayView(OrderManager orderManager, OrderView orderView) {
        super("결제하기");

        this.orderManager = orderManager;
        initUI();
        this.orderView = orderView;

        setSize(900, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // 화면 중앙에 표시
        setVisible(true);
    }

    private void initUI() {
        menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));

        updateMenuPanel();

        JScrollPane scrollPane = new JScrollPane(menuPanel);

        totalPriceLabel = new JLabel("총 가격: " + orderManager.calculateTotalPrice() + "원");

        JButton cancelButton = new JButton("이전");
        cancelButton.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PayView.this.dispose();
            }
        });

        JButton payButton = new JButton("결제하기");
        payButton.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        payButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                orderManager.getOrders().clear();
                orderView.dispose();
                PayView.this.dispose();

                SwingUtilities.invokeLater(CardView::new);  // EDT에서 실행되도록 변경
            }
        });

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(totalPriceLabel, BorderLayout.NORTH);
        add(cancelButton, BorderLayout.SOUTH);
        add(payButton, BorderLayout.EAST);
    }

    private void updateMenuPanel() {
        SwingUtilities.invokeLater(() -> {
            menuPanel.removeAll(); // 기존의 모든 컴포넌트를 제거

            Map<Menu, Integer> orders = orderManager.getOrders();
            for (Menu menu : orders.keySet()) {
                JPanel menuEntry = createMenuEntryPanel(menu, orders.get(menu));
                menuPanel.add(menuEntry);
            }

            menuPanel.revalidate(); // 새로운 컴포넌트를 추가한 후 다시 그리기
            menuPanel.repaint(); // 다시 그리기
        });
    }

    private JPanel createMenuEntryPanel(Menu menu, int quantity) {
        JPanel menuEntryPanel = new JPanel();
        menuEntryPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        menuEntryPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        menuEntryPanel.setPreferredSize(new Dimension(800, 80));

        JLabel nameLabel = new JLabel(menu.getName());
        nameLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20));

        JLabel priceLabel = new JLabel("단가: " + menu.getPrice() + "원");
        priceLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 16));

        JLabel quantityLabel = new JLabel("수량: " + quantity);
        quantityLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 16));

        JButton minusButton = new JButton("-");
        minusButton.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        minusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (quantity > 1) {
                    orderManager.addOrder(menu, -1);
                    updateMenuPanel();
                    updateTotalPrice();
                } else {
                    orderManager.removeOrder(menu);
                    updateMenuPanel();
                    updateTotalPrice();
                }
            }
        });

        JButton plusButton = new JButton("+");
        plusButton.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        plusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                orderManager.addOrder(menu, 1);
                updateMenuPanel();
                updateTotalPrice();
            }
        });

        menuEntryPanel.add(nameLabel);
        menuEntryPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        menuEntryPanel.add(priceLabel);
        menuEntryPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        menuEntryPanel.add(quantityLabel);
        menuEntryPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        menuEntryPanel.add(minusButton);
        menuEntryPanel.add(plusButton);

        return menuEntryPanel;
    }

    private void updateTotalPrice() {
        totalPriceLabel.setText("총 가격: " + orderManager.calculateTotalPrice() + "원");
    }
}
