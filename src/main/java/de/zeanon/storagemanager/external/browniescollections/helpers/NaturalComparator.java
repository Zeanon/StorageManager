/*
 * Copyright 2013 by Thomas Mauch
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * $Id: NaturalComparator.java 2923 2015-09-02 23:01:46Z origo $
 *
 * Copied by Zeanon to reduce final jar size
 */
package de.zeanon.storagemanager.external.browniescollections.helpers;


/**
 * The NaturalComparator will compare object using their natural order.
 *
 * @param <T> element type
 *
 * @author Thomas Mauch
 * @version $Id: NaturalComparator.java 2923 2015-09-02 23:01:46Z origo $
 */
@SuppressWarnings("ALL")
public class NaturalComparator<T> extends SingletonComparator<T> {

	/**
	 * Singleton.
	 */
	@SuppressWarnings("rawtypes")
	private static final NaturalComparator INSTANCE = new NaturalComparator();

	/**
	 * Prevent construction.
	 */
	private NaturalComparator() {
	}

	/**
	 * @return singleton
	 */
	@SuppressWarnings("unchecked")
	public static <T> NaturalComparator<T> INSTANCE() {
		return INSTANCE;
	}

	@SuppressWarnings("unchecked")
	public static <T> NaturalComparator<T> INSTANCE(Class<T> c) {
		return INSTANCE;
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	@Override
	public int compare(T o1, T o2) {
		return ((Comparable) o1).compareTo(o2);
	}
}
