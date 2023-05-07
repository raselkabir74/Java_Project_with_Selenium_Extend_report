package utilities;

import java.io.File;

public class FilesUtil {

    /**
     * Rename specific file
     *
     * @param filePath    file Path
     * @param oldFileName old File Name
     * @param newFileName new File Name
     */
    public static void renameFile(String filePath, String oldFileName, String newFileName) {
        try {
            File oldFile = new File(filePath + "/" + oldFileName);
            File newFile = new File(filePath + "/" + newFileName);

            if (newFile.exists()) {
                LogWriter.writeToLogFile(LogWriter.logLevel.ERROR, "New '" + newFileName + "' file already exist in the '" + filePath + "' directory");
            } else {
                if (oldFile.renameTo(newFile)) {
                    LogWriter.writeToLogFile(LogWriter.logLevel.INFO, "Successfully renamed the old file '" + oldFileName + "' to new file '" + newFileName + "'");
                }
            }
        } catch (Exception e) {
            LogWriter.writeToLogFile(LogWriter.logLevel.ERROR, "Failed to rename the '" + oldFileName + "' old due to\n" + e.getMessage());
        }
    }
}
