public class Pank {

    private String nimi;
    private double tasuSisene;
    private double tasuVäline;

    public Pank(String nimi, double tasuSisene, double tasuVäline) {
        this.nimi = nimi;
        this.tasuSisene = tasuSisene;
        this.tasuVäline = tasuVäline;
    }

    public double getTasuSisene() {
        return tasuSisene;
    }

    public double getTasuVäline() {
        return tasuVäline;
    }
}
