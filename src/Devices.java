public abstract class Devices {
    private String name;
    private int id;
    private int u;
    private int i;
    private boolean isOn;
    private String category;
    protected SmartHome smartHome;

    public Devices(String name, int id, int u, int i, String category, SmartHome smartHome) {
        this.name = name;
        this.id = id;
        this.u = u;
        this.i = i;
        this.category = category;
        this.isOn = false;
        this.smartHome = smartHome;
    }

    public void turnOn() {
        this.isOn = true;
        System.out.println(name + " (#" + id + ") bekapcsolva.");
    }

    public void turnOff() {
        this.isOn = false;
        System.out.println(name + " (#" + id + ") kikapcsolva.");
    }

    public abstract void work();

    public boolean isOn() { return isOn; }
    public String getName() { return name; }
    public int getId() { return id; }
    public String getCategory() { return category; }

    public double getPowerConsumption() {
        return isOn ? (u * i) : 0.0;
    }
}