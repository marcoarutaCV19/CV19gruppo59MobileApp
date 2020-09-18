package com.example.consigliaviaggi19.Controllers;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**Questo programma usufruisce di Firebase db avente funzioni esterne già testate e implementate
 * pertanto questo testing è a scopo illustrativo e non è ne compilabile ne eseguibile
 *BLACK-BOX TESTING:
 * Usufrendo del parametro uid si distinguono 2 classi di equivalenza:
 * Lo user che è iscritto realmente all'applicazione CV19(assertTrue)
 * Lo user che non è iscritto all'applicazione CV19 (assertFalse)
 * Identificando la struttura con un parametro è possibile fare accesso al database e sviluppare
 * conseguentemente 2 testing cases:
 * utenteIsritto();
 * utenteNonIscritto();
 * WHITE-BOX TESTING:
 * Analizzando il flow chart visibile all'interno della documentazione si sono
 * sviluppati testing cases per node coverage e branch coverage chiamati rispettivamente:
 * WhiteBoxTestingPath_23_456_78_101112_15()
 * WhiteBoxPathTesting_1_15()
 */
public class JUnitLoginTest {
    ControllerSignup Controller;
    @Before
    public void setUp() throws Exception {
        Controller = new ControllerSignup(null);
    }
    /*PREMESSA: le email corrispondono a utenti reali e le password sono del tutto fittizie
    e non rappresentano dati reali sensibili se non per l'utente iscritto a cui è stata
    attribuita questa password per rendere possibile il testing*/
    @Test
    public void utenteIsritto() {
        assertFalse(Controller.createUser("arancino97.fdm@gmail.com", "pippobaudo", "Francesca", "Ragusa", "franci", '0'));
    }
    @Test
    public void utenteNonIscritto() {
        assertTrue(Controller.createUser("dirknowitzki41@virgilio.it", "asd123", "Rita", "Sera", "RSera", '0'));
    }


    //White Box

    @Test
    public void WhiteBoxTestingPath_23_456_78_101112_15() {
        assertTrue(Controller.createUser("dirknowitzki41@virgilio.it", "asd123", "Rita", "Sera", "RSera", '0'));
    }
    @Test
    public void WhiteBoxTestingPath_1_15() {
        assertTrue(Controller.createUser("", "", "", "", "", ' ' ));
    }
}
