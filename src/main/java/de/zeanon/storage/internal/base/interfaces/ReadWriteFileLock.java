package de.zeanon.storage.internal.base.interfaces;

import de.zeanon.storage.internal.utility.locks.ExtendedFileLock;
import java.io.*;
import java.nio.channels.FileChannel;
import org.jetbrains.annotations.NotNull;


/**
 * Basic interface for ReadWriteAble FileLocks
 */
@SuppressWarnings("unused")
public interface ReadWriteFileLock extends AutoCloseable {

	void lock();

	boolean tryLock();

	void unlock();

	@NotNull ExtendedFileLock baseLock();

	@NotNull FileChannel getFileChannel();

	@NotNull String getFilePath();

	@NotNull PrintWriter createPrintWriter();

	@NotNull PrintWriter createPrintWriter(final @NotNull String csName);

	@NotNull BufferedReader createBufferedReader();

	@NotNull BufferedReader createBufferedReader(final @NotNull String csName);

	@NotNull BufferedReader createBufferedReader(final int buffer_size);

	@NotNull BufferedReader createBufferedReader(final @NotNull String csName, final int buffer_size);

	@NotNull BufferedInputStream createBufferedInputStream();

	@NotNull BufferedOutputStream createBufferedOutputStream();

	void truncateChannel(final long size) throws IOException;


	@Override
	void close() throws IOException;
}