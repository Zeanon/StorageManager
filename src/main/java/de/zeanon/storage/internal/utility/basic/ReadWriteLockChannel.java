package de.zeanon.storage.internal.utility.basic;

import de.zeanon.storage.internal.base.exceptions.RuntimeIOException;
import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.StampedLock;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;


@EqualsAndHashCode
@SuppressWarnings({"unused", "SameReturnValue"})
public class ReadWriteLockChannel implements AutoCloseable {


	private static final @NotNull Map<ReadWriteLockChannel, String> openChannels = new ConcurrentHashMap<>();


	private final @NotNull StampedLock internalLock = new StampedLock();
	private final @NotNull FileChannel localChannel;

	private final @NotNull AtomicReference<FileLock> readLock = new AtomicReference<>();
	private final @NotNull AtomicReference<FileLock> writeLock = new AtomicReference<>();

	private final @NotNull AtomicInteger instances = new AtomicInteger();
	private final @NotNull AtomicInteger readers = new AtomicInteger();


	@Contract(pure = true)
	private ReadWriteLockChannel(final @NotNull File file, final @NotNull String mode) throws FileNotFoundException {
		this.localChannel = new RandomAccessFile(file, mode).getChannel();
		this.instances.getAndIncrement();
	}


	@Contract(pure = true)
	public static ReadWriteLockChannel get(final @NotNull File file) throws IOException {
		return getOrCreateChannel(file, "rws");
	}

	@Contract(pure = true)
	public static ReadWriteLockChannel get(final @NotNull File file, final @NotNull String mode) throws IOException {
		return getOrCreateChannel(file, mode);
	}


	public void readLock() throws IOException {
		final long lockStamp = this.internalLock.readLock();
		try {
			while (this.writeLock.get() != null) {
				Thread.sleep(5);
			}
			this.readLock.compareAndSet(null, this.localChannel.lock(0, Long.MAX_VALUE, true));
			this.readers.getAndIncrement();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new IOException(e);
		} finally {
			this.internalLock.unlockRead(lockStamp);
		}
	}

	public void releaseRead() {
		if (this.readers.get() == 1 && this.readLock.get() != null) {
			this.readers.set(0);
			this.readLock.getAndUpdate(current -> {
				try {
					current.release();
					return null;
				} catch (IOException e) {
					throw new RuntimeIOException(e);
				}
			});
		} else {
			this.readers.getAndDecrement();
		}
	}


	public void writeLock() throws IOException {
		final long lockStamp = this.internalLock.writeLock();
		try {
			while (this.readLock.get() != null || this.writeLock.get() != null) {
				Thread.sleep(100);
			}
			this.writeLock.set(this.localChannel.lock());
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new IOException(e);
		} finally {
			this.internalLock.unlockWrite(lockStamp);
		}
	}

	public void releaseWrite() {
		this.writeLock.updateAndGet(current -> {
			try {
				current.release();
				return null;
			} catch (IOException e) {
				throw new RuntimeIOException(e);
			}
		});
	}


	public @NotNull FileChannel getChannel() {
		return this.localChannel;
	}

	/**
	 * Closes this resource, relinquishing any underlying resources.
	 * This method is invoked automatically on objects managed by the
	 * {@code try}-with-resources statement.
	 *
	 * <p>While this interface method is declared to throw {@code
	 * Exception}, implementers are <em>strongly</em> encouraged to
	 * declare concrete implementations of the {@code close} method to
	 * throw more specific exceptions, or to throw no exception at all
	 * if the close operation cannot fail.
	 *
	 * <p> Cases where the close operation may fail require careful
	 * attention by implementers. It is strongly advised to relinquish
	 * the underlying resources and to internally <em>mark</em> the
	 * resource as closed, prior to throwing the exception. The {@code
	 * close} method is unlikely to be invoked more than once and so
	 * this ensures that the resources are released in a timely manner.
	 * Furthermore it reduces problems that could arise when the resource
	 * wraps, or is wrapped, by another resource.
	 *
	 * <p><em>Implementers of this interface are also strongly advised
	 * to not have the {@code close} method throw {@link
	 * InterruptedException}.</em>
	 * <p>
	 * This exception interacts with a thread's interrupted status,
	 * and runtime misbehavior is likely to occur if an {@code
	 * InterruptedException} is {@linkplain Throwable#addSuppressed
	 * suppressed}.
	 * <p>
	 * More generally, if it would cause problems for an
	 * exception to be suppressed, the {@code AutoCloseable.close}
	 * method should not throw it.
	 *
	 * <p>Note that unlike the {@link Closeable#close close}
	 * method of {@link Closeable}, this {@code close} method
	 * is <em>not</em> required to be idempotent.  In other words,
	 * calling this {@code close} method more than once may have some
	 * visible side effect, unlike {@code Closeable.close} which is
	 * required to have no effect if called more than once.
	 * <p>
	 * However, implementers of this interface are strongly encouraged
	 * to make their {@code close} methods idempotent.
	 *
	 * @throws IOException if this resource cannot be closed
	 */
	@Override
	public void close() throws IOException {
		if (this.instances.get() == 1) {
			openChannels.remove(this);
		} else {
			this.instances.getAndDecrement();
		}
		this.localChannel.close();
	}


	@Contract(pure = true)
	private static ReadWriteLockChannel getOrCreateChannel(final @NotNull File file, final @NotNull String mode) throws IOException {
		final @NotNull String canonicalPath = file.getCanonicalPath();
		for (final @NotNull Map.Entry<ReadWriteLockChannel, String> entry : openChannels.entrySet()) {
			if (canonicalPath.equals(entry.getValue())) {
				return entry.getKey();
			}
		}
		final @NotNull ReadWriteLockChannel tempChannel = new ReadWriteLockChannel(file, mode);
		openChannels.put(tempChannel, canonicalPath);
		return tempChannel;
	}
}