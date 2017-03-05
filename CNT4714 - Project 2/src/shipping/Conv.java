/*
 * Name: Wenyang Wu
 * Course: CNT 4714 - Spring 2017
 * Assignment Title: Program 2 - Multi-Threaded Programming in Java
 * Date: Sunday February 12, 2016
 */
package shipping;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Conv{
	
	private final static String CONFIG_FILE = "config.txt";

	private static ArrayList<Station> StationList;
	private static int Stations;

	
	/**
	 * WorkLoad is the thread that will run until the WorkLoad for each station is done.
	 */
	class WorkLoad implements Runnable{
		Station ThisStation;
		Station PreviousStation;
		public WorkLoad(Station mStation){
			this.ThisStation = mStation;
			int pos = StationList.indexOf(mStation);
			if(pos != 0) PreviousStation = StationList.get(pos - 1);
			else PreviousStation = StationList.get(StationList.size()-1);
		}
		@Override
		public void run() {
			System.out.println("Station "+this.ThisStation.StationID+": In-Connection set to conveyor "+this.ThisStation.conveyor1);
			System.out.println("Station "+this.ThisStation.StationID+": Out-Connection set to conveyor "+this.ThisStation.conveyor2);
			System.out.println("Station "+this.ThisStation.StationID+": Workload set to "+this.ThisStation.WorkLoad);

			writeFile("Station "+this.ThisStation.StationID+": In-Connection set to conveyor "+this.ThisStation.conveyor1);
			writeFile("Station "+this.ThisStation.StationID+": Out-Connection set to conveyor "+this.ThisStation.conveyor2);
			writeFile("Station "+this.ThisStation.StationID+": Workload set to "+this.ThisStation.WorkLoad);
			
			while(ThisStation.WorkLoad != 0){
				Random random = new Random();
				try {
                    Thread.sleep(random.nextInt(10));
                } catch (InterruptedException e) {}
				ThisStation.DoWork(PreviousStation);
			}
			if(ThisStation.WorkLoad == 0){
				System.out.println("Station "+this.ThisStation.StationID+": Workload successfully completed.");
				writeFile("Station "+this.ThisStation.StationID+": Workload successfully completed.");
			}
			
		}
		
	}
	/**
	 * Reads in the config file to the Station object.
	 */
	private static void readFile(String CONFIG_FILE){
		FileReader mFile = null;
		BufferedReader mReader = null;
		try {
			mFile = new FileReader(CONFIG_FILE);
			mReader = new BufferedReader(mFile);
			String mLine;
			Stations = Integer.valueOf(mReader.readLine());;
			int i = 0;
			int prevPipe;
			while((mLine = mReader.readLine()) != null){
				if(i == 0) prevPipe = Stations - 1;
				else prevPipe = i - 1;
				Station mStation = new Station(i,prevPipe,Integer.valueOf(mLine));
				StationList.add(mStation);
				++i;
			}
			
		} catch (FileNotFoundException e) {
			System.out.println("Could not open file for reading!");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Could not read from file"+CONFIG_FILE+"!");
			e.printStackTrace();
		}finally{
			try {
				mFile.close();
				mReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static void writeFile(String mString){
		String FILE = "output.txt";
		try {
			FileWriter mFileWriter = new FileWriter(FILE,true);
			BufferedWriter mBufferedWriter = new BufferedWriter(mFileWriter);
			mBufferedWriter.write(mString);
			mBufferedWriter.newLine();
			mBufferedWriter.close();
			mFileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Starts the all of the threads
	 */
	public static void main(String[] args){
                String newLine = System.getProperty("line.separator");
		System.out.println(newLine + "* * * SIMULATION BEGINS * * *" + newLine + newLine + "* * * SIMULATION ENDS * * *" + newLine);
		StationList = new ArrayList<Station>();
		readFile(CONFIG_FILE);
		for(Station mS: StationList){
			new Thread(new Conv().new WorkLoad(mS)).start();
			
		}
	}

}
