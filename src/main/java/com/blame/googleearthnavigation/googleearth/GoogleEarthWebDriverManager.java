package com.blame.googleearthnavigation.googleearth;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.blame.googleearthnavigation.bean.Coordinates;
import com.blame.googleearthnavigation.bean.NavigationPoint;
import com.blame.googleearthnavigation.geonavigation.Navigator;

public class GoogleEarthWebDriverManager {
	private static Logger logger = LogManager.getLogger(GoogleEarthWebDriverManager.class);

	private static String URL_PATTERN = "https://www.google.es/maps/@{latitude},{longitude},{groundAltitude}a,35y,{heading}h,{tilt}t/data=!3m1!1e3?hl=es";
	
	protected WebDriver webDriver;
	protected String picsFolderName;
	protected int screenshotIndex;

	public GoogleEarthWebDriverManager(String baseFolderName) {
		this.webDriver = new ChromeDriver();
		this.webDriver.manage().window().maximize();
		
		this.picsFolderName = baseFolderName + File.separator + "pics";
		File runFolder = new File(picsFolderName);
		if(runFolder.exists() ) {
			runFolder.delete();
		}
		boolean didCreate = runFolder.mkdir();
		
		this.screenshotIndex = 0;
	}

	public String buildURL(NavigationPoint navigationPoint) {
		
		String url = URL_PATTERN;
		Coordinates coordinates = navigationPoint.getCoordinates();
		double latitudeRounded = round(coordinates.getLatitude(), 7);
		double longitudeRounded = round(coordinates.getLongitude(), 7);
		double headingRounded = round(navigationPoint.getLookDirectionH(), 2);
		double tiltRounded = round(navigationPoint.getLookDirectionT(), 2);
		
		url = url.replace("{latitude}", String.valueOf(latitudeRounded));
		url = url.replace("{longitude}", String.valueOf(longitudeRounded));
		url = url.replace("{heading}", String.valueOf(headingRounded));
		url = url.replace("{tilt}", String.valueOf(tiltRounded));
		url = url.replace("{groundAltitude}", String.valueOf(Navigator.GROUND_ALTITUDE));

		return url;
	}
	
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = BigDecimal.valueOf(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}

	/**
	 * 
	 * @param url
	 */
	public void prepareScreenshot(String url) {
		webDriver.get(url);
		
		WebElement elConsentLater = (new WebDriverWait(webDriver, 5))
				.until(ExpectedConditions.visibilityOfElementLocated(By.className("widget-consent-button-later")));
		elConsentLater.click();

		(new WebDriverWait(webDriver, 20)).until(ExpectedConditions.or(
						ExpectedConditions.visibilityOfElementLocated(By.className("section-homescreen-expand-entrypoint-text")), 
						ExpectedConditions.visibilityOfElementLocated(By.className("section-action-chip-button"))));
		List<WebElement> elExpandPanel = webDriver.findElements(By.className("section-homescreen-expand-entrypoint-text"));
		if(!elExpandPanel.isEmpty() && elExpandPanel.get(0).isDisplayed()) {
			elExpandPanel.get(0).click();
		}

		WebElement elHamburgerButton = (new WebDriverWait(webDriver, 5))
				.until(ExpectedConditions.visibilityOfElementLocated(By.className("searchbox-hamburger")));
		elHamburgerButton.click();
		
		WebElement elEarthItem = (new WebDriverWait(webDriver, 5))
				.until(ExpectedConditions.visibilityOfElementLocated(By.className("widget-settings-earth-item")));
		elEarthItem.findElements(By.className("widget-settings-button")).get(1).click();
		
		WebElement elTogglePanel = (new WebDriverWait(webDriver, 5))
				.until(ExpectedConditions.elementToBeClickable(By.className("widget-pane-toggle-button")));
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {/* nothing to do */}
		elTogglePanel.click();
	}
	
	/**
	 * 
	 * @return
	 */
	public void takeScreenshot() {
		
		screenshotIndex++;
		
		byte[] imageBytes = null;
		try (FileOutputStream fos = new FileOutputStream(picsFolderName + File.separator + screenshotIndex + ".png")) {
			imageBytes = ((ChromeDriver) webDriver).getScreenshotAs(OutputType.BYTES);
			fos.write(imageBytes);
			fos.close();
		} 
		catch (IOException e) {
			logger.catching(e);
		}
	}
	
	public void takeScreenshotToJPG() {

		// TODO: run at least the conversion as a Thread
		screenshotIndex++;
		
		try {
			logger.debug("Taking screenshot ...");
			byte[] imageBytes = ((ChromeDriver) webDriver).getScreenshotAs(OutputType.BYTES);
			logger.debug("Converting image to JPG and storing to file ...");
			BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imageBytes));
			// create a blank, RGB, same width and height, and a white background
			BufferedImage newBufferedImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
			newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, Color.WHITE, null);
			// write to jpeg file
			ImageIO.write(newBufferedImage, "jpg", new File(picsFolderName + File.separator + screenshotIndex + ".jpg"));
		} 
		catch (IOException e) {
			logger.catching(e);
		}
	}

	/**
	 * 
	 * @return
	 */
	public Object getCurrentURL() {
		return webDriver.getCurrentUrl();
	}

	/**
	 * 
	 */
	public void close() {
		webDriver.close();
	}

}
