package org.example.mapper;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.model.Menu;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;

public class MenuAdapter {
    private static final String FILE_PATH = "src/main/resources/menu.xlsx";

    public static void updateMenuImage(List<Menu> menus) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet();

            XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();

            for (int i = 0; i < menus.size(); i++) {
                Menu menu = menus.get(i);

                // 이미지가 변경되었다면 엑셀에 추가
                if (menu.getImage() != null) {
                    addImageToSheet(workbook, sheet, drawing, menu.getImage(), i);
                }

                // 나머지 정보 추가 (이름, 가격, 설명, 카테고리)
                Row row = sheet.createRow(i);
                row.createCell(4).setCellValue(menu.getName());
                row.createCell(5).setCellValue(menu.getPrice());
                row.createCell(6).setCellValue(menu.getDescription());
                row.createCell(7).setCellValue(menu.getCategory());
            }

            try (FileOutputStream fileOut = new FileOutputStream(FILE_PATH)) {
                workbook.write(fileOut);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateMenuInfo(List<Menu> menus) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet();

            XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();

            for (int i = 0; i < menus.size(); i++) {
                Menu menu = menus.get(i);

                // 이미지가 변경되었다면 엑셀에 추가
                if (menu.getImage() != null) {
                    addImageToSheet(workbook, sheet, drawing, menu.getImage(), i);
                }

                // 나머지 정보 추가 (이름, 가격, 설명, 카테고리)
                Row row = sheet.createRow(i);
                row.createCell(1).setCellValue(menu.getName());
                row.createCell(2).setCellValue(menu.getPrice());
                row.createCell(3).setCellValue(menu.getDescription());
                row.createCell(4).setCellValue(menu.getCategory());
            }

            try (FileOutputStream fileOut = new FileOutputStream(FILE_PATH)) {
                workbook.write(fileOut);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void addImageToSheet(Workbook workbook, Sheet sheet, XSSFDrawing drawing, Image image, int row) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write((BufferedImage) image, "png", baos);
            byte[] bytes = baos.toByteArray();
            int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);

            CreationHelper helper = workbook.getCreationHelper();

            ClientAnchor anchor = helper.createClientAnchor();
            anchor.setCol1(0);
            anchor.setRow1(row);
            anchor.setCol2(1);
            anchor.setRow2(row + 1);

            drawing.createPicture(anchor, pictureIdx);
        } catch (IOException e) {
            throw new RuntimeException("Error adding image to sheet: " + e.getMessage());
        }
    }
}
