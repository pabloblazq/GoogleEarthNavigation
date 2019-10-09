package com.blame.googleearthnavigation.googleearth;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

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
import com.blame.googleearthnavigation.geonavigation.GeoNavigationUtils;
import com.blame.googleearthnavigation.io.ImageConverter;

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

	/**
	 * 
	 * @param webDriverManager 
	 * @param navigationPointList
	 * @throws InterruptedException 
	 */
	public void takeScreenshots(List<NavigationPoint> navigationPointList) {
		logger.info("Taking screenshots using a WebDriver ...");

//		List<Integer> errorList = Arrays.asList(145, 325, 453, 791);
//        for(int errorIndex = 0; errorIndex < errorList.size(); errorIndex++) {
//        	int i = errorList.get(errorIndex);

        for(int i = 0; i < navigationPointList.size(); i++) {
        	try {
        		logger.info("------------------------------------------------------------------------------------------------------------------------");
				NavigationPoint navigationPoint = navigationPointList.get(i);
				String url = buildURL(navigationPoint);
				logger.info("Taking  screenshot for URL {}: {}", i + 1, url);
				prepareScreenshot(url);
				takeScreenshotToJPG();
				logger.info("Taken   screenshot for URL {}: {}", i + 1, getCurrentURL());
			} catch (Exception e) {
				logger.catching(e);
			}
        }		
		logger.info("------------------------------------------------------------------------------------------------------------------------");
	}

	protected String buildURL(NavigationPoint navigationPoint) {
		
		String url = URL_PATTERN;
		Coordinates coordinates = navigationPoint.getCoordinates();
		double latitudeRounded = GeoNavigationUtils.round(coordinates.getLatitude(), 6);
		double longitudeRounded = GeoNavigationUtils.round(coordinates.getLongitude(), 6);
		double headingRounded = GeoNavigationUtils.round(navigationPoint.getSpotLookDirectionH(), 2);
		double tiltRounded = GeoNavigationUtils.round(navigationPoint.getSpotLookDirectionT(), 2);
		int groundAltitude = navigationPoint.getGroundAltitude();
		
		url = url.replace("{latitude}", String.valueOf(latitudeRounded));
		url = url.replace("{longitude}", String.valueOf(longitudeRounded));
		url = url.replace("{heading}", String.valueOf(headingRounded));
		url = url.replace("{tilt}", String.valueOf(tiltRounded));
		url = url.replace("{groundAltitude}", String.valueOf(groundAltitude));

		return url;
	}
	
	/**
	 * 
	 * @param url
	 */
	protected void prepareScreenshot(String url) {
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
	protected void takeScreenshot() {
		
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
	
	protected void takeScreenshotToJPG() {

		screenshotIndex++;

		logger.debug("Taking screenshot ...");
		byte[] imageBytes = ((ChromeDriver) webDriver).getScreenshotAs(OutputType.BYTES);
		String filename = picsFolderName + File.separator + screenshotIndex + ".jpg";
		ImageConverter imageConverter = new ImageConverter(imageBytes, filename);
		imageConverter.start();
	}

	/**
	 * 
	 * @return
	 */
	protected Object getCurrentURL() {
		return webDriver.getCurrentUrl();
	}

	/**
	 * 
	 */
	public void close() {
		webDriver.close();
	}

}
