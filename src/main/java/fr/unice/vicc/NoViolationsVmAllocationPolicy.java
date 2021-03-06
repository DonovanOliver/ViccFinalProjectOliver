package fr.unice.vicc;

import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicy;
import org.cloudbus.cloudsim.VmStateHistoryEntry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Fabien Hermenier
 */
public class NoViolationsVmAllocationPolicy extends VmAllocationPolicy {
	
	/**
	 * 1) la compl�xit� de mon algorithme est de O(N^2) car pour tout les hosts il doit trouver la ou il n'y a pas de p�nalit�.
	 */

    //To track the Host for each Vm. The string is the unique Vm identifier, composed by its id and its userId
    private Map<String, Host> vmTable;

    public NoViolationsVmAllocationPolicy(List<? extends Host> list) {
        super(list);
        vmTable = new HashMap<>();
    }
    
    private boolean penalties(Vm vm){
    	for (int i = 0; i < Helper.VM_TYPES; i++) {
            if (vm.getMips() == Helper.VM_MIPS[i]) {
            	return Helper.PRIZES[i]>0.2;
            }
        }
        throw new IllegalArgumentException("No type for Vm " + vm.getId());
    }
    
    private double missingMips(VmStateHistoryEntry e, double d) {
        double want = e.getRequestedMips() * d;
        double got = e.getAllocatedMips() * d;
        if (got < want) {
            return want - got;
        }
        return 0;
    }
    
    private double availability(Vm v) {
        double totalMissing = 0;
        double prev = 0;
        //Browse the Vm history
        for (VmStateHistoryEntry e : v.getStateHistory()) {
            //the time elapsed since the last event
            double diff = e.getTime() - prev;
            prev = e.getTime();
            //Get the number of missing mips for that period
            totalMissing += missingMips(e, diff);
        }
        //The total number of Mips the VMs should have in theory
        double totalAllocated = v.getMips() * Constants.SIMULATION_LIMIT;
        //The % of time it eventually had enough MIPS
        double availabilityPct = (totalAllocated - totalMissing) / totalAllocated * 100;
	    return availabilityPct;
		}
    
    public boolean allocateHostForVm(Vm vm, Host host) {
        if (!penalties(vm)&&host.vmCreate(vm)) {
            //the host is appropriate, we track it
            vmTable.put(vm.getUid(), host);
            return true;
        }
        return false;
    }

    public Host getHost(Vm vm) {
        // We must recover the Host which hosting Vm
        return this.vmTable.get(vm.getUid());
    }

    public Host getHost(int vmId, int userId) {
        // We must recover the Host which hosting Vm
        return this.vmTable.get(Vm.getUid(userId, vmId));
    }

    public boolean allocateHostForVm(Vm vm) {
        //First fit algorithm, run on the first suitable node
        for (Host h : getHostList()) {
            if (!penalties(vm)&&h.vmCreate(vm)) {
                //track the host
                vmTable.put(vm.getUid(), h);
                return true;
            }
        }
        return false;
    }

    public void deallocateHostForVm(Vm vm,Host host) {
        vmTable.remove(vm.getUid());
        host.vmDestroy(vm);
    }

    @Override
    public void deallocateHostForVm(Vm v) {
        //get the host and remove the vm
        vmTable.get(v.getUid()).vmDestroy(v);
    }

    public static Object optimizeAllocation() {
        return null;
    }

    @Override
    public List<Map<String, Object>> optimizeAllocation(List<? extends Vm> arg0) {
        //Static scheduling, no migration, return null;
        return null;
    }
}
