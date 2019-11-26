package de.zeanon.storage.internal.base.cache.base;

import de.zeanon.storage.internal.base.exceptions.ProviderException;
import java.lang.reflect.InvocationTargetException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;


@SuppressWarnings("unused")
@AllArgsConstructor(onConstructor_ = {@Contract(pure = true)}, access = AccessLevel.PROTECTED)
public abstract class Provider<M, L> {

	private Class<? extends M> mapType;
	private Class<? extends L> listType;

	public @NotNull M newMap() {
		try {
			return mapType.getDeclaredConstructor().newInstance();
		} catch (@NotNull InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
			throw new ProviderException(e);
		}
	}

	public @NotNull L newList() {
		try {
			return listType.getDeclaredConstructor().newInstance();
		} catch (@NotNull InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			throw new ProviderException(e);
		}
	}

	public @NotNull M newMap(Class<?>[] parameterTypes, Object... parameters) {
		try {
			return mapType.getDeclaredConstructor(parameterTypes).newInstance(parameters);
		} catch (@NotNull InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
			throw new ProviderException(e);
		}
	}

	public @NotNull L newList(Class<?>[] parameterTypes, Object... parameters) {
		try {
			return listType.getDeclaredConstructor(parameterTypes).newInstance(parameters);
		} catch (@NotNull InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			throw new ProviderException(e);
		}
	}

	public String getMapType() {
		return this.mapType.getTypeName();
	}

	public void setMapType(final @NotNull Class<? extends M> mapType) {
		this.mapType = mapType;
	}

	public String getListType() {
		return this.listType.getTypeName();
	}

	public void setListType(final @NotNull Class<? extends L> listType) {
		this.listType = listType;
	}
}