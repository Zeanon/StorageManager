package de.zeanon.storage.internal.base.interfaces;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.channels.FileChannel;
import org.jetbrains.annotations.NotNull;


@SuppressWarnings("unused")
public interface ReadWriteFileLock extends AutoCloseable {

	void lock();

	void unlock();

	@NotNull FileChannel getFileChannel();

	@NotNull String getFilePath();

	@NotNull PrintWriter createPrintWriter();

	@NotNull PrintWriter createPrintWriter(final @NotNull String csName);

	@NotNull BufferedReader createBufferedReader();

	@NotNull BufferedReader createBufferedReader(final @NotNull String csName);

	@NotNull BufferedReader createBufferedReader(final int buffer_size);

	@NotNull BufferedReader createBufferedReader(final @NotNull String csName, final int buffer_size);

	@Override
	void close() throws IOException;
}