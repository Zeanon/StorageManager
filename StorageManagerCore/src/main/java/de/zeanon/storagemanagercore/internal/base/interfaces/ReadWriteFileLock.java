package de.zeanon.storagemanagercore.internal.base.interfaces;

import de.zeanon.storagemanagercore.internal.utility.filelock.ExtendedFileLock;
import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.charset.CharsetEncoder;
import org.jetbrains.annotations.NotNull;


/**
 * Basic interface for ReadWriteAble FileLocks
 */
@SuppressWarnings("unused")
public interface ReadWriteFileLock extends AutoCloseable {

	/**
	 * Lock this File
	 * Waits till it can acquire the lock
	 */
	void lock();

	/**
	 * Tries to lock this File
	 *
	 * @return true if the Lock could be acquired, otherwise false
	 */
	boolean tryLock();

	/**
	 * Release the Lock
	 */
	void unlock();

	/**
	 * Convert the Lock to it's counterpart (WriteLock to ReadLock and vice versa)
	 *
	 * @return the new Lock
	 */
	@NotNull ReadWriteFileLock convertLock();

	/**
	 * @return the ExtendedFileLock corresponding to this ReadWriteFileLock
	 */
	@NotNull ExtendedFileLock baseLock();

	/**
	 * @return the RandomAccessFile corresponding to this ReadWriteFileLock
	 */
	@NotNull RandomAccessFile getRandomAccessFile();

	/**
	 * @return the FileChannel corresponding to this ReadWriteFileLock
	 */
	@NotNull FileChannel getFileChannel();

	/**
	 * @return the FilePath of the corresponding File
	 */
	@NotNull String getFilePath();

	/**
	 * @return a new Writer for the corresponding File
	 */
	@NotNull Writer createWriter();

	/**
	 * @return a new Writer for the corresponding File
	 */
	@NotNull Writer createWriter(final @NotNull String csName);

	/**
	 * @return a new Writer for the corresponding File
	 */
	@NotNull Writer createWriter(final @NotNull CharsetEncoder charsetEncoder, final int minBufferCap);

	/**
	 * @return a new PrintWriter for the corresponding File
	 */
	default @NotNull PrintWriter createPrintWriter() {
		return new PrintWriter(this.createWriter());
	}

	/**
	 * @return a new PrintWriter for the corresponding File
	 */
	default @NotNull PrintWriter createPrintWriter(final boolean autoFlush) {
		return new PrintWriter(this.createWriter(), autoFlush);
	}

	/**
	 * @return a new PrintWriter for the corresponding File
	 */
	default @NotNull PrintWriter createPrintWriter(final @NotNull String csName) {
		return new PrintWriter(this.createWriter(csName));
	}

	/**
	 * @return a new PrintWriter for the corresponding File
	 */
	default @NotNull PrintWriter createPrintWriter(final @NotNull String csName, final boolean autoFlush) {
		return new PrintWriter(this.createWriter(csName), autoFlush);
	}

	/**
	 * @return a new Reader for the corresponding File
	 */
	@NotNull Reader createReader();

	/**
	 * @return a new Reader for the corresponding File
	 */
	@NotNull Reader createReader(final @NotNull String csName);

	/**
	 * @return a new BufferedReader for the corresponding File
	 */
	default @NotNull BufferedReader createBufferedReader() {
		return new BufferedReader(this.createReader());
	}

	/**
	 * @return a new BufferedReader for the corresponding File
	 */
	default @NotNull BufferedReader createBufferedReader(final @NotNull String csName) {
		return new BufferedReader(this.createReader(csName));
	}

	/**
	 * @return a new BufferedReader for the corresponding File
	 */
	default @NotNull BufferedReader createBufferedReader(final int buffer_size) {
		return new BufferedReader(this.createReader(), buffer_size);
	}

	/**
	 * @return a new BufferedReader for the corresponding File
	 */
	default @NotNull BufferedReader createBufferedReader(final @NotNull String csName, final int buffer_size) {
		return new BufferedReader(this.createReader(csName), buffer_size);
	}


	/**
	 * @return a new InputStream for the corresponding File
	 */
	@NotNull InputStream createInputStream();

	/**
	 * @return a new BufferedInputStream for the corresponding File
	 */
	default @NotNull BufferedInputStream createBufferedInputStream() {
		return new BufferedInputStream(this.createInputStream());
	}

	/**
	 * @return a new InputStream for the corresponding File
	 */
	@NotNull OutputStream createOutputStream();

	/**
	 * @return a new BufferedInputStream for the corresponding File
	 */
	default @NotNull BufferedOutputStream createBufferedOutputStream() {
		return new BufferedOutputStream(this.createOutputStream());
	}

	/**
	 * Truncate the FileChannel of the corresponding File to the given size
	 */
	void truncateChannel(final long size) throws IOException;

	default void clearFile() throws IOException {
		this.truncateChannel(0);
	}


	/**
	 * Close the underlying channel
	 */
	@Override
	void close() throws IOException;
}