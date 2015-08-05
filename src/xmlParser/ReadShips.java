//reads SCAF data files, converts from SH4 format to XML to be used
//by SubSkipper

package xmlParser;
import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.regex.*;

public class ReadShips {

/*
An Example SCAF ship record. starts with empty line, ends with 2 trailing empty lines

[Unit] -- Detect this and start reading
ClassName=COKaibokan2
3DModelFileName=data/Sea/COKaibokan/COKaibokan -- not used
UnitType=1 -- Not Used
MaxSpeed=19 
Length=89.5
Width=11.1
Mast=27.7
Draft=3.4  -- ALL WE NEED IS 7 LINES FROM THE RECORD
Displacement=900 --Not used
RenownAwarded=120 --Not used
CrewComplement=30 --Not used
SurvivalRate=70 --Not used
SurvivalPercentage=20 --Not used
*/
	//Luckily, this data is all in metric. 
	
	
	   
	private String tempShips[] = new String[8]; //Array of useful strings copied from SCAF file.
	//later, a Ship object is constructed from this data.
	
	
	private String namesPath = "shipData/Roster/Names.cfg"; //path to Names file
	
	
	//read from the file and construct a single Ship object.
	public void readShipRecord(String file){

		 FileInputStream fs = null;
		 try{ 
			 fs= new FileInputStream(file);
			 BufferedReader br = new BufferedReader(new InputStreamReader(fs));
			 String curLine = "";
			 int append = 0; // incremented when adding to tempShips array so we don't add nulls.
			 while(br.ready()){ 
				 
				 curLine = br.readLine().trim();
				 if(curLine.contains("[Unit]")){//checks if the record is valid
					 curLine = br.readLine().trim(); //move to next line after check	 
				 }
				 
				 //Skip this line as it is unused.
				 if(curLine.contains("3DModel")){
					 curLine = br.readLine().trim();
				 }
				 
				 //We extracted all the useful records.
				 if(curLine.contains("Renown")){
					 br.close();
					 break;
				 }
				 
				 //start saving to temp array of lines. Each array creates one ship
				 //Skip any blank lines.
				 else if(!(curLine.equals(null) || curLine.equals("") || curLine==null)){
					 
					 tempShips[append] = curLine;
					 append ++;
				 }
			 }
			
		 }
		 catch(FileNotFoundException F){
			 System.out.println("IOexception while reading.");}
		 
		 catch (IOException e){
             e.printStackTrace();
             System.out.println("could not read file.");
         }   
		
		printTempShips(); //print results.
		stripVars(); //run method for stripping SCAF artefacts.
		nameLookup("bigasstitties"); //find the name for this ship
		printTempShips(); //print results.
	}
	
	//methods for formatting public array tempShips into format suitable for Ship.class
	//after that construct an instance of the ship, to be later parsed to XML.
	//takes no arguments
	public void stripVars(){ //Strips incompatible data from array.
		String curTempShip = "";
		for(int i = 0; i<tempShips.length;i++){ //go through all array cells.
			
			if(tempShips[i]==null){
				break;
			}
			
			else{
				curTempShip = tempShips[i];
				curTempShip = curTempShip.substring(curTempShip.indexOf("=")+1, curTempShip.length());
				tempShips[i] = curTempShip;
			}
		}
	}
	
	public void printTempShips(){
		 //Print tempShips to make sure there's no mistake.
		 for(int j = 0; j<tempShips.length; j++){
			 if(j==0){System.out.println("\n\n Starting tempShips dump:");}
			 System.out.println(j + " " + tempShips[j]);
		 }
	}
	
	//this method looks up a query from names.cfg using a linear line-by-line search.
	//takes the short className from the Ship file as its input, and returns a stripped ship name.
	public void nameLookup(String query){
		
		System.out.println("Query: " + query);
		boolean found = false;
		String curLine = "";
		String name = "ReadShips.nameLookup() failed";
		FileInputStream fs = null;
				 
		 try{ 
			 fs= new FileInputStream(namesPath);
			 BufferedReader br = new BufferedReader(new InputStreamReader(fs));
			 
			 while(!found){
				 if (! br.ready()){
					 System.out.println("Reached Names.cfg EOF. Breaking.");
					 break;}

				 curLine = br.readLine().trim();
				 
				 if(curLine.contains(query)){ //we found the name, change tempShips[0]
						curLine = curLine.substring(curLine.indexOf("=")+1, curLine.length());
						name = curLine;
						br.close();
						found = true;
				 }
			 }
		 }
		 catch(FileNotFoundException F){
			 System.out.println("IOexception while reading.");}
		 
		 catch (IOException e){
             e.printStackTrace();
             System.out.println("could not read file.");
         }   
		 
		 if(found){
			 tempShips[0] = name;
		 }
		 else{tempShips[0] = tempShips[0] + " | ERROR: \"" +query +"\" not found in ReadShips.nameLookup().";}
		 //this ought to show up in XML if nameLookup fails, and make debugging easier
 
	}

	
}//EOF

