import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Collections;
import java.util.Arrays;

public class Peaklass {
    public static void main(String[] args) {

        Pank SEB = new Pank("SEB", 0.16, 0.38);
        Pank Swedbank = new Pank("Swedbank", 0, 0.38);
        Pank LHV = new Pank("LHV", 0, 0);

        Isik i1 = new Isik("Joosep", 10.54, "EE123433545234", SEB);
        Isik i2 = new Isik("Klaus", 25.33, "EE123433545234", SEB);
        Isik i3 = new Isik("Sten", 4, "EE123433545234", Swedbank);
        Isik i4 = new Isik("Joss", 14.7, "EE123433545234", SEB);
        Isik i5 = new Isik("Jose", 44.89, "EE123433545234", Swedbank);
        Isik i6 = new Isik("Karl", 35.32, "EE125543666643", LHV);
        Isik i7 = new Isik("Miq", 7.2, "EE56756473456", LHV);

        Isik[] isikud = {i1, i7, i2, i3, i4, i5, i6};

        System.out.println(odavaimTeenustasu(isikud));

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

        for (int i = 0; i < kulutused.length; i++) {
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

    public List<Võlg> üksMaksabKõik(List<Isik> isikud) {
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

    public static double teenusTasu(List<Võlg> võlad){
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

    public static List<Võlg> odavaimTeenustasu(Isik[] isikud) {
        List<Võlg> võlad = arvelda(isikud);
        double odavaim = teenusTasu(võlad);
        System.out.println(odavaim);
        System.out.println(võlad);
        List<Võlg> odavaimVõlg = võlad;      //Odavaim võlgade list

        for (int i = 0; i < 100000; i++) {
            Collections.shuffle(Arrays.asList(isikud));
            võlad = arvelda(isikud);
            if (teenusTasu(võlad) < odavaim)
                odavaim = teenusTasu(võlad);
                odavaimVõlg = võlad;
        }
        System.out.println(odavaim);
        return odavaimVõlg;
    }
}
