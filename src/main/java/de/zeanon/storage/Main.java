package de.zeanon.storage;

import java.util.HashMap;
import java.util.Map;


public class Main {

	public static void main(String[] args) {
		Map<String[], Object> test = new HashMap<>();
		test.put(new String[]{"HI"}, 25);
		test.put(new String[]{"HI"}, 35);
		System.out.println(test.size());
		System.out.println(test.get(new String[]{"HI"}));
	}
}
