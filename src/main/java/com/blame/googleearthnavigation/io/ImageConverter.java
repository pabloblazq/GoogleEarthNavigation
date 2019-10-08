package com.blame.googleearthnavigation.io;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ImageConverter extends Thread {

	private static Logger logger = LogManager.getLogger(ImageConverter.class);

	protected byte[] imageBytes;
	protected String filename;
	
	public ImageConverter(byte[] imageBytes, String filename) {
		this.imageBytes = imageBytes;
		this.filename = filename;
	}

	@Override
	public void run() {

		try {
			logger.debug("Converting image to JPG and storing to file ...");
			BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imageBytes));
			// create a blank, RGB, same width and height, and a white background
			BufferedImage newBufferedImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
			newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, Color.WHITE, null);
			// write to jpeg file
			ImageIO.write(newBufferedImage, "jpg", new File(filename));
			logger.debug("Image converted");
		} catch (IOException e) {
			logger.catching(e);
		}
	}

	
}
