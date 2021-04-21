package de.zeanon.storagemanagercore.internal.utility.basic;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Implementation of a Stack with a given maximum size.
 * If the stack is full, the oldest entry will be discarded when something new is added
 * <p>
 * Be aware that due to performance this implementation is not Thread-Safe to reduce overhead
 *
 * @param <T> the Type of the entries to be stored
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class SizedStack<T> {

	@Getter
	private int maxSize;
	private T[] data;
	@Getter
	private int size;
	private int head;
	private int tail;

	public SizedStack(int size) {
		this.maxSize = size;
		//noinspection unchecked
		this.data = (T[]) new Object[this.maxSize];
		this.head = 0;
		this.tail = 0;
		this.size = 0;
	}

	public T push(final @Nullable T element) {
		this.data[this.head] = element;
		this.increaseHead();
		this.increaseSize();
		return element;
	}

	public T pushBottom(final @Nullable T element) {
		this.decreaseTail();
		this.data[this.tail] = element;
		this.increaseSize();
		return element;
	}

	public T pop() {
		this.decreaseHead();
		this.decreaseSize();
		final T result = this.data[this.head];
		this.data[this.head] = null;
		return result;
	}

	public T popBottom() {
		this.decreaseSize();
		final T result = this.data[this.tail];
		this.data[this.head] = null;
		this.increaseTail();
		return result;
	}

	public T peek() {
		return this.data[this.head];
	}

	public T peekBottom() {
		return this.data[this.tail];
	}

	/**
	 * Resizes the SizedStack to the given new maxSize.
	 * When newSize is smaller than the current maxSize the oldest entries will be removed, otherwise the maxSize will just be increased
	 *
	 * @param newSize the size the new SizedStack should have
	 *
	 * @return a new SizedStack with the given size
	 */
	public void resize(final int newSize) {
		if (newSize != this.maxSize) {
			final @NotNull SizedStack<T> temp = new SizedStack<>(newSize);
			while (!this.empty() && temp.size < newSize) {
				temp.pushBottom(this.pop());
			}
			this.maxSize = temp.maxSize;
			this.data = temp.data;
			this.head = temp.head;
			this.tail = temp.tail;
			this.size = temp.size;
		}
	}

	public boolean empty() {
		return this.size == 0;
	}

	protected boolean canEqual(final Object other) {
		return other instanceof SizedStack;
	}

	private void increaseHead() {
		this.head++;
		while (this.head > this.maxSize - 1) {
			this.head -= this.maxSize;
		}
		if (this.head == this.tail + 1) {
			this.increaseTail();
		}
	}

	private void decreaseHead() {
		this.head--;
		while (this.head < 0) {
			this.head += this.maxSize;
		}
		if (this.head == this.tail - 1) {
			this.decreaseTail();
		}
	}

	private void increaseTail() {
		this.tail++;
		while (this.tail > this.maxSize - 1) {
			this.tail -= this.maxSize;
		}
		if (this.tail == this.head + 1) {
			this.increaseHead();
		}
	}

	private void decreaseTail() {
		this.tail--;
		while (this.tail < 0) {
			this.tail += this.maxSize;
		}
		if (this.tail == this.head - 1) {
			this.decreaseHead();
		}
	}

	private void increaseSize() {
		if (this.size < this.maxSize) {
			this.size++;
		}
	}

	private void decreaseSize() {
		if (this.size > 0) {
			this.size--;
		}
	}

	@Override
	public int hashCode() {
		final int PRIME = 59;
		int result = 1;
		result = result * PRIME + this.maxSize;
		result = result * PRIME + this.toString().hashCode();
		result = result * PRIME + this.size;
		return result;
	}

	@Override
	public boolean equals(final Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof SizedStack)) {
			return false;
		}
		final SizedStack<?> other = (SizedStack<?>) o;
		if (!other.canEqual(this)) {
			return false;
		}
		if (this.maxSize != other.maxSize) {
			return false;
		}
		if (this.size != other.size) {
			return false;
		}
		if (!this.data.getClass().equals(other.data.getClass())) {
			return false;
		}
		return this.toString().equals(other.toString());
	}

	@Override
	public String toString() {
		final @NotNull StringBuilder result = new StringBuilder("[");
		for (int i = 0; i < this.size - 1; i++) {
			result.append(this.data[(this.head - i - 1 < 0) ? (this.head - i - 1 + this.maxSize) : (this.head - i - 1)]).append(",");
		}
		result.append(this.data[(this.head - this.size < 0) ? (this.head - this.size + this.maxSize) : (this.head - this.size)]);
		result.append("]");
		return result.toString();
	}
}