package org.globaleaks.android.test;

import java.io.StringReader;
import java.util.List;

import junit.framework.TestCase;

import org.globaleaks.model.Context;
import org.globaleaks.model.Node;
import org.globaleaks.model.Receiver;
import org.globaleaks.model.Submission;
import org.globaleaks.util.Parser;
import org.json.JSONException;

import android.util.Log;

public class ModelParsing extends TestCase {

	private Parser parser = new Parser();
	
	private static final String NODE_JSON = "{\"description\": \"Test Globaleaks Node.\n\nThis GL Node is used to develop, debug and test GLDroid client.\", \"languages\": [{\"code\": \"it\", \"name\": \"Italiano\"}, {\"code\": \"en\", \"name\": \"English\"}], \"public_site\": \"\", \"email\": \"mpodroid@gmail.com\", \"hidden_service\": \"\", \"name\": \"TestLeak\"}";
	private static final String CONTEXT_JSON = "[{\"name\": \"iPhoneCTX\", \"receivers\": [\"0cb7ef92-8c38-4853-abcf-04d978d7ddbf\", \"78880803-6a5f-4d1e-8195-683ab0c7768f\"], \"fields\": [{\"name\": \"headline\", \"presentation_order\": 1, \"hint\": \"Hint, I'm required\", \"required\": true, \"value\": \"\", \"label\": \"headline\", \"type\": \"text\"}, {\"name\": \"Sun\", \"presentation_order\": 2, \"hint\": \"The name of the Sun\", \"required\": true, \"value\": \"I'm the sun, I've not name\", \"label\": \"Sun\", \"type\": \"radio\"}, {\"name\": \"description\", \"presentation_order\": 0, \"hint\": \"Description of leaks\", \"required\": false, \"label\": \"Description\", \"type\": \"select\"}], \"selectable_receiver\": true, \"context_gus\": \"a89b7f94-8d88-4c49-b8cf-9bc5da985792\", \"tip_timetolive\": 42, \"escalation_threshold\": null, \"file_max_download\": 42, \"tip_max_access\": 42, \"description\": \"iPhone leaks context\"}, {\"name\": \"AndroidCTX\", \"receivers\": [\"0cb7ef92-8c38-4853-abcf-04d978d7ddbf\"], \"fields\": [{\"name\": \"headline\", \"hint\": \"Hint, I'm required\", \"required\": true, \"presentation_order\": 1, \"value\": \"\", \"label\": \"headline\", \"type\": \"text\"}, {\"name\": \"Sun\", \"hint\": \"The name of the Sun\", \"required\": true, \"presentation_order\": 2, \"value\": \"I'm the sun, I've not name\", \"label\": \"Sun\", \"type\": \"text\"}], \"selectable_receiver\": true, \"context_gus\": \"f9bb16fb-c133-46f9-a13d-d582389ffd8f\", \"tip_timetolive\": 42, \"escalation_threshold\": null, \"file_max_download\": 42, \"tip_max_access\": 42, \"description\": \"Android leaks context\"}]";
	private static final String RECEIVER_JSON = "[{\"update_date\": \"Mon Mar  4 21:12:13 2013\", \"name\": \"gmail\", \"contexts\": [\"a89b7f94-8d88-4c49-b8cf-9bc5da985792\", \"f9bb16fb-c133-46f9-a13d-d582389ffd8f\"], \"receiver_level\": 1, \"creation_date\": \"Mon Mar  4 21:11:28 2013\", \"can_delete_submission\": true, \"receiver_gus\": \"0cb7ef92-8c38-4853-abcf-04d978d7ddbf\", \"description\": \"\"}, {\"update_date\": \"Mon Mar  4 21:12:18 2013\", \"name\": \"marco\", \"contexts\": [\"a89b7f94-8d88-4c49-b8cf-9bc5da985792\"], \"receiver_level\": 1, \"creation_date\": \"Mon Mar  4 21:12:06 2013\", \"can_delete_submission\": true, \"receiver_gus\": \"78880803-6a5f-4d1e-8195-683ab0c7768f\", \"description\": \"\"}]";
		
	public void testNodeParser() {
		Node n=null;
		try {
			n = parser.parseNode(new StringReader(NODE_JSON));
		} catch (Exception e) {
			assertTrue("Fail parsing node", false);
		}
		Node org = new Node("TestLeak","Test Globaleaks Node.\n\nThis GL Node is used to develop, debug and test GLDroid client.", "mpodroid@gmail.com");
		assertEquals(n, org);
	}
	
	public void testContextParser() {
		List<Context> c;
		try {
			c = parser.parseContexts(new StringReader(CONTEXT_JSON));
		} catch (Exception e) {
			assertTrue("Fail parsing context list", false);
		}
		assertTrue(true);
	}
	
	public void testReceiverParser() {
		List<Receiver> r;
		try {
			r = parser.parseReceivers(new StringReader(RECEIVER_JSON));
		} catch (Exception e) {
			assertTrue("Fail parsing receivers list", false);
		}
		assertTrue(true);
	}
	
	public void testEmptySubmission() {
		Context c = new Context();
		c.setId("f9bb16fb-c133-46f9-a13d-d582389ffd8f");
		Submission s = new Submission(c);
		
		String j = null;
		try {
			j = s.toJSON();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		assertEquals(j, "{submission_gux=\"f9bb16fb-c133-46f9-a13d-d582389ffd8f\"}");
	}


}
