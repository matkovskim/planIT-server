package planit.project.dto;

public class MessageDTO {

	private String message;

	private Long createdAt;

	private String sender;

	private Long serverTeamId;

	private Long messageId;

	public MessageDTO() {

	}

	public MessageDTO(String message, Long createdAt, String sender, Long serverTeamId, Long messageId) {
		super();
		this.message = message;
		this.createdAt = createdAt;
		this.sender = sender;
		this.serverTeamId = serverTeamId;
		this.messageId = messageId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Long getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Long createdAt) {
		this.createdAt = createdAt;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public Long getServerTeamId() {
		return serverTeamId;
	}

	public void setServerTeamId(Long serverTeamId) {
		this.serverTeamId = serverTeamId;
	}

	public Long getMessageId() {
		return messageId;
	}

	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}

}
