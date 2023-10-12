package mie.example;

import java.util.ArrayList;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

public class ReviewApprovals implements JavaDelegate {
	@Override
	public void execute(DelegateExecution execution) {
		Boolean approved = getBooleanVariable(execution, "approved");
		if (approved) {
			return;
		}
		
		Integer approvalCounter = 0;

		for (Boolean approval : getApprovalDecisions(execution)) {
			if (approval == null) {
				continue;
			} else if (approval) {
				approvalCounter = approvalCounter + 1;
			}
		}
		
		Integer requiredApprovals = 2;

		if (requiredApprovals == approvalCounter) {
			approvePurchaseRequisition(execution);
		}
	}
	
	private Boolean getBooleanVariable(DelegateExecution execution, String id) {
		Boolean var = (Boolean) execution.getVariable(id);
		
		if (var == null) {
			execution.setVariable(id, false);
			return false;
		}
		
		return var;
	}
	
	// Feel free to use this helper method to get the total purchase amount
	private Long getTotalPurchaseAmountVariable(DelegateExecution execution) {
		return Long.parseLong((String) execution.getVariable("total"));
	}
	
	private ArrayList<Boolean> getApprovalDecisions(DelegateExecution execution) {
		ArrayList<Boolean> approvals = new ArrayList<Boolean>();
		approvals.add((Boolean) execution.getVariable("managerApproval"));
		approvals.add((Boolean) execution.getVariable("financeApproval"));
		return approvals;
	}
	
	private void approvePurchaseRequisition(DelegateExecution execution) {
		execution.setVariable("approved", true);
	}
}
