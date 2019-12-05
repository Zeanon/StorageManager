package de.zeanon.storage.internal.utility.locks;

import de.zeanon.storage.internal.base.exceptions.RuntimeIOException;
import de.zeanon.storage.internal.base.interfaces.ReadWriteFileLock;
import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.StampedLock;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;


@SuppressWarnings({"unused", "WeakerAccess"})
public class ExtendedFileLock implements AutoCloseable {


	private final @NotNull ExtendedFileLock.ReadWriteLockableChannel localChannel;
	private final @NotNull ExtendedFileLock.ReadLock readLock;
	private final @NotNull WriteLock writeLock;


	@Contract(pure = true)
	public ExtendedFileLock(final @NotNull File file) throws IOException {
		this.localChannel = ReadWriteLockableChannel.getOrCreateChannel(file, "rws");
		this.readLock = new ReadLock(this.localChannel);
		this.writeLock = new WriteLock(this.localChannel);
	}

	@Contract(pure = true)
	public ExtendedFileLock(final @NotNull File file, final @NotNull String mode) throws IOException {
		this.localChannel = ReadWriteLockableChannel.getOrCreateChannel(file, mode);
		this.readLock = new ReadLock(this.localChannel);
		this.writeLock = new WriteLock(this.localChannel);
	}


	public @NotNull ReadWriteFileLock readLock() {
		return this.readLock;
	}

	public @NotNull ReadWriteFileLock writeLock() {
		return this.writeLock;
	}


	public void unlock() {
		this.readLock.unlock();
		this.writeLock.unlock();
	}


	public @NotNull FileChannel getChannel() {
		return this.localChannel.getChannel();
	}

	public @NotNull String getFilePath() {
		return this.localChannel.getFilePath();
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
		this.unlock();
		this.localChannel.close();
	}


	/**
	 * Local inner class representing a ReadWriteLockable FileChannel
	 * There is at most one instance of ReadWriteLockableChannel created per File on the System
	 */
	@EqualsAndHashCode
	@SuppressWarnings({"unused", "SameReturnValue"})
	private static class ReadWriteLockableChannel {


		private static final @NotNull Queue<ReadWriteLockableChannel> openChannels = new ConcurrentLinkedQueue<>();
		private static final @NotNull StampedLock factoryLock = new StampedLock();


		private final @NotNull StampedLock internalLock = new StampedLock();
		private final @NotNull FileChannel localChannel;
		private final @NotNull String absolutePath;

		private final @NotNull AtomicReference<FileLock> readLock = new AtomicReference<>();
		private final @NotNull AtomicReference<FileLock> writeLock = new AtomicReference<>();

		private final @NotNull AtomicInteger instances = new AtomicInteger();
		private final @NotNull AtomicInteger readers = new AtomicInteger();


		@Contract(pure = true)
		private ReadWriteLockableChannel(final @NotNull File file, final @NotNull String mode) throws FileNotFoundException {
			this.localChannel = new RandomAccessFile(file, mode).getChannel();
			this.absolutePath = file.getAbsolutePath();
			this.instances.incrementAndGet();
		}

		@Contract(pure = true)
		private static @NotNull ReadWriteLockableChannel getOrCreateChannel(final @NotNull File file, final @NotNull String mode) throws IOException {
			long lockStamp = ReadWriteLockableChannel.factoryLock.readLock();
			try {
				final @NotNull String absolutePath = file.getAbsolutePath();
				for (final @NotNull ReadWriteLockableChannel tempChannel : ReadWriteLockableChannel.openChannels) {
					if (absolutePath.equals(tempChannel.getFilePath())) {
						return tempChannel;
					}
				}
				long tempLock = ReadWriteLockableChannel.factoryLock.tryConvertToWriteLock(lockStamp);
				if (ReadWriteLockableChannel.factoryLock.validate(tempLock)) {
					lockStamp = tempLock;
				} else {
					ReadWriteLockableChannel.factoryLock.unlockRead(lockStamp);
					lockStamp = ReadWriteLockableChannel.factoryLock.writeLock();
				}
				final @NotNull ExtendedFileLock.ReadWriteLockableChannel tempChannel = new ReadWriteLockableChannel(file, mode);
				ReadWriteLockableChannel.openChannels.add(tempChannel);
				return tempChannel;
			} finally {
				ReadWriteLockableChannel.factoryLock.unlockRead(lockStamp);
			}
		}


		@Contract(pure = true)
		private @NotNull FileChannel getChannel() {
			return this.localChannel;
		}

		@Contract(pure = true)
		private @NotNull String getFilePath() {
			return this.absolutePath;
		}


		private void lockRead() throws IOException {
			final long lockStamp = this.internalLock.readLock();
			try {
				while (this.writeLock.get() != null) {
					Thread.sleep(5);
				}
				this.readLock.compareAndSet(null, this.localChannel.lock(0, Long.MAX_VALUE, true));
				this.readers.incrementAndGet();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				throw new IOException(e);
			} finally {
				this.internalLock.unlockRead(lockStamp);
			}
		}

		private void unlockRead() {
			if (this.readers.get() == 1 && this.readLock.get() != null) {
				this.readers.set(0);
				this.readLock.updateAndGet(current -> {
					try {
						current.release();
						return null;
					} catch (IOException e) {
						throw new RuntimeIOException(e);
					}
				});
			} else {
				this.readers.decrementAndGet();
			}
		}


		private void lockWrite() throws IOException {
			final long lockStamp = this.internalLock.writeLock();
			try {
				while (this.readLock.get() != null || this.writeLock.get() != null) {
					Thread.sleep(5);
				}
				this.writeLock.set(this.localChannel.lock(0, Long.MAX_VALUE, false));
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				throw new IOException(e);
			} finally {
				this.internalLock.unlockWrite(lockStamp);
			}
		}

		private void unlockWrite() {
			this.writeLock.updateAndGet(current -> {
				try {
					current.release();
					return null;
				} catch (IOException e) {
					throw new RuntimeIOException(e);
				}
			});
		}


		private void close() throws IOException {
			if (this.instances.get() == 1) {
				openChannels.remove(this);
				this.localChannel.close();
			} else {
				this.instances.decrementAndGet();
			}
		}
	}


	/**
	 * Local inner class representing a ReadLock for a File
	 */
	@EqualsAndHashCode
	private static class ReadLock implements ReadWriteFileLock {

		private final @NotNull ExtendedFileLock.ReadWriteLockableChannel readWriteLockableChannel;

		private final @NotNull AtomicBoolean locked = new AtomicBoolean();

		@Contract(pure = true)
		ReadLock(final @NotNull ExtendedFileLock.ReadWriteLockableChannel readWriteLockableChannel) {
			this.readWriteLockableChannel = readWriteLockableChannel;
		}


		@Override
		public synchronized void lock() throws IOException {
			this.readWriteLockableChannel.lockRead();
			this.locked.set(true);
		}

		@Override
		public synchronized void unlock() {
			if (this.locked.compareAndSet(true, false)) {
				this.readWriteLockableChannel.unlockRead();
			}
		}


		@Override
		public @NotNull FileChannel getChannel() {
			return this.readWriteLockableChannel.getChannel();
		}

		@Override
		public @NotNull String getFilePath() {
			return this.readWriteLockableChannel.getFilePath();
		}

		@Override
		public void close() {
			this.unlock();
		}
	}


	/**
	 * Local inner class representing a WriteLock for a File
	 */
	@EqualsAndHashCode
	private static class WriteLock implements ReadWriteFileLock {

		private final @NotNull ExtendedFileLock.ReadWriteLockableChannel readWriteLockableChannel;

		private final @NotNull AtomicBoolean locked = new AtomicBoolean();

		@Contract(pure = true)
		WriteLock(final @NotNull ExtendedFileLock.ReadWriteLockableChannel readWriteLockableChannel) {
			this.readWriteLockableChannel = readWriteLockableChannel;
		}


		@Override
		public synchronized void lock() throws IOException {
			this.readWriteLockableChannel.lockWrite();
			this.locked.set(true);
		}

		@Override
		public synchronized void unlock() {
			if (this.locked.compareAndSet(true, false)) {
				this.readWriteLockableChannel.unlockWrite();
			}
		}


		@Override
		public @NotNull FileChannel getChannel() {
			return this.readWriteLockableChannel.getChannel();
		}

		@Override
		public @NotNull String getFilePath() {
			return this.readWriteLockableChannel.getFilePath();
		}

		@Override
		public void close() {
			this.unlock();
		}
	}
}