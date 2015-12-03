/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mcm.notification.services.gcm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.android.gcm.server.Constants;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

/**
 * Servlet that registers a device, whose registration id is identified by
 * {@link #PARAMETER_REG_ID}.
 *
 * <p>
 * The client app should call this servlet everytime it receives a
 * {@code com.google.android.c2dm.intent.REGISTRATION C2DM} intent without an
 * error or {@code unregistered} extra.
 */
@SuppressWarnings("serial") 
public class RegisterAndSendLoanServlet extends BaseServlet {

  private static final String PARAMETER_REG_ID = "regId";
  private Sender sender;

  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    sender = newSender(config);
  }

  /**
   * Creates the {@link Sender} based on the servlet settings.
   */
  protected Sender newSender(ServletConfig config) {
    String key = (String) config.getServletContext()
        .getAttribute(ApiKeyInitializer.ATTRIBUTE_ACCESS_KEY);
    return new Sender(key); 
  }
  
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    String regId = getParameter(req, PARAMETER_REG_ID);
    if (regId.contains("Summa")){
    	List<String> devices = Datastore.getDevices();
	    StringBuilder status = new StringBuilder();
	    if (devices.isEmpty()) {
	      status.append("Sending loan failed as there is no device registered!");
	    } else {
	      List<Result> results = new ArrayList<Result>();
	      // NOTE: check below is for demonstration purposes; a real application
	      // could always send a multicast, even for just one recipient
		  String id = regId;
	      if (id != null) {
	        // send a multicast message using JSON
	        Message message = new Message.Builder()
			.timeToLive(3)
	        .delayWhileIdle(true)
	        .addData("Loan offer: ", id)
	        .build();
	        MulticastResult result = sender.send(message, devices, 5);
	        results = result.getResults();
	      }
	      // analyze the results
	      for (int i = 0; i < results.size(); i++) {
	        Result result = results.get(i);
	        if (result.getMessageId() != null) {
	          status.append("Succesfully sent loan offer to all devices #").append(i);
	          String canonicalRegId = result.getCanonicalRegistrationId();
	          if (canonicalRegId != null) {
	            // same device has more than on registration id: update it
	            logger.finest("canonicalRegId " + canonicalRegId);
	            devices.set(i, canonicalRegId);
	            status.append(". Also updated registration id!");
	          }
	        } else {
	          String error = result.getErrorCodeName();
	          if (error.equals(Constants.ERROR_NOT_REGISTERED)) {
	            // application has been removed from device - unregister it
	            status.append("Unregistered device #").append(i);
	            String loanId = devices.get(i);
	            Datastore.unregister(loanId);
	          } else {
				for (int j = 0; j < devices.size(); j++){
			      status.append(devices.get(i).toString());
				}
	            status.append("Error sending message to device #").append(i)
	                .append(": ").append(error);
	          }
	        }
	        status.append("<br/>");
	      }
	    }
	    req.setAttribute(HomeServlet.ATTRIBUTE_STATUS, status.toString());
	    getServletContext().getRequestDispatcher("/home").forward(req, resp);
    } else{
		Datastore.register(regId);
		setSuccess(resp);
    }
  }

}
