package com.jenin.FacebookManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;

import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class FriendList {
  private String DEACTIVATED_ACC_URL = null;

  public void main(WebDriver driver, String list_url) {
    DEACTIVATED_ACC_URL = list_url + "#";
    File file = new File("Friend_list.xlsx");
    String anim= "|/-\\";
    if(file.exists() && !file.isDirectory()) {
      System.out.println("Friend_list.xlsx already exists...Creating friend list aborted...");
      return ;
    }

    driver.navigate().to(list_url);
    //System.out.println("Building Friend List...");

    JavascriptExecutor js = (JavascriptExecutor) driver;
    int a = 0;
    while(driver.findElements(By.xpath("//div[@class='uiHeader']/div/div/h3")).size() == 0) {

      js.executeScript("window.scrollTo(0, document.body.scrollHeight)");

      if(a > 10000) break;
      a += 1;
      String data = "\rBuilding Friend List  " + anim.charAt(a % anim.length());
      System.out.print(data);
    }
    System.out.println("\rScrolling Done (" + a + ") ...");
    List<WebElement> frnd_lst = driver.findElements(By.xpath("//div[@class='uiProfileBlockContent']/div/div[2]"));
    XSSFWorkbook workbook = new XSSFWorkbook();
    XSSFSheet sheet = workbook.createSheet("FriendList");
    DataFormat fmt = workbook.createDataFormat();
    CellStyle cellStyle = workbook.createCellStyle();
    cellStyle.setDataFormat(fmt.getFormat("@"));


    int rowNum = 0, friendCNT = 0;
    Row row = sheet.createRow(rowNum++);
    Cell cell = null; 
    cell = row.createCell(0);	cell.setCellStyle(cellStyle);	cell.setCellValue("ID");
    cell = row.createCell(1);	cell.setCellStyle(cellStyle); 	cell.setCellValue("UserID");	
    cell = row.createCell(2);	cell.setCellStyle(cellStyle);	cell.setCellValue("Name");
    cell = row.createCell(3);	cell.setCellStyle(cellStyle);	cell.setCellValue("Link");
    cell = row.createCell(4);	cell.setCellStyle(cellStyle);	cell.setCellValue("Status");
    cell = row.createCell(5);	cell.setCellStyle(cellStyle);	cell.setCellValue("newStatus");
    cell = row.createCell(6);	cell.setCellStyle(cellStyle);	cell.setCellValue("Change");
    cell = row.createCell(7);	cell.setCellStyle(cellStyle);	cell.setCellValue("TimeStamp");

    System.out.println("Generating excel file...\nPlease wait...");
    for(WebElement element : frnd_lst) {
      //System.out.println(element);
      String link = element.findElements(By.tagName("a")).get(0).getAttribute("href");
      String name = element.findElements(By.tagName("a")).get(0).getText();
      String id = "";
      String userID = "";
      String newstatus = "null";
      String status = "null";
      String change = "yes";
      String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
      URL url = null;
      if(!link.equals(DEACTIVATED_ACC_URL)) {
        try {
          url = new URL(link);
          if(!url.getPath().toString().equals("/profile.php")) {
            link = url.getHost().toString()+url.getPath().toString();
            userID = url.getPath().substring(1).toString();
          }else {
            link = link.substring(0, link.indexOf("&"));
            userID = url.getQuery().toString().substring(3, url.getQuery().toString().indexOf("&"));
          }
          // numeric-id
          List<WebElement> aTags = element.findElements(By.tagName("a"));
          for(WebElement tag : aTags) {
            String tmp = tag.getAttribute("href");
            if(tmp.contains("uid")) {
              id = new URL(tmp).getQuery().toString().substring(4);
            }
          }
        } catch (MalformedURLException e1) {
          // TODO Auto-generated catch block
          e1.printStackTrace();
        }
      }else {
        link = "";
      }

      //System.out.println("Name: "+name+"\tID: "+id+"\tLink: "+link);
      friendCNT++;
      String data = "\rTotal " + + friendCNT+" profiles completed.   " + anim.charAt(friendCNT % anim.length());
      System.out.print(data);
      row = sheet.createRow(rowNum++);

      cell = row.createCell(0);	cell.setCellStyle(cellStyle);	cell.setCellValue(id);
      cell = row.createCell(1);	cell.setCellStyle(cellStyle); 	cell.setCellValue(userID);
      cell = row.createCell(2);	cell.setCellStyle(cellStyle);	cell.setCellValue(name);
      cell = row.createCell(3);	cell.setCellStyle(cellStyle);	cell.setCellValue(link);
      cell = row.createCell(4);	cell.setCellStyle(cellStyle);	cell.setCellValue(status);
      cell = row.createCell(5);	cell.setCellStyle(cellStyle);	cell.setCellValue(newstatus);
      cell = row.createCell(6);	cell.setCellStyle(cellStyle);	cell.setCellValue(change);
      cell = row.createCell(7);	cell.setCellStyle(cellStyle);	cell.setCellValue(timeStamp);

    }
    System.out.println("\nFriend list generating done...\nTotal number of friends: "+friendCNT);
    try {
      FileOutputStream outputStream = new FileOutputStream(file);
      workbook.write(outputStream);
      workbook.close();
      System.out.println("Excel file written...");
    }catch(FileNotFoundException e) {
      e.printStackTrace();
    }catch(IOException e) {
      e.printStackTrace();
    } 
  }
}
