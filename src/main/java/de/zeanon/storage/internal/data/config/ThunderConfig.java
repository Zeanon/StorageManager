package de.zeanon.storage.internal.data.config;

import de.zeanon.storage.internal.base.interfaces.CommentSettingBase;
import de.zeanon.storage.internal.base.interfaces.ConfigBase;
import de.zeanon.storage.internal.base.interfaces.DataTypeBase;
import de.zeanon.storage.internal.base.interfaces.ReloadSettingBase;
import de.zeanon.storage.internal.data.raw.ThunderFile;
import de.zeanon.storage.internal.data.section.ThunderConfigSection;
import de.zeanon.storage.internal.settings.Comment;
import de.zeanon.storage.internal.utils.basic.Objects;
import de.zeanon.storage.internal.utils.datafiles.ThunderUtils;
import de.zeanon.storage.internal.utils.editor.ThunderEditor;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Extended ThunderFile with added methods for Config purposes
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("unused")
public class ThunderConfig extends ThunderFile implements ConfigBase {

	protected ThunderConfig(final @NotNull File file, final @Nullable InputStream inputStream, final @Nullable ReloadSettingBase reloadSetting, final @Nullable CommentSettingBase commentSetting, final @Nullable DataTypeBase dataType) {
		super(file, inputStream, reloadSetting, commentSetting == null ? Comment.PRESERVE : commentSetting, dataType);
	}


	@Override
	public List<String> getHeader() {
		this.update();

		if (this.commentSetting() == Comment.PRESERVE) {
			return ThunderUtils.getHeader(this.fileData, this.dataType(), this.commentSetting());
		} else {
			return new ArrayList<>();
		}
	}

	@Override
	public void setHeader(final @Nullable List<String> header) {
		this.update();

		if (this.commentSetting() == Comment.PRESERVE) {
			Map<String, Object> tempMap = ThunderUtils.setHeader(this.fileData, header, this.dataType(), this.commentSetting());
			if (!this.fileData.toString().equals(tempMap.toString())) {
				ThunderEditor.writeData(this.file, tempMap, this.commentSetting());
			}
		}
	}

	public List<String> getHeader(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");

		this.update();

		if (this.commentSetting() == Comment.PRESERVE) {
			return ThunderUtils.getHeader(this.fileData, key, this.dataType(), this.commentSetting());
		} else {
			return new ArrayList<>();
		}
	}

	public void setHeader(final @NotNull String key, final @Nullable List<String> header) {
		Objects.checkNull(key, "Key must not be null");

		this.update();

		if (this.commentSetting() == Comment.PRESERVE) {
			Map<String, Object> tempMap = ThunderUtils.setHeader(this.fileData, key, header, this.dataType(), this.commentSetting());
			if (!fileData.toString().equals(tempMap.toString())) {
				ThunderEditor.writeData(this.file, tempMap, this.commentSetting());
			}
		}
	}


	@Override
	public List<String> getFooter() {
		this.update();

		if (this.commentSetting() == Comment.PRESERVE) {
			return ThunderUtils.getFooter(fileData, this.dataType(), this.commentSetting());
		} else {
			return new ArrayList<>();
		}
	}

	@Override
	public void setFooter(final @Nullable List<String> footer) {
		this.update();

		if (this.commentSetting() == Comment.PRESERVE) {
			Map<String, Object> tempMap = ThunderUtils.setFooter(this.fileData, footer, this.dataType(), this.commentSetting());
			if (!this.fileData.toString().equals(tempMap.toString())) {
				ThunderEditor.writeData(this.file, tempMap, this.commentSetting());
			}
		}
	}

	public List<String> getFooter(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");

		this.update();

		if (this.commentSetting() == Comment.PRESERVE) {
			return ThunderUtils.getFooter(this.fileData, key, this.dataType(), this.commentSetting());
		} else {
			return new ArrayList<>();
		}
	}

	public void setFooter(final @NotNull String key, final @Nullable List<String> footer) {
		Objects.checkNull(key, "Key must not be null");

		this.update();

		if (this.commentSetting() == Comment.PRESERVE) {
			Map<String, Object> tempMap = ThunderUtils.setFooter(this.fileData, key, footer, this.dataType(), this.commentSetting());
			if (!fileData.toString().equals(tempMap.toString())) {
				ThunderEditor.writeData(this.file, tempMap, this.commentSetting());
			}
		}
	}


	@Override
	public List<String> getComments() {
		this.update();

		if (this.commentSetting() == Comment.PRESERVE) {
			return ThunderUtils.getComments(this.fileData, this.dataType(), this.commentSetting(), true);
		} else {
			return new ArrayList<>();
		}
	}

	public List<String> getComments(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");

		this.update();

		if (this.commentSetting() == Comment.PRESERVE) {
			return ThunderUtils.getComments(this.fileData, key, this.dataType(), this.commentSetting(), true);
		} else {
			return new ArrayList<>();
		}
	}

	public List<String> getBlockComments() {
		this.update();

		if (this.commentSetting() == Comment.PRESERVE) {
			return ThunderUtils.getComments(this.fileData, this.dataType(), this.commentSetting(), false);
		} else {
			return new ArrayList<>();
		}
	}

	public List<String> getBlockComments(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");

		this.update();

		if (this.commentSetting() == Comment.PRESERVE) {
			return ThunderUtils.getComments(this.fileData, key, this.dataType(), this.commentSetting(), false);
		} else {
			return new ArrayList<>();
		}
	}

	/**
	 * Get a Section with a defined SectionKey
	 *
	 * @param sectionKey the sectionKey to be used as a prefix by the Section
	 * @return the Section using the given sectionKey
	 */
	@Override
	public ThunderConfigSection getSection(final @NotNull String sectionKey) {
		return new LocalSection(sectionKey, this);
	}


	private static class LocalSection extends ThunderConfigSection {

		private LocalSection(final @NotNull String sectionKey, final @NotNull ThunderConfig thunderConfig) {
			super(sectionKey, thunderConfig);
		}
	}
}