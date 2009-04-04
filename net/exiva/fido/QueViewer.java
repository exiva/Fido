
package net.exiva.fido;

import danger.app.*;
import java.util.Random;
import danger.ui.*;
import danger.util.DEBUG;
import java.io.File;
import danger.storage.*;
import danger.util.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import danger.net.*;
import java.net.URLEncoder;
public class QueViewer extends ScreenWindow implements Resources, Commands {
	private static ActiveListView listView;

    public QueViewer() {
				
		buildMenu();
    }
	
	
	public static QueViewer makeNew() {
        QueViewer t = (QueViewer)Application.getCurrentApp().getResources().getScreen(ID_FEEDSCREEN);
        return t;
    }

	public void loadFile(String filepath)
	{	
	}
	


	public void onDecoded() {
	
			listView = (ActiveListView) this.getDescendantWithID(ID_ACTIVE_VIEW);
				
	}
    public final void buildMenu() {

    }
    
    public static void updateView() {
		StdActiveList templist = new StdActiveList();
		for(int i=0;i<Fido.fileque.size();i++)
		{
			FileInfo tempinfo = (FileInfo)Fido.fileque.get(i);
			templist.addItem(tempinfo.getURL());
		}
		listView.setList(templist);
		templist = null;		
	}
    public boolean receiveEvent(Event e) {
        switch (e.type) {

        }
        return super.receiveEvent(e);
    }
	


    public boolean eventWidgetUp(int inWidget, Event e) {
        switch (inWidget) {
			case Event.DEVICE_BUTTON_CANCEL:
				Application.getCurrentApp().returnToLauncher();
				return true;

			case Event.DEVICE_BUTTON_BACK:
				Application.getCurrentApp().returnToLauncher();
				return true;
	}
        return super.eventWidgetUp(inWidget, e);
    }
}

