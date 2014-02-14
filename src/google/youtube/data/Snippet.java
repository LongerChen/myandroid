package google.youtube.data;

public class Snippet {
	String publishedAt;
	String channelId;
	String title;
	String description;
	Thumbnails thumbnails;
	String channelTitle;
	String liveBroadcastContent;

	public String getPublishedAt() {
		return publishedAt;
	}

	public void setPublishedAt(String publishedAt) {
		this.publishedAt = publishedAt;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Thumbnails getThumbnails() {
		return thumbnails;
	}

	public void setThumbnails(Thumbnails thumbnails) {
		this.thumbnails = thumbnails;
	}

	public String getChannelTitle() {
		return channelTitle;
	}

	public void setChannelTitle(String channelTitle) {
		this.channelTitle = channelTitle;
	}

	public String getLiveBroadcastContent() {
		return liveBroadcastContent;
	}

	public void setLiveBroadcastContent(String liveBroadcastContent) {
		this.liveBroadcastContent = liveBroadcastContent;
	}

}
