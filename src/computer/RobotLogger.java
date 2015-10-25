package computer;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class RobotLogger {
	
	private static final Logger LOGGER = Logger.getLogger( "RobotOverview" );
	private static final Logger LOGGER_MESSAGE = Logger.getLogger( "RobotMessage" );
	private static final int FILE_SIZE = 1024000000;
	private static FileHandler logFile = null;
	private static FileHandler logMessageFile = null;
	private static boolean isEnabled = true;

	public synchronized static void setLogger(boolean _isEnabled){
		isEnabled = _isEnabled;
	}
	
	public synchronized static void log(String msg){
		if (isEnabled) {
			if(logFile == null){
				try {
					logFile = new FileHandler("RobotOverview.log",FILE_SIZE,1,true);
					SimpleFormatter formatter = new SimpleFormatter(); 
					logFile.setFormatter(formatter);
					LOGGER.setLevel(Level.ALL);
					LOGGER.setUseParentHandlers(false);
					LOGGER.addHandler(logFile);
				} catch (SecurityException | IOException e) {
					e.printStackTrace();
				} 
			}
			LOGGER.info(msg);
		}
	}
	
	public synchronized static void logMessage(String msg){
		if (isEnabled) {
			if(logMessageFile == null){
				try {
					logMessageFile = new FileHandler("RobotMessage.log",FILE_SIZE,1,true);
					SimpleFormatter formatter = new SimpleFormatter(); 
					logMessageFile.setFormatter(formatter);
					LOGGER_MESSAGE.setLevel(Level.ALL);
					LOGGER_MESSAGE.setUseParentHandlers(false);
					LOGGER_MESSAGE.addHandler(logMessageFile);
				} catch (SecurityException | IOException e) {
					e.printStackTrace();
				} 
			}
			LOGGER_MESSAGE.info(msg);
		}
	}
	
	public synchronized static void closeLogs(){
		for(Handler h:LOGGER.getHandlers())
		{
		    h.close();
		}
		for(Handler h:LOGGER_MESSAGE.getHandlers())
		{
		    h.close();
		}
	}
}
