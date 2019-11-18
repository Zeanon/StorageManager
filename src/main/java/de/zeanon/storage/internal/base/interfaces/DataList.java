package de.zeanon.storage.internal.base.interfaces;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Predicate;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;


@SuppressWarnings({"UnusedReturnValue", "unused"})
public interface DataList<E> extends Iterable<E> {

	int size();

	boolean isEmpty();

	boolean contains(Object o);

	@NotNull
	Iterator<E> iterator();

	@NotNull
	Object[] toArray();

	@NotNull <T> T[] toArray(@NotNull T[] a);

	boolean add(E entry);

	boolean remove(Object o);

	boolean removeIf(Predicate<? super E> filter);

	boolean containsAll(@NotNull Collection<?> c);

	boolean addAll(@NotNull Collection<? extends E> c);

	boolean addAll(@NotNull DataList<? extends E> c);

	boolean addAll(int index, @NotNull Collection<? extends E> c);

	boolean addAll(int index, @NotNull DataList<? extends E> c);

	boolean removeAll(@NotNull Collection<?> c);

	boolean retainAll(@NotNull Collection<?> c);

	void clear();

	E get(int index);

	E set(int index, E element);

	void add(int index, E element);

	E remove(int index);

	int indexOf(Object o);

	int lastIndexOf(Object o);

	@NotNull
	ListIterator<E> listIterator();

	@NotNull
	ListIterator<E> listIterator(int index);

	@NotNull
	DataList<E> subList(int fromIndex, int toIndex);

	@NotNull List<E> getList();

	@Getter
	@EqualsAndHashCode
	@Accessors(chain = true)
	class Entry<K, V> implements Comparable<Entry> {

		@NotNull
		private final K key;
		private final int line;
		@Setter
		@NotNull
		private V value;


		public Entry(final @NotNull K key, final @NotNull V value) {
			this.key = key;
			this.value = value;
			this.line = 0;
		}

		public Entry(final @NotNull K key, final @NotNull V value, final int line) {
			this.key = key;
			this.value = value;
			this.line = line;
		}

		@Override
		public int compareTo(@NotNull DataList.Entry entry) {
			return Integer.compare(this.line, entry.line);
		}

		@NotNull
		@Override
		public String toString() {
			return "[" + this.key + "," + this.value + "," + this.line + "]";
		}
	}
}