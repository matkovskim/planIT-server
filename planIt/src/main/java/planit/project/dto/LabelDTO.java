package planit.project.dto;

public class LabelDTO {

    private Long id;
    private Long globalId;
    private String name;
    private String color;
    private Long connectionId;

    public LabelDTO(Long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

	public Long getGlobalId() {
		return globalId;
	}

	public void setGlobalId(Long globalId) {
		this.globalId = globalId;
	}

	public Long getConnectionId() {
		return connectionId;
	}

	public void setConnectionId(Long connectionId) {
		this.connectionId = connectionId;
	}
	
}

