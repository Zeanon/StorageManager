package de.zeanon.storage.internal.base.cache.base;

import de.zeanon.storage.internal.base.exceptions.ProviderException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;


/**
 * Provider class to register a custom Collection Framework
 *
 * @param <M> the Map-Type to be used
 * @param <L> the List<Type to be used
 *
 * @author Zeanon
 * @version 1.3.0
 */
@Getter
@Setter
@AllArgsConstructor(onConstructor_ = {@Contract(pure = true)}, access = AccessLevel.PROTECTED)
@SuppressWarnings("unused")
public abstract class Provider<M extends Map, L extends List> {

	/**
	 * The Map implementation to be used
	 */
	private Class<? extends M> mapType;
	/**
	 * the List implementation to be used
	 */
	private Class<? extends L> listType;


	/**
	 * Get yourself a new Instance of the saved Map-Type
	 */
	public @NotNull M newMap() {
		try {
			return mapType.getDeclaredConstructor().newInstance();
		} catch (@NotNull InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
			throw new ProviderException(e);
		}
	}

	/**
	 * Get yourself a new Instance of the saved List-Type
	 */
	public @NotNull L newList() {
		try {
			return listType.getDeclaredConstructor().newInstance();
		} catch (@NotNull InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			throw new ProviderException(e);
		}
	}

	/**
	 * Get yourself a new Instance of the saved Map-Type with the given parameters
	 */
	public @NotNull M newMap(final @NotNull Object... parameters) {
		try {
			@NotNull List<Class<?>> parameterTypes = new ArrayList<>();
			for (@NotNull Object parameter : parameters) {
				parameterTypes.add(parameter.getClass());
			}
			return mapType.getDeclaredConstructor(parameterTypes.toArray(new Class<?>[0])).newInstance(parameters);
		} catch (@NotNull InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
			throw new ProviderException(e);
		}
	}

	/**
	 * Get yourself a new Instance of the saved Map-Type with the given parameters and parameter-types
	 */
	public @NotNull M newMap(final @NotNull Class<?>[] parameterTypes, final @NotNull Object... parameters) {
		try {
			return mapType.getDeclaredConstructor(parameterTypes).newInstance(parameters);
		} catch (@NotNull InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
			throw new ProviderException(e);
		}
	}

	/**
	 * Get yourself a new Instance of the saved List-Type with the given parameters
	 */
	public @NotNull L newList(final @NotNull Object... parameters) {
		try {
			@NotNull List<Class<?>> parameterTypes = new ArrayList<>();
			for (@NotNull Object parameter : parameters) {
				parameterTypes.add(parameter.getClass());
			}
			return listType.getDeclaredConstructor(parameterTypes.toArray(new Class<?>[0])).newInstance(parameters);
		} catch (@NotNull InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			throw new ProviderException(e);
		}
	}

	/**
	 * Get yourself a new Instance of the saved List-Type with the given parameters and parameter-types
	 */
	public @NotNull L newList(final @NotNull Class<?>[] parameterTypes, final @NotNull Object... parameters) {
		try {
			return listType.getDeclaredConstructor(parameterTypes).newInstance(parameters);
		} catch (@NotNull InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
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