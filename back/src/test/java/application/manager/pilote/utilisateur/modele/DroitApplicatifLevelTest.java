package application.manager.pilote.utilisateur.modele;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

class DroitApplicatifLevelTest {

	@Test
	void test() {
		Assert.assertTrue(DroitApplicatifLevel.valueOf("DEV").getLibelle().equals("Developpeur"));
	}

}
