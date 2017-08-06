package branch;

public class Branch
{
	private String locateName;
	private int id;
	private int pid;
	private String message;
	private int statues;
	private String branchids;
	private int disable;
	private int TopYardMust;

	public int getDisable()
	{
		return this.disable;
	}

	public void setDisable(int disable)
	{
		this.disable = disable;
	}

	public String getBranchids()
	{
		return this.branchids;
	}

	public void setBranchids(String branchids)
	{
		this.branchids = branchids;
	}

	public int getStatues()
	{
		return this.statues;
	}

	public void setStatues(int statues)
	{
		this.statues = statues;
	}

	public int getId()
	{
		return this.id;
	}

	public int getPid()
	{
		return this.pid;
	}

	public void setPid(int pid)
	{
		this.pid = pid;
	}

	public String getMessage()
	{
		return this.message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getLocateName()
	{
		return this.locateName;
	}

	public void setLocateName(String locateName)
	{
		this.locateName = locateName;
	}

	public int getTopYardMust()
	{
		return this.TopYardMust;
	}

	public void setTopYardMust(int topYardMust)
	{
		this.TopYardMust = topYardMust;
	}
}
