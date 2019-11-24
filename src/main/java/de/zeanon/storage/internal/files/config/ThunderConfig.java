package de.zeanon.storage.internal.files.config;

import de.zeanon.storage.internal.base.interfaces.CommentSetting;
import de.zeanon.storage.internal.base.interfaces.Config;
import de.zeanon.storage.internal.base.interfaces.ReloadSetting;
import de.zeanon.storage.internal.base.settings.Comment;
import de.zeanon.storage.internal.files.raw.ThunderFile;
import de.zeanon.storage.internal.files.section.ThunderConfigSection;
import de.zeanon.storage.internal.utility.utils.datafiles.ThunderUtils;
import de.zeanon.storage.internal.utility.utils.editor.ThunderEditor;
import de.zeanon.utils.basic.Objects;
import de.zeanon.utils.exceptions.FileParseException;
import de.zeanon.utils.exceptions.RuntimeIOException;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Extended ThunderFile with added methods for Config purposes
 *
 * @author Zeanon
 * @version 2.1.0
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class ThunderConfig extends ThunderFile implements Config {


	/**
	 * @param file           the File to be used as a backend
	 * @param inputStream    the FileContent to be set on the creation of the File
	 * @param reloadSetting  the ReloadSetting to be used with this instance
	 * @param commentSetting the CommentSetting to be used with this instance
	 *
	 * @throws RuntimeIOException if the File can not be accessed properly
	 * @throws FileParseException if the Content of the File can not be parsed properly
	 */
	protected ThunderConfig(final @NotNull File file, final @Nullable InputStream inputStream, final @NotNull ReloadSetting reloadSetting, final @NotNull CommentSetting commentSetting, final boolean bigMap) {
		super(file, inputStream, reloadSetting, commentSetting, bigMap);
	}


	@NotNull
	@Override
	public List<String> getHeader() {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return ThunderUtils.getHeader(this.getFileData());
		} else {
			return new ArrayList<>();
		}
	}

	@Override
	public void setHeader(final @Nullable List<String> header) {
		this.setHeader(header == null ? null : header.toArray(new String[0]));
	}

	@Override
	public void setHeader(final @Nullable String... header) {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			@NotNull String tempData = this.getFileData().toString();
			ThunderUtils.setHeader(this.getFileData(), header, this.getFastMap());
			if (!this.getFileData().toString().equals(tempData)) {
				ThunderEditor.writeData(this.getFile(), this.getFileData(), this.getCommentSetting());
			}
		}
	}

	@NotNull
	public List<String> getHeader(final @NotNull String key) {
		Objects.checkNull(key, "Key  must not be null");

		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return ThunderUtils.getHeader(this.getFileData(), key);
		} else {
			return new ArrayList<>();
		}
	}

	@NotNull
	public List<String> getHeaderUseArray(final @NotNull String... key) {
		Objects.checkNull(key, "Key  must not be null");

		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return ThunderUtils.getHeaderUseArray(this.getFileData(), key);
		} else {
			return new ArrayList<>();
		}
	}

	public void setHeader(final @NotNull String key, final @Nullable List<String> header) {
		this.setHeader(key, header == null ? null : header.toArray(new String[0]));
	}

	public void setHeader(final @NotNull String key, final @Nullable String... header) {
		Objects.checkNull(key, "Key  must not be null");

		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			@NotNull String tempData = this.getFileData().toString();
			ThunderUtils.setHeader(this.getFileData(), key, header, this.getFastMap());
			if (!this.getFileData().toString().equals(tempData)) {
				ThunderEditor.writeData(this.getFile(), this.getFileData(), this.getCommentSetting());
			}
		}
	}

	public void setHeaderUseArray(final @NotNull String[] key, final @Nullable List<String> header) {
		this.setHeaderUseArray(key, header == null ? null : header.toArray(new String[0]));
	}

	public void setHeaderUseArray(final @NotNull String[] key, final @Nullable String... header) {
		Objects.checkNull(key, "Key  must not be null");

		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			@NotNull String tempData = this.getFileData().toString();
			ThunderUtils.setHeaderUseArray(this.getFileData(), key, header, this.getFastMap());
			if (!this.getFileData().toString().equals(tempData)) {
				ThunderEditor.writeData(this.getFile(), this.getFileData(), this.getCommentSetting());
			}
		}
	}


	@NotNull
	@Override
	public List<String> getFooter() {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return ThunderUtils.getFooter(this.getFileData());
		} else {
			return new ArrayList<>();
		}
	}

	@Override
	public void setFooter(final @Nullable List<String> footer) {
		this.setFooter(footer == null ? null : footer.toArray(new String[0]));
	}

	@Override
	public void setFooter(final @Nullable String... footer) {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			@NotNull String tempData = this.getFileData().toString();
			ThunderUtils.setFooter(this.getFileData(), footer, this.getFastMap());
			if (!this.getFileData().toString().equals(tempData)) {
				ThunderEditor.writeData(this.getFile(), this.getFileData(), this.getCommentSetting());
			}
		}
	}

	@NotNull
	public List<String> getFooter(final @NotNull String key) {
		Objects.checkNull(key, "Key  must not be null");

		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return ThunderUtils.getFooter(this.getFileData(), key);
		} else {
			return new ArrayList<>();
		}
	}

	@NotNull
	public List<String> getFooterUseArray(final @NotNull String... key) {
		Objects.checkNull(key, "Key  must not be null");

		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return ThunderUtils.getFooterUseArray(this.getFileData(), key);
		} else {
			return new ArrayList<>();
		}
	}

	public void setFooter(final @NotNull String key, final @Nullable List<String> footer) {
		this.setFooter(key, footer == null ? null : footer.toArray(new String[0]));
	}

	public void setFooter(final @NotNull String key, final @Nullable String... footer) {
		Objects.checkNull(key, "Key  must not be null");

		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			@NotNull String tempData = this.getFileData().toString();
			ThunderUtils.setFooter(this.getFileData(), key, footer, this.getFastMap());
			if (!this.getFileData().toString().equals(tempData)) {
				ThunderEditor.writeData(this.getFile(), this.getFileData(), this.getCommentSetting());
			}
		}
	}

	public void setFooterUseArray(final @NotNull String[] key, final @Nullable List<String> footer) {
		this.setFooterUseArray(key, footer == null ? null : footer.toArray(new String[0]));
	}

	public void setFooterUseArray(final @NotNull String[] key, final @Nullable String... footer) {
		Objects.checkNull(key, "Key  must not be null");

		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			@NotNull String tempData = this.getFileData().toString();
			ThunderUtils.setFooterUseArray(this.getFileData(), key, footer, getFastMap());
			if (!this.getFileData().toString().equals(tempData)) {
				ThunderEditor.writeData(this.getFile(), this.getFileData(), this.getCommentSetting());
			}
		}
	}

	@NotNull
	@Override
	public List<String> getComments() {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return ThunderUtils.getComments(this.getFileData(), true);
		} else {
			return new ArrayList<>();
		}
	}

	@NotNull
	public List<String> getComments(final @NotNull String key) {
		Objects.checkNull(key, "Key  must not be null");

		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return ThunderUtils.getComments(this.getFileData(), key, true);
		} else {
			return new ArrayList<>();
		}
	}

	@NotNull
	public List<String> getCommentsUseArray(final @NotNull String... key) {
		Objects.checkNull(key, "Key  must not be null");

		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return ThunderUtils.getCommentsUseArray(this.getFileData(), key, true);
		} else {
			return new ArrayList<>();
		}
	}

	@NotNull
	public List<String> getBlockComments() {
		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return ThunderUtils.getComments(this.getFileData(), false);
		} else {
			return new ArrayList<>();
		}
	}

	@NotNull
	public List<String> getBlockComments(final @NotNull String key) {
		Objects.checkNull(key, "Key  must not be null");

		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return ThunderUtils.getComments(this.getFileData(), key, false);
		} else {
			return new ArrayList<>();
		}
	}

	@NotNull
	public List<String> getBlockCommentsUseArray(final @NotNull String... key) {
		Objects.checkNull(key, "Key  must not be null");

		this.update();

		if (this.getCommentSetting() == Comment.PRESERVE) {
			return ThunderUtils.getCommentsUseArray(this.getFileData(), key, false);
		} else {
			return new ArrayList<>();
		}
	}

	/**
	 * Get a Section with a defined SectionKey
	 *
	 * @param sectionKey the sectionKey to be used as a prefix by the Section
	 *
	 * @return the Section using the given sectionKey
	 */
	@NotNull
	@Override
	public ThunderConfigSection getSection(final @NotNull String sectionKey) {
		return new LocalSection(sectionKey, this);
	}

	@NotNull
	@Override
	public ThunderConfigSection getSectionUseArray(final @NotNull String... sectionKey) {
		return new LocalSection(sectionKey, this);
	}


	private static class LocalSection extends ThunderConfigSection {

		private LocalSection(final @NotNull String sectionKey, final @NotNull ThunderConfig thunderConfig) {
			super(sectionKey, thunderConfig);
		}

		private LocalSection(final @NotNull String[] sectionKey, final @NotNull ThunderConfig thunderConfig) {
			super(sectionKey, thunderConfig);
		}
	}
}