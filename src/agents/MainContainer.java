package agents;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.ExtendedProperties;
import jade.util.leap.Properties;
import jade.wrapper.AgentContainer;
import jade.wrapper.ControllerException;

public class MainContainer {
    public static void main(String[] args) throws ControllerException {
        Runtime runtime = Runtime.instance();
        Properties p = new ExtendedProperties();
        p.setProperty("gui","true");
        ProfileImpl pc = new ProfileImpl(p);
        AgentContainer container = runtime.createMainContainer(pc);
        container.start();

    }
}
