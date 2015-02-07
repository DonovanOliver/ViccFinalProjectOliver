package fr.unice.vicc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicy;

public class StatEnergyVmAllocationPolicy extends VmAllocationPolicy{
	/**
	 * 1)la compléxité de l'algorithme est de O(nlog(n))
	 * Car il faut chercher le host qui convient (O(n)), puis pour chercher un élément cela prendra O(log(n)) car on utilise la classe HashMap
	 *  
	 */
	
	private int nbHosts=2;
	
    //To track the Host for each Vm. The string is the unique Vm identifier, composed by its id and its userId
    private Map<String, Host> vmTable;
    private Map<String,Map<String,Boolean>> intervalVm;

	public StatEnergyVmAllocationPolicy(List<? extends Host> list) {
		super(list);
        vmTable = new HashMap<>();
        intervalVm=new HashMap<>();
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
        if ((intervalVm.get(""+host.getId()).get(""+(vm.getId()/nbHosts))==null||
        	intervalVm.get(""+host.getId()).get(""+(vm.getId()/nbHosts))==false)&&
        	host.vmCreate(vm)){
            //the host is appropriate, we track it
    		intervalVm.get(""+host.getId()).put(""+(vm.getId()/nbHosts),true);
            vmTable.put(vm.getUid(), host);
            return true;
        }
        return false;
    }

    public boolean allocateHostForVm(Vm vm) {
        //First fit algorithm, run on the first suitable node
        for (Host h : getHostList()) {
            
            //track the host
        	if(intervalVm.get(""+h.getId())==null){
        		intervalVm.put(""+h.getId(),new HashMap<String,Boolean>());
        	}
        	if(intervalVm.get(""+h.getId()).get(""+(vm.getId()/nbHosts))==null||
        	   intervalVm.get(""+h.getId()).get(""+(vm.getId()/nbHosts))==false){
        		if (h.vmCreate(vm)) {
            		vmTable.put(vm.getUid(), h);
            		intervalVm.get(""+h.getId()).put(""+(vm.getId()/nbHosts),true);
                    return true;
        		}
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
