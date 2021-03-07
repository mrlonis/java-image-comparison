package com.mrlonis.enums;

import com.mrlonis.dto.Image;
import com.mrlonis.utils.Constants;

public enum Painting {
	MONA_LISA("davinci.jpg"),
	CHRISTINAS_WORLD("wyeth.jpg"),
	STARRY_NIGHT("vangogh.jpg"),
	BLUE_DANCERS("degas.jpg");
  
	private final Image image;

	Painting(String filename) {
		this.image = new Image(Constants.IMAGE_DIR + "/" + filename);
	}

	public Image get() {
		return image;
	}
}


