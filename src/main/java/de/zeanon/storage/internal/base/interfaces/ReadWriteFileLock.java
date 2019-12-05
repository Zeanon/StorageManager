package de.zeanon.storage.internal.base.interfaces;

import java.io.IOException;
import java.nio.channels.FileChannel;
import org.jetbrains.annotations.NotNull;


@SuppressWarnings("unused")
public interface ReadWriteFileLock extends AutoCloseable {

	void lock() throws IOException;

	void unlock();

	@NotNull FileChannel getChannel();

	@NotNull String getFilePath();

	@Override
	void close();
}