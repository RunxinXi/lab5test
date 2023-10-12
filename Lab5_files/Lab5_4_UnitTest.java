package mie.UnitTestexample;

import static org.junit.Assert.assertTrue;			// assertTrue function for JUnitTest

import java.util.ArrayList;		// ArrayList
import java.util.Collection;	// Collection
import java.util.HashMap;		// HashMap

import org.flowable.engine.RuntimeService;			// Creating runtime service
import org.flowable.task.api.Task;					// Selecting user task

import org.junit.BeforeClass; 						// Setting up filename that extended from LabBaseUnitTest class
import org.junit.Test;								// for JUnitTest
import org.junit.runner.RunWith;					// for @RunWith
import org.junit.runners.Parameterized;				// For @RunWith(Parameterized.class)
import org.junit.runners.Parameterized.Parameters;	// For @Parameters


@RunWith(Parameterized.class)
public class Lab5_4_UnitTest extends LabBaseUnitTest {

	@BeforeClass
	public static void setupFile() {
		filename = "src/main/resources/diagrams/Lab5_4.bpmn";
	}

	private String item;
	private String quantity;
	private String price;
	private String total;
	private String managerApproval;
	private String expectedStatus;

	public Lab5_4_UnitTest(String item, String quantity, String price, String total, String managerApproval, String expectedStatus) {
		this.item = item;
		this.quantity = quantity;
		this.price = price;
		this.total = total;
		this.managerApproval = managerApproval;
		this.expectedStatus = expectedStatus;
	}
	
	@Parameters
	public static Collection<String[]> data() {
		ArrayList<String[]> parameters = new ArrayList<String[]>();
		parameters.add(new String[]{"pencil", "10000", "1", "10000", "true", "approved"});
		return parameters;
	}

	@Test
	public void myTest() {
		startProcess();
		
		createPurchaseRequisition();
		submitManagerApproval();
		
		// Hint: expectedStatus matches the variable name used to track "approved" status
		String variableName = expectedStatus;
		Boolean status = (Boolean) flowableContext.getHistoryService().createHistoricVariableInstanceQuery().variableName(variableName).singleResult().getValue();
		assertTrue(status);
	}

	private void createPurchaseRequisition() {
		Task usertask1 = flowableContext.getTaskService().createTaskQuery().taskDefinitionKey("usertask1").singleResult();

		HashMap<String, String> formEntries = new HashMap<String, String>();
		formEntries.put("item", item);
		formEntries.put("quantity", quantity);
		formEntries.put("price", price);
		formEntries.put("total", total);

		flowableContext.getFormService().submitTaskFormData(usertask1.getId(), formEntries);
	}
	
	private void submitManagerApproval() {
		Task task = flowableContext.getTaskService().createTaskQuery().taskDefinitionKey("submitManagerApproval").singleResult();
		HashMap<String, String> formEntries = new HashMap<String, String>();
		formEntries.put("managerApproval", managerApproval);
		flowableContext.getFormService().submitTaskFormData(task.getId(), formEntries);
	}

	private void startProcess() {
		RuntimeService runtimeService = flowableContext.getRuntimeService();
		processInstance = runtimeService.startProcessInstanceByKey("process1");
	}
}
