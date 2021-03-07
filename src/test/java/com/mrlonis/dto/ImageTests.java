package com.mrlonis.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ImageTests {
    @Test
    public void shouldReturnNameWidthHeight_whenProvidedImageFilename() {
        String name = "alien";
        int width = 501;
        int height = 400;

        String filename = "src/test/resources/images/alien.jpg";
        Image image = new Image(filename);

        assertEquals(name, image.getName());
        assertEquals(width, image.getWidth());
        assertEquals(height, image.getHeight());
    }

    @Test
    public void shouldReturnNameWidthHeight_whenProvidedImageSource() {
        String name = "alien";
        int width = 501;
        int height = 400;

        String filename = "src/test/resources/images/alien.jpg";
        Image imageSource = new Image(filename);
        Image image = new Image(imageSource);

        assertEquals(name, image.getName());
        assertEquals(width, image.getWidth());
        assertEquals(height, image.getHeight());
    }
}
