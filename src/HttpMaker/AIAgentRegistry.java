package HttpMaker;

import java.util.LinkedHashMap;
import java.util.Map;

public class AIAgentRegistry {
    private final Map<String, AIAgentInfo> agentsByModelName = new LinkedHashMap<>();

    public AIAgentRegistry() {
        register(new AIAgentInfo(
                "demo-model",
                "agent-001",
                "Demo Monitoring Agent",
                "monitoring-data.txt",
                "Reads monitoring file data and returns a JSON report."));
    }

    public void register(AIAgentInfo agentInfo) {
        agentsByModelName.put(agentInfo.getModelName(), agentInfo);
    }

    public AIAgentInfo findByModelName(String modelName) {
        return agentsByModelName.get(modelName);
    }
}
