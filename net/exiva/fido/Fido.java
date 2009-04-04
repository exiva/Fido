// Copyright 2001-2005, Danger, Inc.  All Rights Reserved.
// This file is subject to the Danger, Inc. Sample Code License,
// which is provided in the file SAMPLE_CODE_LICENSE.
// Copies are also available from http://developer.danger.com/

/*
	Todo: Rename the package for your application.
	This package name should be replaced with your compa
	ny domain
    (ie. com.danger for applications Danger writes).
	To do this you need to:
	1. Move all the java source files from com/example to the new path.
	2. Change the package declaration in all the java source files.
	3. Change the package declaration in all the .rsrc files.
	3. Change the path in the interface and events declarations in all the .rsrc files.
*/
package net.exiva.fido;

import danger.app.Application;
import danger.app.IPCIncoming;
import danger.app.IPCMessage;
import danger.util.DEBUG;
import danger.parsing.*;
import danger.net.*;
import danger.util.*;
import java.io.StringReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import danger.storage.*;
import java.util.Vector;
import danger.system.Hardware;
import danger.ui.MarqueeAlert;
import danger.ui.NotificationManager;
import danger.app.IPCMessage;
import danger.app.Registrar;
import danger.app.Event;

/**
 * Implements the main application class.
 */
public class Fido
		extends Application
		implements Resources, Commands {
	static private QueViewer mQue;
	private static FileOutputStream outstream;
	private static String fetchfilepath;
	static public Vector fileque = new Vector();
	//*	--------------------	Fido

	/**
	 * Creates the main application class.
	 */
	public Fido() {

// register as a link provider so our app shows up on wheel holding
		Registrar.registerProvider("send-via",this,6969,getResources().getBitmap(ID_FETCH_ICON),"Fido");
	}


	//*	--------------------	launch

	/**
	 * Handles the launch event. Called once whenever the application is
	 * launched.
	 */
	public void launch() {
		fetchfilepath = new String();
		listprovider();
		DEBUG.p("Fido: launch");
		try
		{
        		fetchfilepath = new String(((String[])StorageManager.getRemovablePaths())[0]);
		}
		catch (Exception e2)
		{
			DEBUG.p("Failed to mount path");
			fetchfilepath = null;
		}
		if(fetchfilepath != null)
		{
			DEBUG.p("Mount Not Null Mount is: "+fetchfilepath);
			File basedirs = new File(fetchfilepath+"/DCIM/12seconds/");
			try
			{
				if(!basedirs.exists())
				basedirs.mkdir();
				DEBUG.p("fetch: mkdir");
			}
			catch (Exception e2)
			{
				DEBUG.p("Shit, wtf happened?");
			}
		}
		mQue = QueViewer.makeNew();
		mQue.show();
        	StorageManager.registerStorageDeviceListener(this);
	}

	//*	--------------------	resume

	/**
	 * Handles the resume event. Called automatically whenever the
	 * application is resumed.
	 */
	public void resume() {
		DEBUG.p("Fido: resume");

	}

	//*	--------------------	Suspend

	/**
	 * Handles the Suspend event. Called automatically whenever the
	 * application is suspended.
	 */
	public void suspend() {
		DEBUG.p("Fido: suspend");
	}
	
	public boolean
	receiveEvent(Event e)
	{
		switch (e.type)
		{
        	        case Event.EVENT_STORAGE_DEVICE_STATE_CHANGED : 
	                	updateMountPath();
				break;
			case Event.EVENT_MESSAGE:
			    if (e.what == 6969)
				    {
			               return handleIPCMessage((IPCIncoming)e.argument);
				    }
				break;

			default:
				break;
		}
		return (super.receiveEvent(e));
	}


	public void updateMountPath()
	{
		DEBUG.p("Fido Updating Mount Path");
		try
		{
        		fetchfilepath = new String(((String[])StorageManager.getRemovablePaths())[0]);
		}
		catch (Exception e2)
		{
			DEBUG.p("Failed To Mount Path");
			fetchfilepath = null;
		}
		if(fetchfilepath != null)
		{
			DEBUG.p("Mount Not Null Mount is: "+fetchfilepath);
			File basedirs = new File(fetchfilepath+"/Fetched/");
			try
			{
				if(!basedirs.exists())
					basedirs.mkdir();
			}
			catch (Exception e2)
			{
			}
		}
	}

    private final boolean handleIPCMessage(IPCIncoming incoming)
    {
        IPCMessage message = incoming.getMessage();
        String command = message.findString("action");
	if(command.equals("send"))
	{

		DEBUG.p("Command: "+command);
        	//String body = message.findString("body");
        	//String to = message.findString("to");
		String body = message.findString("body");
		URL tempurl = new URL(body);
		//DEBUG.p("file: "+tempurl.getFile());
		FileInfo tempinfo = new FileInfo(Hardware.getSystemTime(), body, fetchfilepath+"/Fetched/"+tempurl.getFile());
		fileque.add(tempinfo);
		DEBUG.p("Fileque is now: "+fileque.size());

		HTTPConnection.get(tempinfo.getURL(),"",(short)0,tempinfo.getID());

		DEBUG.p("URL: "+message.findString("body"));
		tempinfo = null;
		QueViewer.updateView();
	}
        return true;
    }

public static String listprovider() {
	DEBUG.p("Providers: "+Registrar.listAllProviders());
}

	public void networkEvent(Object object)
    {
        if (object instanceof HTTPTransaction)
        {
            HTTPTransaction t = (HTTPTransaction) object;
			System.err.println("Got network event: " + t);
			//System.err.println("Class version: " + t.getClassVersion());
			//System.err.println("App class name: " + t.getAppClassName());
			//System.err.println("Command: " + t.getCommand());
			//System.err.println("Sequence ID: " + t.getSequenceID());
			//System.err.println("Bytes: " + t.getBytes());
			//System.err.println("String: " + t.getString());
			//DEBUG.p("Headers: "+t.getHTTPHeaders());
			//System.err.println("Time: " + System.currentTimeMillis());
			//DEBUG.p("RESPONSECODE: "+t.getResponse());
			//DEBUG.p("String: "+t.getString());
			if(t.getSequenceID() == 1)
			{
				if(t.getResponse() == 200)
				{
		
				}
				else
				{
					DEBUG.p("Response "+t.getResponse()+" not 200");
				}
				
			}
			
			else
			{
			  if(fetchfilepath != null)
			  {
				FileInfo tempfileinfo;
				for(int i=0;i<fileque.size();i++)
				{
					tempfileinfo = (FileInfo)fileque.get(i);

					if(tempfileinfo.getID() == t.getSequenceID())
					{
						//DEBUG.p("Received file for queued feed: "+tempfileinfo.getID());
						fileque.removeElementAt(i);
						//DEBUG.p("Vector Size is Now: "+fileque.size());

					 if(!tempfileinfo.getFile().exists())
					 {
						//DEBUG.p("URL :"+t.getURL());
						//String[] p = StorageManager.getRemovablePaths();
						File curfile = tempfileinfo.getFile();
						try {

/////////////////////////////////////////////////////
/////////////////////////////////////////////////////
/////////////////////////////////////////////////////
/////////////////////// must use a \\ instead of / on indexOf if using on sdk
/////////////////////////////////////////////////////
/////////////////////////////////////////////////////
							File dirs = new File(curfile.getPath().substring(0,curfile.getPath().lastIndexOf("/")));
							DEBUG.p("Making dirs: "+dirs.getPath());
							dirs.mkdirs();
							dirs = null;
							//DEBUG.p("Made Directories");
						}
						catch (Exception e2)
						{
							DEBUG.p("Directory Creation Failed");
						}
						
						try {
						curfile.createNewFile();
						}
						catch (Exception e2)
						{
							//DEBUG.p("Failed To Make File: "+e2.toString());
						}
						DEBUG.p("File is: "+curfile.getName());
						DEBUG.p("Path: "+curfile.getPath());
						//curfile.delete();
						//curfile.createNewFile();
						//DEBUG.p("Write Length Is: "+ThisText.getText().getBytes().length);
						try {
							outstream = new FileOutputStream(curfile, false);
							//DEBUG.p("FileOutputStream Created");
						}
						catch (Exception e2) { 
							DEBUG.p("Failed to create FileOutputStream on file "+e2.toString());
						}
						try {
							outstream.write(t.getBytes());
					
							//DEBUG.p("Stream Written");
						}
						catch (Exception e2) {
							DEBUG.p("Failed to Write to Stream");
						}
						try {
							outstream.close();
							IPCMessage ipc = new IPCMessage();
							ipc.addItem("action", "rebuild-index");
							Registrar.sendMessage("music-player", ipc, null);
//							NotificationManager.marqueeAlertNotify(new MarqueeAlert("Fetched: "+tempfileinfo.getURL(),1));
//							NotificationManager.marqueeAlertNotify(new MarqueeAlert("Successfully Fetched: "+tempfileinfo.getFile(),Application.getCurrentApp().getResources().getBitmap(ID_SMALL_ICON),1));
							NotificationManager.marqueeAlertNotify(new MarqueeAlert("Successfully Fetched: "+curfile.getName(),getResources().getBitmap(ID_MAR_ICON),1));

							//DEBUG.p("FILE CLOSED");

						}
						catch (Exception e2)
						{	
							DEBUG.p("Error closing file: "+e2.toString());
						}
				
					 }
					 QueViewer.updateView();
				    }
				}
				tempfileinfo = null;
			  }
			  else
			  {
				NotificationManager.marqueeAlertNotify(new MarqueeAlert("Fetch Failed: No Mount Point Detected",getResources().getBitmap(ID_MAR_ICON),1));
			  }
			} //end of else
			t = null;
			
        } //end of if object HTTPTransaction
		
	//network event ends below
    }
}
