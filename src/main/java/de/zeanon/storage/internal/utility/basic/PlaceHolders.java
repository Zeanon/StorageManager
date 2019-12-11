package de.zeanon.storage.internal.utility.basic;

import de.zeanon.storage.internal.base.cache.datamap.ConcurrentGapDataMap;
import de.zeanon.storage.internal.base.interfaces.DataMap;
import java.util.Map;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;


@SuppressWarnings("unused")
public class PlaceHolders {


	private final @NotNull DataMap<CharSequence, CharSequence> placeHolderMap;


	@Contract(pure = true)
	private PlaceHolders() {
		this.placeHolderMap = new ConcurrentGapDataMap<>();
	}


	@Contract(value = "-> new", pure = true)
	public static @NotNull PlaceHolders generateReplacer() {
		return new PlaceHolders();
	}

	@Contract("_, _ -> this")
	public @NotNull PlaceHolders add(final @NotNull String placeHolder, final @NotNull String replacement) {
		this.placeHolderMap.add(placeHolder, replacement);
		return this;
	}

	@Contract("_, _ -> this")
	public @NotNull PlaceHolders put(final @NotNull String placeHolder, final @NotNull String replacement) {
		this.placeHolderMap.put(placeHolder, replacement);
		return this;
	}

	@Contract("_ -> this")
	public @NotNull PlaceHolders remove(final @NotNull String placeHolder) {
		this.placeHolderMap.remove(placeHolder);
		return this;
	}

	public @NotNull String replace(final @NotNull String input) {
		@NotNull String output = input;
		for (final @NotNull Map.Entry<CharSequence, CharSequence> entry : placeHolderMap.entrySet()) {
			output = output.replace(entry.getKey(), entry.getValue());
		}
		return output;
	}
}