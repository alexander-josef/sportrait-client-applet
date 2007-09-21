/*-*
 *
 * FILENAME  :
 *    $RCSfile$
 *
 *    @author alex$
 *    @since 06.08.2007$
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
package wjhk.jupload2;

import wjhk.jupload2.gui.JUploadPanel;
import wjhk.jupload2.gui.JUploadTextArea;
import wjhk.jupload2.policies.UploadPolicy;
import wjhk.jupload2.policies.UploadPolicyFactory;

import java.awt.*;

public class JUploadApplication extends Frame
{
    JUploadPanel jUploadPanel;


    public JUploadApplication()
    {
        try
        {
            JUploadTextArea logWindow = new JUploadTextArea(20, 20);
            UploadPolicy uploadPolicy = UploadPolicyFactory.getUploadPolicy(null);
            this.jUploadPanel = new JUploadPanel(this, logWindow,
                    uploadPolicy);
            add("Center", jUploadPanel);
        } catch (Exception e)
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public static void main(String[] args)
    {
        JUploadApplication jUploadApplication = new JUploadApplication();
        jUploadApplication.pack();
        jUploadApplication.setVisible(true);
    }
}
