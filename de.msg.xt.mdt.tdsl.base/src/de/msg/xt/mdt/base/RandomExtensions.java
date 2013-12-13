package de.msg.xt.mdt.base;

import java.util.Collection;
import java.util.List;
import java.util.Random;

public class RandomExtensions {

	public static <T> T rand(List<T> list) {
		Random rand = new Random(System.currentTimeMillis());
		return list.get(rand.nextInt(list.size()));
	}

	public static <T> T rand(Collection<T> col) {
		Random rand = new Random(System.currentTimeMillis());
		int index = rand.nextInt(col.size());
		return (T) col.toArray()[index];
	}
}
