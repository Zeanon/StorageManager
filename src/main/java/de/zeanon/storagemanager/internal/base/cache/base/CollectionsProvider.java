package de.zeanon.storagemanager.internal.base.cache.base;

import de.zeanon.storagemanager.external.browniescollections.GapList;
import de.zeanon.storagemanager.internal.base.exceptions.ProviderException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
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
 * @version 1.5.0
 */
@AllArgsConstructor(onConstructor_ = {@Contract(pure = true)})
@SuppressWarnings("unused")
public class CollectionsProvider<M extends Map, L extends List> implements Serializable {


	private static final long serialVersionUID = 474999389806011522L;


	/**
	 * The Map implementation to be used
	 */
	@Getter
	private volatile @NotNull Class<? extends M> mapType;
	/**
	 * The List implementation to be used
	 */
	@Getter
	private volatile @NotNull Class<? extends L> listType;


	public void setMapType(final @NotNull Class<? extends M> mapType) {
		this.mapType = mapType;
	}

	public void setListType(final @NotNull Class<? extends L> listType) {
		this.listType = listType;
	}


	/**
	 * Get yourself a new Instance of the saved Map-Type
	 */
	public @NotNull M newMap() {
		try {
			return this.mapType.getDeclaredConstructor().newInstance();
		} catch (final @NotNull InstantiationException
				| InvocationTargetException
				| NoSuchMethodException
				| IllegalAccessException e) {
			throw new ProviderException(e);
		}
	}

	/**
	 * Get yourself a new Instance of the saved List-Type
	 */
	public @NotNull L newList() {
		try {
			return this.listType.getDeclaredConstructor().newInstance();
		} catch (final @NotNull InstantiationException
				| IllegalAccessException
				| InvocationTargetException
				| NoSuchMethodException e) {
			throw new ProviderException(e);
		}
	}

	/**
	 * Get yourself a new Instance of the saved Map-Type with the given parameters
	 */
	public @NotNull M newMap(final @NotNull Object... parameters) {
		try {
			final @NotNull List<Class<?>> parameterTypes = new GapList<>();
			for (final @NotNull Object parameter : parameters) {
				parameterTypes.add(parameter.getClass());
			}
			return this.mapType.getDeclaredConstructor(parameterTypes.toArray(new Class<?>[0]))
							   .newInstance(parameters);
		} catch (final @NotNull InstantiationException
				| InvocationTargetException
				| NoSuchMethodException
				| IllegalAccessException e) {
			throw new ProviderException(e);
		}
	}

	/**
	 * Get yourself a new Instance of the saved Map-Type with the given parameters and parameter-types
	 */
	public @NotNull M newMap(final @NotNull Class<?>[] parameterTypes,
							 final @NotNull Object... parameters) {
		try {
			return this.mapType.getDeclaredConstructor(parameterTypes)
							   .newInstance(parameters);
		} catch (final @NotNull InstantiationException
				| InvocationTargetException
				| NoSuchMethodException
				| IllegalAccessException e) {
			throw new ProviderException(e);
		}
	}

	/**
	 * Get yourself a new Instance of the saved List-Type with the given parameters
	 */
	public @NotNull L newList(final @NotNull Object... parameters) {
		try {
			final @NotNull List<Class<?>> parameterTypes = new GapList<>();
			for (final @NotNull Object parameter : parameters) {
				parameterTypes.add(parameter.getClass());
			}
			return this.listType.getDeclaredConstructor(parameterTypes.toArray(new Class<?>[0]))
								.newInstance(parameters);
		} catch (final @NotNull InstantiationException
				| IllegalAccessException
				| InvocationTargetException
				| NoSuchMethodException e) {
			throw new ProviderException(e);
		}
	}

	/**
	 * Get yourself a new Instance of the saved List-Type with the given parameters and parameter-types
	 */
	public @NotNull L newList(final @NotNull Class<?>[] parameterTypes,
							  final @NotNull Object... parameters) {
		try {
			return this.listType.getDeclaredConstructor(parameterTypes)
								.newInstance(parameters);
		} catch (final @NotNull InstantiationException
				| IllegalAccessException
				| InvocationTargetException
				| NoSuchMethodException e) {
			throw new ProviderException(e);
		}
	}

	/**
	 * Get the TypeName of the saved Map-Type
	 */
	public String getMapTypeName() {
		return this.mapType.getTypeName();
	}

	/**
	 * Get the TypeName of the save List-Type
	 */
	public String getListTypeName() {
		return this.listType.getTypeName();
	}
}