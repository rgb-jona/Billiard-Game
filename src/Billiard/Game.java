package Billiard;

public class Game {
	
	public static void main(String[] args) {
		WindowController controller = new WindowController();
		SoundEngine soundEngine = new SoundEngine();
		StandardGame billiard = new StandardGame(controller, soundEngine);
		Menue mainMenue = new Menue(soundEngine, controller);
		controller.addMainMenue(mainMenue);
		controller.addGame(billiard);
		controller.inForeground("main");
	}
}
