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
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.eclipse.californium.core.network.Endpoint;
import org.eclipse.leshan.core.request.WriteRequest;
import org.eclipse.leshan.core.request.exception.RequestRejectedException;
import org.eclipse.leshan.core.request.exception.ResourceAccessException;
import org.eclipse.leshan.core.response.LwM2mResponse;
import org.eclipse.leshan.core.response.WriteResponse;
import org.eclipse.leshan.server.LwM2mServer;
import org.eclipse.leshan.server.californium.impl.LeshanServer;
import org.eclipse.leshan.server.client.Registration;
import org.eclipse.leshan.server.demo.servlet.json.SecurityDeserializer;
import org.eclipse.leshan.server.demo.servlet.json.SecuritySerializer;
import org.eclipse.leshan.server.security.EditableSecurityStore;
import org.eclipse.leshan.server.security.NonUniqueSecurityInfoException;
import org.eclipse.leshan.server.security.SecurityInfo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

/**
 * Service HTTP REST API calls for security information.
 */
public class SecurityServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(SecurityServlet.class);

    private static final long serialVersionUID = 1L;

    private final EditableSecurityStore store;

    private final Gson gsonSer;
    private final Gson gsonDes;

    private PublicKey serverPublicKey;
    private  static List<String> UserId;
    private  static List<String> Password;
    
    private  static List<String> RoomId;
    private  static List<String> Name;
    private  static List<String> Email;
    public  static List<String> GroupNo;
    private final LeshanServer server;
    
    private static int counter;
    private static final long TIMEOUT = 5000; // ms
    
    private List<String> l;
    
    public SecurityServlet(LeshanServer server,EditableSecurityStore store, PublicKey serverPublicKey) {
        this.store = store;
        this.serverPublicKey = serverPublicKey;

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(SecurityInfo.class, new SecuritySerializer());
        this.gsonSer = builder.create();

        builder = new GsonBuilder();
        builder.registerTypeAdapter(SecurityInfo.class, new SecurityDeserializer());
        this.gsonDes = builder.create();
        this.UserId=new ArrayList<String>();
        this.Password=new ArrayList<String>();
        this.RoomId=new ArrayList<String>();
        this.Name=new ArrayList<String>();
        this.Email=new ArrayList<String>();
        this.GroupNo=new ArrayList<String>();
        this.server=server;
        this.counter=-1;
        
        
       //
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String[] path = StringUtils.split(req.getPathInfo(), '/');

        if (path.length != 1 && "clients".equals(path[0])) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            SecurityInfo info = gsonDes.fromJson(new InputStreamReader(req.getInputStream()), SecurityInfo.class);
            LOG.debug("New security info for end-point {}: {}", info.getEndpoint(), info);

            store.add(info);

            resp.setStatus(HttpServletResponse.SC_OK);

        } catch (NonUniqueSecurityInfoException e) {
            LOG.warn("Non unique security info: " + e.getMessage());
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().append(e.getMessage()).flush();
        } catch (JsonParseException e) {
            LOG.warn("Could not parse request body", e);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().append("Invalid request body").flush();
        } catch (RuntimeException e) {
            LOG.warn("unexpected error for request " + req.getPathInfo(), e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String[] path = StringUtils.split(req.getPathInfo(), '/');
        
        if (path.length >= 3 &&req.getPathInfo().contains("UserData")) {
            try {
                //String target = StringUtils.substringBetween(req.getPathInfo(), clientEndpoint, "/write");
               // String Value=path[path.length-1];
                String Value = StringUtils.substringAfterLast(req.getPathInfo(), "UserData/");
                
                System.out.println(Value);
                JSONArray arr = null;
                JSONObject Obj = null;
                String fname="C:\\Users\\s168877\\Documents\\IOT.txt";
               // String fname="/home/snorriste/iot.txt";
                BufferedReader br = new BufferedReader(new FileReader(fname));
               
                String line = br.readLine();
            	System.out.println(line);
            	String[] endpoints = StringUtils.split(line, ',');
            	
            	
            		
            	//check duplicates
            	
            	
            	
                
                /* for (Endpoint endpoint : server.getCoapServer().getEndpoints()) {
                	System.out.println(endpoint);
                	//endpoint.addInterceptor(coapMessageTracer);
                }*/
                
				try {
					arr = new JSONArray(Value);
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                for (int x=0;x<arr.length();x++)
                {
                	try {
						 Obj= arr.getJSONObject(x);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                	
                		try {
							UserId.add(Obj.getString("UserID"));
						
							Password.add(Obj.getString("Password"));
							GroupNo.add(Obj.getString("GroupNo"));
						
							RoomId.add(Obj.getString("RoomID"));
						
							
						
							Email.add(Obj.getString("Email"));
						
							Name.add(Obj.getString("Name"));
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                		
                		
                	}
                
                System.out.println(GroupNo.get(0));/*
                		for(int i=0;i<UserId.size();i++)
                		{
                			
                			System.out.println(UserId.get(i));
                		}*/
            
               String  response = "{"+'"'+"Status"+'"'+":"+'"'+"Success"+'"'+"}";
                resp.setContentType("application/json");
                resp.getOutputStream().write(response.getBytes());
                resp.setStatus(HttpServletResponse.SC_OK);
                
                if(endpoints.length>=UserId.size())
                {
                	counter++;
                int GrNo=Integer.parseInt(GroupNo.get(counter));
                Registration registration = server.getRegistrationService().getByEndpoint(endpoints[counter]);
                
                
                 try {
                	 
        			 WriteRequest request = new WriteRequest(10250,0,7,GrNo);
                     WriteResponse cResponse = server.send(registration, request, TIMEOUT);
                     processDeviceResponse(req, resp, cResponse);
    			} catch (InterruptedException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
                 
                 
                  try {
                	 
        			 WriteRequest request = new WriteRequest(10350,0,4,GrNo);
                     WriteResponse cResponse = server.send(registration, request, TIMEOUT);
                     processDeviceResponse(req, resp, cResponse);
    			} catch (InterruptedException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
                  
                  
                  
                  
                  String roomid=RoomId.get(counter);
                  
                  
                  try {
                 	 
         			 WriteRequest request = new WriteRequest(10250,0,10,roomid);
                      WriteResponse cResponse = server.send(registration, request, TIMEOUT);
                      processDeviceResponse(req, resp, cResponse);
     			} catch (InterruptedException e) {
     				// TODO Auto-generated catch block
     				e.printStackTrace();
     			}
                  
                  
                   try {
                 	 
         			 WriteRequest request = new WriteRequest(10350,0,7,roomid);
                      WriteResponse cResponse = server.send(registration, request, TIMEOUT);
                      processDeviceResponse(req, resp, cResponse);
     			} catch (InterruptedException e) {
     				// TODO Auto-generated catch block
     				e.printStackTrace();
     			}
                   
                  
                  
                
                
                }
                
                
            } catch (IllegalArgumentException e) {
                LOG.warn("Invalid request or response", e);
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().append(e.getMessage()).flush();
            } catch (ResourceAccessException | RequestRejectedException e) {
                LOG.warn(String.format("Error accessing resource %s%s.", req.getServletPath(), req.getPathInfo()), e);
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().append(e.getMessage()).flush();
            }
            return;
        }
        
        
        
        
        
        if(req.getPathInfo().contains("Login"))
        {
        	
        	//start
        	
        	
        	try {
               
                String Value = StringUtils.substringAfterLast(req.getPathInfo(), "Login/");
                
                System.out.println(Value);
                JSONArray arr = null;
                JSONObject Obj = null;
				try {
					Obj = new JSONObject(Value);
					String Uid=Obj.getString("Email");
					String pwd=Obj.getString("Password");
					System.out.println(Uid);
					System.out.println(pwd);
					int p=-1;
					for (int i=0;i<UserId.size();i++)
					{
						if(Uid.equals(UserId.get(i)))
								p=i;
						
					}
					
					if(p==-1)
					{
						
			              
							 String  response = "{"+'"'+"Status"+'"'+":"+'"'+"Failure"+'"'+"}";
				              
							resp.setContentType("application/json");
			                resp.getOutputStream().write(response.getBytes());
			                resp.setStatus(HttpServletResponse.SC_OK);
						
					}
					else
					{
						if(pwd.equals(Password.get(p)))
						{
							String  response = "{"+'"'+"Status"+'"'+":"+'"'+"Success"+'"'+"}";
				              
							resp.setContentType("application/json");
			                resp.getOutputStream().write(response.getBytes());
			                resp.setStatus(HttpServletResponse.SC_OK);
						}
						else
						{
							
							String  response = "{"+'"'+"Status"+'"'+":"+'"'+"Failure"+'"'+"}";
				              
							resp.setContentType("application/json");
			                resp.getOutputStream().write(response.getBytes());
			                resp.setStatus(HttpServletResponse.SC_OK);
							
						}
							
						
					}
					
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
                
                	
                	
                		
                		
                		
		
                
                
                
                
                
            } catch (IllegalArgumentException e) {
                LOG.warn("Invalid request or response", e);
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().append(e.getMessage()).flush();
            } catch (ResourceAccessException | RequestRejectedException e) {
                LOG.warn(String.format("Error accessing resource %s%s.", req.getServletPath(), req.getPathInfo()), e);
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().append(e.getMessage()).flush();
            }
            return;
        
        
        	
        	
        	
        	
        	//end
        	
        }
        else
        {
        if (path.length != 1) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if ("clients".equals(path[0])) {
            Collection<SecurityInfo> infos = this.store.getAll();

            String json = this.gsonSer.toJson(infos);
            resp.setContentType("application/json");
            resp.getOutputStream().write(json.getBytes("UTF-8"));
            resp.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        if ("server".equals(path[0])) {
            String json = this.gsonSer.toJson(SecurityInfo.newRawPublicKeyInfo("leshan", serverPublicKey));
            resp.setContentType("application/json");
            resp.getOutputStream().write(json.getBytes("UTF-8"));
            resp.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String endpoint = StringUtils.substringAfter(req.getPathInfo(), "/clients/");
        if (StringUtils.isEmpty(endpoint)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        LOG.debug("Removing security info for end-point {}", endpoint);
        if (this.store.remove(endpoint) != null) {
            resp.sendError(HttpServletResponse.SC_OK);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

private void processDeviceResponse(HttpServletRequest req, HttpServletResponse resp, LwM2mResponse cResponse)
        throws IOException {
    String response = null;
    if (cResponse == null) {
        LOG.warn(String.format("Request %s%s timed out.", req.getServletPath(), req.getPathInfo()));
        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        resp.getWriter().append("Request timeout").flush();
    } else {
        response = this.gsonSer.toJson(cResponse);
        resp.setContentType("application/json");
        resp.getOutputStream().write(response.getBytes());
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}

}