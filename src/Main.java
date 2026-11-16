import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Ház létrehozása max 10 eszközzel
        SmartHome myHome = new SmartHome(21.0, 45.0, 70, 10);

        // Kezdeti eszközök felvétele
        Devices thermostat = new SmartThermostat("Nappali Termosztát", 1, 230, 2, "Nappali", myHome, 22.5);
        Devices purifier = new SmartAirpurifier("Háló Légtisztító", 2, 230, 1, "Hálószoba", myHome);
        Devices humidifier = new SmartAirhumidifier("Párásító", 3, 230, 1, "Gyerekszoba", myHome);
        Devices lamp = new SmartLamp("Mennyezeti Lámpa", 4, 230, 1, "Nappali", myHome);

        myHome.addDevice(thermostat);
        myHome.addDevice(purifier);
        myHome.addDevice(humidifier);
        myHome.addDevice(lamp);

        boolean running = true;
        int nextId = 5;

        while (running) {
            System.out.println("\n==================================================");
            System.out.println("          🏠 OKOSOTTHON KONZOL VEZÉRLŐ           ");
            System.out.println("==================================================");
            System.out.printf(" Hőmérséklet: %.1f °C | Páratartalom: %.1f%% | Lev.min: %d\n",
                    myHome.getTemperature(), myHome.getHumidity(), myHome.getAirQuality());
            System.out.printf(" ⚡ Összesített pillanatnyi fogyasztás: %.1f W\n", myHome.getTotalPowerConsumption());
            System.out.println("--------------------------------------------------");
            System.out.println("1. Eszközök listázása (ID, csoport, státusz, eco)");
            System.out.println("2. Eszköz bekapcsolása (ID alapján)");
            System.out.println("3. Eszköz kikapcsolása (ID alapján)");
            System.out.println("4. Eco mód váltása (kompatibilis eszközön ID alapján)");
            System.out.println("5. Eszköz működtetése (work() hívás ID alapján)");
            System.out.println("6. Új eszköz hozzáadása");
            System.out.println("7. Minden bekapcsolt eszköz működtetése egyszerre");
            System.out.println("0. Kilépés");
            System.out.print("Válassz egy opciót: ");

            int choice = -1;
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
            } else {
                scanner.next();
                System.out.println("Érvénytelen választás!");
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.println("\n--- Jelenlegi eszközök a házban ---");
                    for (int i = 0; i < myHome.getDeviceCount(); i++) {
                        Devices d = myHome.getDevices()[i];
                        if (d != null) {
                            String ecoStr = "";
                            if (d instanceof EcoModeable) {
                                ecoStr = " | Eco: " + (((EcoModeable) d).isEcoModeActive() ? "BE" : "KI");
                            }
                            System.out.printf("- ID [%d] %s (%s) | Állapot: %s%s | Fogyasztás: %.1fW\n",
                                    d.getId(), d.getName(), d.getCategory(), (d.isOn() ? "BE" : "KI"), ecoStr, d.getPowerConsumption());
                        }
                    }
                    break;
                case 2:
                    System.out.print("Add meg a bekapcsolandó eszköz ID-jét: ");
                    int onId = scanner.nextInt();
                    Devices devOn = myHome.findDeviceById(onId);
                    if (devOn != null) {
                        devOn.turnOn();
                    } else {
                        System.out.println("Nincs ilyen ID-jű eszköz!");
                    }
                    break;
                case 3:
                    System.out.print("Add meg a kikapcsolandó eszköz ID-jét: ");
                    int offId = scanner.nextInt();
                    Devices devOff = myHome.findDeviceById(offId);
                    if (devOff != null) {
                        devOff.turnOff();
                    } else {
                        System.out.println("Nincs ilyen ID-jű eszköz!");
                    }
                    break;
                case 4:
                    System.out.print("Add meg az Eco mód váltáshoz az eszköz ID-jét: ");
                    int ecoId = scanner.nextInt();
                    Devices devEco = myHome.findDeviceById(ecoId);
                    if (devEco != null) {
                        if (devEco instanceof EcoModeable) {
                            EcoModeable ecoDev = (EcoModeable) devEco;
                            if (ecoDev.isEcoModeActive()) {
                                ecoDev.disableEcoMode();
                            } else {
                                ecoDev.enableEcoMode();
                            }
                        } else {
                            System.out.println("Ez az eszköz nem támogatja az Eco módot!");
                        }
                    } else {
                        System.out.println("Nincs ilyen ID-jű eszköz!");
                    }
                    break;
                case 5:
                    System.out.print("Add meg a működtetendő eszköz ID-jét: ");
                    int workId = scanner.nextInt();
                    Devices devWork = myHome.findDeviceById(workId);
                    if (devWork != null) {
                        devWork.work();
                    } else {
                        System.out.println("Nincs ilyen ID-jű eszköz!");
                    }
                    break;
                case 6:
                    scanner.nextLine(); // Puffer kiürítése (ez így önmagában egy helyes utasítás)
                    System.out.print("Add meg az új eszköz nevét: ");
                    String name = scanner.nextLine();
                    System.out.print("Add meg a szobát / csoportot: ");
                    String category = scanner.nextLine();
                    System.out.println("Válassz típust (1: Lámpa, 2: Termosztát, 3: Légtisztító): ");
                    int type = scanner.nextInt();

                    Devices newDev;
                    int assignedId = nextId++;
                    if (type == 2) {
                        newDev = new SmartThermostat(name, assignedId, 230, 2, category, myHome, 22.0);
                    } else if (type == 3) {
                        newDev = new SmartAirpurifier(name, assignedId, 230, 1, category, myHome);
                    } else {
                        newDev = new SmartLamp(name, assignedId, 230, 1, category, myHome);
                    }

                    myHome.addDevice(newDev);
                    System.out.println("Eszköz sikeresen hozzáadva ID: " + assignedId);
                    break;
                case 7:
                    System.out.println("\n-> Az összes bekapcsolt eszköz működik...");
                    myHome.operateAll();
                    break;
                case 0:
                    running = false;
                    System.out.println("Kilépés...");
                    break;
                default:
                    System.out.println("Ismeretlen parancs!");
            }
        }
        scanner.close();
    }
}