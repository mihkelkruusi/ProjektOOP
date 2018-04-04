import java.util.*;

public class Peaklass {
    public static void main(String[] args) {

        List<Isik> isikud = new ArrayList<>();

        Pank SEB = new Pank("SEB", 0.16, 0.38);    //Loome panga isendid
        Pank Swedbank = new Pank("Swedbank", 0, 0.38);
        Pank LHV = new Pank("LHV", 0, 0);
        Pank Luminor = new Pank("Luminor", 0, 0.38);
        Pank[] pangad = {SEB, Swedbank, LHV, Luminor};

        Scanner sc = new Scanner(System.in);    //Kasutajaga suhtlus, kasutaja sisestab andmed
        System.out.println("Programm jagab ühised kulutused");
        System.out.println("Vali pank järgnevast pankade loetelust: (Swedbank, SEB, LHV, Luminor)");
        System.out.println("Sisesta osalejate arv");
        int osalejateArv = Integer.parseInt(sc.nextLine());
        while (isikud.size() != osalejateArv) {    //Isiku andmete küsimine ja isiku isendite loomine kuni kõik andmed on korrektselt sisestatud
            int i = isikud.size();
            System.out.println(i+1 + ". osaleja andmed (kujul: nimi, kulu, kontonumber, panga nimi)");
            String andmed = sc.nextLine();
            String[] tükid = andmed.split(",");

            if(tükid.length != 4) {                     //kontrollib, kas sisestati vajalik arv andmeid
                System.out.println("Vale sisend");
                continue;
            }
            for (int j = 0; j < tükid.length; j++) {
                tükid[j] = tükid[j].trim(); // eemaldab tühikud stringi eest ja tagant
            }

            List<String> panganimed = new ArrayList<>();
            for (Pank pank : pangad) {
                panganimed.add(pank.getNimi());
            }
            if (!panganimed.contains(tükid[3])){         //kontrollib kas panga nimi on õigesti sisestatud, kui mitte, küsib osaleja andmed uuesti
                System.out.println("Panga nimi on valesti sisestatud");
                continue;
            }
            Pank osalejaPank = Swedbank;
            for (Pank pank : pangad) {
                if(pank.getNimi().equals(tükid[3]))
                    osalejaPank = pank;
            }
            try {
                isikud.add(new Isik(tükid[0], Double.parseDouble(tükid[1]), tükid[2], osalejaPank));
            }
            catch(Exception e){
                System.out.println("Vigane sisend");
            }
        }

        Isik[] osalejad = new Isik[isikud.size()];
        osalejad = isikud.toArray(osalejad);

        System.out.println("Kas jaotan kulud võrdselt (1) või loosin ühe õnneliku, kes katab kõik kulud (2)? (Vali valiku number (1/2))");
        int vastus;
        try {    //Kulud võrdselt/random
            vastus = Integer.parseInt(sc.nextLine());
        }
        catch (NumberFormatException e){
            System.out.println("Vale sisend, jaotan kulud võrdselt");
            vastus = 1;
        }

        List<Võlg> võlad = new ArrayList<>();
        if (vastus == 1) {    //Soovitud meetodi käivitamine
            võlad = odavaimTeenustasu(osalejad);
        } else {
            võlad = üksMaksabKõik(isikud);
        }

        List<Isik> võlaUsaldajad = new ArrayList<>();
        for (Võlg võlg : võlad) {    //Võlausaldajate list ja võlgade väljastamine
            if(!võlaUsaldajad.contains(võlg.getKellele()))
                võlaUsaldajad.add(võlg.getKellele());
            System.out.println(võlg);
        }
        for (Isik isik : võlaUsaldajad) {    //Kontonumbrite väljastamine
            System.out.println(isik.getNimi() + " " + isik.getKontoNr());
        }

    }



    public static List<Võlg> arvelda(Isik[] isikud) {

        double näkku = 0;
        double kanda;
        double kanna;
        int indeks;
        int n = isikud.length;
        double[] kulutused = new double[n];
        List<Võlg> võlad = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            kulutused[i] = isikud[i].getKulutas();
            näkku += isikud[i].getKulutas() / n;
        }
        näkku = Math.round(näkku * 100.0) / 100.0;

        for (int i = 0; i < kulutused.length; i++) {     //Kulutuste võrdne jagamine ja võla isendite loomine
            if (kulutused[i] < näkku) {
                kanda = näkku - kulutused[i];
                indeks = suurimIndeks(kulutused);
                if (kulutused[indeks] - näkku < kanda) {
                    while (kanda > 0.01) {
                        indeks = suurimIndeks(kulutused);
                        kanna = kulutused[indeks] - näkku;
                        if (kanna > kanda) {
                            kanna = kanda;
                        }
                        kanda = kanda - kanna;
                        kulutused[i] = kulutused[i] + kanna;
                        kulutused[indeks] = kulutused[indeks] - kanna;
                        kanna = Math.round(kanna * 100.0)/100.0;
                        võlad.add(new Võlg(kanna, isikud[i], isikud[indeks]));
                    }
                }
                else {
                    kulutused[i] = näkku;
                    kulutused[indeks] = kulutused[indeks] - kanda;
                    kanda = Math.round(kanda * 100.0)/100.0;
                    võlad.add(new Võlg(kanda, isikud[i], isikud[indeks]));
                }
            }
        }
        return võlad;
    }


    public static int suurimIndeks(double[] arr) {

        double suurim = 0;
        int indeks = 0;
        for(int i = 0; i < arr.length; i++) {
            if(arr[i] > suurim) {
                suurim = arr[i];
                indeks = i;
            }
        }
        return indeks;
    }

    public static List<Võlg> üksMaksabKõik(List<Isik> isikud) {    //Leiame randomiga isiku, kes kõik kinni maksab
        List<Võlg> võlad = new ArrayList();
        Random rand = new Random();
        int N = rand.nextInt(isikud.size());
        double summa = 0;
        for (Isik isik : isikud) {
            summa = isik.getKulutas();
            if(isik.getKulutas() != 0 && isik != isikud.get(N)){
                võlad.add(new Võlg(summa, isikud.get(N), isik));
            }
        }
        return võlad;
    }

    public static double teenusTasu(List<Võlg> võlad){    //Teenustasude summa leidmine
        double teenusTasu = 0;
        for (Võlg võlg : võlad) {
            Pank kellelt = võlg.getKellelt().getPank();
            Pank kellele = võlg.getKellele().getPank();
            if(kellelt.equals(kellele))
                teenusTasu += kellelt.getTasuSisene();
            else
                teenusTasu += kellelt.getTasuVäline();
        }
        return teenusTasu;
    }

    public static List<Võlg> odavaimTeenustasu(Isik[] isikud) {    //Odavaima teenustasu leidmine
        List<Võlg> võlad = arvelda(isikud);
        double odavaim = teenusTasu(võlad);
        List<Võlg> odavaimVõlg = võlad;      //Odavaim võlgade list

        for (int i = 0; i < 10000; i++) {
            Collections.shuffle(Arrays.asList(isikud));
            võlad = arvelda(isikud);
            if (teenusTasu(võlad) < odavaim)
                odavaim = teenusTasu(võlad);
            odavaimVõlg = võlad;
        }
        return odavaimVõlg;
    }
}
