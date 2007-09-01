//
// $Id: CustomizedNbFilesPerRequestUploadPolicy.java 137 2007-05-12 23:34:08Z felfert $
// 
// jupload - A file upload applet.
// Copyright 2007 The JUpload Team
// 
// Created: 2006-05-06
// Creator: Etienne Gauthier
// Last modified: $Date: 2007-05-13 01:34:08 +0200 (So, 13 Mai 2007) $
//
// This program is free software; you can redistribute it and/or modify it under
// the terms of the GNU General Public License as published by the Free Software
// Foundation; either version 2 of the License, or (at your option) any later
// version. This program is distributed in the hope that it will be useful, but
// WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
// details. You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software Foundation, Inc.,
// 675 Mass Ave, Cambridge, MA 02139, USA.

package wjhk.jupload2.policies;

import wjhk.jupload2.JUploadApplet;
import wjhk.jupload2.exception.JUploadException;

/**
 * These is a now deprecated specialization of
 * {@link wjhk.jupload2.policies.DefaultUploadPolicy}. The DefaultUploadPolicy
 * now reads itself the nbFilesPerRequest applet parameter. <BR>
 * 
 * @author Etienne Gauthier
 * @version $Revision: 137 $
 * @see #CustomizedNbFilesPerRequestUploadPolicy(JUploadApplet)
 * @deprecated This class is of no use, as it actually behaves exactly as the
 *             {@link wjhk.jupload2.policies.DefaultUploadPolicy}.
 */
@Deprecated
public class CustomizedNbFilesPerRequestUploadPolicy extends
        DefaultUploadPolicy {

    /**
     * @param theApplet The applet to whom the UploadPolicy must apply.
     * @see UploadPolicy
     */
    public CustomizedNbFilesPerRequestUploadPolicy(JUploadApplet theApplet)
            throws JUploadException {
        super(theApplet);
    }

}
