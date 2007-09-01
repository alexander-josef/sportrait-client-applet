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

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.XmlRpcException;

import javax.swing.*;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Iterator;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;

import wjhk.jupload2.policies.UploadPolicy;
import wjhk.jupload2.exception.JUploadException;

public class JUnartigUploadClientPanel extends JPanel implements ActionListener, ListCellRenderer
{
    private HashMap albumMap;
    private UploadPolicy uploadPolicy;
    private JComboBox chooseYourEventComboBox;
    private static final String _PHOTOGRAPHER_PASS = "photographerPass";
    private static final String _PHOTOGRAPHER_ID = "photographerId";


    public JUnartigUploadClientPanel(UploadPolicy uploadPolicy)
    {
        this.uploadPolicy = uploadPolicy;
        init();

    }

    private void init()
    {
        Object [] albums = null;
        try
        {
           albums = getSportraitAlbums();
        } catch (MalformedURLException e)
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (XmlRpcException e)
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


        JLabel label = new JLabel("Album für Foto-Upload wählen:");

        chooseYourEventComboBox = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();


//        defaultComboBoxModel1.addElement("Sola 07");
//        defaultComboBoxModel1.addElement("ECZ Meisterschaften");
        defaultComboBoxModel1.addElement("-1");
        for (int i = 0; i < albums.length; i++)
        {
            String albumKey = (String)albums[i];
            defaultComboBoxModel1.addElement(albumKey);
        }
        chooseYourEventComboBox.setModel(defaultComboBoxModel1);
//        defaultComboBoxModel1.addElement("Bitte waehlen");
        chooseYourEventComboBox.setRenderer(this);

        chooseYourEventComboBox.addActionListener(this);

        this.add(label);
        this.add(chooseYourEventComboBox);
    }

    /**
     * 
     * @return the keys of the albums-map; keys are the levelid from the server
     * @throws MalformedURLException
     * @throws XmlRpcException
     */
    private Object[] getSportraitAlbums() throws MalformedURLException, XmlRpcException
    {
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        config.setServerURL(new URL("http://sportrait.local/xmlrpc"));
        XmlRpcClient client = new XmlRpcClient();
        client.setConfig(config);
        Object[] params = new Object[]{new Integer(33), new Integer(9)};
        Integer result = (Integer) client.execute("Calculator.add", params);
        System.out.println("result = " + result);

        // XML RPC Call to getAlbums
//        String photographerId = "2";
        String photographerId = uploadPolicy.getApplet().getParameter(_PHOTOGRAPHER_ID);
//        String password = "";
        String password = uploadPolicy.getApplet().getParameter(_PHOTOGRAPHER_PASS);
        Object[] sportraitServerAuthParams = new Object[]{photographerId, password};
        Object xmlRpcResult = client.execute("Calculator.getAlbums",sportraitServerAuthParams);

        System.out.println("xmlRpcResult.getClass().getName() = " + xmlRpcResult.getClass().getName());

        albumMap = (HashMap) xmlRpcResult;
        System.out.println("albumMap.size() = " + albumMap.size());
        for (Iterator iterator = albumMap.keySet().iterator(); iterator.hasNext();)
        {
            String key = (String) iterator.next();
            System.out.println("key = " + key);
            String eventName = (String) albumMap.get(key);
            System.out.println("eventName = " + eventName);
        }


        Object[] keys = albumMap.keySet().toArray();
        System.out.println("keys.getClass().getName() = " + keys.getClass().getName());
        return keys;


//        for (int i = 0; i < xmlRpcResult.length; i++)
//        {
//            Object event = xmlRpcResult[i];
//            System.out.println("event.getClass().getName() = " + event.getClass().getName());
//            System.out.println("***** event = " + event);
//        }
        

//        for (int i = 0; i < xmlRpcResult.size(); i++)
//        {
//            String s = (String) xmlRpcResult.get(i);
//            System.out.println("s = " + s);
//        }

    }


    /**
     * Upon selecting or changing the album, transmit the id to the ... upload policy??
     * @param e
     */
    public void actionPerformed(ActionEvent e)
    {
        try
        {
            uploadPolicy.setProperty(UploadPolicy.PROP_ALBUM_ID,(String)chooseYourEventComboBox.getSelectedItem());
        } catch (JUploadException e1)
        {
            e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw new RuntimeException(e1);
        }
    }

    /**
     * Renderer for the entry in the sportrait album list.
     * @param list
     * @param value
     * @param index
     * @param isSelected
     * @param cellHasFocus
     * @return
     */
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
    {
        JLabel label;
        if (Long.parseLong((String) value) > 0)
        {
            label = new JLabel(albumMap.get(value) + " -- " + value);
        } else
        {
            label = new JLabel("--Bitte wählen--");
            label.setHorizontalAlignment(SwingConstants.CENTER);
        }
        return label;
    }
}
