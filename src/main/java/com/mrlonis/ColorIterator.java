package com.mrlonis;

import com.mrlonis.interfaces.Iterator;
import java.awt.Color;

/**
 * This class represents an iterator that returns all the colors in a color key space
 * based on a certain bits per channel. Bits per channel can be anywhere from 1 to 8.
 * 
 * @author mrlonis
 */

public class ColorIterator implements Iterator {
	private final int inc;
	private final ColorTable colorTable;
	private int r;
	private int g;
	private int b;
	private boolean initial;

	public ColorIterator(ColorTable colorTable) {
		this.initial = true;
		this.colorTable = colorTable;
		this.r = 0;
		this.g = 0;
		this.b = 0;
		this.inc = (int) Math.pow(2, (8 - colorTable.bitsPerChannel));
	}

	public long next() {
		Color color = new Color(r, g, b);
		long value = colorTable.get(color);
		
		b += inc;
		this.initial = false;
		
		if (b > 255) {
			b = 0;
			g += inc;
			if (g > 255) {
				g = 0;
				r += inc;
				if (r > 255) {
					r = 0;
				}
			}
		}
		
		return value;
	}

	public boolean hasNext() {
		if (initial) {
			return true;
		} else {
			return (r != 0) || (g != 0) || (b != 0);
		}
	}
}
