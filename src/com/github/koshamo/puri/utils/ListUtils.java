package com.github.koshamo.puri.utils;

import java.util.LinkedList;
import java.util.List;

public class ListUtils {

	private ListUtils() {
		/* just to prevent instantiation */
	}
	
	public static <T> List<T> generateRandomList(List<T> input) {
		int numItems = input.size();
		List<T> result = new LinkedList<>();
		
		for (int i = 0; i < numItems; i++) {
			double rnd = Math.random() * input.size();
			result.add(input.remove((int) rnd));
		}
		
		return result;
	}
}
