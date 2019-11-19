package de.zeanon.storage;

import de.zeanon.storage.internal.data.cache.ThunderFileData;


public class Main {

	public static void main(String[] args) {
		ThunderFileData test = new ThunderFileData();

		test.insert("this.is.a.test", true);
		System.out.println(test);
	}
}
