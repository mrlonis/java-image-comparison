package com.mrlonis.utils;

import com.mrlonis.ColorTable;
import com.mrlonis.interfaces.Iterator;
import java.awt.Color;

public class Util {

	public static double cosineSimilarity(ColorTable A, ColorTable B) {
		double dotProduct = dotProduct(A, B);
		double vectorMagA = vectorMagnitude(A);
		double vectorMagB = vectorMagnitude(B);
		double cosineSim = (dotProduct / (vectorMagA * vectorMagB));
		return cosineSim;
	}

	public static double dotProduct(ColorTable A, ColorTable B) {
		double result = 0.0;
		Iterator aIT = A.iterator();
		Iterator bIT = B.iterator();
		  
		  while (aIT.hasNext() && bIT.hasNext()) {
			  result += (aIT.next() * bIT.next());
		  }
	  
		  return result;
	}

	public static double vectorMagnitude(ColorTable A) {
		long result = 0;
		Iterator aIT = A.iterator();
	  
		while (aIT.hasNext()) {
			long value = aIT.next();
			result += (value * value);
		}
	  
		return Math.sqrt(result);
	}

	public static boolean isPrime(int n) {
		if (n < 2) 
			return false;
		if (n == 2 || n == 3) 
			return true;
		if (n % 2 == 0 || n % 3 == 0) 
			return false;
		long sqrtN = (long) Math.sqrt(n) + 1;
		for (int i = 6; i <= sqrtN; i += 6) {
			if (n % (i - 1) == 0 || n % (i + 1) == 0) 
				return false;
		}
		return true;
	}

	public static int pack(Color color, int bitsPerChannel) {
		int r = color.getRed(), g = color.getGreen(), b = color.getBlue(); 
		if (bitsPerChannel >= 1 && bitsPerChannel <= 8) {
			int leftovers = 8 - bitsPerChannel;
			int mask = (1 << bitsPerChannel) - 1; // In binary, this is bitsPerChannel ones.
			// Isolate the higher bitsPerChannel bits of each color component byte by
			// shifting right and masking off the higher order bits.
			r >>= leftovers; 
			r &= mask;
			g >>= leftovers; 
		g &= mask;
		b >>= leftovers; 
		b &= mask;
		// Finally, pack the color components into an int by left shifting into position
		// and xor-ing together.
		return (((r << bitsPerChannel) ^ g) << bitsPerChannel) ^ b;
		}
		else {
			throw new RuntimeException(String.format("Unsupported number of bits per channel: %d",
					bitsPerChannel));
		}
	}

	public static Color unpack(int code, int bitsPerChannel) {
		int r = code, g = code, b = code;
		if (bitsPerChannel >= 1 && bitsPerChannel <= 8) {
			int mask = (1 << bitsPerChannel) - 1; // In binary, this is bitsPerChannel ones.
			int leftovers = 8 - bitsPerChannel;
			// Isolate the higher bitsPerChannel bits of each color component byte.
			b &= mask;
			b <<= leftovers;
			g >>= bitsPerChannel;
			g &= mask;
			g <<= leftovers;
			r >>= 2 * bitsPerChannel;
			r &= mask;
			r <<= leftovers;
			return new Color(r, g, b);
		}
		else 
			throw new RuntimeException("Unsupported number of bits per channel; use an int in the range [1..8]");    
	}

	/**
	 * Simple testing.
	 */
	public static void main(String[] args) {
		System.out.println(isPrime(Constants.MAX_CAPACITY));
		int j = 536870896;
		System.out.println(Constants.MAX_CAPACITY == 4 * j + 3);
        
		int black = pack(Color.BLACK, 6);
		System.out.println("black encoded in " + (3 * 6) + " bits: " + black);
		int white = pack(Color.WHITE, 8);
		System.out.println("white encoded in " + (3 * 8) + " bits: " + white);
		white = pack(Color.WHITE, 1);
		System.out.println("white encoded in " + (3 * 1) + " bits: " + white);
		int green = pack(Color.GREEN, 3);
		System.out.println("green encoded in " + (3 * 3) + " bits: " + green);
		green = pack(Color.GREEN, 4);
		System.out.println("green encoded in " + (3 * 4) + " bits: " + green);
		System.out.println(unpack(green, 4));
    
		for (int n = 0; n < 300; n++) {
			if (isPrime(n)) 
				System.out.println(n + "  ");
		}
	}
}
