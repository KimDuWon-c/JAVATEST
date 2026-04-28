package monitoring.service;

import java.util.LinkedHashMap;
import java.util.Map;

import monitoring.model.AgentInfo;

// Simple in-memory registry that maps model names to agent metadata.
public class AgentRegistry {
    private final Map<String, AgentInfo> agentsByModelName = new LinkedHashMap<String, AgentInfo>();

    public AgentRegistry() {
    }

    // Registers one model-to-agent mapping.
    public void register(AgentInfo agentInfo) {
        agentsByModelName.put(agentInfo.getModelName(), agentInfo);
    }

    // Looks up agent information by model name.
    public AgentInfo findByModelName(String modelName) {
        return agentsByModelName.get(modelName);
    }

    // Builds a small default registry so the demo can run without extra setup.
    public static AgentRegistry createDefault() {
        AgentRegistry registry = new AgentRegistry();
        registry.register(new AgentInfo(
                "demo-model",
                "agent-001",
                "Demo Monitoring Agent",
                "monitoring-data.txt",
                "Reads monitoring file data and returns a JSON report."));
        return registry;
    }
}
