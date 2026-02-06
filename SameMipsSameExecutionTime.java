import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.*;

import java.util.*;

public class SameMipsSameExecutionTime {

    public static void main(String[] args) {

        try {
            int numUsers = 1;
            Calendar calendar = Calendar.getInstance();
            boolean traceFlag = false;

            // Step 1: Initialize CloudSim
            CloudSim.init(numUsers, calendar, traceFlag);

            // Step 2: Create Datacenter
            createDatacenter("Datacenter_1");

            // Step 3: Create Broker
            DatacenterBroker broker = new DatacenterBroker("Broker_1");
            int brokerId = broker.getId();

            // Step 4: Create VMs (ALL SAME MIPS)
            int vmCount = 2;
            List<Vm> vmList = new ArrayList<>();

            int vmMips = 1000; // SAME MIPS for all VMs

            for (int i = 0; i < vmCount; i++) {
                Vm vm = new Vm(
                        i,
                        brokerId,
                        vmMips,
                        1,
                        512,
                        1000,
                        10000,
                        "Xen",
                        new CloudletSchedulerTimeShared()
                );
                vmList.add(vm);
            }

            broker.submitVmList(vmList);

            // Step 5: Create Cloudlets (ALL SAME LENGTH)
            int cloudletCount = 2;
            List<Cloudlet> cloudletList = new ArrayList<>();

            long cloudletLength = 10000; // SAME length → SAME execution time
            UtilizationModel model = new UtilizationModelFull();

            for (int i = 0; i < cloudletCount; i++) {
                Cloudlet cloudlet = new Cloudlet(
                        i,
                        cloudletLength,
                        1,
                        300,
                        300,
                        model,
                        model,
                        model
                );
                cloudlet.setUserId(brokerId);
                cloudlet.setVmId(i); // One cloudlet per VM
                cloudletList.add(cloudlet);
            }

            broker.submitCloudletList(cloudletList);

            // Step 6: Start Simulation
            CloudSim.startSimulation();

            // Step 7: Results
            List<Cloudlet> resultList = broker.getCloudletReceivedList();

            CloudSim.stopSimulation();

            // Step 8: Display Output
            System.out.println("\n========== CLOUDLET EXECUTION RESULTS ==========");
            for (Cloudlet c : resultList) {
                System.out.println(
                        "Cloudlet ID: " + c.getCloudletId() +
                                " | VM ID: " + c.getVmId() +
                                " | Start Time: " + c.getExecStartTime() +
                                " | Finish Time: " + c.getFinishTime()
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Datacenter with ONE HOST
    private static Datacenter createDatacenter(String name) {

        List<Host> hostList = new ArrayList<>();

        // One PE with enough MIPS
        List<Pe> peList = new ArrayList<>();
        peList.add(new Pe(0, new PeProvisionerSimple(2000)));

        Host host = new Host(
                0,
                new RamProvisionerSimple(4096),
                new BwProvisionerSimple(10000),
                1000000,
                peList,
                new VmSchedulerTimeShared(peList)
        );

        hostList.add(host);

        DatacenterCharacteristics characteristics =
                new DatacenterCharacteristics(
                        "x86",
                        "Linux",
                        "Xen",
                        hostList,
                        10.0,
                        3.0,
                        0.05,
                        0.001,
                        0.0
                );

        try {
            return new Datacenter(
                    name,
                    characteristics,
                    new VmAllocationPolicySimple(hostList),
                    new LinkedList<Storage>(),
                    0
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
