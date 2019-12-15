package de.zeanon.storage.internal.utility.locks;

import de.zeanon.storage.internal.base.exceptions.RuntimeIOException;
import de.zeanon.storage.internal.base.interfaces.ReadWriteFileLock;
import java.io.*;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.charset.CharsetEncoder;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.StampedLock;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Lock that implements a ReadWriteLock behaviour for files
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
		this.readWriteLockableChannel = ReadWriteLockableChannel.getOrCreateChannel(file, true);
		this.readWriteLockableChannel.instanceCount++;
		this.writeLock = new WriteLock(this);
		this.readLock = new ReadLock(this);
	}

	@Contract(pure = true)
	public ExtendedFileLock(final @NotNull File file, final boolean writeMetaData) throws IOException {
		this.readWriteLockableChannel = ReadWriteLockableChannel.getOrCreateChannel(file, writeMetaData);
		this.readWriteLockableChannel.instanceCount++;
		this.writeLock = new WriteLock(this);
		this.readLock = new ReadLock(this);
	}


	/**
	 * Get yourself the WriteLock corresponding to this FileLock
	 */
	public @NotNull ReadWriteFileLock writeLock() {
		return this.writeLock;
	}

	/**
	 * Get yourself the ReadLock corresponding to this FileLock
	 */
	public @NotNull ReadWriteFileLock readLock() {
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

	public @NotNull RandomAccessFile getRandomAccessFile() {
		return this.readWriteLockableChannel.getRandomAccessFile();
	}

	public @NotNull FileChannel getFileChannel() {
		return this.readWriteLockableChannel.getFileChannel();
	}

	public @NotNull String getFilePath() {
		return this.readWriteLockableChannel.getFilePath();
	}


	@Contract("-> new")
	public @NotNull Writer createWriter() {
		return Channels.newWriter(this.readWriteLockableChannel.getFileChannel(), "UTF-8");
	}

	@Contract("null -> fail; !null -> new")
	public @NotNull Writer createWriter(final @NotNull String csName) {
		return Channels.newWriter(this.readWriteLockableChannel.getFileChannel(), csName);
	}

	@Contract("null, _ -> fail; !null, _ -> new")
	public @NotNull Writer createWriter(final @NotNull CharsetEncoder charsetEncoder,
										final int minBufferCap) {
		return Channels.newWriter(this.readWriteLockableChannel.getFileChannel(), charsetEncoder, minBufferCap);
	}

	@Contract("-> new")
	public @NotNull PrintWriter createPrintWriter() {
		return new PrintWriter(Channels.newWriter(this.readWriteLockableChannel.getFileChannel(), "UTF-8"));
	}

	@Contract("_ -> new")
	public @NotNull PrintWriter createPrintWriter(final boolean autoFlush) {
		return new PrintWriter(Channels.newWriter(this.readWriteLockableChannel.getFileChannel(), "UTF-8"), autoFlush);
	}

	@Contract("null -> fail; !null -> new")
	public @NotNull PrintWriter createPrintWriter(final @NotNull String csName) {
		return new PrintWriter(Channels.newWriter(this.readWriteLockableChannel.getFileChannel(), csName));
	}

	@Contract("null, _ -> fail; !null, _ -> new")
	public @NotNull PrintWriter createPrintWriter(final @NotNull String csName,
												  final boolean autoFlush) {
		return new PrintWriter(Channels.newWriter(this.readWriteLockableChannel.getFileChannel(), csName), autoFlush);
	}

	@Contract("-> new")
	public @NotNull BufferedReader createBufferedReader() {
		return new BufferedReader(Channels.newReader(this.readWriteLockableChannel.getFileChannel(), "UTF-8"));
	}

	@Contract("null -> fail; !null -> new")
	public @NotNull BufferedReader createBufferedReader(final @NotNull String csName) {
		return new BufferedReader(Channels.newReader(this.readWriteLockableChannel.getFileChannel(), csName));
	}

	@Contract("_ -> new")
	public @NotNull BufferedReader createBufferedReader(final int buffer_size) {
		return new BufferedReader(Channels.newReader(this.readWriteLockableChannel.getFileChannel(), "UTF-8"), buffer_size);
	}

	@Contract("null, _ -> fail; !null, _ -> new")
	public @NotNull BufferedReader createBufferedReader(final @NotNull String csName,
														final int buffer_size) {
		return new BufferedReader(Channels.newReader(this.readWriteLockableChannel.getFileChannel(), csName), buffer_size);
	}

	@Contract("-> new")
	public @NotNull BufferedInputStream createBufferedInputStream() {
		return new BufferedInputStream(Channels.newInputStream(this.readWriteLockableChannel.getFileChannel()));
	}

	@Contract("-> new")
	public @NotNull BufferedOutputStream createBufferedOutputStream() {
		return new BufferedOutputStream(Channels.newOutputStream(this.readWriteLockableChannel.getFileChannel()));
	}

	public void truncateChannel(final long size) throws IOException {
		this.readWriteLockableChannel.getFileChannel().truncate(size);
	}


	@Override
	public void close() throws IOException {
		this.readWriteLockableChannel.close();
	}

	public static class LockConversionException extends RuntimeException {

		private static final long serialVersionUID = -1401779907874296889L;

		public LockConversionException(final @NotNull String message) {
			super(message);
		}
	}
	/**
	 * Local inner class representing a ReadWriteLockable FileChannel
	 * There is at most one instance of ReadWriteLockableChannel created per File on the System
	 *
	 * @author Zeanon
	 * @version 1.1.0
	 */
	@EqualsAndHashCode
	private static class ReadWriteLockableChannel implements Serializable {


		private static final long serialVersionUID = 115260807719631111L;


		private static final transient @NotNull ConcurrentMap<String, ReadWriteLockableChannel> openChannels = new ConcurrentHashMap<>();
		private static final transient @NotNull StampedLock factoryLock = new StampedLock();


		private final @NotNull String absolutePath;
		private final boolean writeMetaData;

		private final transient @NotNull AtomicReference<ReferenceChannel> localChannel = new AtomicReference<>();
		private transient int instanceCount = 0;


		@Contract(pure = true)
		private ReadWriteLockableChannel(final @NotNull File file, final boolean writeMetaData) throws FileNotFoundException {
			this.localChannel.set(new ReferenceChannel(file, writeMetaData));
			this.absolutePath = file.getAbsolutePath();
			this.writeMetaData = writeMetaData;
		}

		public @NotNull Object readResolve() {
			try {
				return ReadWriteLockableChannel.getOrCreateChannel(new File(this.absolutePath), this.writeMetaData);
			} catch (IOException e) {
				throw new RuntimeIOException(e);
			}
		}

		@Contract(pure = true)
		private static @NotNull ReadWriteLockableChannel getOrCreateChannel(final @NotNull File file, final boolean writeMetaData) throws IOException {
			final long lockStamp = ReadWriteLockableChannel.factoryLock.readLock();
			try {
				if (!ReadWriteLockableChannel.openChannels.containsKey(file.getAbsolutePath())) {
					ReadWriteLockableChannel.openChannels.putIfAbsent(file.getAbsolutePath(), new ReadWriteLockableChannel(file, writeMetaData));
				}
				return ReadWriteLockableChannel.openChannels.get(file.getAbsolutePath());
			} finally {
				ReadWriteLockableChannel.factoryLock.unlockRead(lockStamp);
			}
		}

		@Contract(pure = true)
		private @NotNull RandomAccessFile getRandomAccessFile() {
			return this.localChannel.get().localRandomAccessFile;
		}

		@Contract(pure = true)
		private @NotNull FileChannel getFileChannel() {
			return this.getRandomAccessFile().getChannel();
		}

		@Contract(pure = true)
		private @NotNull String getFilePath() {
			return this.absolutePath;
		}

		private void lockRead() {
			this.localChannel.updateAndGet(current -> {
				if (!current.writeLockActive && current.lockHoldCount > 0) {
					current.lockHoldCount++;
				} else {
					try {
						while (current.writeLockActive) {
							Thread.sleep(5);
						}
						current.lockHoldCount = 1;
						current.fileLock = current.localRandomAccessFile.getChannel().lock(0, Long.MAX_VALUE, true);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					} catch (IOException e) {
						throw new RuntimeIOException(e.getMessage(), e.getCause());
					}
				}
				return current;
			});
		}

		private boolean tryLockRead() {
			return !this.localChannel.get().equals(this.localChannel.updateAndGet(current -> {
				if (!current.writeLockActive && current.lockHoldCount > 0) {
					current.lockHoldCount++;
				} else if (!current.writeLockActive) {
					try {
						current.lockHoldCount = 1;
						current.fileLock = current.localRandomAccessFile.getChannel().lock(0, Long.MAX_VALUE, true);
					} catch (IOException e) {
						throw new RuntimeIOException(e.getMessage(), e.getCause());
					}
				}
				return current;
			}));
		}

		private void unlockRead() {
			this.localChannel.updateAndGet(current -> {
				if (current.lockHoldCount == 0 || current.fileLock == null || current.writeLockActive) {
					throw new IllegalMonitorStateException("Lock ist not held");
				} else if ((current.lockHoldCount--) == 0) {
					try {
						if (current.fileLock.isValid()) {
							current.fileLock.release();
						}
						current.fileLock = null;
					} catch (IOException e) {
						throw new RuntimeIOException(e.getMessage(), e.getCause());
					}
				}
				return current;
			});
		}

		private void lockWrite() {
			this.localChannel.updateAndGet(current -> {
				if (current.writeLockActive && current.lockHoldCount > 0 && current.currentWritingThread == Thread.currentThread().getId()) {
					current.lockHoldCount++;
				} else {
					try {
						while (current.fileLock != null) {
							Thread.sleep(5);
						}
						current.writeLockActive = true;
						current.lockHoldCount = 1;
						current.currentWritingThread = Thread.currentThread().getId();
						current.fileLock = current.localRandomAccessFile.getChannel().lock(0, Long.MAX_VALUE, false);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					} catch (IOException e) {
						throw new RuntimeIOException(e.getMessage(), e.getCause());
					}
				}
				return current;
			});
		}

		private boolean tryLockWrite() {
			return !this.localChannel.get().equals(this.localChannel.updateAndGet(current -> {
				if (current.writeLockActive && current.lockHoldCount > 0 && current.currentWritingThread == Thread.currentThread().getId()) {
					current.lockHoldCount++;
				} else if (current.fileLock == null) {
					try {
						current.writeLockActive = true;
						current.lockHoldCount = 1;
						current.currentWritingThread = Thread.currentThread().getId();
						current.fileLock = current.localRandomAccessFile.getChannel().lock(0, Long.MAX_VALUE, false);
					} catch (IOException e) {
						throw new RuntimeIOException(e.getMessage(), e.getCause());
					}
				}
				return current;
			}));
		}

		private void unlockWrite() {
			this.localChannel.updateAndGet(current -> {
				if (current.lockHoldCount == 0 || current.fileLock == null || !current.writeLockActive) {
					throw new IllegalMonitorStateException("Lock ist not held");
				} else if ((current.lockHoldCount--) == 0) {
					try {
						if (current.fileLock.isValid()) {
							current.fileLock.release();
						}
						current.writeLockActive = false;
						current.currentWritingThread = -1;
						current.fileLock = null;
					} catch (IOException e) {
						throw new RuntimeIOException(e.getMessage(), e.getCause());
					}
				}
				return current;
			});
		}

		private void convertLock() {
			this.localChannel.updateAndGet(current -> {
				if (current.writeLockActive) {
					if (current.lockHoldCount == 0 || current.fileLock == null) {
						throw new IllegalMonitorStateException("Lock ist not held");
					} else if (current.lockHoldCount == 1) {
						try {
							if (current.fileLock.isValid()) {
								current.fileLock.release();
							}
							current.writeLockActive = false;
							current.currentWritingThread = -1;
							current.fileLock = current.localRandomAccessFile.getChannel().lock(0, Long.MAX_VALUE, true);
						} catch (IOException e) {
							throw new RuntimeIOException(e.getMessage(), e.getCause());
						}
					} else {
						throw new LockConversionException("Lock could not be converted, lock ist still being held");
					}
				} else {
					if (current.lockHoldCount == 0 || current.fileLock == null) {
						throw new IllegalMonitorStateException("Lock ist not held");
					} else if (current.lockHoldCount > 0) {
						try {
							while (current.lockHoldCount > 1) {
								Thread.sleep(5);
							}
							if (current.fileLock.isValid()) {
								current.fileLock.release();
							}
							current.writeLockActive = true;
							current.currentWritingThread = Thread.currentThread().getId();
							current.fileLock = current.localRandomAccessFile.getChannel().lock(0, Long.MAX_VALUE, false);
						} catch (IOException e) {
							throw new RuntimeIOException(e.getMessage(), e.getCause());
						} catch (InterruptedException e) {
							Thread.currentThread().interrupt();
						}
					} else {
						throw new LockConversionException("Lock could not be converted, lock ist still being held");
					}
				}
				return current;
			});
		}

		private void unlock() {
			this.localChannel.updateAndGet(current -> {
				if (current.lockHoldCount == 0 || current.fileLock == null) {
					throw new IllegalMonitorStateException("Lock ist not held");
				} else if ((current.lockHoldCount--) == 0) {
					if (current.writeLockActive) {
						try {
							if (current.fileLock.isValid()) {
								current.fileLock.release();
							}
							current.writeLockActive = false;
							current.currentWritingThread = -1;
						} catch (IOException e) {
							throw new RuntimeIOException(e.getMessage(), e.getCause());
						}
					} else {
						try {
							if (current.fileLock.isValid()) {
								current.fileLock.release();
							}
						} catch (IOException e) {
							throw new RuntimeIOException(e.getMessage(), e.getCause());
						}
					}
					current.fileLock = null;
				}
				return current;
			});
		}

		private void close() throws IOException {
			final long lockStamp = ReadWriteLockableChannel.factoryLock.writeLock();
			try {
				this.unlock();
				if ((this.instanceCount--) == 0) {
					ReadWriteLockableChannel.openChannels.remove(this.getFilePath());
					this.localChannel.get().localRandomAccessFile.close();
				}
			} finally {
				ReadWriteLockableChannel.factoryLock.unlockWrite(lockStamp);
			}
		}


		private static class ReferenceChannel {

			private final @NotNull StampedLock internalLock = new StampedLock();
			private final @NotNull RandomAccessFile localRandomAccessFile;
			private @Nullable FileLock fileLock;
			private boolean writeLockActive = false;
			private long currentWritingThread = -1;
			private int lockHoldCount = 0;


			@Contract(pure = true)
			private ReferenceChannel(final @NotNull File file, final boolean writeMetaData) throws FileNotFoundException {
				this.localRandomAccessFile = new RandomAccessFile(file, writeMetaData ? "rws" : "rwd");
			}
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
		public @NotNull ExtendedFileLock.WriteLock convertLock() {
			this.extendedFileLock.readWriteLockableChannel.convertLock();
			return this.extendedFileLock.writeLock;
		}

		@Override
		public @NotNull ExtendedFileLock baseLock() {
			return this.extendedFileLock;
		}

		@Override
		public @NotNull RandomAccessFile getRandomAccessFile() {
			return this.extendedFileLock.getRandomAccessFile();
		}

		@Override
		public @NotNull FileChannel getFileChannel() {
			return this.extendedFileLock.getFileChannel();
		}

		@Override
		public @NotNull String getFilePath() {
			return this.extendedFileLock.getFilePath();
		}


		@Override
		@Contract("-> new")
		public @NotNull Writer createWriter() {
			return this.extendedFileLock.createWriter();
		}

		@Override
		@Contract("null -> fail; !null -> new")
		public @NotNull Writer createWriter(final @NotNull String csName) {
			return this.extendedFileLock.createWriter(csName);
		}

		@Override
		@Contract("null, _ -> fail; !null, _ -> new")
		public @NotNull Writer createWriter(final @NotNull CharsetEncoder charsetEncoder,
											final int minBufferCap) {
			return this.extendedFileLock.createWriter(charsetEncoder, minBufferCap);
		}

		@Override
		@Contract("-> new")
		public @NotNull PrintWriter createPrintWriter() {
			return this.extendedFileLock.createPrintWriter();
		}

		@Override
		@Contract("_ -> new")
		public @NotNull PrintWriter createPrintWriter(final boolean autoFlush) {
			return this.extendedFileLock.createPrintWriter(autoFlush);
		}

		@Override
		@Contract("null -> fail; !null -> new")
		public @NotNull PrintWriter createPrintWriter(final @NotNull String csName) {
			return this.extendedFileLock.createPrintWriter(csName);
		}

		@Override
		@Contract("null, _ -> fail; !null, _ -> new")
		public @NotNull PrintWriter createPrintWriter(final @NotNull String csName, final boolean autoFlush) {
			return this.extendedFileLock.createPrintWriter(csName, autoFlush);
		}

		@Override
		@Contract("-> new")
		public @NotNull BufferedReader createBufferedReader() {
			return this.extendedFileLock.createBufferedReader();
		}

		@Override
		@Contract("null -> fail; !null -> new")
		public @NotNull BufferedReader createBufferedReader(final @NotNull String csName) {
			return this.extendedFileLock.createBufferedReader(csName);
		}

		@Override
		@Contract("_ -> new")
		public @NotNull BufferedReader createBufferedReader(final int buffer_size) {
			return this.extendedFileLock.createBufferedReader(buffer_size);
		}

		@Override
		@Contract("null, _ -> fail; !null, _ -> new")
		public @NotNull BufferedReader createBufferedReader(final @NotNull String csName,
															final int buffer_size) {
			return this.extendedFileLock.createBufferedReader(csName, buffer_size);
		}

		@Override
		@Contract("-> new")
		public @NotNull BufferedInputStream createBufferedInputStream() {
			return this.extendedFileLock.createBufferedInputStream();
		}

		@Override
		@Contract("-> new")
		public @NotNull BufferedOutputStream createBufferedOutputStream() {
			return this.extendedFileLock.createBufferedOutputStream();
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
		public @NotNull ExtendedFileLock.ReadLock convertLock() {
			this.extendedFileLock.readWriteLockableChannel.convertLock();
			return this.extendedFileLock.readLock;
		}

		@Override
		public @NotNull ExtendedFileLock baseLock() {
			return this.extendedFileLock;
		}


		@Override
		public @NotNull RandomAccessFile getRandomAccessFile() {
			return this.extendedFileLock.getRandomAccessFile();
		}

		@Override
		public @NotNull FileChannel getFileChannel() {
			return this.extendedFileLock.getFileChannel();
		}

		@Override
		public @NotNull String getFilePath() {
			return this.extendedFileLock.getFilePath();
		}


		@Override
		@Contract("-> new")
		public @NotNull Writer createWriter() {
			return this.extendedFileLock.createWriter();
		}

		@Override
		@Contract("null -> fail; !null -> new")
		public @NotNull Writer createWriter(final @NotNull String csName) {
			return this.extendedFileLock.createWriter(csName);
		}

		@Override
		@Contract("null, _ -> fail; !null, _ -> new")
		public @NotNull Writer createWriter(final @NotNull CharsetEncoder charsetEncoder,
											final int minBufferCap) {
			return this.extendedFileLock.createWriter(charsetEncoder, minBufferCap);
		}

		@Override
		@Contract("-> new")
		public @NotNull PrintWriter createPrintWriter() {
			return this.extendedFileLock.createPrintWriter();
		}

		@Override
		@Contract("_ -> new")
		public @NotNull PrintWriter createPrintWriter(final boolean autoFlush) {
			return this.extendedFileLock.createPrintWriter(autoFlush);
		}

		@Override
		@Contract("null -> fail; !null -> new")
		public @NotNull PrintWriter createPrintWriter(final @NotNull String csName) {
			return this.extendedFileLock.createPrintWriter(csName);
		}

		@Override
		@Contract("null, _ -> fail; !null, _ -> new")
		public @NotNull PrintWriter createPrintWriter(final @NotNull String csName, final boolean autoFlush) {
			return this.extendedFileLock.createPrintWriter(csName, autoFlush);
		}

		@Override
		@Contract("-> new")
		public @NotNull BufferedReader createBufferedReader() {
			return this.extendedFileLock.createBufferedReader();
		}

		@Override
		@Contract("null -> fail; !null -> new")
		public @NotNull BufferedReader createBufferedReader(final @NotNull String csName) {
			return this.extendedFileLock.createBufferedReader(csName);
		}

		@Override
		@Contract("_ -> new")
		public @NotNull BufferedReader createBufferedReader(final int buffer_size) {
			return this.extendedFileLock.createBufferedReader(buffer_size);
		}

		@Override
		@Contract("null, _ -> fail; !null, _ -> new")
		public @NotNull BufferedReader createBufferedReader(final @NotNull String csName, final int buffer_size) {
			return this.extendedFileLock.createBufferedReader(csName, buffer_size);
		}

		@Override
		@Contract("-> new")
		public @NotNull BufferedInputStream createBufferedInputStream() {
			return this.extendedFileLock.createBufferedInputStream();
		}

		@Override
		@Contract("-> new")
		public @NotNull BufferedOutputStream createBufferedOutputStream() {
			return this.extendedFileLock.createBufferedOutputStream();
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
}