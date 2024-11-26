//class that represents the slot machine, holding their jackpot odds and multipliers
public class Machine {
	private String name;
	private int jackpotChance;
	private int jackpotMultiplier;

	//constructor to initialize machine
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
