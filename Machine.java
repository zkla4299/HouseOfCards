
public class Machine {
	private String name;
	private int jackpotChance;
	private int jackpotMultiplier;

	public Machine(String name, int jackpotChance, int jackpotMultiplier) {
		super();
		this.name = name;
		this.jackpotChance = jackpotChance;
		this.jackpotMultiplier = jackpotMultiplier;
	}
	

	public String getName() {
		return name;
	}


	public int getJackpotChance() {
		return jackpotChance;
	}


	public int getJackpotMultiplier() {
		return jackpotMultiplier;
	}


}
