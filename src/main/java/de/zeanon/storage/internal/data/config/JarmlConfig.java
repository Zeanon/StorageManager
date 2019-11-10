package de.zeanon.storage.internal.data.config;

import de.zeanon.storage.internal.base.interfaces.CommentSettingBase;
import de.zeanon.storage.internal.base.interfaces.ConfigBase;
import de.zeanon.storage.internal.base.interfaces.DataTypeBase;
import de.zeanon.storage.internal.base.interfaces.ReloadSettingBase;
import de.zeanon.storage.internal.data.raw.JarmlFile;
import de.zeanon.storage.internal.data.section.JarmlConfigSection;
import de.zeanon.storage.internal.settings.Comment;
import de.zeanon.storage.internal.utils.basic.Objects;
import de.zeanon.storage.internal.utils.datafiles.JarmlUtils;
import de.zeanon.storage.internal.utils.editor.JarmlEditor;
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
 * Extended JarmlFile with added methods for Config purposes
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings("unused")
public class JarmlConfig extends JarmlFile implements ConfigBase {

	protected JarmlConfig(final @NotNull File file, final @Nullable InputStream inputStream, final @Nullable ReloadSettingBase reloadSetting, final @Nullable CommentSettingBase commentSetting, final @Nullable DataTypeBase dataType) {
		super(file, inputStream, reloadSetting, commentSetting == null ? Comment.PRESERVE : commentSetting, dataType);
	}


	@Override
	public List<String> getHeader() {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return JarmlUtils.getHeader(this.fileData, this.getDataType(), this.getCommentSetting());
		} else {
			return new ArrayList<>();
		}
	}

	@Override
	public void setHeader(final @Nullable List<String> header) {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			Map<String, Object> tempMap = JarmlUtils.setHeader(this.fileData, header, this.getDataType(), this.getCommentSetting());
			if (!this.fileData.toString().equals(tempMap.toString())) {
				JarmlEditor.writeData(this.file, tempMap, this.getCommentSetting());
			}
		}
	}

	@Override
	public List<String> getFooter() {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return JarmlUtils.getFooter(fileData, this.getDataType(), this.getCommentSetting());
		} else {
			return new ArrayList<>();
		}
	}

	@Override
	public void setFooter(final @Nullable List<String> footer) {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			Map<String, Object> tempMap = JarmlUtils.setFooter(this.fileData, footer, this.getDataType(), this.getCommentSetting());
			if (!this.fileData.toString().equals(tempMap.toString())) {
				JarmlEditor.writeData(this.file, tempMap, this.getCommentSetting());
			}
		}
	}

	public List<String> getHeader(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");

		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return JarmlUtils.getHeader(this.fileData, key, this.getDataType(), this.getCommentSetting());
		} else {
			return new ArrayList<>();
		}
	}

	public void setHeader(final @NotNull String key, final @Nullable List<String> header) {
		Objects.checkNull(key, "Key must not be null");

		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			Map<String, Object> tempMap = JarmlUtils.setHeader(this.fileData, key, header, this.getDataType(), this.getCommentSetting());
			if (!fileData.toString().equals(tempMap.toString())) {
				JarmlEditor.writeData(this.file, tempMap, this.getCommentSetting());
			}
		}
	}

	public List<String> getFooter(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");

		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return JarmlUtils.getFooter(this.fileData, key, getDataType(), this.getCommentSetting());
		} else {
			return new ArrayList<>();
		}
	}

	public void setFooter(final @NotNull String key, final @Nullable List<String> footer) {
		Objects.checkNull(key, "Key must not be null");

		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			Map<String, Object> tempMap = JarmlUtils.setFooter(this.fileData, key, footer, this.getDataType(), this.getCommentSetting());
			if (!fileData.toString().equals(tempMap.toString())) {
				JarmlEditor.writeData(this.file, tempMap, this.getCommentSetting());
			}
		}
	}

	@Override
	public List<String> getComments() {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return JarmlUtils.getComments(this.fileData, this.getDataType(), this.getCommentSetting(), true);
		} else {
			return new ArrayList<>();
		}
	}

	public List<String> getComments(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");

		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return JarmlUtils.getComments(this.fileData, key, this.getDataType(), this.getCommentSetting(), true);
		} else {
			return new ArrayList<>();
		}
	}

	public List<String> getBlockComments() {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return JarmlUtils.getComments(this.fileData, this.getDataType(), this.getCommentSetting(), false);
		} else {
			return new ArrayList<>();
		}
	}

	public List<String> getBlockComments(final @NotNull String key) {
		Objects.checkNull(key, "Key must not be null");

		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return JarmlUtils.getComments(this.fileData, key, this.getDataType(), this.getCommentSetting(), false);
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
	public JarmlConfigSection getSection(final @NotNull String sectionKey) {
		return new LocalSection(sectionKey, this);
	}


	private static class LocalSection extends JarmlConfigSection {

		private LocalSection(final @NotNull String sectionKey, final @NotNull JarmlConfig jarmlConfig) {
			super(sectionKey, jarmlConfig);
		}
	}
}