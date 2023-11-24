package org.example.model;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class MenuTableModel extends AbstractTableModel {
    private final List<Menu> menus;
    private final String[] columnNames = {"메뉴 이름", "가격", "메뉴 설명", "카테고리"};

    public MenuTableModel(List<Menu> menus) {
        this.menus = menus;
    }

    @Override
    public int getRowCount() {
        return menus.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Menu menu = menus.get(rowIndex);

        return switch (columnIndex) {
            case 0 -> menu.getName();
            case 1 -> menu.getPrice();
            case 2 -> menu.getDescription();
            case 3 -> menu.getCategory();
            default -> null;
        };
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 1) {
            return Integer.class;
        }
        return super.getColumnClass(columnIndex);
    }
}

