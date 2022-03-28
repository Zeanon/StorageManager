package de.zeanon.storagemanagercore.internal.utility.filelock;

import de.zeanon.storagemanagercore.external.browniescollections.GapList;
import de.zeanon.storagemanagercore.internal.base.interfaces.ReadWriteFileLock;
import de.zeanon.storagemanagercore.internal.utility.basic.Objects;
import java.io.*;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.charset.CharsetEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.StampedLock;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;


/**
 * Lock that implements a ReadWriteLock behaviour for files
 * <p>
 * Note that serializing a Lock only saves it's settings and the which File it's bound to.
 * It will be recreated with those parameters up on deserialization
 *
 * @author Zeanon
 * @version 1.1.0
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class ExtendedFileLock implements AutoCloseable, Serializable {


	private static final long serialVersionUID = 721278232016156889L;


	private final @NotNull ReadWriteLockableChannel readWriteLockableChannel;
	private final transient @NotNull WriteLock writeLock;
	private final transient @NotNull ReadLock readLock;


	@Contract(pure = true)
	public ExtendedFileLock(final @NotNull File file) throws IOException {
		this(file, true, true);
	}

	@Contract(pure = true)
	public ExtendedFileLock(final @NotNull File file, final boolean writeSynchronized, final boolean writeMetaData) throws IOException {
		this.readWriteLockableChannel = ReadWriteLockableChannel.getOrCreateChannel(file, writeSynchronized, writeMetaData);
		this.writeLock = new WriteLock(this);
		this.readLock = new ReadLock(this);
	}


	/**
	 * Get yourself the WriteLock corresponding to this FileLock
	 */
	public @NotNull
	ReadWriteFileLock writeLock() {
		return this.writeLock;
	}

	/**
	 * Get yourself the ReadLock corresponding to this FileLock
	 */
	public @NotNull
	ReadWriteFileLock readLock() {
		return this.readLock;
	}


	/**
	 * Completely unlock this lock
	 */
	public void unlock() {
		this.readWriteLockableChannel.unlock();
	}

	/**
	 * Convert this lock
	 * ReadLock to WriteLock or WriteLock to ReadLock respectively
	 */
	public void convertLock() {
		this.readWriteLockableChannel.convertLock();
	}

	public @NotNull
	RandomAccessFile getRandomAccessFile() {
		return this.readWriteLockableChannel.getRandomAccessFile();
	}

	public @NotNull
	FileChannel getFileChannel() {
		return this.readWriteLockableChannel.getFileChannel();
	}

	public @NotNull
	String getFilePath() {
		return this.readWriteLockableChannel.getFilePath();
	}


	@Contract("-> new")
	public @NotNull
	Writer createWriter() {
		return Channels.newWriter(this.readWriteLockableChannel.getFileChannel(), "UTF-8");
	}

	public @NotNull
	Writer createWriter(final @NotNull String csName) {
		return Channels.newWriter(this.readWriteLockableChannel.getFileChannel(), csName);
	}

	public @NotNull
	Writer createWriter(final @NotNull CharsetEncoder charsetEncoder,
						final int minBufferCap) {
		return Channels.newWriter(this.readWriteLockableChannel.getFileChannel(), charsetEncoder, minBufferCap);
	}

	@Contract("-> new")
	public @NotNull
	PrintWriter createPrintWriter() {
		return new PrintWriter(Channels.newWriter(this.readWriteLockableChannel.getFileChannel(), "UTF-8"));
	}

	@Contract("_ -> new")
	public @NotNull
	PrintWriter createPrintWriter(final boolean autoFlush) {
		return new PrintWriter(Channels.newWriter(this.readWriteLockableChannel.getFileChannel(), "UTF-8"), autoFlush);
	}

	public @NotNull
	PrintWriter createPrintWriter(final @NotNull String csName) {
		return new PrintWriter(Channels.newWriter(this.readWriteLockableChannel.getFileChannel(), csName));
	}

	public @NotNull
	PrintWriter createPrintWriter(final @NotNull String csName,
								  final boolean autoFlush) {
		return new PrintWriter(Channels.newWriter(this.readWriteLockableChannel.getFileChannel(), csName), autoFlush);
	}

	@Contract("-> new")
	public @NotNull
	Reader createReader() {
		return Channels.newReader(this.readWriteLockableChannel.getFileChannel(), "UTF-8");
	}

	public @NotNull
	Reader createReader(final @NotNull String csName) {
		return Channels.newReader(this.readWriteLockableChannel.getFileChannel(), csName);
	}

	@Contract("-> new")
	public @NotNull
	BufferedReader createBufferedReader() {
		return new BufferedReader(Channels.newReader(this.readWriteLockableChannel.getFileChannel(), "UTF-8"));
	}

	public @NotNull
	BufferedReader createBufferedReader(final @NotNull String csName) {
		return new BufferedReader(Channels.newReader(this.readWriteLockableChannel.getFileChannel(), csName));
	}

	@Contract("_ -> new")
	public @NotNull
	BufferedReader createBufferedReader(final int buffer_size) {
		return new BufferedReader(Channels.newReader(this.readWriteLockableChannel.getFileChannel(), "UTF-8"), buffer_size);
	}

	public @NotNull
	BufferedReader createBufferedReader(final @NotNull String csName,
										final int buffer_size) {
		return new BufferedReader(Channels.newReader(this.readWriteLockableChannel.getFileChannel(), csName), buffer_size);
	}

	@Contract("-> new")
	public @NotNull
	InputStream createInputStream() {
		return Channels.newInputStream(this.readWriteLockableChannel.getFileChannel());
	}

	@Contract("-> new")
	public @NotNull
	BufferedInputStream createBufferedInputStream() {
		return new BufferedInputStream(Channels.newInputStream(this.readWriteLockableChannel.getFileChannel()));
	}

	@Contract("-> new")
	public @NotNull
	OutputStream createOutputStream() {
		return Channels.newOutputStream(this.readWriteLockableChannel.getFileChannel());
	}

	@Contract("-> new")
	public @NotNull
	BufferedOutputStream createBufferedOutputStream() {
		return new BufferedOutputStream(Channels.newOutputStream(this.readWriteLockableChannel.getFileChannel()));
	}

	public void truncateChannel(final long size) throws IOException {
		this.readWriteLockableChannel.getFileChannel().truncate(size);
	}


	@Override
	public void close() throws IOException {
		this.readWriteLockableChannel.close();
	}


	/**
	 * Local inner class representing a ReadWriteLockable FileChannel
	 * There is at most one instance of ReadWriteLockableChannel created per File on the System
	 *
	 * @author Zeanon
	 * @version 1.1.0
	 */
	@SuppressWarnings("BusyWait")
	@EqualsAndHashCode
	private static class ReadWriteLockableChannel implements Serializable {


		private static final long serialVersionUID = 115260807719631111L;


		private static final transient @NotNull Map<String, ReadWriteLockableChannel> openChannels = new HashMap<>();
		private static final transient @NotNull StampedLock factoryLock = new StampedLock();


		private final @NotNull String absolutePath;
		private final boolean writeMetaData;
		private final boolean writeSynchronized;

		private final transient @NotNull AtomicReference<FileLock> fileLock = new AtomicReference<>();
		private final transient @NotNull AtomicInteger writeLockActive = new AtomicInteger();
		private final transient @NotNull StampedLock internalLock = new StampedLock();

		private final transient @NotNull AtomicLong currentWritingThread = new AtomicLong(-1);
		private final transient @NotNull List<Long> readingThreads = Collections.synchronizedList(new GapList<>());
		private final transient @NotNull AtomicInteger instanceCount = new AtomicInteger();
		private final transient @NotNull AtomicInteger lockHoldCount = new AtomicInteger();

		private final transient @NotNull RandomAccessFile localRandomAccessFile;


		@Contract(pure = true)
		private ReadWriteLockableChannel(final @NotNull File file, final boolean writeSynchronized, final boolean writeMetaData) throws IOException { //NOSONAR
			try {
				if (!file.exists()) {
					if (file.getParentFile() != null && !file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
						throw new IOException("Could not create parents of '" + file.getAbsolutePath() + "'");
					}
					if (!file.createNewFile()) {
						throw new IOException("Could not create '" + file.getAbsolutePath() + "'");
					}
				}
				this.localRandomAccessFile = new RandomAccessFile(file, writeSynchronized ? (writeMetaData ? "rws" : "rwd") : "rw");
				this.absolutePath = file.getAbsolutePath();
				this.writeMetaData = writeMetaData;
				this.writeSynchronized = writeSynchronized;
				this.instanceCount.incrementAndGet();
			} catch (final @NotNull IOException e) {
				throw new IOException("Error while creating '"
									  + file.getAbsolutePath()
									  + "'"
									  + System.lineSeparator()
									  + e.getMessage(),
									  e);
			}
		}

		@Contract(pure = true)
		private static @NotNull
		ReadWriteLockableChannel getOrCreateChannel(final @NotNull File file, final boolean writeSynchronized, final boolean writeMetaData) throws IOException {
			final long lockStamp = ReadWriteLockableChannel.factoryLock.readLock();
			try {
				if (!ReadWriteLockableChannel.openChannels.containsKey(file.getAbsolutePath())) {
					ReadWriteLockableChannel.openChannels.putIfAbsent(file.getAbsolutePath(), new ReadWriteLockableChannel(file, writeSynchronized, writeMetaData));
				}
				return ReadWriteLockableChannel.openChannels.get(file.getAbsolutePath());
			} finally {
				ReadWriteLockableChannel.factoryLock.unlockRead(lockStamp);
			}
		}


		@Contract(pure = true)
		private @NotNull
		RandomAccessFile getRandomAccessFile() {
			return this.localRandomAccessFile;
		}

		@Contract(pure = true)
		private @NotNull
		FileChannel getFileChannel() {
			return this.localRandomAccessFile.getChannel();
		}

		@Contract(pure = true)
		private @NotNull
		String getFilePath() {
			return this.absolutePath;
		}


		private void lockRead() {
			if ((this.writeLockActive.get() == 0 && this.lockHoldCount.intValue() > 0)
				|| (this.writeLockActive.get() > 0 && this.currentWritingThread.get() == Thread.currentThread().getId())) {
				this.readingThreads.add(Thread.currentThread().getId());
				this.lockHoldCount.incrementAndGet();
			} else {
				final long lockStamp = this.internalLock.readLock();
				try {
					while (this.writeLockActive.get() > 0) {
						Thread.sleep(5);
					}

					this.fileLock.updateAndGet(current -> {
						try {
							this.readingThreads.add(Thread.currentThread().getId());
							this.lockHoldCount.set(1);
							return this.localRandomAccessFile.getChannel().lock(0, Long.MAX_VALUE, true);
						} catch (final @NotNull IOException e) {
							throw new UncheckedIOException(e.getMessage(), e);
						}
					});
				} catch (final @NotNull InterruptedException e) {
					Thread.currentThread().interrupt();
				} finally {
					this.internalLock.unlockRead(lockStamp);
				}
			}
		}

		private boolean tryLockRead() {
			if ((this.writeLockActive.get() == 0 && this.lockHoldCount.intValue() > 0)
				|| (this.writeLockActive.get() > 0 && this.currentWritingThread.get() == Thread.currentThread().getId())) {
				this.readingThreads.add(Thread.currentThread().getId());
				this.lockHoldCount.incrementAndGet();
				return true;
			} else {
				final long lockStamp = this.internalLock.tryReadLock();
				if (lockStamp != 0) {
					try {
						if (this.writeLockActive.get() > 0) {
							return false;
						} else {
							this.fileLock.updateAndGet(current -> {
								try {
									this.readingThreads.add(Thread.currentThread().getId());
									this.lockHoldCount.set(1);
									return this.localRandomAccessFile.getChannel().lock(0, Long.MAX_VALUE, true);
								} catch (final @NotNull IOException e) {
									throw new UncheckedIOException(e.getMessage(), e);
								}
							});
							return true;
						}
					} finally {
						this.internalLock.unlockRead(lockStamp);
					}
				} else {
					return false;
				}
			}
		}

		private void unlockRead() {
			this.fileLock.updateAndGet(current -> {
				if (this.lockHoldCount.intValue() == 0 || this.fileLock.get() == null || (this.writeLockActive.get() > 0 && this.currentWritingThread.get() != Thread.currentThread().getId())) {
					throw new LockNotHeldException("Lock is not held");
				} else if (this.lockHoldCount.decrementAndGet() == 0) {
					try {
						if (current != null && current.isValid()) {
							current.release();
						}
						this.readingThreads.clear();
						return null;
					} catch (final @NotNull IOException e) {
						throw new UncheckedIOException(e.getMessage(), e);
					}
				} else {
					this.readingThreads.remove(Thread.currentThread().getId());
					return current;
				}
			});
		}


		private void lockWrite() {
			if (this.lockHoldCount.intValue() > 0
				&& (this.currentWritingThread.longValue() == Thread.currentThread().getId()
					|| Objects.containsOnly(this.readingThreads, Thread.currentThread().getId()))) {
				this.writeLockActive.incrementAndGet();
				this.lockHoldCount.incrementAndGet();
			} else {
				final long lockStamp = this.internalLock.writeLock();
				try {
					while (this.fileLock.get() != null) {
						Thread.sleep(5);
					}
					this.internalLockWrite();
				} catch (final @NotNull InterruptedException e) {
					Thread.currentThread().interrupt();
				} finally {
					this.internalLock.unlockWrite(lockStamp);
				}
			}
		}

		private boolean tryLockWrite() {
			if (this.lockHoldCount.intValue() > 0
				&& (this.currentWritingThread.longValue() == Thread.currentThread().getId()
					|| Objects.containsOnly(this.readingThreads, Thread.currentThread().getId()))) {
				this.writeLockActive.incrementAndGet();
				this.lockHoldCount.incrementAndGet();
				this.currentWritingThread.set(Thread.currentThread().getId());
				return true;
			} else {
				final long lockStamp = this.internalLock.tryWriteLock();
				if (lockStamp != 0) {
					try {
						if (this.fileLock.get() != null) {
							return false;
						} else {
							this.internalLockWrite();
							return true;
						}
					} finally {
						this.internalLock.unlockWrite(lockStamp);
					}
				} else {
					return false;
				}
			}
		}

		private void internalLockWrite() {
			this.fileLock.updateAndGet(current -> {
				try {
					this.writeLockActive.set(1);
					this.lockHoldCount.set(1);
					this.currentWritingThread.set(Thread.currentThread().getId());
					return this.localRandomAccessFile.getChannel().lock(0, Long.MAX_VALUE, false);
				} catch (final @NotNull IOException e) {
					throw new UncheckedIOException(e.getMessage(), e);
				}
			});
		}

		private void unlockWrite() {
			this.fileLock.updateAndGet(current -> {
				if (this.lockHoldCount.intValue() == 0 || this.fileLock.get() == null || this.writeLockActive.get() == 0) {
					System.out.println(this.lockHoldCount.intValue() == 0);
					System.out.println(this.fileLock.get() == null);
					System.out.println(this.writeLockActive.get() == 0);
					throw new LockNotHeldException("Lock is not held");
				} else if (this.lockHoldCount.decrementAndGet() == 0) {
					try {
						if (current != null && current.isValid()) {
							current.release();
						}
						this.writeLockActive.set(0);
						this.currentWritingThread.set(-1);
						return null;
					} catch (final @NotNull IOException e) {
						throw new UncheckedIOException(e.getMessage(), e);
					}
				} else {
					this.writeLockActive.decrementAndGet();
					return current;
				}
			});
		}


		private void convertLock() {
			final long lockStamp = this.internalLock.writeLock();
			try {
				while (this.fileLock.get() != null) {
					Thread.sleep(5);
				}
				if (this.writeLockActive.get() > 0) {
					this.writeToRead();
				} else {
					this.readToWrite();
				}
			} catch (final @NotNull InterruptedException | RuntimeInterruptedException e) {
				Thread.currentThread().interrupt();
			} finally {
				this.internalLock.unlockWrite(lockStamp);
			}
		}

		private void writeToRead() {
			this.fileLock.updateAndGet(current -> {
				if (this.lockHoldCount.intValue() == 0 || this.fileLock.get() == null) {
					throw new LockNotHeldException("Lock is not held");
				} else if (this.lockHoldCount.intValue() == 1) {
					try {
						if (current != null && current.isValid()) {
							current.release();
						}
						this.writeLockActive.set(0);
						this.readingThreads.add(Thread.currentThread().getId());
						this.currentWritingThread.set(-1);
						return this.localRandomAccessFile.getChannel().lock(0, Long.MAX_VALUE, true);
					} catch (final @NotNull IOException e) {
						throw new UncheckedIOException(e.getMessage(), e);
					}
				} else {
					throw new LockNotHeldException("Lock is not held");
				}
			});
		}

		private void readToWrite() {
			this.fileLock.updateAndGet(current -> {
				if (this.lockHoldCount.intValue() == 0 || this.fileLock.get() == null) {
					throw new LockNotHeldException("Lock is not held");
				} else if (this.lockHoldCount.intValue() > 0 && this.fileLock.get() != null) {
					try {
						while (!this.readingThreads.isEmpty() && !Objects.containsOnly(this.readingThreads, Thread.currentThread().getId())) {
							Thread.sleep(5);
						}

						if (current != null && current.isValid()) {
							current.release();
						}
						this.writeLockActive.set(1);
						this.readingThreads.remove(Thread.currentThread().getId());
						this.currentWritingThread.set(Thread.currentThread().getId());
						return this.localRandomAccessFile.getChannel().lock(0, Long.MAX_VALUE, false);
					} catch (final @NotNull IOException e) {
						throw new UncheckedIOException(e.getMessage(), e);
					} catch (final @NotNull InterruptedException e) { //NOSONAR
						throw new RuntimeInterruptedException(e.getMessage());
					}
				} else {
					throw new LockNotHeldException("Lock could not be converted, lock is still being held");
				}
			});
		}


		private void close() throws IOException {
			final long lockStamp = ReadWriteLockableChannel.factoryLock.writeLock();
			try {
				try {
					this.unlock();
				} catch (final @NotNull LockNotHeldException e) {
					//Do nothing, no lock is held, so nothing has to be changed
				}

				if (this.instanceCount.decrementAndGet() == 0) {
					ReadWriteLockableChannel.openChannels.remove(this.getFilePath());
					this.localRandomAccessFile.close();
				}
			} finally {
				ReadWriteLockableChannel.factoryLock.unlockWrite(lockStamp);
			}
		}


		private void unlock() {
			this.fileLock.updateAndGet(current -> {
				if (this.lockHoldCount.intValue() == 0 || this.fileLock.get() == null) {
					throw new LockNotHeldException("Lock is not held");
				} else {
					final long currentThread = Thread.currentThread().getId();
					if (this.currentWritingThread.get() == currentThread) {
						this.lockHoldCount.set(0);
						this.readingThreads.clear();
						this.writeLockActive.set(0);
						this.currentWritingThread.set(-1);
						try {
							if (current != null && current.isValid()) {
								current.release();
							}
							return null;
						} catch (final @NotNull IOException e) {
							throw new UncheckedIOException(e.getMessage(), e);
						}
					}

					final long reading = this.readingThreads.stream().filter(id -> id == currentThread).count();
					if (reading > 0) {
						for (int i = 0; i < reading; i++) {
							this.lockHoldCount.updateAndGet(count -> (int) (count - reading));
							this.readingThreads.remove(currentThread);
						}
						if (this.lockHoldCount.get() == 0) {
							try {
								if (current != null && current.isValid()) {
									current.release();
								}
								return null;
							} catch (final @NotNull IOException e) {
								throw new UncheckedIOException(e.getMessage(), e);
							}
						}
					}
				}
				return current;
			});
		}


		private @NotNull
		Object readResolve() throws IOException {
			return ReadWriteLockableChannel.getOrCreateChannel(new File(this.absolutePath), this.writeSynchronized, this.writeMetaData);
		}
	}


	/**
	 * Local inner class representing a ReadLock for a File
	 */
	@EqualsAndHashCode
	private static class ReadLock implements ReadWriteFileLock {


		private final @NotNull ExtendedFileLock extendedFileLock;


		@Contract(pure = true)
		private ReadLock(final @NotNull ExtendedFileLock extendedFileLock) {
			this.extendedFileLock = extendedFileLock;
		}


		@Override
		public void lock() {
			this.extendedFileLock.readWriteLockableChannel.lockRead();
		}

		@Override
		public boolean tryLock() {
			return this.extendedFileLock.readWriteLockableChannel.tryLockRead();
		}

		@Override
		public void unlock() {
			this.extendedFileLock.readWriteLockableChannel.unlockRead();
		}

		@Override
		public @NotNull
		ExtendedFileLock.WriteLock convertLock() {
			this.extendedFileLock.readWriteLockableChannel.readToWrite();
			return this.extendedFileLock.writeLock;
		}

		@Override
		public @NotNull
		ExtendedFileLock baseLock() {
			return this.extendedFileLock;
		}

		@Override
		public @NotNull
		RandomAccessFile getRandomAccessFile() {
			return this.extendedFileLock.getRandomAccessFile();
		}

		@Override
		public @NotNull
		FileChannel getFileChannel() {
			return this.extendedFileLock.getFileChannel();
		}

		@Override
		public @NotNull
		String getFilePath() {
			return this.extendedFileLock.getFilePath();
		}


		@Override
		@Contract("-> new")
		public @NotNull
		Writer createWriter() {
			return this.extendedFileLock.createWriter();
		}

		@Override
		public @NotNull
		Writer createWriter(final @NotNull String csName) {
			return this.extendedFileLock.createWriter(csName);
		}

		@Override
		public @NotNull
		Writer createWriter(final @NotNull CharsetEncoder charsetEncoder,
							final int minBufferCap) {
			return this.extendedFileLock.createWriter(charsetEncoder, minBufferCap);
		}

		@Override
		@Contract("-> new")
		public @NotNull
		Reader createReader() {
			return this.extendedFileLock.createReader();
		}

		@Override
		public @NotNull
		Reader createReader(final @NotNull String csName) {
			return this.extendedFileLock.createReader(csName);
		}

		@Override
		@Contract("-> new")
		public @NotNull
		InputStream createInputStream() {
			return this.extendedFileLock.createInputStream();
		}

		@Override
		@Contract("-> new")
		public @NotNull
		OutputStream createOutputStream() {
			return this.extendedFileLock.createOutputStream();
		}

		@Override
		public void truncateChannel(final long size) throws IOException {
			this.extendedFileLock.truncateChannel(size);
		}


		@Override
		public void close() throws IOException {
			this.extendedFileLock.close();
		}
	}


	/**
	 * Local inner class representing a WriteLock for a File
	 */
	@EqualsAndHashCode
	private static class WriteLock implements ReadWriteFileLock {


		private final @NotNull ExtendedFileLock extendedFileLock;


		@Contract(pure = true)
		private WriteLock(final @NotNull ExtendedFileLock extendedFileLock) {
			this.extendedFileLock = extendedFileLock;
		}


		@Override
		public void lock() {
			this.extendedFileLock.readWriteLockableChannel.lockWrite();
		}

		@Override
		public boolean tryLock() {
			return this.extendedFileLock.readWriteLockableChannel.tryLockWrite();
		}

		@Override
		public void unlock() {
			this.extendedFileLock.readWriteLockableChannel.unlockWrite();
		}

		@Override
		public @NotNull
		ExtendedFileLock.ReadLock convertLock() {
			this.extendedFileLock.readWriteLockableChannel.writeToRead();
			return this.extendedFileLock.readLock;
		}

		@Override
		public @NotNull
		ExtendedFileLock baseLock() {
			return this.extendedFileLock;
		}


		@Override
		public @NotNull
		RandomAccessFile getRandomAccessFile() {
			return this.extendedFileLock.getRandomAccessFile();
		}

		@Override
		public @NotNull
		FileChannel getFileChannel() {
			return this.extendedFileLock.getFileChannel();
		}

		@Override
		public @NotNull
		String getFilePath() {
			return this.extendedFileLock.getFilePath();
		}


		@Override
		@Contract("-> new")
		public @NotNull
		Writer createWriter() {
			return this.extendedFileLock.createWriter();
		}

		@Override
		public @NotNull
		Writer createWriter(final @NotNull String csName) {
			return this.extendedFileLock.createWriter(csName);
		}

		@Override
		public @NotNull
		Writer createWriter(final @NotNull CharsetEncoder charsetEncoder,
							final int minBufferCap) {
			return this.extendedFileLock.createWriter(charsetEncoder, minBufferCap);
		}

		@Override
		@Contract("-> new")
		public @NotNull
		Reader createReader() {
			return this.extendedFileLock.createReader();
		}

		@Override
		public @NotNull
		Reader createReader(final @NotNull String csName) {
			return this.extendedFileLock.createReader(csName);
		}

		@Override
		@Contract("-> new")
		public @NotNull
		InputStream createInputStream() {
			return this.extendedFileLock.createInputStream();
		}

		@Override
		@Contract("-> new")
		public @NotNull
		OutputStream createOutputStream() {
			return this.extendedFileLock.createOutputStream();
		}

		@Override
		public void truncateChannel(final long size) throws IOException {
			this.extendedFileLock.truncateChannel(size);
		}


		@Override
		public void close() throws IOException {
			this.extendedFileLock.close();
		}
	}


	/**
	 * Class to throw a InterruptedException from an Unary Operator
	 */
	private static class RuntimeInterruptedException extends RuntimeException {

		private static final long serialVersionUID = 5207822674700149137L;

		public RuntimeInterruptedException(final @NotNull String message) {
			super(message);
		}
	}

	private static class LockNotHeldException extends IllegalMonitorStateException {

		private static final long serialVersionUID = 5517290635971429495L;

		public LockNotHeldException() {
			super();
		}

		public LockNotHeldException(final @NotNull String message) {
			super(message);
		}
	}
}