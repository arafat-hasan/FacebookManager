package com.jenin.FacebookManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.WebDriver;

public class NavigateList {
  private String XLXS_FILE;
  private WebDriver driver;

  public NavigateList(String filePath, WebDriver d) {
    // TODO Auto-generated constructor stub
    XLXS_FILE = filePath;
    driver = d;

  }


  public void start() {
    try {
      FileInputStream inputFile = new FileInputStream(new File(XLXS_FILE));
      XSSFWorkbook workbook = new XSSFWorkbook(inputFile);
      inputFile.close();
      XSSFSheet worksheet = workbook.getSheetAt(0);
      Iterator<Row> iterator = worksheet.iterator();
      NavigateFriend frnd = new NavigateFriend(driver);
      Row row = iterator.next();
      try {
        while (iterator.hasNext()) {
          row = iterator.next();
          String id  = row.getCell(0).getStringCellValue();
          String userID  = row.getCell(1).getStringCellValue();
          //String name  = row.getCell(2).getStringCellValue();
          String link  = row.getCell(3).getStringCellValue();
          //String preStatus  = row.getCell(4).getStringCellValue();
          String newStatus  = row.getCell(5).getStringCellValue();
          String change  = row.getCell(6).getStringCellValue();
          //String timeStamp  = row.getCell(6).getStringCellValue();


          if(change.equals("yes")) {
            //call him
            String newTimeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
            String newID = null;
            String newPreStatus = null;
            String newCurrStatus = null;
            String order = newStatus;
            if(newStatus.equals("null"))
              order = frnd.follow;

            //System.out.println("preStatus: "+ preStatus + "\tcurrStatus: "+currStatus);
            Boolean errorInNavigate = false;
            try{
              String[] tmp = frnd.navigate(link, order);
              newPreStatus = tmp[0];
              newCurrStatus = tmp[1];
              if(id.equals("")) newID = tmp[2];
            }catch (Exception e) {
              System.err.println("\nError occured when navigating profile '" + userID+"'");
              //              e.printStackTrace();
              errorInNavigate = true;
            }

            if(errorInNavigate == false) {
              System.out.println("Working on profile id: " + userID + "\tpreStatus: " + newPreStatus + "\tnewStatus: " + newCurrStatus);
              if(id.equals("")) row.getCell(0).setCellValue(newID);
              row.getCell(4).setCellValue(newPreStatus);
              if(newStatus.equals("null")) row.getCell(5).setCellValue(newCurrStatus);
              row.getCell(6).setCellValue("no");
              row.getCell(7).setCellValue(newTimeStamp);
            }
          }

        }
      }catch (Exception e) {
        System.err.println("Error occured when reading/writing xlxs file...");
        e.printStackTrace();
      }

      FileOutputStream outputFile = new FileOutputStream(new File(XLXS_FILE));
      workbook.write(outputFile);
      outputFile.close();
      workbook.close();
      System.out.println("Excel file written...");
    }catch(FileNotFoundException e) {
      System.err.println("File Not Found...\n");
      e.printStackTrace();
    }catch (IOException e) {
      e.printStackTrace();
    }catch (Exception e) {
      e.printStackTrace();
    }
  }
}

