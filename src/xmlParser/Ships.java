package xmlParser;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import coreLogic.Ship;
 

//Wrapper for martialing XML correctly. Stores Ship object as a list of Ships
@XmlRootElement(name = "ships")
public class Ships
{
    ArrayList<Ship> ships;
 
    @XmlElement( name = "ship" )
    public void setShips( ArrayList<Ship> ships)
    {
    	this.ships = ships;
    }
    
    //return this for array list of ships
	public ArrayList<Ship> getShips() {
		return ships;
	}
    
	//return a single ship at position i in ships ArrList
	public Ship getShip(int i){
		ArrayList<Ship> multi = this.getShips();
		Ship single = multi.get(i);
		return single;
	}

}

