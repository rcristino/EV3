package exec;

import java.awt.Dimension;
import java.io.IOException;
import java.net.ConnectException;
import java.util.Collection;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import lejos.robotics.filter.PublishedSource;
import lejos.robotics.filter.SubscribedProvider;

public class PCMonitoring extends JFrame {
	private static final long serialVersionUID = 1L;
	private static String title = "Robot Published Samples";
	private String[] fixedColumnNames = {
			"IP Address",
            "Host",
            "Port",
            "Name",
            "Sample Size",
            "Frequency",
            "Last Message"
            };
	private String[] columnNames;
	private JTable table;
	private Object[][] data;
	
	public PCMonitoring() throws IOException {
		super(title);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setData(PublishedSource.getSources());
		table = new JTable(data, columnNames);
		this.getContentPane().add(new JScrollPane(table));
		
		setPreferredSize(new Dimension(1000,200));
	}
	
	public void setData(Collection<PublishedSource> sources) {
		int maxSampleSize = 0;
		for(PublishedSource source: sources) if (source.sampleSize() > maxSampleSize) maxSampleSize = source.sampleSize();
		data = new Object[sources.size()][fixedColumnNames.length + maxSampleSize];
		columnNames = new String[fixedColumnNames.length + maxSampleSize];
		for(int i=0;i<fixedColumnNames.length;i++) columnNames[i] = fixedColumnNames[i];
		for(int i=0;i<maxSampleSize;i++) columnNames[fixedColumnNames.length+i] = "sample[" + i + "]";
		
		int row = 0;
		for(PublishedSource source: sources) {
			data[row][0] = source.getIpAddress();
			data[row][1] = source.getHost();
			data[row][2] = source.getPort();
			data[row][3] = source.getName();
			data[row][4] = source.sampleSize();
			data[row][5] = source.getFrequency();
			data[row][6] = source.getTime();
			
			/*String logMsg = " IpAddress: " + source.getIpAddress() + 
					" Host: " + source.getHost() + 
					" Port: " + source.getPort() + 
					" Name: " + source.getName() + 
					" Simple Size: " + source.sampleSize() +
					" Frequency: " + source.getFrequency() + 
					" Time: " + source.getTime();*/
			
			SubscribedProvider provider;
			try {
				provider = source.connect();
				float[] sample = new float[source.sampleSize()];
				
				provider.fetchSample(sample, 0);
				
				for(int i=0;i<source.sampleSize();i++) {
					data[row][fixedColumnNames.length+i] = sample[i];
					//logMsg = logMsg + " Sample[" + i + "]: " + sample[i];
				}
				source.close();
			} catch (ConnectException e) {
				for(int i=0;i<source.sampleSize();i++) {
					data[row][fixedColumnNames.length+i] = "n/a";
					//logMsg = logMsg + " Sample[" + i + "]: " + "n/a";
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			row++;
		}
	}
	
	public void run() throws IOException {
		for(;;) {
			Collection<PublishedSource> sources = PublishedSource.getSources();
			setData(sources);
		    SwingUtilities.invokeLater(new Runnable() {
		        public void run() {
		        	table.setModel(new DefaultTableModel(data, columnNames));
		        }
		    });
			
		}
	}
	
	public static void main(String[] args) throws IOException {
		
		PCMonitoring frame = new PCMonitoring();
		
		frame.pack();
		frame.setVisible(true);
		frame.run();
	}
}
