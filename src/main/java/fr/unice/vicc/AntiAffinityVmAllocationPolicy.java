package fr.unice.vicc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicy;

public class AntiAffinityVmAllocationPolicy extends VmAllocationPolicy{
	
	/**
	 * 2)
	 * la compléxité de l'algorithme est de O(nlog(n))
	 * Car il faut chercher le host qui convient (O(n)), puis pour chercher un élément cela prendra O(log(n)) car on utilise la classe HashMap
	 * 
	 * 3)
	 * l'impact est qu'on a besoin de créé un autre hashMap qui prend beaucoup de place celon le nombre de noeud,
	 *  car l'allocation est quadratique celon les noeud et le nombre de VM qui sont implémentés.
	 *  Si n est le nombre de noeud et m le nombre de VM qui il a au maximum dans un instervalle,
	 *  la compléxité serait de O(nm);
	 */
	
    //To track the Host for each Vm. The string is the unique Vm identifier, composed by its id and its userId
    private Map<String, Host> vmTable;
    private Map<String,Map<String,Boolean>> intervalVm;

	public AntiAffinityVmAllocationPolicy(List<? extends Host> list) {
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
        if ((intervalVm.get(""+host.getId()).get(""+(vm.getId()/100))==null||
        	intervalVm.get(""+host.getId()).get(""+(vm.getId()/100))==false)&&
        	host.vmCreate(vm)){
            //the host is appropriate, we track it
    		intervalVm.get(""+host.getId()).put(""+(vm.getId()/100),true);
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
        	if(intervalVm.get(""+h.getId()).get(""+(vm.getId()/100))==null||
        	   intervalVm.get(""+h.getId()).get(""+(vm.getId()/100))==false){
        		if (h.vmCreate(vm)) {
            		vmTable.put(vm.getUid(), h);
            		intervalVm.get(""+h.getId()).put(""+(vm.getId()/100),true);
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
