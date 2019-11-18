package de.zeanon.storage.internal.base.lists;

import de.zeanon.storage.internal.base.interfaces.DataList;
import java.util.*;
import java.util.function.Predicate;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;


@SuppressWarnings({"SuspiciousMethodCalls", "unused"})
@NoArgsConstructor
@AllArgsConstructor
public class ArrayDataList<E> implements DataList<E> {

	@NotNull
	private List<E> localList = new ArrayList<>();

	public ArrayDataList(final @NotNull DataList<E> list) {
		this.localList = new ArrayList<>(list.getList());
	}

	@Override
	public int size() {
		return this.localList.size();
	}

	@Override
	public boolean isEmpty() {
		return this.localList.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return this.localList.contains(o);
	}

	@NotNull
	@Override
	public Iterator<E> iterator() {
		return this.localList.iterator();
	}

	@NotNull
	@Override
	public Object[] toArray() {
		return this.localList.toArray();
	}

	@NotNull
	@Override
	public <T> T[] toArray(@NotNull T[] a) {
		//noinspection SuspiciousToArrayCall
		return this.localList.toArray(a);
	}

	@Override
	public boolean add(E entry) {
		return this.localList.add(entry);
	}

	@Override
	public boolean remove(Object o) {
		return this.localList.remove(o);
	}

	@Override
	public boolean removeIf(@NotNull Predicate<? super E> filter) {
		return this.localList.removeIf(filter);
	}

	@Override
	public boolean containsAll(@NotNull Collection<?> c) {
		return this.localList.containsAll(c);
	}

	@Override
	public boolean addAll(@NotNull Collection<? extends E> c) {
		return this.localList.addAll(c);
	}

	@Override
	public boolean addAll(@NotNull DataList<? extends E> c) {
		return this.localList.addAll(c.getList());
	}

	@Override
	public boolean addAll(int index, @NotNull Collection<? extends E> c) {
		return this.localList.addAll(index, c);
	}

	@Override
	public boolean addAll(int index, @NotNull DataList<? extends E> c) {
		return this.localList.addAll(index, c.getList());
	}

	@Override
	public boolean removeAll(@NotNull Collection<?> c) {
		return this.localList.removeAll(c);
	}

	@Override
	public boolean retainAll(@NotNull Collection<?> c) {
		return this.localList.retainAll(c);
	}

	@Override
	public void clear() {
		this.localList.clear();
	}

	@Override
	public E get(int index) {
		return this.localList.get(index);
	}

	@Override
	public E set(int index, E element) {
		return this.localList.set(index, element);
	}

	@Override
	public void add(int index, E element) {
		this.localList.add(element);
	}

	@Override
	public E remove(int index) {
		return this.localList.remove(index);
	}

	@Override
	public int indexOf(Object o) {
		return this.localList.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		return this.localList.lastIndexOf(o);
	}

	@NotNull
	@Override
	public ListIterator<E> listIterator() {
		return this.localList.listIterator();
	}

	@NotNull
	@Override
	public ListIterator<E> listIterator(int index) {
		return this.localList.listIterator(index);
	}

	@NotNull
	@Override
	public DataList<E> subList(int fromIndex, int toIndex) {
		return new ArrayDataList<>(this.localList.subList(fromIndex, toIndex));
	}

	@NotNull
	@Override
	public List<E> getList() {
		return this.localList;
	}
}