package tests;

import org.apache.jmeter.protocol.http.control.Header;
import org.apache.jmeter.protocol.http.control.HeaderManager;
import org.apache.jmeter.protocol.http.sampler.HTTPSampler;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.testelement.TestPlan;
import org.apache.jmeter.threads.ThreadGroup;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.ListedHashTree;

import java.io.File;

public class N11LoadTest {
    public static void main(String[] args) {
        try {

            // Set JMeter Home directory in C driver
            String jmeterHome = "C:\\apache-jmeter-5.6.3";
            setupJMeterEnvironment(jmeterHome);

            // Initialize JMeter
            JMeterUtils.setJMeterHome(jmeterHome);
            JMeterUtils.loadJMeterProperties(jmeterHome + "/bin/jmeter.properties");
            JMeterUtils.initLocale();

            // Create JMeter Engine
            StandardJMeterEngine jmeter = new StandardJMeterEngine();

            // Create Test Plan
            TestPlan testPlan = new TestPlan("N11 Search Test Plan");

            // Create Thread Group
            ThreadGroup threadGroup = new ThreadGroup();
            threadGroup.setName("N11 Search Thread Group");
            threadGroup.setNumThreads(1);
            threadGroup.setRampUp(1);

            // Create Loop Controller
            LoopController loopController = new LoopController();
            loopController.setLoops(1);
            loopController.setFirst(true);
            threadGroup.setSamplerController(loopController);

            // Create HTTP Header Manager
            HeaderManager headerManager = new HeaderManager();
            headerManager.add(new Header("Content-Type", "application/json"));
            headerManager.add(new Header("Accept", "application/json"));
            headerManager.add(new Header("User-Agent", "Mozilla/5.0"));

            // Create search samplers
            HTTPSampler[] samplers = {
                    createSearchRequest("Basic Search", "laptop"),
                    createSearchRequest("Special Char Search", "ipad&Tablet kalemi"),
                    createSearchRequest("Numeric Search", "iphone14"),
                    createSearchRequest("Empty Search", ""),
                    createSearchRequest("Turkish Char Search", "bilgisayar")
            };

            // Build the test plan tree
            ListedHashTree testPlanTree = new ListedHashTree();
            ListedHashTree threadGroupHashTree = testPlanTree.add(testPlan).add(threadGroup);

            // Add elements to thread group
            threadGroupHashTree.add(headerManager);
            for (HTTPSampler sampler : samplers) {
                threadGroupHashTree.add(sampler);
            }

            // Run Test
            jmeter.configure(testPlanTree);
            jmeter.run();

            System.out.println("Test completed successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void setupJMeterEnvironment(String jmeterHome) throws Exception {
        File jmeterHomeDir = new File(jmeterHome);
        if (!jmeterHomeDir.exists()) {
            jmeterHomeDir.mkdirs();
        }

        File propsDir = new File(jmeterHome + "/bin");
        if (!propsDir.exists()) {
            propsDir.mkdirs();
        }

        File propsFile = new File(propsDir, "jmeter.properties");
        if (!propsFile.exists()) {
            java.io.PrintWriter writer = new java.io.PrintWriter(propsFile);
            writer.println("jmeter.version=5.6.3");
            writer.println("language=en");
            writer.println("saveservice_properties=/org/apache/jmeter/save/saveservice.properties");
            writer.close();
        }
    }

    private static HTTPSampler createSearchRequest(String name, String searchTerm) {
        HTTPSampler sampler = new HTTPSampler();
        sampler.setName(name);
        sampler.setDomain("www.n11.com");
        sampler.setPort(443);
        sampler.setProtocol("https");
        sampler.setPath("/arama");
        sampler.setMethod("GET");
        sampler.setFollowRedirects(true);
        sampler.setUseKeepAlive(true);

        // Create query parameters correctly
        Arguments arguments = new Arguments();
        arguments.addArgument("q", searchTerm,"=");
        sampler.setArguments(arguments);
        return sampler;
    }
}
