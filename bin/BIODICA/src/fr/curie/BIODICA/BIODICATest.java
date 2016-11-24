package fr.curie.BIODICA;

import vdaoengine.data.VDataTable;
import vdaoengine.data.io.VDatReadWrite;
import vdaoengine.utils.VSimpleProcedures;

public class BIODICATest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
			
			VDataTable vt = VDatReadWrite.LoadFromSimpleDatFile("C:/Datas/BODICA/data/test1/transcriptome/BRCA.txt", true, "\t");
			VSimpleProcedures.findAllNumericalColumns(vt);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
