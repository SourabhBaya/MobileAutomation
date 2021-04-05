package org.oiShoppingList.common.util;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;



public class FileLocker 
{
	private FileChannel channel = null;
	private FileLock lock = null;

	/**
	 * #Function Name:  public boolean isLockedByAnotherTestCase()
	 *
	 * #Description: This method is used to check if the file is locked by any other testcase
	 *
	 * #Input Parameters:
	 *			@return true - if file is locked, else false
	 *
	 * #Author: Sourabh Baya
	 */

    public boolean isLockedByAnotherTestCase(String strFileLockerName)
    {
		File file = null;

    	try {
			file = new File(strFileLockerName);
			if(!file.exists()) {
				file.createNewFile();
			}
		}
    	catch(Exception e) {
    		LoggerConfig.log.debug("Exception occurred while creating/reading locker file : "+e.getMessage());
		}
    	
    	boolean bFileLockedByOtherTC = false;

		RandomAccessFile rFile = null;

		try {
             rFile = new RandomAccessFile(file, "rw");

            channel = rFile.getChannel();

            try 
            {        
            	lock = channel.tryLock();
            	
            	if(lock==null)
            	{
            		LoggerConfig.log.debug("Lock is null, so file is locked by another Test Case");
            		
            		bFileLockedByOtherTC = true;
            	}
            	else
            	{            	
            	   LoggerConfig.log.debug("File locked successfully");
            	}
            }
            catch (OverlappingFileLockException e) 
            {
            	LoggerConfig.log.debug("In Exception, so file is locked by another Test Case");
                LoggerConfig.log.debug("Overlapping File Lock Error: " + e);
                
                bFileLockedByOtherTC = true;
            }
 

        }
        catch (IOException e) {
            LoggerConfig.log.debug("I/O Error: " + e);
        }
        
        return bFileLockedByOtherTC;
         
    }

    public void releaseLockAndChannels() {
    	try {
    		if(lock != null) {
				lock.release();
			}
    		if(channel != null) {
				channel.close();
			}
		}
    	catch(Exception e) {
    		LoggerConfig.log.debug("Exception encountered in releaseLockAndChannels method : "+e.getMessage());
		}
	}
}
