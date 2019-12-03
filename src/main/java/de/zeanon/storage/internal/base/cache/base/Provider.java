package de.zeanon.storage.internal.base.cache.base;

import de.zeanon.storage.internal.base.exceptions.ProviderException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;


/**
 * Provider class to register a custom Collection Framework
 *
 * @param <M> the basic Map-Type to be used
 * @param <L> the basic List<Type to be used
 *
 * @author Zeanon
 * @version 1.3.0
 */
@AllArgsConstructor(onConstructor_ = {@Contract(pure = true)}, access = AccessLevel.PROTECTED)
@SuppressWarnings("unused")
public abstract class Provider<M extends Map, L extends List> {


	/**
	 * Lock access to the mapType so no new MapType is set while a new instance is generated
	 */
	private final @NotNull ReadWriteLock mapLock = new ReentrantReadWriteLock(true);
	/**
	 * Lock access to the listType so no new ListType is set while a new instance is generated
	 */
	private final @NotNull ReadWriteLock listLock = new ReentrantReadWriteLock(true);


	/**
	 * The Map implementation to be used
	 */
	@Getter
	private @NotNull Class<? extends M> mapType;
	/**
	 * the List implementation to be used
	 */
	@Getter
	private @NotNull Class<? extends L> listType;


	public void setMapType(final @NotNull Class<? extends M> mapType) {
		this.mapLock.writeLock().lock();
		try {
			this.mapType = mapType;
		} finally {
			this.mapLock.writeLock().unlock();
		}
	}

	public void setListType(final @NotNull Class<? extends L> listType) {
		this.listLock.writeLock().lock();
		try {
			this.listType = listType;
		} finally {
			this.listLock.writeLock().unlock();
		}
	}


	/**
	 * Get yourself a new Instance of the saved Map-Type
	 */
	public @NotNull M newMap() {
		this.mapLock.readLock().lock();
		try {
			return mapType.getDeclaredConstructor().newInstance();
		} catch (@NotNull InstantiationException
				| InvocationTargetException
				| NoSuchMethodException
				| IllegalAccessException e) {
			throw new ProviderException(e);
		} finally {
			this.mapLock.readLock().unlock();
		}
	}

	/**
	 * Get yourself a new Instance of the saved List-Type
	 */
	public @NotNull L newList() {
		this.listLock.readLock().lock();
		try {
			return listType.getDeclaredConstructor().newInstance();
		} catch (@NotNull InstantiationException
				| IllegalAccessException
				| InvocationTargetException
				| NoSuchMethodException e) {
			throw new ProviderException(e);
		} finally {
			this.listLock.readLock().unlock();
		}
	}

	/**
	 * Get yourself a new Instance of the saved Map-Type with the given parameters
	 */
	public @NotNull M newMap(final @NotNull Object... parameters) {
		try {
			this.mapLock.readLock().lock();
			final @NotNull List<Class<?>> parameterTypes = new ArrayList<>();
			for (@NotNull Object parameter : parameters) {
				parameterTypes.add(parameter.getClass());
			}
			return mapType.getDeclaredConstructor(parameterTypes.toArray(new Class<?>[0]))
						  .newInstance(parameters);
		} catch (@NotNull InstantiationException
				| InvocationTargetException
				| NoSuchMethodException
				| IllegalAccessException e) {
			throw new ProviderException(e);
		} finally {
			this.mapLock.readLock().unlock();
		}
	}

	/**
	 * Get yourself a new Instance of the saved Map-Type with the given parameters and parameter-types
	 */
	public @NotNull M newMap(final @NotNull Class<?>[] parameterTypes,
							 final @NotNull Object... parameters) {
		this.mapLock.readLock().lock();
		try {
			return mapType.getDeclaredConstructor(parameterTypes)
						  .newInstance(parameters);
		} catch (@NotNull InstantiationException
				| InvocationTargetException
				| NoSuchMethodException
				| IllegalAccessException e) {
			throw new ProviderException(e);
		} finally {
			this.mapLock.readLock().unlock();
		}
	}

	/**
	 * Get yourself a new Instance of the saved List-Type with the given parameters
	 */
	public @NotNull L newList(final @NotNull Object... parameters) {
		this.listLock.readLock().lock();
		try {
			final @NotNull List<Class<?>> parameterTypes = new ArrayList<>();
			for (@NotNull Object parameter : parameters) {
				parameterTypes.add(parameter.getClass());
			}
			return listType.getDeclaredConstructor(parameterTypes.toArray(new Class<?>[0]))
						   .newInstance(parameters);
		} catch (@NotNull InstantiationException
				| IllegalAccessException
				| InvocationTargetException
				| NoSuchMethodException e) {
			throw new ProviderException(e);
		} finally {
			this.listLock.readLock().unlock();
		}
	}

	/**
	 * Get yourself a new Instance of the saved List-Type with the given parameters and parameter-types
	 */
	public @NotNull L newList(final @NotNull Class<?>[] parameterTypes,
							  final @NotNull Object... parameters) {
		this.listLock.readLock().lock();
		try {
			return listType.getDeclaredConstructor(parameterTypes)
						   .newInstance(parameters);
		} catch (@NotNull InstantiationException
				| IllegalAccessException
				| InvocationTargetException
				| NoSuchMethodException e) {
			throw new ProviderException(e);
		} finally {
			this.listLock.readLock().unlock();
		}
	}

	/**
	 * Get the TypeName of the saved Map-Type
	 */
	public String getMapTypeName() {
		this.mapLock.readLock().lock();
		try {
			return this.mapType.getTypeName();
		} finally {
			this.mapLock.readLock().unlock();
		}
	}

	/**
	 * Get the TypeName of the save List-Type
	 */
	public String getListTypeName() {
		this.listLock.readLock().lock();
		try {
			return this.listType.getTypeName();
		} finally {
			this.listLock.readLock().lock();
		}
	}
}