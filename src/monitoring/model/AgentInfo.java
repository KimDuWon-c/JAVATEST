package monitoring.model;

public class AgentInfo {
    private String modelName;
    private String agentId;
    private String agentName;
    private String monitoringDataPath;
    private String description;

    public AgentInfo() {
    }

    public AgentInfo(String modelName, String agentId, String agentName, String monitoringDataPath, String description) {
        this.modelName = modelName;
        this.agentId = agentId;
        this.agentName = agentName;
        this.monitoringDataPath = monitoringDataPath;
        this.description = description;
    }

    public String getModelName() {
        return modelName;
    }

    public String getAgentId() {
        return agentId;
    }

    public String getAgentName() {
        return agentName;
    }

    public String getMonitoringDataPath() {
        return monitoringDataPath;
    }

    public String getDescription() {
        return description;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public void setMonitoringDataPath(String monitoringDataPath) {
        this.monitoringDataPath = monitoringDataPath;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
