package de.zeanon.storagemanager.internal.files.section;

import de.zeanon.storagemanager.internal.base.interfaces.CommentSetting;
import de.zeanon.storagemanager.internal.base.interfaces.Config;
import de.zeanon.storagemanager.internal.files.config.TomlConfig;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("unused")
public class TomlConfigSection extends TomlFileSection implements Config {


	private final @NotNull
	TomlConfig baseFile;


	protected TomlConfigSection(final @NotNull String sectionKey, final @NotNull TomlConfig baseFile) {
		super(sectionKey, baseFile);
		this.baseFile = baseFile;
	}

	protected TomlConfigSection(final @NotNull String[] sectionKey, final @NotNull TomlConfig baseFile) {
		super(sectionKey, baseFile);
		this.baseFile = baseFile;
	}


	@Override
	public void setCommentSetting(final @NotNull CommentSetting commentSetting) {
		this.baseFile.setCommentSetting(commentSetting);
	}

	@Override
	public @Nullable
	List<String> getHeader() {
		return this.baseFile.getHeader(this.getSectionKey());
	}

	@Override
	public void setHeader(final @Nullable String... header) {
		this.baseFile.setHeader(this.getSectionKey(), header);
	}

	public @Nullable
	List<String> getHeader(final @NotNull String blockKey) {
		return this.baseFile.getHeader(this.getFinalKey(blockKey));
	}

	public void setHeader(final @NotNull String blockKey, final @Nullable List<String> header) {
		this.baseFile.setHeader(this.getFinalKey(blockKey), header);
	}


	@Override
	public @NotNull
	List<String> getFooter() {
		return this.baseFile.getFooter(this.getSectionKey());
	}

	@Override
	public void setFooter(final @Nullable String... footer) {
		this.baseFile.setFooter(this.getSectionKey(), footer);
	}

	public @NotNull
	List<String> getFooter(final @NotNull String blockKey) {
		return this.baseFile.getFooter(this.getFinalKey(blockKey));
	}

	public void setFooter(final @NotNull String blockKey, final @Nullable List<String> footer) {
		this.baseFile.setFooter(this.getFinalKey(blockKey), footer);
	}


	@Override
	public @NotNull
	List<String> getComments() {
		return this.baseFile.getComments(this.getSectionKey());
	}

	public @NotNull
	List<String> getComments(final @NotNull String blockKey) {
		return this.baseFile.getComments(this.getFinalKey(blockKey));
	}

	public @NotNull
	List<String> getBlockComments() {
		return this.baseFile.getBlockComments(this.getSectionKey());
	}

	public @NotNull
	List<String> getBlockComments(final @NotNull String blockKey) {
		return this.baseFile.getBlockComments(this.getFinalKey(blockKey));
	}


	@Override
	public @NotNull
	TomlConfigSection getSection(final @NotNull String sectionKey) {
		return new TomlConfigSection(this.getFinalKey(sectionKey), this.baseFile);
	}

	@Override
	public @NotNull
	TomlConfigSection getSectionUseArray(final @NotNull String... sectionKey) {
		return new TomlConfigSection(this.getFinalArrayKey(sectionKey), this.baseFile);
	}
}