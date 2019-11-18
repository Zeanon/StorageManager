package de.zeanon.storage.internal.settings;

import de.zeanon.storage.internal.base.exceptions.ObjectNullException;
import de.zeanon.storage.internal.base.interfaces.CommentSettingBase;
import de.zeanon.storage.internal.base.interfaces.DataList;
import de.zeanon.storage.internal.base.interfaces.DataTypeBase;
import de.zeanon.storage.internal.base.lists.ArrayDataList;
import de.zeanon.storage.internal.base.lists.LinkedDataList;
import de.zeanon.storage.internal.utils.basic.Objects;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * An Enum defining how the Data should be stored
 */
@SuppressWarnings("unused")
public enum DataType implements DataTypeBase {

	/**
	 * The Data is stored in a LinkedHashMap
	 */
	SORTED {
		@NotNull
		@Override
		public <E> DataList<E> getNewDataList(final @Nullable CommentSettingBase commentSetting, final @Nullable DataList<E> list) {
			return list == null ? new LinkedDataList<>() : new LinkedDataList<>(list);
		}

		@NotNull
		@Override
		public <E> DataList<E> getNewDataList(final @Nullable CommentSettingBase commentSetting, final @Nullable List<E> list) {
			return list == null ? new LinkedDataList<>() : new LinkedDataList<>(list);
		}

		@NotNull
		@Override
		public <K> List<K> getNewList(final @Nullable CommentSettingBase commentSetting, final @Nullable List<K> list) {
			return list == null ? new LinkedList<>() : new LinkedList<>(list);
		}
	},
	/**
	 * The Data is stored in a HashMap
	 */
	STANDARD {
		@NotNull
		@Override
		public <E> DataList<E> getNewDataList(final @Nullable CommentSettingBase commentSetting, final @Nullable DataList<E> list) {
			return list == null ? new ArrayDataList<>() : new ArrayDataList<>(list);
		}

		@NotNull
		@Override
		public <E> DataList<E> getNewDataList(final @Nullable CommentSettingBase commentSetting, final @Nullable List<E> list) {
			return list == null ? new ArrayDataList<>() : new ArrayDataList<>(list);
		}

		@NotNull
		@Override
		public <K> List<K> getNewList(final @Nullable CommentSettingBase commentSetting, final @Nullable List<K> list) {
			return list == null ? new ArrayList<>() : new ArrayList<>(list);
		}
	},
	/**
	 * The Storage type depends on the CommentSetting(HashMap for SKIP, LinkedHashMap for PRESERVE)
	 */
	AUTOMATIC {
		/**
		 * @throws ObjectNullException if the passed CommentSetting is null
		 */
		@NotNull
		@Override
		public <E> DataList<E> getNewDataList(final @NotNull CommentSettingBase commentSetting, final @Nullable DataList<E> list) {
			if (Objects.notNull(commentSetting, "CommentSetting must not be null") == Comment.PRESERVE) {
				return list == null ? new LinkedDataList<>() : new LinkedDataList<>(list);
			} else {
				return list == null ? new ArrayDataList<>() : new ArrayDataList<>(list);
			}
		}

		@NotNull
		@Override
		public <E> DataList<E> getNewDataList(final @NotNull CommentSettingBase commentSetting, final @Nullable List<E> list) {
			if (Objects.notNull(commentSetting, "CommentSetting must not be null") == Comment.PRESERVE) {
				return list == null ? new LinkedDataList<>() : new LinkedDataList<>(list);
			} else {
				return list == null ? new ArrayDataList<>() : new ArrayDataList<>(list);
			}
		}

		/**
		 * @throws ObjectNullException if the passed CommentSetting is null
		 */
		@NotNull
		@Override
		public <K> List<K> getNewList(final @NotNull CommentSettingBase commentSetting, final @Nullable List<K> list) {
			if (Objects.notNull(commentSetting, "CommentSetting must not be null") == Comment.PRESERVE) {
				return list == null ? new LinkedList<>() : new LinkedList<>(list);
			} else {
				return list == null ? new ArrayList<>() : new ArrayList<>(list);
			}
		}
	};

	@NotNull
	@Override
	public abstract <E> DataList<E> getNewDataList(final @NotNull CommentSettingBase commentSetting, final @Nullable DataList<E> list);

	@NotNull
	@Override
	public abstract <E> DataList<E> getNewDataList(final @NotNull CommentSettingBase commentSetting, final @Nullable List<E> list);

	@NotNull
	@Override
	public abstract <K> List<K> getNewList(final @NotNull CommentSettingBase commentSetting, final @Nullable List<K> list);
}