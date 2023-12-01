package org.example.mapper;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFPicture;
import org.apache.poi.xssf.usermodel.XSSFPictureData;
import org.apache.poi.xssf.usermodel.XSSFShape;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.model.Menu;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MenuLoader {
    private final static String FILE_PATH = "/menu.xlsx";

    public static List<Menu> loadMenuFromExcel() {
        List<Menu> menus = new ArrayList<>();

        String path = new File("").getAbsolutePath();
        File file = new File(path + FILE_PATH);

        try (InputStream inputStream = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();

            List<XSSFShape> shapes = drawing.getShapes();

            for (int i = 0; i < shapes.size(); i++) {
                Menu menu;
                Row row = sheet.getRow(i);

                try {
                    row.getCell(1);
                } catch (Exception e) {
                    return menus;
                }
                Cell nameCell = row.getCell(1);
                Cell priceCell = row.getCell(2);
                Cell descriptionCell = row.getCell(3);
                Cell categoryCell = row.getCell(4);

                BufferedImage image = null;

                XSSFShape shape = shapes.get(i);
                if (shape instanceof XSSFPicture) {
                    XSSFPicture picture = (XSSFPicture) shape;
                    if (picture.getPictureData() == null) {
                        System.out.println("사진 Path 사용");
                        continue;
                    }

                    XSSFPictureData xssfPictureData = picture.getPictureData();

                    // Image로 변환
                    byte[] data = xssfPictureData.getData();
                    image = convertByteArrayToImage(data);
                }

                if (nameCell != null && priceCell != null && descriptionCell != null && categoryCell != null) {
                    String name = nameCell.getStringCellValue();
                    int price = (int) priceCell.getNumericCellValue();
                    String description = descriptionCell.getStringCellValue();
                    String category = categoryCell.getStringCellValue();

                    menu = new Menu(name, price, description, category, image);

                    menus.add(menu);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return menus;
    }

    private static BufferedImage convertByteArrayToImage(byte[] data) {
        try {
            return ImageIO.read(new ByteArrayInputStream(data));
        } catch (IOException e) {
            throw new RuntimeException("Error converting byte array to image: " + e.getMessage());
        }
    }
}
