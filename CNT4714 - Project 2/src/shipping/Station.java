/*
 * Name: Wenyang Wu
 * Course: CNT 4714 - Spring 2017
 * Assignment Title: Program 2 - Multi-Threaded Programming in Java
 * Date: Sunday February 12, 2016
 */
package shipping;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Station{
	
	private final Lock mPipeLock = new ReentrantLock();
	
	public int StationID;
	public int WorkLoad;
	public int conveyor1;
	public int conveyor2;
	
	/**
	 * Station constructor to define a stations number, the two conveyors it connects to and the workload.
	 */
	public Station(int StationID, int conveyor1, int WorkLoad){
		this.conveyor1 = conveyor1;
		this.conveyor2 = StationID;
		this.StationID = StationID;
		this.WorkLoad = WorkLoad;
		
		
	}
	
	/**
	 * This method checks the lock for the current station and the adjacent one to see if we can do work, return true/false if the locks can be locked
	 */
	public boolean LockStat(Station mAdjecentStation){
		boolean mPipeLockX = false;
		boolean mPipeLockY = false;
		try{
			if(mPipeLockX = this.mPipeLock.tryLock()) {
				System.out.println("Station "+StationID+": granted access to conveyor "+this.conveyor1);
				writeFile("Station "+StationID+": granted access to conveyor "+this.conveyor1);
			}
			if(mPipeLockY = mAdjecentStation.mPipeLock.tryLock()){
				System.out.println("Station "+StationID+": granted access to conveyor "+this.conveyor2);
				writeFile("Station "+StationID+": granted access to conveyor "+this.conveyor2);
			}
		}finally{
			if(!(mPipeLockX && mPipeLockY)){
				if(mPipeLockX){
					this.mPipeLock.unlock();
					System.out.println("Station "+StationID+": released access to conveyor "+this.conveyor1);
					writeFile("Station "+StationID+": released access to conveyor "+this.conveyor1);
				}
				if(mPipeLockY){
					mAdjecentStation.mPipeLock.unlock();
					System.out.println("Station "+StationID+": released access to conveyor "+this.conveyor2);
					writeFile("Station "+StationID+": released access to conveyor "+this.conveyor2);
				}
			}
		}
		return mPipeLockX && mPipeLockY;
	}
	
	/**
	 * Check the locks with the method defined above and if true 'does work' by sleeping for a random period of time and decrementing the workload.
	 */
	public void DoWork(Station mAdjecentStation){
		
		if(LockStat(mAdjecentStation)){
			try{
				System.out.println("Station "+StationID+": successfully moves package on conveyor "+WorkLoad);
				writeFile("Station "+StationID+": successfully moves package on conveyor "+WorkLoad);
				Random r = new Random();
				try {
					Thread.sleep(r.nextInt(10));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				WorkLoad -= 1;
			}finally{
				this.mPipeLock.unlock();
				mAdjecentStation.mPipeLock.unlock();
				System.out.println("Station "+StationID+": released access to conveyor "+this.conveyor1);
				System.out.println("Station "+StationID+": released access to conveyor "+this.conveyor2);
				writeFile("Station "+StationID+": released access to conveyor "+this.conveyor1);
				writeFile("Station "+StationID+": released access to conveyor "+this.conveyor2);
			}
			
		}else{
			//System.out.println("A station is currently using that conveyor!");
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
}
