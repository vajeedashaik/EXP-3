 # Experiment3 CloudSim Experiment – Same MIPS, Same Execution Time

##  Overview
This repository contains a **CloudSim 3.0.3 simulation experiment** that demonstrates the execution of **two cloudlets** on **virtual machines with identical MIPS requirements**.  
Since both the cloudlets and VMs are homogeneous, the cloudlets complete execution in the **same amount of time**.

This experiment is commonly used in **Cloud Computing laboratories**, **academic projects**, and **baseline IEEE simulations**.



##  Aim
To create a datacenter with one host and execute two cloudlets on virtual machines having the same MIPS requirements such that both cloudlets take the same time to complete execution.

---

##  Objectives
- To simulate a cloud datacenter using CloudSim
- To create virtual machines with identical MIPS values
- To execute cloudlets with equal computational length
- To verify that cloudlets complete execution at the same time

---

##  Tools & Technologies
- **Java JDK**: 1.8  
- **CloudSim Toolkit**: 3.0.3  
- **IDE**: IntelliJ IDEA / Eclipse  
- **Operating System**: Windows / Linux  

---

##  System Configuration

### Datacenter Configuration
| Parameter | Value |
|---------|------|
| Datacenters | 1 |
| Hosts | 1 |
| RAM | 4096 MB |
| Storage | 1,000,000 MB |
| Bandwidth | 10,000 Mbps |
| VM Scheduler | Time-Shared |

### Virtual Machine Configuration
| Parameter | Value |
|--------|------|
| Number of VMs | 2 |
| MIPS | 1000 |
| RAM | 512 MB |
| Bandwidth | 1000 Mbps |
| VMM | Xen |
| Cloudlet Scheduler | Time-Shared |

### Cloudlet Configuration
| Parameter | Value |
|--------|------|
| Number of Cloudlets | 2 |
| Cloudlet Length | 10,000 MI |
| File Size | 300 MB |
| Output Size | 300 MB |
| Processing Elements | 1 |


## Program 
--  
```java
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

```








##  Algorithm
1. Initialize the CloudSim library.
2. Create a datacenter with one host.
3. Create a datacenter broker.
4. Create two virtual machines with identical MIPS values.
5. Submit the virtual machines to the broker.
6. Create two cloudlets with equal computational length.
7. Assign one cloudlet to each VM.
8. Start the CloudSim simulation.
9. Record the execution start and finish times.
10. Stop the simulation and analyze the results.

---

## Execution Time Formula
Execution Time = Cloudlet Length / VM MIPS

## Sample output 

========== CLOUDLET EXECUTION RESULTS ==========
Cloudlet ID: 0 | VM ID: 0 | Start Time: 0.1 | Finish Time: 10.1
Cloudlet ID: 1 | VM ID: 1 | Start Time: 0.1 | Finish Time: 10.1



## Screenshots of exectuted output 

<img width="516" height="311" alt="image" src="https://github.com/user-attachments/assets/3c31448a-27f8-45f1-973e-27d3ed1b3ee3" />






## Result

The experiment successfully demonstrates that cloudlets with identical computational requirements, executed on virtual machines with the same MIPS capacity, complete execution at the same time.



## Conclusion

This simulation validates the correctness and fairness of CloudSim scheduling in a homogeneous cloud environment. Equal workloads executed on equally provisioned virtual machines result in identical execution times.
