package de.zeanon.storage.internal.base.interfaces;

import java.nio.channels.FileChannel;
import org.jetbrains.annotations.NotNull;


@SuppressWarnings("unused")
public interface ReadWriteFileLock extends AutoCloseable {

	void lock();

	void unlock();

	@NotNull FileChannel getChannel();

	@NotNull String getFilePath();

	@Override
	void close();
}