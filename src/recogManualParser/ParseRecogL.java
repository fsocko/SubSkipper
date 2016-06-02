package recogManualParser;

import java.io.IOException;

import com.x5.template.Chunk;
import com.x5.template.Theme;

import coreLogic.FileIO;
import coreLogic.OutFormat;
import coreLogic.Ship;
import xmlParser.PrepShipData;
import xmlParser.Ships;

//Parse data from XML parser which parses SCAF data, into long form HTML recognition manual.

public class ParseRecogL {

	public static void main(String[] args) throws IOException {
		
		PrepShipData target = new PrepShipData();
		writeRecogLHTML(target.sortShipsName(target.getShips()), "recog/recogHTML/recogLSN.html", false);
		writeRecogLHTML(target.sortShipsType(target.getShips()), "recog/recogHTML/recogLST.html", false);
	}
		
		//takes Ship list Ships, takes filename of doc. - Long recog manual
		//with images and data in long format, styled with CSS.
		//boolean imperial turns relevant units to "wrong units"
		public static void writeRecogLHTML(Ships shipList, String filename, boolean imperial){
						
			//<head>
			String htmlDoc = HTMLStartL();
			
			//Main Ship HTML
			for(int i = 0; i < shipList.getShips().size(); i++)
			{
				htmlDoc += HTMLShipL(shipList.getShip(i), imperial);
			}
			//Close remaining tags
			htmlDoc += HTMLEndL();
			writeHTML(htmlDoc, filename);
			
			System.out.println("HTML Written.");
		}
		
		private static String HTMLStartL(){
			
			Theme theme = new Theme();
			Chunk h = theme.makeChunk("recogL2#start"); //Chunk used to write to HTML: mostly <head>.
			h.set("title", "Recognition Manual (SH4, TMO, SCAF)." ); //Altering the title tag.
			//System.out.println(h.toString()); //Temporarily outputs to console. Will make it send to file.
			return h.toString();
		}
		
		private static String HTMLShipL(Ship record, boolean imperial){
			
			String unit = "m";
			if(imperial){
				unit = "ft";
				record.makeImperial();
				
			}
			
			OutFormat f = new OutFormat();
			Theme theme = new Theme();	
			Chunk h = theme.makeChunk("recogL2#ship");
			h.set("flag", "flag"); //TODO: figure out how ships are sorted, assign flags. Maybe typeInt?
			h.set("name", record.getName());
			h.set("class", record.getTypeName());
			h.set("speed", f.addUnit(record.getMaxSpeed(), "kn"));
			h.set("length", f.addUnit(record.getLength(), unit));
			h.set("height", f.addUnit(record.getMast(), unit));
			h.set("draft", f.addUnit(record.getDraft(), unit));
			h.set("disp", record.getDisp() + " GRT");
			h.set("aspect", f.fourDP(record.getRefAspect()));
			
			//Convert image path to filename
			String pngPath = record.getImagePath();
			pngPath = pngPath.substring((pngPath.lastIndexOf("\\") + 1), pngPath.lastIndexOf("."));
			String figPath = "../figures/" + pngPath + ".png";	
			h.set("image", figPath);
			return h.toString(); //Temporarily outputs to console. Will make it send to file.	

		}
		
		private static String HTMLEndL(){
			Theme theme = new Theme();	
			Chunk h = theme.makeChunk("recogL2#terminate");
			return h.toString();
		}

		private static void writeHTML(String input, String path){
			
			FileIO htmlW = new FileIO();
			htmlW.writeLine(path, input);
		}
	}
	