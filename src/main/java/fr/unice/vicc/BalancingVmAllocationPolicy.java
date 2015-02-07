package fr.unice.vicc;

import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Fabien Hermenier
 */
public class BalancingVmAllocationPolicy extends VmAllocationPolicy {

	/**
	 * 1) la compléxité de mon algorithme est de O(2N) car il cherche le host qui à le plus de capacité,
	 * puis doit trouver le maximum l'équivalent du host qui à le plus de capacité.
	 */
	
    //To track the Host for each Vm. The string is the unique Vm identifier, composed by its id and its userId
    private Map<String, Host> vmTable;
    private Map<String, Integer> vmMIPS;

	public BalancingVmAllocationPolicy(List<? extends Host> list) {
        super(list);
        vmTable = new HashMap<>();
        vmMIPS = new HashMap<>();
    }
    
    private Integer getHostMostAvailableMips(){
    	Entry<String, Integer> res = null;

	    for(Entry<String, Integer> entry : vmMIPS.entrySet()) {
            if(res == null || res.getValue() < entry.getValue()){
            	res = entry;
            }
	    }
	    if(res==null){
	    	return 0;
	    }
	    return res.getValue();
    }

    public Host getHost(Vm vm) {
        // We must recover the Host which hosting Vm
        return this.vmTable.get(vm.getUid());
    }

    public Host getHost(int vmId, int userId) {
        // We must recover the Host which hosting Vm
        return this.vmTable.get(Vm.getUid(userId, vmId));
    }

    public boolean allocateHostForVm(Vm vm, Host host) {
        if (getHostMostAvailableMips()<=host.getAvailableMips()&&
        	host.vmCreate(vm)) {
            //the host is appropriate, we track it
            vmTable.put(vm.getUid(), host);
            vmMIPS.put(""+host.getId(),(int) (host.getAvailableMips()));
            return true;
        }
        return false;
    }

    public boolean allocateHostForVm(Vm vm) {
        //First fit algorithm, run on the first suitable node
    	Integer max=getHostMostAvailableMips();
        for (Host h : getHostList()) {
            if (max<=h.getAvailableMips()&&
            	h.vmCreate(vm)) {
                //track the host
                vmTable.put(vm.getUid(), h);
                vmMIPS.put(""+h.getId(),(int) h.getAvailableMips());
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
