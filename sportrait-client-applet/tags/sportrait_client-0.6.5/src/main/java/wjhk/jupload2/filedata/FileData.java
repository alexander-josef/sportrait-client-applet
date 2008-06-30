//
// $Id: FileData.java 95 2007-05-02 03:27:05Z
// /C=DE/ST=Baden-Wuerttemberg/O=ISDN4Linux/OU=Fritz
// Elfert/CN=svn-felfert@isdn4linux.de/emailAddress=fritz@fritz-elfert.de $
// 
// jupload - A file upload applet.
// Copyright 2007 The JUpload Team
// 
// Created: 2006-11-20
// Creator: Etienne Gauthier
// Last modified: $Date: 2007-06-07 17:31:40 +0200 (Do, 07 Jun 2007) $
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

package wjhk.jupload2.filedata;

import java.io.File;
import java.io.InputStream;
import java.util.Date;

import wjhk.jupload2.exception.JUploadException;
import wjhk.jupload2.policies.UploadPolicy;
import wjhk.jupload2.upload.FileUploadThread;

/**
 * This class contains all data and methods for a file to upload. The current
 * {@link wjhk.jupload2.policies.UploadPolicy} contains the necessary parameters
 * to personalize the way files must be handled. <BR>
 * <BR>
 * This class is the interface that all FileData must implement. The
 * {@link DefaultFileData} class contains the default implementation for this
 * interface. The {@link  PictureFileData} contains another implementation of
 * this interface, adapted to manage pictures (rotation, resizing...). <BR>
 * The instance of FileData is created by the
 * {@link UploadPolicy#createFileData(File, File)} method. This method can be
 * overrided in a new upoad policy, to create an instance of another FileData.
 * See {@link  PictureFileData} for an example of that.
 * 
 * @author Etienne Gauthier
 */

public interface FileData {

    /**
     * Prepare the fileData to upload. For instance, picture data can be resized
     * before upload (see {@link PictureFileData}. This method is called before
     * the upload of this file.
     * 
     * @see FileUploadThread
     */
    public void beforeUpload() throws JUploadException;

    /**
     * Get size of upload, which may be different from th actual file length.
     * 
     * @return The length of upload. In this class, this is the size of the
     *         file, as it isn't transformed for upload. This size may change if
     *         encoding is necessary (needs a new FileData class), or if picture
     *         is to be resized or rotated.
     * @see PictureFileData
     */
    public long getUploadLength() throws JUploadException;

    /**
     * This function is called after upload, whether it is successful or not. It
     * allows fileData to free any resssource created for the upload. For
     * instance, {@link PictureFileData#afterUpload()} removes the temporary
     * file, if any was created.
     */
    public void afterUpload();

    /**
     * This function creates an InputStream from this file. The
     * {@link FileUploadThread} class then reads bytes from it and transfers
     * them to the webserver. The caller is responsible for closing this stream.
     * 
     * @return An InputStream, representing this instance.
     */
    public InputStream getInputStream() throws JUploadException;

    /**
     * Get the original filename. This is the name of the file, into the local
     * hardrive
     * 
     * @return The original filename
     */
    public String getFileName();

    /**
     * @return The extension for the original file.
     */
    public String getFileExtension();

    /**
     * @return The length of the original file.
     */
    public long getFileLength();

    /**
     * @return The original file date.
     */
    public Date getLastModified();

    /**
     * Get the directory of the file.
     * 
     * @return The directory where this file is stored.
     */
    public String getDirectory();

    /**
     * This function return the FileData content type.
     * 
     * @return The mimeType for the file.
     */
    public String getMimeType();

    /**
     * Indicate if this file can be read.
     */
    public boolean canRead();

    /**
     * @return the File instance associated with this row.
     */
    public File getFile();

    /**
     * Retrieves the path of this file relative to it's root dir
     * 
     * @return This instance's relative path or an empty string if it was not
     *         created using a root parameter.
     */
    public String getRelativeDir();

}
