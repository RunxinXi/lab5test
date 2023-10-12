package mie.example;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

public class GeneratePurchaseOrder implements JavaDelegate {

	@Override
	public void execute(DelegateExecution execution) {
		Boolean purchaseOrderGenerated = (Boolean) execution.getVariable("purchaseOrderGenerated");
		if (purchaseOrderGenerated == null) {
			execution.setVariable("purchaseOrderGenerated", true);
			System.out.println("Generating purchase order...");
		}
	}

}
