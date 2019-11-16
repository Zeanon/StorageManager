package de.zeanon.storage.internal.settings;

import de.zeanon.storage.internal.base.exceptions.ObjectNullException;
import de.zeanon.storage.internal.base.interfaces.CommentSettingBase;
import de.zeanon.storage.internal.base.interfaces.DataTypeBase;
import de.zeanon.storage.internal.utils.basic.Objects;
import java.util.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * An Enum defining how the Data should be stored
 */
@SuppressWarnings("unused")
public enum DataType implements DataTypeBase {

	/**
	 * The Data is stored in a LinkedHashMap.
	 */
	SORTED {
		@NotNull
		@Override
		public Map<String, Object> getNewDataMap(final @Nullable CommentSettingBase commentSetting, final @Nullable Map<String, Object> map) {
			return map == null ? new LinkedHashMap<>() : new LinkedHashMap<>(map);
		}

		@NotNull
		@Override
		public List<String> getNewDataList(final @Nullable CommentSettingBase commentSetting, final @Nullable List<String> list) {
			return list == null ? new LinkedList<>() : new LinkedList<>(list);
		}
	},
	/**
	 * The Data is stored in a HashMap.
	 */
	STANDARD {
		@NotNull
		@Override
		public Map<String, Object> getNewDataMap(final @Nullable CommentSettingBase commentSetting, final @Nullable Map<String, Object> map) {
			return map == null ? new HashMap<>() : new HashMap<>(map);
		}

		@NotNull
		@Override
		public List<String> getNewDataList(final @Nullable CommentSettingBase commentSetting, final @Nullable List<String> list) {
			return list == null ? new ArrayList<>() : new ArrayList<>(list);
		}
	},
	/**
	 * The Storage type depends on the CommentSetting(HashMap for SKIP, LinkedHashMap for PRESERVE).
	 */
	AUTOMATIC {
		/**
		 * @throws ObjectNullException if the passed CommentSetting is null.
		 */
		@NotNull
		@Override
		public Map<String, Object> getNewDataMap(final @NotNull CommentSettingBase commentSetting, final @Nullable Map<String, Object> map) {
			if (Objects.notNull(commentSetting, "CommentSetting must not be null") == Comment.PRESERVE) {
				return map == null ? new LinkedHashMap<>() : new LinkedHashMap<>(map);
			} else {
				return map == null ? new HashMap<>() : new HashMap<>(map);
			}
		}

		/**
		 * @throws ObjectNullException if the passed CommentSetting is null.
		 */
		@NotNull
		@Override
		public List<String> getNewDataList(final @NotNull CommentSettingBase commentSetting, final @Nullable List<String> list) {
			if (Objects.notNull(commentSetting, "CommentSetting must not be null") == Comment.PRESERVE) {
				return list == null ? new LinkedList<>() : new LinkedList<>(list);
			} else {
				return list == null ? new ArrayList<>() : new ArrayList<>(list);
			}
		}
	};

	@NotNull
	@Override
	public abstract Map<String, Object> getNewDataMap(final @NotNull CommentSettingBase commentSetting, final @Nullable Map<String, Object> map);

	@NotNull
	@Override
	public abstract List<String> getNewDataList(final @NotNull CommentSettingBase commentSetting, final @Nullable List<String> list);
}