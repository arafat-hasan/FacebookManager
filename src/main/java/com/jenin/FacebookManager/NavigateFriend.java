package com.jenin.FacebookManager;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public class NavigateFriend {

  private WebDriver driver;
  private String profilePicCSS = "a[href*='referrer_profile_id']";
  private String altProfilePicCSS = "a[href*='?profile_id=']";
  private String followCSS = "button[data-status='follow']";
  private String seeFirstCSS = "button[data-status='see_first']";
  private String bigText = "span[role='heading']";
  public String follow = "Default", unfollow = "Unfollow", seeFirst = "See First", unfriend = "unfriend";
  private int delayTime = 200;
  private int bigDelayTime = 9000;

  public NavigateFriend(WebDriver d) {
    driver = d;
  }

  public String[] navigate(String url, String status) {
    // TODO Auto-generated method stub

    if (!url.toLowerCase().matches("^\\w+://.*")) {
      url = "https://" + url;
    }
    driver.navigate().to(url);

    String previous_status = "null";
    String changed_status = "null";
    String profilePicUrl = null;
    if(driver.findElements(By.cssSelector(followCSS)).size() > 0 && driver.findElement(By.cssSelector(followCSS)).isDisplayed()){
      previous_status = follow;
    }else if(driver.findElements(By.cssSelector(seeFirstCSS)).size() > 0 && driver.findElement(By.cssSelector(seeFirstCSS)).isDisplayed()){
      previous_status = seeFirst;
    }else {
      previous_status = unfollow;
    }

    //System.out.println("previous_status#: "+previous_status);

    if(driver.findElements(By.cssSelector(profilePicCSS)).size() > 0) {
      profilePicUrl = driver.findElements(By.cssSelector(profilePicCSS)).get(0).getAttribute("href");
    }else if(driver.findElements(By.cssSelector(altProfilePicCSS)).size() > 0) {
      profilePicUrl = driver.findElements(By.cssSelector(altProfilePicCSS)).get(0).getAttribute("href");
    }
    String calledProfileID = profilePicUrl.substring(profilePicUrl.lastIndexOf("=")+1, profilePicUrl.length());

    try {
      Thread.sleep(bigDelayTime);


      if(previous_status.equals(status)) 		return new String[]{ previous_status, status, calledProfileID };


      if(previous_status.equals(unfollow)) {
        if(status.equals(follow)) {
          driver.findElement(By.xpath("//a[@rel='async-post']")).click();
        } else {
          driver.findElement(By.xpath("//a[@rel='async-post']")).click();
          Actions action = new Actions(driver);
          WebElement we = driver.findElement(By.xpath("//button[@data-status='follow']"));
          action.moveToElement(we).perform();
          List<WebElement> tmp = driver.findElements(By.xpath("//a[@rel='async-post']"));
          for(WebElement i : tmp) {
            //System.out.println(i.getText());
            Thread.sleep(delayTime);
            WebElement mouse = i;
            action.moveToElement(mouse).perform();
            if(i.getText().contains(status)) {
              action.click().build().perform();
              //break;
            }
          }
        }
      }else if(previous_status.equals(follow)){
        Actions action = new Actions(driver);
        WebElement we = driver.findElement(By.xpath("//button[@data-status='follow']"));
        action.moveToElement(we).perform();

        List<WebElement> tmp = driver.findElements(By.xpath("//a[@rel='async-post']"));
        for(WebElement i : tmp) {
          //System.out.println(i.getText());
          Thread.sleep(delayTime);
          WebElement mouse = i;
          action.moveToElement(mouse).perform();
          if(i.getText().contains(status)) {
            action.click().build().perform();
            //break;
          }
        }
      }else if(previous_status.equals(seeFirst)){

        Actions action = new Actions(driver);
        WebElement we = driver.findElement(By.xpath("//button[@data-status='see_first']"));
        action.moveToElement(we).perform();
        List<WebElement> tmp = driver.findElements(By.xpath("//a[@rel='async-post']"));
        for(WebElement i : tmp) {
          //System.out.println(i.getText());
          Thread.sleep(delayTime);
          WebElement mouse = i;
          action.moveToElement(mouse).perform();
          if(i.getText().contains(status)) {
            action.click().build().perform();
            //break;
          }
        }

      }

      driver.findElement(By.cssSelector(bigText)).click();
      driver.navigate().refresh();
      Thread.sleep(delayTime);

      if(driver.findElements(By.cssSelector(followCSS)).size() > 0 && driver.findElement(By.cssSelector(followCSS)).isDisplayed()){
        changed_status = follow;
      }else if(driver.findElements(By.cssSelector(seeFirstCSS)).size() > 0 && driver.findElement(By.cssSelector(seeFirstCSS)).isDisplayed()){
        changed_status = seeFirst;
      }else {
        changed_status = unfollow;
      }

    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return new String[]{ previous_status, changed_status, calledProfileID };

  }
}
