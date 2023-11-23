package org.example.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.example.model.Menu;
import org.example.model.OrderManager;

public class OrderView extends JFrame {
    private JLabel nameLabel;
    private JLabel descriptionLabel;
    private JLabel priceLabel;
    private JLabel quantityLabel;
    private JButton buyButton;
    private JButton addToCartButton;
    private JButton previousButton;

    private Menu menu;
    private final OrderManager orderManager;
    private int quantity = 1;
    private int pricePerItem = 0;

    public OrderView(Menu menu, OrderManager orderManager) {
        super("구매하기");

        this.menu = menu;
        this.orderManager = orderManager;
        pricePerItem = this.menu.getPrice();

        // UI 요소 초기화
        initUI(menu.getName(), menu.getDescription());

        setSize(700, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // 화면 중앙에 표시
        setVisible(true);
    }

    private void initUI(String itemName, String itemDescription) {
        Container container = getContentPane();
        container.setLayout(new BorderLayout()); // BorderLayout으로 변경

        // 상단 패널
        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0)); // 위아래 여백 추가
        container.add(topPanel, BorderLayout.NORTH);

        // 메뉴 정보 표시 레이블
        nameLabel = new JLabel(itemName);
        nameLabel.setFont(new Font("맑은 고딕", Font.BOLD, 30));
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.add(nameLabel);

        descriptionLabel = new JLabel(itemDescription);
        descriptionLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 20));
        descriptionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.add(descriptionLabel);

        // 중단 패널 (수량 조절 및 주문 가격)
        JPanel centerPanel = new JPanel(new FlowLayout());
        container.add(centerPanel, BorderLayout.CENTER);

        // 메뉴 이미지
        JPanel imagePanel = new JPanel();
        imagePanel.setLayout(new BoxLayout(imagePanel, BoxLayout.Y_AXIS));
        centerPanel.add(imagePanel);

        ImageIcon imageIcon = new ImageIcon(menu.getImage().getScaledInstance(300, 400, Image.SCALE_SMOOTH));
        JLabel imageLabel = new JLabel(imageIcon);
        imagePanel.add(imageLabel);

        // 수량 조절 및 주문 가격 정보를 담은 패널
        JPanel quantityPricePanel = new JPanel();
        quantityPricePanel.setLayout(new BoxLayout(quantityPricePanel, BoxLayout.Y_AXIS));
        centerPanel.add(quantityPricePanel);

        // 수량 조절 버튼
        JPanel quantityPanel = new JPanel(new FlowLayout());
        quantityLabel = new JLabel("수량: " + quantity + "개");
        quantityLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 20)); // 폰트 크기 키우기
        quantityPanel.add(quantityLabel);

        JButton minusButton = new JButton("-");
        minusButton.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        minusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (quantity > 1) {
                    quantity--;
                    updateQuantityAndPrice();
                }
            }
        });

        JButton plusButton = new JButton("+");
        plusButton.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        plusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                quantity++;
                updateQuantityAndPrice();
            }
        });

        quantityPanel.add(minusButton);
        quantityPanel.add(plusButton);

        quantityPricePanel.add(quantityPanel);

        // 주문 가격 정보
        priceLabel = new JLabel("가격: " + pricePerItem + "원");
        priceLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 20)); // 폰트 크기 키우기
        quantityPricePanel.add(priceLabel);

        // 하단 패널 (버튼)
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 10));
        container.add(bottomPanel, BorderLayout.SOUTH);

        addToCartButton = new JButton("담고 더 둘러보기");
        addToCartButton.setPreferredSize(new Dimension(200, 60));
        addToCartButton.setFont(new Font("맑은 고딕", Font.BOLD, 25)); // 폰트 크기 키우기
        bottomPanel.add(addToCartButton);

        previousButton = new JButton("이전");
        previousButton.setPreferredSize(new Dimension(130, 60));
        previousButton.setFont(new Font("맑은 고딕", Font.BOLD, 25)); // 폰트 크기 키우기
        bottomPanel.add(previousButton);

        buyButton = new JButton("구매하기");
        buyButton.setPreferredSize(new Dimension(130, 60));
        buyButton.setFont(new Font("맑은 고딕", Font.BOLD, 25)); // 폰트 크기 키우기
        bottomPanel.add(buyButton);

        // 이벤트 핸들러 등록
        registerEventHandlers();
    }

    private void registerEventHandlers() {
        addToCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // "담고 더 둘러보기" 버튼 클릭 시 추가 기능 구현
                // 여기에 장바구니에 해당 아이템을 추가하는 등의 로직을 추가하면 됩니다.
                orderManager.addOrder(menu, quantity);

                // OrderView를 숨김
                OrderView.this.setVisible(false);
            }
        });

        previousButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OrderView.this.dispose();
            }
        });

        buyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                orderManager.addOrder(menu, quantity);

                // OrderView.this를 명시적으로 사용하여 OrderView 인스턴스 참조
                new PayView(orderManager, OrderView.this);
            }
        });
    }

    private void updateQuantityAndPrice() {
        quantityLabel.setText("수량: " + quantity + "개");
        int totalPrice = quantity * pricePerItem;
        priceLabel.setText("가격: " + totalPrice + "원");
    }
}