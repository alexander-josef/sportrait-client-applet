/*-*
 *
 * FILENAME  :
 *    $RCSfile$
 *
 *    @author alex$
 *    @since 15.08.2007$
 *
 * Copyright (c) 2007 Alexander Josef,unartig AG; All rights reserved
 *
 * STATUS  :
 *    $Revision$, $State$, $Name$
 *
 *    $Author$, $Locker$
 *    $Date$
 *
 *************************************************
 * $Log$
 ****************************************************************/
package ch.unartig.sportrait.client;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import wjhk.jupload2.exception.JUploadException;
import wjhk.jupload2.policies.UploadPolicy;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import ch.unartig.sportrait.client.jUploadPolicies.UnartigSportraitUploadPolicy;

public class JUnartigUploadClientPanel extends JPanel implements ActionListener
{
    private UploadPolicy uploadPolicy;
//    private JComboBox chooseYourEventComboBox;
    private JList chooseYourEventList;
    private JList chooseYourCategoryList;
    private static final String _PHOTOGRAPHER_PASS = "photographerPass";
    private static final String _PHOTOGRAPHER_ID = "photographerId";
    private static final String _XML_RPC_SERVER_URL = "xmlRpcServerUrl";
    private HashMap eventCategoryMap;
    private HashMap eventMap;
    private DefaultListModel sportraitEventListModel;
    private DefaultListModel sportraitEventCategoryListModel;


    public JUnartigUploadClientPanel(UploadPolicy uploadPolicy)
    {
        this.uploadPolicy = uploadPolicy;
        init();

    }


    /**
     * Retrieve the albums via XmlRpc
     * Set label and Render received albums as combobox
     *
     * new:
     * retrieve the events via xmlrpc
     * retrieve the categories via xmlrpc
     * create empty event list
     * create empty event-category list
     *
     *
     * on change in the event list, adapt the category list
     */
    private void init()
    {
        this.setMinimumSize(new Dimension(600,200));
        this.setLayout(new GridBagLayout());
        System.out.println("JUnartigUploadClientPanel.init :  got albums");

        JLabel label = new JLabel("Event und Kategorie für Foto-Upload wählen:");

//        chooseYourEventComboBox = new JComboBox();
        chooseYourEventList = new JList();
        chooseYourEventList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        chooseYourEventList.setLayoutOrientation(JList.VERTICAL);
        chooseYourEventList.setVisibleRowCount(-1);
        chooseYourEventList.addListSelectionListener(new EventListSelectionListener());
        JScrollPane eventListScroller = new JScrollPane(chooseYourEventList);
        eventListScroller.setPreferredSize(new Dimension(200,100));
        eventListScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        chooseYourCategoryList = new JList();
        chooseYourCategoryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        chooseYourCategoryList.setLayoutOrientation(JList.VERTICAL);
        chooseYourCategoryList.addListSelectionListener(new EventCategotyListSelectionListener());
        JScrollPane categoryListScroller = new JScrollPane(chooseYourCategoryList);
        categoryListScroller.setPreferredSize(new Dimension(200, 100));
        categoryListScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        sportraitEventListModel = new DefaultListModel();

        sportraitEventCategoryListModel = new DefaultListModel();
        try
        {
            loadEventList();
        } catch (Exception e)
        {
            throw new RuntimeException("Can not initialize events");
        }
        // insert categories in list
        // todo doesn't need to be initialized
//        for (Object category : categories)
//        {
//            String categoryKey = (String) category;
//            sportraitEventCategoryListModel.addElement("Bitte Anlass waehlen");
//        }

        chooseYourEventList.setModel(sportraitEventListModel);
        chooseYourEventList.setCellRenderer(new EventRenderer());

        chooseYourCategoryList.setModel(sportraitEventCategoryListModel);
        chooseYourCategoryList.setCellRenderer(new EventCategoryRenderer());


        this.add(label);
        this.add(eventListScroller);
        this.add(categoryListScroller);
    }

    private void loadEventList()
            throws MalformedURLException, XmlRpcException
    {
        // insert events in list
        Object [] events;
        events = getSportraitEvents();

        for (Object event : events)
        {
            String eventKey = (String) event;
            sportraitEventListModel.addElement(eventKey);
        }
    }

    /**
     * Load the event categories from the server and reset the list-model with the result.
     * @param eventId id of event that has been selected
     * @throws MalformedURLException 
     * @throws XmlRpcException
     */
    private void loadEventCategoryList(String eventId) throws MalformedURLException, XmlRpcException
    {
        // insert events in list
        Object [] eventCategories;
        eventCategories = getSportraitEventCategories(eventId);
        System.out.println("eventCategories = " + Arrays.toString(eventCategories));
        sportraitEventCategoryListModel.clear();
        for (Object o : eventCategories)
        {
            String eventCategoryKey = (String) o;
            sportraitEventCategoryListModel.addElement(eventCategoryKey);
        }

        if (sportraitEventCategoryListModel.size() <1)
        {
            sportraitEventCategoryListModel.addElement("-1");
        }

    }

    /**
     * Make a call to the xmlRpc getSportraitEvents method; todo move 
     * @return the key-set of the map retrieved by the xml rpc service. the keys are the eventids.
     * @throws MalformedURLException
     * @throws XmlRpcException
     */
    private Object[] getSportraitEvents() throws MalformedURLException, XmlRpcException
    {
        Object xmlRpcResult = getXmlRpcResult("AdminServices.getEvents",null);

        System.out.println("xmlRpcResult.getClass().getName() = " + xmlRpcResult.getClass().getName());

        // the xmlprc result is a map in the form: eventId:title
        eventMap = (HashMap) xmlRpcResult;
        System.out.println("albumMap.size() = " + eventMap.size());
        // debug output:
        for (Iterator iterator = eventMap.keySet().iterator(); iterator.hasNext();)
        {
            String key = (String) iterator.next();
            System.out.println("key = " + key);
            String eventName = (String) eventMap.get(key);
            System.out.println("eventName = " + eventName);
        }


        Object[] keys = eventMap.keySet().toArray();
        System.out.println("keys.getClass().getName() = " + keys.getClass().getName());
        return keys;
    }

    /**
     * Todo better routine name: this routine queries the server, sets the map with <categoryID,title> and return the keyset as array 
     * @return
     * @throws MalformedURLException
     * @throws XmlRpcException
     * @param eventId
     */
    private Object[] getSportraitEventCategories(String eventId) throws MalformedURLException, XmlRpcException
    {
        java.util.List parameters = new ArrayList();
        parameters.add(eventId);
        Object xmlRpcResult = getXmlRpcResult("AdminServices.getEventCategories", parameters);
        System.out.println("xmlRpcResult.getClass().getName() = " + xmlRpcResult.getClass().getName());

        // the xmlprc result is a map:
        eventCategoryMap = (HashMap) xmlRpcResult;
        System.out.println("albumMap.size() = " + eventCategoryMap.size());
        for (Iterator iterator = eventCategoryMap.keySet().iterator(); iterator.hasNext();)
        {
            String key = (String) iterator.next();
            System.out.println("key = " + key);
            String eventName = (String) eventCategoryMap.get(key);
            System.out.println("eventName = " + eventName);
        }
        Object[] keys = eventCategoryMap.keySet().toArray();
        System.out.println("keys.getClass().getName() = " + keys.getClass().getName());
        return keys;
    }

    /**
     * Using the method name in the parameter, make an xmlrpc call.
     * @param xmlRpcMethod
     * @param parameterList
     * @return
     * @throws MalformedURLException
     * @throws XmlRpcException
     */
    private Object getXmlRpcResult(String xmlRpcMethod,java.util.List parameterList) throws MalformedURLException, XmlRpcException
    {
        String photographerId = uploadPolicy.getApplet().getParameter(UnartigSportraitUploadPolicy.PROP_PHOTOGRAPHER_ID);
        String password = uploadPolicy.getApplet().getParameter(_PHOTOGRAPHER_PASS);
        String xmlRpcServerUrl = uploadPolicy.getApplet().getParameter(_XML_RPC_SERVER_URL);
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        config.setServerURL(new URL(xmlRpcServerUrl));
        XmlRpcClient client = new XmlRpcClient();
        client.setConfig(config);

        java.util.List xmlRpcParameterList = new ArrayList();
        xmlRpcParameterList.add(photographerId);
        xmlRpcParameterList.add(password);
        if (parameterList!=null)
        {
            for (int i = 0; i < parameterList.size(); i++)
            {
                Object parameter = parameterList.get(i);
                xmlRpcParameterList.add(parameter);
            }
        }
        System.out.println("going to execute xmlrpc-method");
        return client.execute(xmlRpcMethod,xmlRpcParameterList);
    }



    /**
     * Upon selecting or changing the album, transmit the id to the ... upload policy??
     * todo this needs to change. the parameters must be eventid and category id and then use the already existing routine for creating an album.
     * @param e
     */
    public void actionPerformed(ActionEvent e)
    {
        try
        {
            // todo adjust to event Id and category Id
            uploadPolicy.setProperty(UploadPolicy.PROP_ALBUM_ID,(String)chooseYourEventList.getSelectedValue());
        } catch (JUploadException e1)
        {
            e1.printStackTrace();
            throw new RuntimeException(e1);
        }
    }
    
    private class EventListSelectionListener implements ListSelectionListener
    {
        public void valueChanged(ListSelectionEvent e)
        {
            System.out.println("JUnartigUploadClientPanel$EventListSelectionListener.valueChanged");
            String eventId = (String)chooseYourEventList.getSelectedValue();
            System.out.println("event id = " + eventId);
            try
            {
                uploadPolicy.setProperty(UnartigSportraitUploadPolicy.PROP_EVENT_ID,eventId);
            } catch (JUploadException e1)
            {
                throw new RuntimeException("Error updating parameter");
            }
            // call loadCategoryList with event id
            try
            {
                loadEventCategoryList(eventId);
            } catch (Exception exception)
            {
                throw new RuntimeException("Exception while getting the event categories for ["+eventId+"]",exception);
            }
        }
    }

    private class EventCategotyListSelectionListener implements ListSelectionListener
    {

        public void valueChanged(ListSelectionEvent e)
        {
            try
            {
                uploadPolicy.setProperty(UnartigSportraitUploadPolicy.PROP_EVENT_CATEGORY_ID,(String)chooseYourCategoryList.getSelectedValue());
            } catch (JUploadException e1)
            {
                throw new RuntimeException("Problem choosing the event category");
            }
        }
    }




    /**
     * Renderer for the entry in the sportrait event list.
     */
    class EventRenderer implements ListCellRenderer{
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
        {
            System.out.println("renderer called");
            JLabel label;
            if (Long.parseLong((String) value) > 0)
            {
                label = new JLabel(eventMap.get(value) + " -- " + value);
                if (isSelected)
                {
                    label.setOpaque(true);
                    System.out.println("is selected");
                    label.setBackground(new Color(200,200,200));
                }
            } else
            {
                label = new JLabel("--Bitte wählen--");
                label.setHorizontalAlignment(SwingConstants.CENTER);
            }
            System.out.println("label.getBackground()" + label.getBackground());
            return label;
        }
    }

    /**
     * Renderer for the entry in the sportrait event-category list.
     */
    class EventCategoryRenderer implements ListCellRenderer{
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
        {
            System.out.println("category renderer called");
            JLabel label;
            String valueString = (String) value;
            if (Long.parseLong(valueString) > 0)
            {
                label = new JLabel(eventCategoryMap.get(value) + " -- " + value);
                if (isSelected)
                {
                    label.setOpaque(true);
                    System.out.println("is selected");
                    label.setBackground(new Color(200,200,200));
                }
            } else
            {
                label = new JLabel("--Keine Kategorie vorhanden--");
                label.setHorizontalAlignment(SwingConstants.CENTER);
            }
            System.out.println("label.getBackground()" + label.getBackground());
            return label;
        }
    }

}
