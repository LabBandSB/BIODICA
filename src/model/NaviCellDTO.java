package model;

public class NaviCellDTO {
	
	private String defaultWorkFolderPath;
	private String dataTablePath;
	private String STablePath;
	private String NaviCellMapURL;
	private float thresholdTopContributing;

	public static String navicellMapURLs[] = {
			"https://navicell.vincent-noel.fr/maps/1548847c-db0f-44ca-b0b5-04e625044501/master/index.html",
			"https://acsn.curie.fr/navicell/maps/acsn2/master/index.html",
			"https://acsn.curie.fr/navicell/maps/cellcycle_dnarepair/master/index.html",
			"https://acsn.curie.fr/navicell/maps/emt_senescence/master/index.html",
			"https://acsn.curie.fr/navicell/maps/rcd/master/index.html",			
			"https://acsn.curie.fr/navicell/maps/survival/master/index.html",
			"https://acsn.curie.fr/navicell/maps/telomere_maintenance/master/index.html",
			"https://acsn.curie.fr/navicell/maps/invasion_motility/master/index.html",
			"https://acsn.curie.fr/navicell/maps/adaptive_immune/master/index.html",
			"https://acsn.curie.fr/navicell/maps/angiogenesis/master/index.html",
			"https://acsn.curie.fr/navicell/maps/caf_cell/master/index.html",
			"https://acsn.curie.fr/navicell/maps/innate_immune/master/index.html",
			"https://acsn.curie.fr/navicell/maps/recon2/master/index.html"
			//"http://navicell.curie.fr/navicell/newtest/maps/innateimmune/master/index.html",
			//"http://navicell.curie.fr/navicell/newtest/maps/dendritic/master/index.html",
			//"http://navicell.curie.fr/navicell/newtest/maps/macrophage/master/index.html",
			//"http://navicell.curie.fr/navicell/newtest/maps/natkiller/master/index.html",
			//"http://navicell.curie.fr/navicell/newtest/maps/caf/master/index.html"
			//"https://navicell.curie.fr/navicell/maps/alzheimer/master/index.html",
			//"https://navicell.curie.fr/navicell/newtest/maps/e2f1/master/index.html",
			//"https://navicell.curie.fr/navicell/newtest/maps/flobak/master/index.html"
	};
		
	
	public void setDataTablePath(String dataTablePath) 
	{
	        this.dataTablePath = dataTablePath;
    }
	
	public String getDataTablePath()
	{
		return dataTablePath;
	}
	
	
	public void setDefaultWorkFolderPath(String defaultWorkFolderPath) 
	{
	        this.defaultWorkFolderPath = defaultWorkFolderPath;
    }
	
	public String getDefaultWorkFolderPath()
	{
		return defaultWorkFolderPath;
	}
	
	
	public String getSTablePath() {
		return STablePath;
	}

	public void setSTablePath(String sTablePath) {
		STablePath = sTablePath;
	}

	public String getNaviCellMapURL() {
		return NaviCellMapURL;
	}

	public void setNaviCellMapURL(String naviCellMapURL) {
		NaviCellMapURL = naviCellMapURL;
	}

	public float getThresholdTopContributing() {
		return thresholdTopContributing;
	}

	public void setThresholdTopContributing(float thresholdTopContributing) {
		this.thresholdTopContributing = thresholdTopContributing;
	}
	
}
