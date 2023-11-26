package org.example.mapper;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.model.Menu;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;

public class MenuAdapter {
    private static final String FILE_PATH = "/menu.xlsx";

    public static void updateMenuInfo(List<Menu> menus) throws FileNotFoundException {
        String path = new File("").getAbsolutePath();
        File file = new File(path + FILE_PATH);

        // 절대 경로를 사용하여 파일 스트림 열기
        try (Workbook workbook = new XSSFWorkbook()) {

            Sheet sheet = workbook.createSheet();

            // 데이터와 행의 수 일치시키기
            for (int i = 0; i < menus.size(); i++) {
                Menu menu = menus.get(i);

                // 이미지 추가
                Image menuImage = menu.getImage();
                addImageToSheet(workbook, sheet, menuImage, i);

                // 나머지 정보 추가 (이름, 가격, 설명, 카테고리)
                Row row = sheet.createRow(i);
                row.createCell(1).setCellValue(menu.getName());
                row.createCell(2).setCellValue(menu.getPrice());
                row.createCell(3).setCellValue(menu.getDescription());
                row.createCell(4).setCellValue(menu.getCategory());
            }

            try (FileOutputStream fileOut = new FileOutputStream(file)) {
                workbook.write(fileOut);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void addImageToSheet(Workbook workbook, Sheet sheet, Image image, int row) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write((BufferedImage) image, "png", baos);
            byte[] bytes = baos.toByteArray();
            int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);

            Drawing drawing = sheet.createDrawingPatriarch();
            CreationHelper helper = workbook.getCreationHelper();

            ClientAnchor anchor = helper.createClientAnchor();
            anchor.setCol1(0);
            anchor.setRow1(row);
            anchor.setCol2(1);
            anchor.setRow2(row + 1);

            drawing.createPicture(anchor, pictureIdx);
        } catch (IOException e) {
            throw new RuntimeException("이미지 시트에 추가하다가 에러: " + e.getMessage());
        }
    }
}
