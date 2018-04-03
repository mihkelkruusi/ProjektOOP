public class Isik {

    private String nimi;
    private double kulutas;
    private String kontoNr;
    private Pank pank;

    public Isik(String nimi, double kulutas, String kontoNr, Pank pank) {
        this.nimi = nimi;
        this.kulutas = kulutas;
        this.kontoNr = kontoNr;
        this.pank = pank;
    }

    public String getNimi() {
        return nimi;
    }

    public double getKulutas() {
        return kulutas;
    }

    public String getKontoNr() {
        return kontoNr;
    }

    public Pank getPank() {
        return pank;
    }
}
