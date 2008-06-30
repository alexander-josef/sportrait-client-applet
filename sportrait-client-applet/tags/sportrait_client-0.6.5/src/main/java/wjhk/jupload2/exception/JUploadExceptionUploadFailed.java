//
// $Id: JUploadExceptionUploadFailed.java 95 2007-05-02 03:27:05Z /C=DE/ST=Baden-Wuerttemberg/O=ISDN4Linux/OU=Fritz Elfert/CN=svn-felfert@isdn4linux.de/emailAddress=fritz@fritz-elfert.de $
// 
// jupload - A file upload applet.
// Copyright 2007 The JUpload Team
// 
// Created: 2006-09-15
// Creator: Etienne Gauthier
// Last modified: $Date: 2007-05-02 05:27:05 +0200 (Mi, 02 Mai 2007) $
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

package wjhk.jupload2.exception;

/**
 * This exception occurs when an upload failed. It can be generated if the
 * server response to the upload doesn't match the
 * {@link wjhk.jupload2.policies.UploadPolicy#PROP_STRING_UPLOAD_SUCCESS}
 * regular expression.
 * 
 * @author Etienne Gauthier
 * @author $Revision: 95 $
 */
public class JUploadExceptionUploadFailed extends JUploadException {

    /**
     * 
     */
    private static final long serialVersionUID = -9031106357048838553L;

    /**
     * Constructs a new exception with the specified detail message.
     * @param msg The message for this instance.
     */
    public JUploadExceptionUploadFailed(String msg) {
        super(msg);
    }

    /**
     * Constructs a new exception with the specified cause.
     * @param cause The cause for this instance.
     */
    public JUploadExceptionUploadFailed(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     * @param msg The message for this instance.
     * @param cause The cause for this instance.
     */
    public JUploadExceptionUploadFailed(String msg, Throwable cause) {
        super(msg, cause);
    }
}
