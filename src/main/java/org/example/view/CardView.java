package org.example.view;

import javax.swing.*;
import java.awt.*;

public class CardView extends JFrame {
    String imagePath = "src/main/resources/card.jpg";

    public CardView() {
        super("Card");
        setLayout(null);
        setSize(500, 550);

        // 텍스트 표시
        JLabel textLabel = new JLabel("IC칩을 앞으로 한 상태로 카드를 넣어주세요.");
        textLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        textLabel.setForeground(Color.BLACK);
        textLabel.setHorizontalAlignment(SwingConstants.CENTER);
        textLabel.setVerticalAlignment(SwingConstants.CENTER);
        textLabel.setSize(500, 50);
        textLabel.setLocation(0, 400);
        textLabel.setOpaque(true);  // 배경을 표시하기 위해 true로 설정
        textLabel.setBackground(new Color(255, 255, 255, 200));  // 투명한 흰색 배경
        add(textLabel);

        // 이미지 표시
        ImageIcon imageIcon = new ImageIcon(imagePath);
        JLabel label = new JLabel(imageIcon);
        label.setSize(500, 400);
        label.setLocation(0, 0);
        add(label);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
