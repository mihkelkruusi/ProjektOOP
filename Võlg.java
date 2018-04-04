public class Võlg {
    private double summa;
    private Isik kellelt;
    private Isik kellele;

    public Võlg(double summa, Isik kellelt, Isik kellele) {
        this.summa = summa;
        this.kellelt = kellelt;
        this.kellele = kellele;
    }

    public double getSumma() {
        return summa;
    }

    public Isik getKellelt() {
        return kellelt;
    }

    public Isik getKellele() {
        return kellele;
    }

    @Override
    public String toString() {
        return kellelt.getNimi() + " -> " + kellele.getNimi() + " " + summa;
    }
}
