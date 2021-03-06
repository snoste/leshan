/*******************************************************************************
 * Copyright (c) 2013-2015 Sierra Wireless and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 * 
 * The Eclipse Public License is available at
 *    http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 *    http://www.eclipse.org/org/documents/edl-v10.html.
 * 
 * Contributors:
 *     Sierra Wireless - initial API and implementation
 *******************************************************************************/
package org.eclipse.leshan.server.demo.servlet;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.eclipse.californium.core.network.Endpoint;
import org.eclipse.leshan.core.node.LwM2mNode;
import org.eclipse.leshan.core.node.LwM2mResource;
//import org.eclipse.leshan.core.node.Value;
import org.eclipse.leshan.core.observation.Observation;
import org.eclipse.leshan.core.request.ContentFormat;
import org.eclipse.leshan.core.request.ObserveRequest;
import org.eclipse.leshan.core.request.WriteRequest;
import org.eclipse.leshan.core.response.LwM2mResponse;
import org.eclipse.leshan.core.response.ObserveResponse;
import org.eclipse.leshan.core.response.WriteResponse;
import org.eclipse.leshan.server.californium.impl.LeshanServer;
import org.eclipse.leshan.server.client.Registration;
import org.eclipse.leshan.server.client.RegistrationUpdate;
import org.eclipse.leshan.server.client.RegistrationListener;
import org.eclipse.leshan.server.demo.servlet.json.RegistrationSerializer;
import org.eclipse.leshan.server.demo.servlet.json.LwM2mNodeSerializer;
import org.eclipse.leshan.server.demo.servlet.log.CoapMessage;
import org.eclipse.leshan.server.demo.servlet.log.CoapMessageListener;
import org.eclipse.leshan.server.demo.servlet.log.CoapMessageTracer;
import org.eclipse.leshan.server.demo.utils.EventSource;
import org.eclipse.leshan.server.demo.utils.EventSourceServlet;
import org.eclipse.leshan.server.observation.ObservationListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class EventServlet extends EventSourceServlet {

    private static final String EVENT_DEREGISTRATION = "DEREGISTRATION";

    private static final String EVENT_UPDATED = "UPDATED";

    private static final String EVENT_REGISTRATION = "REGISTRATION";

    private static final String EVENT_NOTIFICATION = "NOTIFICATION";

    private static final String EVENT_COAP_LOG = "COAPLOG";

    private static final String QUERY_PARAM_ENDPOINT = "ep";

    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(EventServlet.class);

    private final Gson gson;

    private final CoapMessageTracer coapMessageTracer;

    private final LeshanServer server;
    
    private static List<String> endpoints;
    private static int counter;
    private static List<String> LightColor;
    private static List<String> LightState;
    
    
    private static List<String> SensorState;
    
    private static List<String> Deployment;
    
    private static List<String> CurrentUser;

    private Set<LeshanEventSource> eventSources = Collections
            .newSetFromMap(new ConcurrentHashMap<LeshanEventSource, Boolean>());

    private final RegistrationListener registrationListener = new RegistrationListener() {

        @Override
        public void registered(Registration registration) {
            String jReg = EventServlet.this.gson.toJson(registration);
            sendEvent(EVENT_REGISTRATION, jReg, registration.getEndpoint());
            LwM2mResponse response;
            String endpoint=registration.getEndpoint();
            endpoints.add(endpoint);
            System.out.println(endpoint);
            
           /* try{
                PrintWriter writer = new PrintWriter("C:\\Users\\s168877\\Documents\\IOT.txt", "UTF-8");
                writer.println(endpoint);
                //writer.println("The second line");
                writer.close();
            } catch (IOException e) {
               // do something
            }
            */
            
            String filename= "C:\\Users\\s168877\\Documents\\IOT.txt";
            // String fname="/home/snorriste/iot.txt";
            
            try
            {
              
                FileWriter fw = new FileWriter(filename,true); //the true will append the new data
                
               
                
                fw.write(endpoint+",");//appends the string to the file
                fw.close();
            }
            catch(IOException ioe)
            {
                System.err.println("IOException: " + ioe.getMessage());
            }
            
           
            
counter++;
            
            
            
            
         
            if(counter==1)
            {
            	
            	
             
            	WriteRequest write = new WriteRequest(10250,0,8,4.0);
             try {
				response = server.send(registration, write);
				System.out.println("Write response: " + response);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
             
             write = new WriteRequest(10250,0,9,8.0);
             try {
 				response = server.send(registration, write);
 				System.out.println("Write response: " + response);
 			} catch (InterruptedException e) {
 				// TODO Auto-generated catch block
 				e.printStackTrace();
 			}
             
             
             
             write = new WriteRequest(10350,0,5,4.0);
             try {
				response = server.send(registration, write);
				System.out.println("Write response: " + response);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
             
             write = new WriteRequest(10350,0,6,8.0);
             try {
 				response = server.send(registration, write);
 				System.out.println("Write response: " + response);
 			} catch (InterruptedException e) {
 				// TODO Auto-generated catch block
 				e.printStackTrace();
 			}
             
             
             
            }
            
            
            
            
            if(counter==2)
            {
            	
            	
             
            	WriteRequest write = new WriteRequest(10250,0,8,6.0);
             try {
				response = server.send(registration, write);
				System.out.println("Write response: " + response);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
             
             write = new WriteRequest(10250,0,9,5.0);
             try {
 				response = server.send(registration, write);
 				System.out.println("Write response: " + response);
 			} catch (InterruptedException e) {
 				// TODO Auto-generated catch block
 				e.printStackTrace();
 			}
             
             
             
             write = new WriteRequest(10350,0,5,6.0);
             try {
				response = server.send(registration, write);
				System.out.println("Write response: " + response);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
             
             write = new WriteRequest(10350,0,6,5.0);
             try {
 				response = server.send(registration, write);
 				System.out.println("Write response: " + response);
 			} catch (InterruptedException e) {
 				// TODO Auto-generated catch block
 				e.printStackTrace();
 			}
             
             
             
            }
            
            
            
            
            
            if(counter==3)
            {
            	
            	
             
            	WriteRequest write = new WriteRequest(10250,0,8,4.0);
             try {
				response = server.send(registration, write);
				System.out.println("Write response: " + response);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
             
             write = new WriteRequest(10250,0,9,2.0);
             try {
 				response = server.send(registration, write);
 				System.out.println("Write response: " + response);
 			} catch (InterruptedException e) {
 				// TODO Auto-generated catch block
 				e.printStackTrace();
 			}
             
             
             
             write = new WriteRequest(10350,0,5,4.0);
             try {
				response = server.send(registration, write);
				System.out.println("Write response: " + response);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
             
             write = new WriteRequest(10350,0,6,2.0);
             try {
 				response = server.send(registration, write);
 				System.out.println("Write response: " + response);
 			} catch (InterruptedException e) {
 				// TODO Auto-generated catch block
 				e.printStackTrace();
 			}
             
             
             
            }
            
            
            
            
            if(counter>=4)
            {
            	
            	
             
            	WriteRequest write = new WriteRequest(10250,0,8,2.0);
             try {
				response = server.send(registration, write);
				System.out.println("Write response: " + response);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
             
             write = new WriteRequest(10250,0,9,5.0);
             try {
 				response = server.send(registration, write);
 				System.out.println("Write response: " + response);
 			} catch (InterruptedException e) {
 				// TODO Auto-generated catch block
 				e.printStackTrace();
 			}
             
             
             
             write = new WriteRequest(10350,0,5,2.0);
             try {
				response = server.send(registration, write);
				System.out.println("Write response: " + response);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
             
             write = new WriteRequest(10350,0,6,5.0);
             try {
 				response = server.send(registration, write);
 				System.out.println("Write response: " + response);
 			} catch (InterruptedException e) {
 				// TODO Auto-generated catch block
 				e.printStackTrace();
 			}
             
             
             
            }
            
            
            
            
             try {
				response = server.send(registration, new ObserveRequest(10250, 0, 7));
				System.out.println("Observe response: " + response);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
             
            
             
            
            
        }

        @Override
        public void updated(RegistrationUpdate update, Registration updatedRegistration) {
            String jReg = EventServlet.this.gson.toJson(updatedRegistration);
            sendEvent(EVENT_UPDATED, jReg, updatedRegistration.getEndpoint());
        };

        @Override
        public void unregistered(Registration registration) {
            String jReg = EventServlet.this.gson.toJson(registration);
            sendEvent(EVENT_DEREGISTRATION, jReg, registration.getEndpoint());
        }
    };

    private final ObservationListener observationListener = new ObservationListener() {

        @Override
        public void cancelled(Observation observation) {
        }

        @Override
        public void newValue(Observation observation, ObserveResponse response) {
        	Registration registration = server.getRegistrationService().getById(observation.getRegistrationId());

        	String target = StringUtils.substringBetween(response.getContent().toString(), "value=", ",");
        	int id = Integer.parseInt(StringUtils.substringBetween(response.getContent().toString(), "id=", ","));
        	String endpoint=registration.getEndpoint();
        	int Objid=observation.getPath().getObjectId();
        	int p=-1;
        	for(int i=0;i<endpoints.size();i++)
        	{
        		if(endpoint.equals(endpoints.get(i)))
        			p=i;
        	}
        	
        	if(p!=-1)
        	{
        	if(Deployment.get(p).toUpperCase().equals("CENTRALIZED"))
        	{
        		
        		if(SensorState.get(p).toUpperCase().equals("OCCUPIED"))
        		{
        			
        			//WriteRequest request = new WriteRequest(objid,instid,rsrcid,grpNo);
               // WriteResponse cResponse = server.send(registration, request, TIMEOUT);
        			//write light state=userstate
        			//write light color=usercolor
        			
        		}
        		
        		else
        		{
        			//write light state=dim
        			//write light color=off
        			
        			
        		}
        		
        	}
        		
        		
        		
        	}
        
        	
        	System.out.println("New value:"+target+"  ep: " +endpoint+" id: "+id+"Objid:"+Objid);
        	if (LOG.isDebugEnabled()) {
                LOG.debug("Received notification from [{}] containing value [{}]", observation.getPath(),
                        response.getContent().toString());
            }
            
            if (registration != null) {
                String data = new StringBuffer("{\"ep\":\"").append(registration.getEndpoint()).append("\",\"res\":\"")
                        .append(observation.getPath().toString()).append("\",\"val\":")
                        .append(gson.toJson(response.getContent()))
                        .append("}").toString();

                sendEvent(EVENT_NOTIFICATION, data, registration.getEndpoint());
            }
        }

        @Override
        public void newObservation(Observation observation) {
        }
        
        
        
    };

    public EventServlet(LeshanServer server, int securePort) {
        this.server = server;
        server.getRegistrationService().addListener(this.registrationListener);
        server.getObservationService().addListener(this.observationListener);
        this.endpoints=new ArrayList<String>();

        // add an interceptor to each endpoint to trace all CoAP messages
        coapMessageTracer = new CoapMessageTracer(server.getRegistrationService());
        for (Endpoint endpoint : server.getCoapServer().getEndpoints()) {
            endpoint.addInterceptor(coapMessageTracer);
        }

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeHierarchyAdapter(Registration.class, new RegistrationSerializer(securePort));
        gsonBuilder.registerTypeHierarchyAdapter(LwM2mNode.class, new LwM2mNodeSerializer());
        gsonBuilder.setDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        this.gson = gsonBuilder.create();
        this.counter=0;
        this.LightColor=new ArrayList<String>();
        this.LightState=new ArrayList<String>();
       
        this.Deployment=new ArrayList<String>();
        this.SensorState=new ArrayList<String>();
        this.CurrentUser=new ArrayList<String>();
    }

    private synchronized void sendEvent(String event, String data, String endpoint) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Dispatching {} event from endpoint {}", event, endpoint);
        }

        for (LeshanEventSource eventSource : eventSources) {
            if (eventSource.getEndpoint() == null || eventSource.getEndpoint().equals(endpoint)) {
                eventSource.sentEvent(event, data);
            }
        }
    }

    class ClientCoapListener implements CoapMessageListener {

        private final String endpoint;

        ClientCoapListener(String endpoint) {
            this.endpoint = endpoint;
        }

        @Override
        public void trace(CoapMessage message) {
            String coapLog = EventServlet.this.gson.toJson(message);
            sendEvent(EVENT_COAP_LOG, coapLog, endpoint);
        }

    }

    private void cleanCoapListener(String endpoint) {
        // remove the listener if there is no more eventSources for this endpoint
        for (LeshanEventSource eventSource : eventSources) {
            if (eventSource.getEndpoint() == null || eventSource.getEndpoint().equals(endpoint)) {
                return;
            }
        }
        coapMessageTracer.removeListener(endpoint);
    }

    @Override
    protected EventSource newEventSource(HttpServletRequest req) {
        String endpoint = req.getParameter(QUERY_PARAM_ENDPOINT);
        return new LeshanEventSource(endpoint);
    }

    private class LeshanEventSource implements EventSource {

        private String endpoint;
        private Emitter emitter;

        public LeshanEventSource(String endpoint) {
            this.endpoint = endpoint;
        }

        @Override
        public void onOpen(Emitter emitter) throws IOException {
            this.emitter = emitter;
            eventSources.add(this);
            if (endpoint != null) {
                coapMessageTracer.addListener(endpoint, new ClientCoapListener(endpoint));
            }
        }

        @Override
        public void onClose() {
            cleanCoapListener(endpoint);
            eventSources.remove(this);
        }

        public void sentEvent(String event, String data) {
            try {
                emitter.event(event, data);
            } catch (IOException e) {
                onClose();
            }
        }

        public String getEndpoint() {
            return endpoint;
        }
    }
}
