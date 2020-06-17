package com.example.lkjhgf.publicTransport.provider.vrr.timeoptimisation;

import com.example.lkjhgf.publicTransport.provider.Farezone;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


public class VRR_Farezones {

    public static ArrayList<Farezone> createVRRFarezone(){
        ArrayList<Farezone> farezones = new ArrayList<>();

        farezones.add(new Farezone(1,"Kerken/Wachtendonk"));
        farezones.add(new Farezone(2,"Kamp-Lintfort"));
        farezones.add(new Farezone(3,"Wesel"));
        farezones.add(new Farezone(4,"Geldern/Issum"));
        farezones.add(new Farezone(5,"Dorsten"));
        farezones.add(new Farezone(6,"Haltern"));

        farezones.add(new Farezone(10,"Straelen"));
        farezones.add(new Farezone(11,"Neukirchen-Vluyn/Rheurdt"));
        farezones.add(new Farezone(12,"Rheinberg"));
        farezones.add(new Farezone(13,"Dinslaken/Voerde"));
        farezones.add(new Farezone(14,"Schermbeck/Hünxe"));
        farezones.add(new Farezone(15,"Marl"));
        farezones.add(new Farezone(16,"Alpen"));
        farezones.add(new Farezone(17,"Recklinghausen/Herten"));
        farezones.add(new Farezone(18,"Oer-Erkenschwick/Datteln"));

        farezones.add(new Farezone(20,"Nettetal/Brüggen"));
        farezones.add(new Farezone(21,"Kempen/Grefrath/Tönisvorst"));
        farezones.add(new Farezone(23,"Duisburg Nord"));
        farezones.add(new Farezone(24,"Oberhausen"));
        farezones.add(new Farezone(25,"Bottrop/Gladbeck"));
        farezones.add(new Farezone(26,"Gelsenkirchen"));
        farezones.add(new Farezone(22,"Moers"));
        farezones.add(new Farezone(27,"Herne"));
        farezones.add(new Farezone(28,"Castrop-Rauxel"));
        farezones.add(new Farezone(29,"Waltrop"));

        farezones.add(new Farezone(30,"Schwalmtal/Niederkrüchten"));
        farezones.add(new Farezone(31,"Viersen"));
        farezones.add(new Farezone(32,"Krefeld"));
        farezones.add(new Farezone(33,"Duisburg Mitte/Süd"));
        farezones.add(new Farezone(34,"Mühlheim/Ruhr"));
        farezones.add(new Farezone(35,"Essen Mitte/Nord"));
        farezones.add(new Farezone(36,"Bochum"));
        farezones.add(new Farezone(37,"Dortmund Mitte/West"));
        farezones.add(new Farezone(38,"Dortmund Ost"));

        farezones.add(new Farezone(41,"Willich"));
        farezones.add(new Farezone(42,"Meerbusch"));
        farezones.add(new Farezone(43,"Düsseldorf Mitte/Nord"));
        farezones.add(new Farezone(44,"Ratingen/Heiligenhaus"));
        farezones.add(new Farezone(45,"Essen Süd"));
        farezones.add(new Farezone(46,"Hattingen/Sprochhövel"));
        farezones.add(new Farezone(47,"Witten/Wetter/Herdecke"));

        farezones.add(new Farezone(50,"Mönchengladbach"));
        farezones.add(new Farezone(51,"Korschenbroich"));
        farezones.add(new Farezone(52,"Neuss/Kaarst"));
        farezones.add(new Farezone(53,"Düsseldorf Süd"));
        farezones.add(new Farezone(54,"Mettmann/Wülfrath"));
        farezones.add(new Farezone(55,"Velbert"));
        farezones.add(new Farezone(58,"Hagen"));

        farezones.add(new Farezone(61,"Grevenbroich"));
        farezones.add(new Farezone(62,"Dormagen"));
        farezones.add(new Farezone(63,"Rommerskirchen"));
        farezones.add(new Farezone(64,"Erkrath/Haan/Hilden"));
        farezones.add(new Farezone(65,"Wuppertal West"));
        farezones.add(new Farezone(66,"Wuppertal Ost"));
        farezones.add(new Farezone(67,"Schwelm/Ennepetal/Gevelsberg/Breckerfeld"));

        farezones.add(new Farezone(71,"Emmerich"));
        farezones.add(new Farezone(72,"Jüchen"));
        farezones.add(new Farezone(73,"Langenfeld/Monheim"));
        farezones.add(new Farezone(74,"Solingen"));
        farezones.add(new Farezone(75,"Remscheid"));
        farezones.add(new Farezone(77,"Uedem"));
        farezones.add(new Farezone(78,"Kalkar"));
        farezones.add(new Farezone(79,"Rees"));

        farezones.add(new Farezone(80,"Kleve"));
        farezones.add(new Farezone(81,"Kranenburg"));
        farezones.add(new Farezone(82,"Bedburg-Hau"));
        farezones.add(new Farezone(83,"Xanten"));
        farezones.add(new Farezone(84,"Sonsbeck"));
        farezones.add(new Farezone(85,"Kevelaer"));
        farezones.add(new Farezone(86,"Goch/Weeze"));
        farezones.add(new Farezone(88,"Hamminkeln"));

        return farezones;
    }

    public static Graph<FarezoneTrips, DefaultEdge> create_vrr_farezone_graph(){
        DefaultDirectedGraph<FarezoneTrips, DefaultEdge> g = new DefaultDirectedGraph(DefaultEdge.class);

        FarezoneTrips n1 = new FarezoneTrips(new Farezone(1,"Kerken/Wachtendonk"));
        g.addVertex(n1);
        FarezoneTrips n2 = new FarezoneTrips(new Farezone(2,"Kamp-Lintfort"));
        g.addVertex(n2);
        FarezoneTrips n3 = new FarezoneTrips(new Farezone(3,"Wesel"));
        g.addVertex(n3);
        FarezoneTrips n4 = new FarezoneTrips(new Farezone(4,"Geldern/Issum"));
        g.addVertex(n4);
        FarezoneTrips n5 = new FarezoneTrips(new Farezone(5,"Dorsten"));
        g.addVertex(n5);
        FarezoneTrips n6 = new FarezoneTrips(new Farezone(6,"Haltern"));
        g.addVertex(n6);
        FarezoneTrips n10 = new FarezoneTrips(new Farezone(10,"Straelen"));
        g.addVertex(n10);
        FarezoneTrips n11 = new FarezoneTrips(new Farezone(11,"Neukirchen-Vluyn/Rheurdt"));
        g.addVertex(n11);
        FarezoneTrips n12 = new FarezoneTrips(new Farezone(12,"Rheinberg"));
        g.addVertex(n12);
        FarezoneTrips n13 = new FarezoneTrips(new Farezone(13,"Dinslaken/Voerde"));
        g.addVertex(n13);
        FarezoneTrips n14 = new FarezoneTrips(new Farezone(14,"Schermbeck/Hünxe"));
        g.addVertex(n14);
        FarezoneTrips n15 = new FarezoneTrips(new Farezone(15,"Marl"));
        g.addVertex(n15);
        FarezoneTrips n16 = new FarezoneTrips(new Farezone(16,"Alpen"));
        g.addVertex(n16);
        FarezoneTrips n17 = new FarezoneTrips(new Farezone(17,"Recklinghausen/Herten"));
        g.addVertex(n17);
        FarezoneTrips n18 = new FarezoneTrips(new Farezone(18,"Oer-Erkenschwick/Datteln"));
        g.addVertex(n18);

        FarezoneTrips n20 = new FarezoneTrips(new Farezone(20,"Nettetal/Brüggen"));
        g.addVertex(n20);
        FarezoneTrips n21 = new FarezoneTrips(new Farezone(21,"Kempen/Grefrath/Tönisvorst"));
        g.addVertex(n21);
        FarezoneTrips n22 = new FarezoneTrips(new Farezone(22,"Moers"));
        g.addVertex(n22);
        FarezoneTrips n23 = new FarezoneTrips(new Farezone(23,"Duisburg Nord"));
        g.addVertex(n23);
        FarezoneTrips n24 = new FarezoneTrips(new Farezone(24,"Oberhausen"));
        g.addVertex(n24);
        FarezoneTrips n25 = new FarezoneTrips(new Farezone(25,"Bottrop/Gladbeck"));
        g.addVertex(n25);
        FarezoneTrips n26 = new FarezoneTrips(new Farezone(26,"Gelsenkirchen"));
        g.addVertex(n26);
        FarezoneTrips n27 = new FarezoneTrips(new Farezone(27,"Herne"));
        g.addVertex(n27);
        FarezoneTrips n28 = new FarezoneTrips(new Farezone(28,"Castrop-Rauxel"));
        g.addVertex(n28);
        FarezoneTrips n29 = new FarezoneTrips(new Farezone(29,"Waltrop"));
        g.addVertex(n29);
        FarezoneTrips n30 = new FarezoneTrips(new Farezone(30,"Schwalmtal/Niederkrüchten"));

        g.addVertex(n30);
        FarezoneTrips n31 = new FarezoneTrips(new Farezone(31,"Viersen"));
        g.addVertex(n31);
        FarezoneTrips n32 = new FarezoneTrips(new Farezone(32,"Krefeld"));
        g.addVertex(n32);
        FarezoneTrips n33 = new FarezoneTrips(new Farezone(33,"Duisburg Mitte/Süd"));
        g.addVertex(n33);
        FarezoneTrips n34 = new FarezoneTrips(new Farezone(34,"Mühlheim/Ruhr"));
        g.addVertex(n34);
        FarezoneTrips n35 = new FarezoneTrips(new Farezone(35,"Essen Mitte/Nord"));
        g.addVertex(n35);
        FarezoneTrips n36 = new FarezoneTrips(new Farezone(36,"Bochum"));
        g.addVertex(n36);
        FarezoneTrips n37 = new FarezoneTrips(new Farezone(37,"Dortmund Mitte/West"));
        g.addVertex(n37);
        FarezoneTrips n38 = new FarezoneTrips(new Farezone(38,"Dortmund Ost"));
        g.addVertex(n38);

        FarezoneTrips n41 = new FarezoneTrips(new Farezone(41,"Willich"));
        g.addVertex(n41);
        FarezoneTrips n42 = new FarezoneTrips(new Farezone(42,"Meerbusch"));
        g.addVertex(n42);
        FarezoneTrips n43 = new FarezoneTrips(new Farezone(43,"Düsseldorf Mitte/Nord"));
        g.addVertex(n43);
        FarezoneTrips n44 = new FarezoneTrips(new Farezone(44,"Ratingen/Heiligenhaus"));
        g.addVertex(n44);
        FarezoneTrips n45 = new FarezoneTrips(new Farezone(45,"Essen Süd"));
        g.addVertex(n45);
        FarezoneTrips n46 = new FarezoneTrips(new Farezone(46,"Hattingen/Sprochhövel"));
        g.addVertex(n46);
        FarezoneTrips n47 = new FarezoneTrips(new Farezone(47,"Witten/Wetter/Herdecke"));
        g.addVertex(n47);

        FarezoneTrips n50 = new FarezoneTrips(new Farezone(50,"Mönchengladbach"));
        g.addVertex(n50);
        FarezoneTrips n51 = new FarezoneTrips(new Farezone(51,"Korschenbroich"));
        g.addVertex(n51);
        FarezoneTrips n52 = new FarezoneTrips(new Farezone(52,"Neuss/Kaarst"));
        g.addVertex(n52);
        FarezoneTrips n53 = new FarezoneTrips(new Farezone(53,"Düsseldorf Süd"));
        g.addVertex(n53);
        FarezoneTrips n54 = new FarezoneTrips(new Farezone(54,"Mettmann/Wülfrath"));
        g.addVertex(n54);
        FarezoneTrips n55 = new FarezoneTrips(new Farezone(55,"Velbert"));
        g.addVertex(n55);
        FarezoneTrips n58 = new FarezoneTrips(new Farezone(58,"Hagen"));
        g.addVertex(n58);

        FarezoneTrips n61 = new FarezoneTrips(new Farezone(61,"Grevenbroich"));
        g.addVertex(n61);
        FarezoneTrips n62 = new FarezoneTrips(new Farezone(62,"Dormagen"));
        g.addVertex(n62);
        FarezoneTrips n63 = new FarezoneTrips(new Farezone(63,"Rommerskirchen"));
        g.addVertex(n63);
        FarezoneTrips n64 = new FarezoneTrips(new Farezone(64,"Erkrath/Haan/Hilden"));
        g.addVertex(n64);
        FarezoneTrips n65 = new FarezoneTrips(new Farezone(65,"Wuppertal West"));
        g.addVertex(n65);
        FarezoneTrips n66 = new FarezoneTrips(new Farezone(66,"Wuppertal Ost"));
        g.addVertex(n66);
        FarezoneTrips n67 = new FarezoneTrips(new Farezone(67,"Schwelm/Ennepetal/Gevelsberg/Breckerfeld"));
        g.addVertex(n67);

        FarezoneTrips n71 = new FarezoneTrips(new Farezone(71,"Emmerich"));
        g.addVertex(n71);
        FarezoneTrips n72 = new FarezoneTrips(new Farezone(72,"Jüchen"));
        g.addVertex(n72);
        FarezoneTrips n73 = new FarezoneTrips(new Farezone(73,"Langenfeld/Monheim"));
        g.addVertex(n73);
        FarezoneTrips n74 = new FarezoneTrips(new Farezone(74,"Solingen"));
        g.addVertex(n74);
        FarezoneTrips n75 = new FarezoneTrips(new Farezone(75,"Remscheid"));
        g.addVertex(n75);
        FarezoneTrips n77 = new FarezoneTrips(new Farezone(77,"Uedem"));
        g.addVertex(n77);
        FarezoneTrips n78 = new FarezoneTrips(new Farezone(78,"Kalkar"));
        g.addVertex(n78);
        FarezoneTrips n79 = new FarezoneTrips(new Farezone(79,"Rees"));
        g.addVertex(n79);

        FarezoneTrips n80 = new FarezoneTrips(new Farezone(80,"Kleve"));
        g.addVertex(n80);
        FarezoneTrips n81 = new FarezoneTrips(new Farezone(81,"Kranenburg"));
        g.addVertex(n81);
        FarezoneTrips n82 = new FarezoneTrips(new Farezone(82,"Bedburg-Hau"));
        g.addVertex(n82);
        FarezoneTrips n83 = new FarezoneTrips(new Farezone(83,"Xanten"));
        g.addVertex(n83);
        FarezoneTrips n84 = new FarezoneTrips(new Farezone(84,"Sonsbeck"));
        g.addVertex(n84);

        FarezoneTrips n85 = new FarezoneTrips(new Farezone(85,"Kevelaer"));
        g.addVertex(n85);
        FarezoneTrips n86 = new FarezoneTrips(new Farezone(86,"Goch/Weeze"));
        g.addVertex(n86);
        FarezoneTrips n88 = new FarezoneTrips(new Farezone(88,"Hamminkeln"));
        g.addVertex(n88);

        g.addEdge(n1, n4);
        g.addEdge(n1, n10);
        g.addEdge(n1, n11);
        g.addEdge(n1, n20);
        g.addEdge(n1, n21);

        g.addEdge(n2, n4);
        g.addEdge(n2, n11);
        g.addEdge(n2, n12);
        g.addEdge(n2, n16);
        g.addEdge(n2, n22);

        g.addEdge(n3, n12);
        g.addEdge(n3, n13);
        g.addEdge(n3, n14);
        g.addEdge(n3, n16);
        g.addEdge(n3, n83);
        g.addEdge(n3, n88);

        g.addEdge(n4, n1);
        g.addEdge(n4, n2);
        g.addEdge(n4, n10);
        g.addEdge(n4, n11);
        g.addEdge(n4, n16);
        g.addEdge(n4, n84);
        g.addEdge(n4, n85);

        g.addEdge(n5, n6);
        g.addEdge(n5, n14);
        g.addEdge(n5, n15);
        g.addEdge(n5, n17);
        g.addEdge(n5, n25);
        g.addEdge(n5, n26);

        g.addEdge(n6, n5);
        g.addEdge(n6, n15);
        g.addEdge(n6, n17);
        g.addEdge(n6, n18);

        g.addEdge(n10, n1);
        g.addEdge(n10, n4);
        g.addEdge(n10, n15);
        g.addEdge(n10, n20);
        g.addEdge(n10, n21);

        g.addEdge(n11, n1);
        g.addEdge(n11, n2);
        g.addEdge(n11, n4);
        g.addEdge(n11, n21);
        g.addEdge(n11, n22);
        g.addEdge(n11, n32);

        g.addEdge(n12, n2);
        g.addEdge(n12, n3);
        g.addEdge(n12, n16);
        g.addEdge(n12, n22);
        g.addEdge(n12, n23);

        g.addEdge(n13, n3);
        g.addEdge(n13, n14);
        g.addEdge(n13, n23);
        g.addEdge(n13, n24);
        g.addEdge(n13, n25);

        g.addEdge(n14, n3);
        g.addEdge(n14, n5);
        g.addEdge(n14, n13);
        g.addEdge(n14, n25);
        g.addEdge(n14, n88);

        g.addEdge(n15, n5);
        g.addEdge(n15, n6);
        g.addEdge(n15, n17);
        g.addEdge(n15, n18);
        g.addEdge(n15, n25);
        g.addEdge(n15, n26);
        g.addEdge(n15, n27);

        g.addEdge(n16, n2);
        g.addEdge(n16, n3);
        g.addEdge(n16, n4);
        g.addEdge(n16, n12);
        g.addEdge(n16, n83);
        g.addEdge(n16, n84);

        g.addEdge(n17, n5);
        g.addEdge(n17, n6);
        g.addEdge(n17, n15);
        g.addEdge(n17, n18);
        g.addEdge(n17, n26);
        g.addEdge(n17, n27);
        g.addEdge(n17, n28);
        g.addEdge(n17, n29);
        g.addEdge(n17, n36);

        g.addEdge(n18, n6);
        g.addEdge(n18, n15);
        g.addEdge(n18, n17);
        g.addEdge(n18, n28);
        g.addEdge(n18, n29);

        g.addEdge(n20, n1);
        g.addEdge(n20, n10);
        g.addEdge(n20, n21);
        g.addEdge(n20, n30);
        g.addEdge(n20, n31);

        g.addEdge(n21, n1);
        g.addEdge(n21, n10);
        g.addEdge(n21, n11);
        g.addEdge(n21, n20);
        g.addEdge(n21, n31);
        g.addEdge(n21, n32);
        g.addEdge(n21, n41);

        g.addEdge(n22, n2);
        g.addEdge(n22, n11);
        g.addEdge(n22, n12);
        g.addEdge(n22, n23);
        g.addEdge(n22, n32);
        g.addEdge(n22, n33);

        g.addEdge(n23, n12);
        g.addEdge(n23, n13);
        g.addEdge(n23, n22);
        g.addEdge(n23, n24);
        g.addEdge(n23, n33);
        g.addEdge(n23, n34);

        g.addEdge(n24, n13);
        g.addEdge(n24, n23);
        g.addEdge(n24, n25);
        g.addEdge(n24, n33);
        g.addEdge(n24, n34);
        g.addEdge(n24, n35);

        g.addEdge(n25, n5);
        g.addEdge(n25, n13);
        g.addEdge(n25, n14);
        g.addEdge(n25, n15);
        g.addEdge(n25, n24);
        g.addEdge(n25, n26);
        g.addEdge(n25, n35);

        g.addEdge(n26, n5);
        g.addEdge(n26, n15);
        g.addEdge(n26, n17);
        g.addEdge(n26, n25);
        g.addEdge(n26, n27);
        g.addEdge(n26, n35);
        g.addEdge(n26, n36);

        g.addEdge(n27, n15);
        g.addEdge(n27, n17);
        g.addEdge(n27, n26);
        g.addEdge(n27, n28);
        g.addEdge(n27, n35);
        g.addEdge(n27, n36);
        g.addEdge(n27, n37);

        g.addEdge(n28, n17);
        g.addEdge(n28, n18);
        g.addEdge(n28, n27);
        g.addEdge(n28, n29);
        g.addEdge(n28, n36);
        g.addEdge(n28, n37);
        g.addEdge(n28, n47);

        g.addEdge(n29, n17);
        g.addEdge(n29, n18);
        g.addEdge(n29, n28);
        g.addEdge(n29, n37);
        g.addEdge(n29, n38);


        g.addEdge(n30, n20);
        g.addEdge(n30, n31);
        g.addEdge(n30, n50);

        g.addEdge(n31, n20);
        g.addEdge(n31, n21);
        g.addEdge(n31, n30);
        g.addEdge(n31, n41);
        g.addEdge(n31, n50);
        g.addEdge(n31, n51);

        g.addEdge(n32, n11);
        g.addEdge(n32, n21);
        g.addEdge(n32, n22);
        g.addEdge(n32, n33);
        g.addEdge(n32, n41);
        g.addEdge(n32, n42);

        g.addEdge(n33, n22);
        g.addEdge(n33, n23);
        g.addEdge(n33, n24);
        g.addEdge(n33, n32);
        g.addEdge(n33, n34);
        g.addEdge(n33, n43);
        g.addEdge(n33, n44);

        g.addEdge(n34, n23);
        g.addEdge(n34, n24);
        g.addEdge(n34, n33);
        g.addEdge(n34, n35);
        g.addEdge(n34, n44);
        g.addEdge(n34, n45);

        g.addEdge(n35, n24);
        g.addEdge(n35, n25);
        g.addEdge(n35, n26);
        g.addEdge(n35, n27);
        g.addEdge(n35, n34);
        g.addEdge(n35, n36);
        g.addEdge(n35, n45);
        g.addEdge(n35, n46);
        g.addEdge(n35, n55);

        g.addEdge(n36, n17);
        g.addEdge(n36, n26);
        g.addEdge(n36, n27);
        g.addEdge(n36, n28);
        g.addEdge(n36, n35);
        g.addEdge(n36, n37);
        g.addEdge(n36, n46);
        g.addEdge(n36, n47);

        g.addEdge(n37, n27);
        g.addEdge(n37, n28);
        g.addEdge(n37, n29);
        g.addEdge(n37, n36);
        g.addEdge(n37, n38);
        g.addEdge(n37, n47);
        g.addEdge(n37, n58);

        g.addEdge(n38, n29);
        g.addEdge(n38, n37);
        g.addEdge(n38, n58);

        g.addEdge(n41, n21);
        g.addEdge(n41, n31);
        g.addEdge(n41, n32);
        g.addEdge(n41, n42);
        g.addEdge(n41, n50);
        g.addEdge(n41, n51);
        g.addEdge(n41, n52);

        g.addEdge(n42, n32);
        g.addEdge(n42, n41);
        g.addEdge(n42, n43);
        g.addEdge(n42, n51);
        g.addEdge(n42, n52);

        g.addEdge(n43, n33);
        g.addEdge(n43, n42);
        g.addEdge(n43, n44);
        g.addEdge(n43, n52);
        g.addEdge(n43, n53);
        g.addEdge(n43, n54);
        g.addEdge(n43, n64);

        g.addEdge(n44, n33);
        g.addEdge(n44, n34);
        g.addEdge(n44, n43);
        g.addEdge(n44, n45);
        g.addEdge(n44, n54);
        g.addEdge(n44, n55);

        g.addEdge(n45, n34);
        g.addEdge(n45, n35);
        g.addEdge(n45, n44);
        g.addEdge(n45, n46);
        g.addEdge(n45, n55);

        g.addEdge(n46, n35);
        g.addEdge(n46, n36);
        g.addEdge(n46, n45);
        g.addEdge(n46, n47);
        g.addEdge(n46, n55);
        g.addEdge(n46, n66);
        g.addEdge(n46, n67);

        g.addEdge(n47, n28);
        g.addEdge(n47, n36);
        g.addEdge(n47, n37);
        g.addEdge(n47, n46);
        g.addEdge(n47, n58);
        g.addEdge(n47, n67);

        g.addEdge(n50, n30);
        g.addEdge(n50, n31);
        g.addEdge(n50, n41);
        g.addEdge(n50, n51);
        g.addEdge(n50, n72);

        g.addEdge(n51, n31);
        g.addEdge(n51, n41);
        g.addEdge(n51, n42);
        g.addEdge(n51, n50);
        g.addEdge(n51, n52);
        g.addEdge(n51, n61);
        g.addEdge(n51, n72);

        g.addEdge(n52, n41);
        g.addEdge(n52, n42);
        g.addEdge(n52, n43);
        g.addEdge(n52, n51);
        g.addEdge(n52, n53);
        g.addEdge(n52, n61);
        g.addEdge(n52, n62);
        g.addEdge(n52, n63);
        g.addEdge(n52, n72);

        g.addEdge(n53, n43);
        g.addEdge(n53, n52);
        g.addEdge(n53, n62);
        g.addEdge(n53, n64);
        g.addEdge(n53, n73);

        g.addEdge(n54, n43);
        g.addEdge(n54, n44);
        g.addEdge(n54, n55);
        g.addEdge(n54, n64);
        g.addEdge(n54, n65);
        g.addEdge(n54, n74);

        g.addEdge(n55, n35);
        g.addEdge(n55, n44);
        g.addEdge(n55, n45);
        g.addEdge(n55, n46);
        g.addEdge(n55, n54);
        g.addEdge(n55, n65);
        g.addEdge(n55, n66);

        g.addEdge(n58, n37);
        g.addEdge(n58, n37);
        g.addEdge(n58, n47);
        g.addEdge(n58, n67);

        g.addEdge(n64, n43);
        g.addEdge(n64, n53);
        g.addEdge(n64, n54);
        g.addEdge(n64, n65);
        g.addEdge(n64, n73);
        g.addEdge(n64, n74);

        g.addEdge(n65, n54);
        g.addEdge(n65, n55);
        g.addEdge(n65, n64);
        g.addEdge(n65, n66);
        g.addEdge(n65, n74);
        g.addEdge(n65, n75);

        g.addEdge(n66, n46);
        g.addEdge(n66, n55);
        g.addEdge(n66, n65);
        g.addEdge(n66, n67);
        g.addEdge(n66, n75);

        g.addEdge(n67, n46);
        g.addEdge(n67, n47);
        g.addEdge(n67, n58);
        g.addEdge(n67, n66);

        g.addEdge(n71, n78);
        g.addEdge(n71, n79);
        g.addEdge(n71, n80);
        g.addEdge(n71, n82);

        g.addEdge(n74, n54);
        g.addEdge(n74, n64);
        g.addEdge(n74, n65);
        g.addEdge(n74, n73);
        g.addEdge(n74, n75);

        g.addEdge(n77, n78);
        g.addEdge(n77, n82);
        g.addEdge(n77, n83);
        g.addEdge(n77, n84);
        g.addEdge(n77, n85);
        g.addEdge(n77, n86);

        g.addEdge(n78, n71);
        g.addEdge(n78, n77);
        g.addEdge(n78, n79);
        g.addEdge(n78, n80);
        g.addEdge(n78, n82);
        g.addEdge(n78, n83);
        g.addEdge(n78, n86);

        g.addEdge(n79, n71);
        g.addEdge(n79, n78);
        g.addEdge(n79, n88);

        g.addEdge(n80, n71);
        g.addEdge(n80, n78);
        g.addEdge(n80, n81);
        g.addEdge(n80, n82);
        g.addEdge(n80, n86);

        g.addEdge(n81, n80);

        g.addEdge(n83, n3);
        g.addEdge(n83, n16);
        g.addEdge(n83, n77);
        g.addEdge(n83, n78);
        g.addEdge(n83, n84);

        g.addEdge(n84, n4);
        g.addEdge(n84, n16);
        g.addEdge(n84, n77);
        g.addEdge(n84, n83);
        g.addEdge(n84, n85);

        g.addEdge(n85, n4);
        g.addEdge(n85, n77);
        g.addEdge(n85, n84);
        g.addEdge(n85, n86);

        g.addEdge(n86, n77);
        g.addEdge(n86, n78);
        g.addEdge(n86, n80);
        g.addEdge(n86, n82);
        g.addEdge(n86, n85);

        g.addEdge(n88, n3);
        g.addEdge(n88, n14);
        g.addEdge(n88, n79);

        return g;
    }

    public static HashMap<Integer, Set<FarezoneTrips>> regionenHashMap(){
        HashMap<Integer, Set<FarezoneTrips>> regionenMitTarifzonenUndFahrten = new HashMap<>();

        FarezoneTrips n1 = new FarezoneTrips(new Farezone(1,"Kerken/Wachtendonk"));
        FarezoneTrips n2 = new FarezoneTrips(new Farezone(2,"Kamp-Lintfort"));
        FarezoneTrips n3 = new FarezoneTrips(new Farezone(3,"Wesel"));
        FarezoneTrips n4 = new FarezoneTrips(new Farezone(4,"Geldern/Issum"));
        FarezoneTrips n5 = new FarezoneTrips(new Farezone(5,"Dorsten"));
        FarezoneTrips n6 = new FarezoneTrips(new Farezone(6,"Haltern"));

        FarezoneTrips n10 = new FarezoneTrips(new Farezone(10,"Straelen"));
        FarezoneTrips n11 = new FarezoneTrips(new Farezone(11,"Neukirchen-Vluyn/Rheurdt"));
        FarezoneTrips n12 = new FarezoneTrips(new Farezone(12,"Rheinberg"));
        FarezoneTrips n13 = new FarezoneTrips(new Farezone(13,"Dinslaken/Voerde"));
        FarezoneTrips n14 = new FarezoneTrips(new Farezone(14,"Schermbeck/Hünxe"));
        FarezoneTrips n15 = new FarezoneTrips(new Farezone(15,"Marl"));
        FarezoneTrips n16 = new FarezoneTrips(new Farezone(16,"Alpen"));
        FarezoneTrips n17 = new FarezoneTrips(new Farezone(17,"Recklinghausen/Herten"));
        FarezoneTrips n18 = new FarezoneTrips(new Farezone(18,"Oer-Erkenschwick/Datteln"));

        FarezoneTrips n20 = new FarezoneTrips(new Farezone(20,"Nettetal/Brüggen"));
        FarezoneTrips n21 = new FarezoneTrips(new Farezone(21,"Kempen/Grefrath/Tönisvorst"));
        FarezoneTrips n22 = new FarezoneTrips(new Farezone(22,"Moers"));
        FarezoneTrips n23 = new FarezoneTrips(new Farezone(23,"Duisburg Nord"));
        FarezoneTrips n24 = new FarezoneTrips(new Farezone(24,"Oberhausen"));
        FarezoneTrips n25 = new FarezoneTrips(new Farezone(25,"Bottrop/Gladbeck"));
        FarezoneTrips n26 = new FarezoneTrips(new Farezone(26,"Gelsenkirchen"));
        FarezoneTrips n27 = new FarezoneTrips(new Farezone(27,"Herne"));
        FarezoneTrips n28 = new FarezoneTrips(new Farezone(28,"Castrop-Rauxel"));
        FarezoneTrips n29 = new FarezoneTrips(new Farezone(29,"Waltrop"));

        FarezoneTrips n30 = new FarezoneTrips(new Farezone(30,"Schwalmtal/Niederkrüchten"));
        FarezoneTrips n31 = new FarezoneTrips(new Farezone(31,"Viersen"));
        FarezoneTrips n32 = new FarezoneTrips(new Farezone(32,"Krefeld"));
        FarezoneTrips n33 = new FarezoneTrips(new Farezone(33,"Duisburg Mitte/Süd"));
        FarezoneTrips n34 = new FarezoneTrips(new Farezone(34,"Mühlheim/Ruhr"));
        FarezoneTrips n35 = new FarezoneTrips(new Farezone(35,"Essen Mitte/Nord"));
        FarezoneTrips n36 = new FarezoneTrips(new Farezone(36,"Bochum"));
        FarezoneTrips n37 = new FarezoneTrips(new Farezone(37,"Dortmund Mitte/West"));
        FarezoneTrips n38 = new FarezoneTrips(new Farezone(38,"Dortmund Ost"));


        FarezoneTrips n41 = new FarezoneTrips(new Farezone(41,"Willich"));
        FarezoneTrips n42 = new FarezoneTrips(new Farezone(42,"Meerbusch"));
        FarezoneTrips n43 = new FarezoneTrips(new Farezone(43,"Düsseldorf Mitte/Nord"));
        FarezoneTrips n44 = new FarezoneTrips(new Farezone(44,"Ratingen/Heiligenhaus"));
        FarezoneTrips n45 = new FarezoneTrips(new Farezone(45,"Essen Süd"));
        FarezoneTrips n46 = new FarezoneTrips(new Farezone(46,"Hattingen/Sprochhövel"));
        FarezoneTrips n47 = new FarezoneTrips(new Farezone(47,"Witten/Wetter/Herdecke"));

        FarezoneTrips n50 = new FarezoneTrips(new Farezone(50,"Mönchengladbach"));
        FarezoneTrips n51 = new FarezoneTrips(new Farezone(51,"Korschenbroich"));
        FarezoneTrips n52 = new FarezoneTrips(new Farezone(52,"Neuss/Kaarst"));
        FarezoneTrips n53 = new FarezoneTrips(new Farezone(53,"Düsseldorf Süd"));
        FarezoneTrips n54 = new FarezoneTrips(new Farezone(54,"Mettmann/Wülfrath"));
        FarezoneTrips n55 = new FarezoneTrips(new Farezone(55,"Velbert"));
        FarezoneTrips n58 = new FarezoneTrips(new Farezone(58,"Hagen"));

        FarezoneTrips n61 = new FarezoneTrips(new Farezone(61,"Grevenbroich"));
        FarezoneTrips n62 = new FarezoneTrips(new Farezone(62,"Dormagen"));
        FarezoneTrips n63 = new FarezoneTrips(new Farezone(63,"Rommerskirchen"));
        FarezoneTrips n64 = new FarezoneTrips(new Farezone(64,"Erkrath/Haan/Hilden"));
        FarezoneTrips n65 = new FarezoneTrips(new Farezone(65,"Wuppertal West"));
        FarezoneTrips n66 = new FarezoneTrips(new Farezone(66,"Wuppertal Ost"));
        FarezoneTrips n67 = new FarezoneTrips(new Farezone(67,"Schwelm/Ennepetal/Gevelsberg/Breckerfeld"));

        FarezoneTrips n71 = new FarezoneTrips(new Farezone(71,"Emmerich"));
        FarezoneTrips n72 = new FarezoneTrips(new Farezone(72,"Jüchen"));
        FarezoneTrips n73 = new FarezoneTrips(new Farezone(73,"Langenfeld/Monheim"));
        FarezoneTrips n74 = new FarezoneTrips(new Farezone(74,"Solingen"));
        FarezoneTrips n75 = new FarezoneTrips(new Farezone(75,"Remscheid"));
        FarezoneTrips n77 = new FarezoneTrips(new Farezone(77,"Uedem"));
        FarezoneTrips n78 = new FarezoneTrips(new Farezone(78,"Kalkar"));
        FarezoneTrips n79 = new FarezoneTrips(new Farezone(79,"Rees"));

        FarezoneTrips n80 = new FarezoneTrips(new Farezone(80,"Kleve"));
        FarezoneTrips n81 = new FarezoneTrips(new Farezone(81,"Kranenburg"));
        FarezoneTrips n82 = new FarezoneTrips(new Farezone(82,"Bedburg-Hau"));
        FarezoneTrips n83 = new FarezoneTrips(new Farezone(83,"Xanten"));
        FarezoneTrips n84 = new FarezoneTrips(new Farezone(84,"Sonsbeck"));
        FarezoneTrips n85 = new FarezoneTrips(new Farezone(85,"Kevelaer"));
        FarezoneTrips n86 = new FarezoneTrips(new Farezone(86,"Goch/Weeze"));
        FarezoneTrips n88 = new FarezoneTrips(new Farezone(88,"Hamminkeln"));


        Set<FarezoneTrips> region1 = new HashSet<>();
        region1.add(n3);
        region1.add(n4);
        region1.add(n16);
        region1.add(n71);
        region1.add(n77);
        region1.add(n78);
        region1.add(n79);
        region1.add(n80);
        region1.add(n81);
        region1.add(n82);
        region1.add(n83);
        region1.add(n84);
        region1.add(n85);
        region1.add(n86);
        region1.add(n88);

        Set<FarezoneTrips> region2 = new HashSet<>();
        region2.add(n1);
        region2.add(n2);
        region2.add(n4);
        region2.add(n10);
        region2.add(n11);
        region2.add(n16);
        region2.add(n71);
        region2.add(n77);
        region2.add(n78);
        region2.add(n79);
        region2.add(n80);
        region2.add(n81);
        region2.add(n82);
        region2.add(n83);
        region2.add(n84);
        region2.add(n85);
        region2.add(n86);

        Set<FarezoneTrips> region3 = new HashSet<>();
        region3.add(n1);
        region3.add(n2);
        region3.add(n3);
        region3.add(n4);
        region3.add(n10);
        region3.add(n11);
        region3.add(n12);
        region3.add(n16);
        region3.add(n20);
        region3.add(n21);
        region3.add(n22);
        region3.add(n23);
        region3.add(n31);
        region3.add(n32);
        region3.add(n33);
        region3.add(n41);
        region3.add(n83);
        region3.add(n84);
        region3.add(n85);

        Set<FarezoneTrips> region4 = new HashSet<>();
        region4.add(n2);
        region4.add(n3);
        region4.add(n4);
        region4.add(n11);
        region4.add(n12);
        region4.add(n13);
        region4.add(n14);
        region4.add(n16);
        region4.add(n22);
        region4.add(n77);
        region4.add(n78);
        region4.add(n79);
        region4.add(n83);
        region4.add(n84);
        region4.add(n85);
        region4.add(n88);

        Set<FarezoneTrips> region5 = new HashSet<>();
        region5.add(n3);
        region5.add(n5);
        region5.add(n12);
        region5.add(n13);
        region5.add(n14);
        region5.add(n16);
        region5.add(n24);
        region5.add(n25);
        region5.add(n71);
        region5.add(n77);
        region5.add(n78);
        region5.add(n79);
        region5.add(n83);
        region5.add(n84);
        region5.add(n88);

        Set<FarezoneTrips> region6 = new HashSet<>();
        region6.add(n2);
        region6.add(n3);
        region6.add(n5);
        region6.add(n6);
        region6.add(n12);
        region6.add(n13);
        region6.add(n14);
        region6.add(n15);
        region6.add(n16);
        region6.add(n17);
        region6.add(n22);
        region6.add(n23);
        region6.add(n24);
        region6.add(n25);
        region6.add(n26);
        region6.add(n33);
        region6.add(n34);
        region6.add(n35);
        region6.add(n45);
        region6.add(n79);
        region6.add(n83);
        region6.add(n88);

        Set<FarezoneTrips> region7 = new HashSet<>();
        region7.add(n5);
        region7.add(n6);
        region7.add(n13);
        region7.add(n14);
        region7.add(n15);
        region7.add(n17);
        region7.add(n18);
        region7.add(n24);
        region7.add(n25);
        region7.add(n26);
        region7.add(n27);
        region7.add(n28);
        region7.add(n29);
        region7.add(n35);
        region7.add(n36);
        region7.add(n37);
        region7.add(n38);
        region7.add(n45);
        region7.add(n46);
        region7.add(n47);

        Set<FarezoneTrips> region8 = new HashSet<>();
        region8.add(n5);
        region8.add(n13);
        region8.add(n14);
        region8.add(n15);
        region8.add(n17);
        region8.add(n24);
        region8.add(n25);
        region8.add(n26);
        region8.add(n27);
        region8.add(n34);
        region8.add(n35);
        region8.add(n36);
        region8.add(n45);
        region8.add(n46);
        region8.add(n47);
        region8.add(n55);

        Set<FarezoneTrips> region9 = new HashSet<>();
        region9.add(n6);
        region9.add(n15);
        region9.add(n17);
        region9.add(n18);
        region9.add(n26);
        region9.add(n27);
        region9.add(n28);
        region9.add(n29);
        region9.add(n35);
        region9.add(n36);
        region9.add(n37);
        region9.add(n38);
        region9.add(n45);
        region9.add(n46);
        region9.add(n47);
        region9.add(n58);
        region9.add(n67);

        Set<FarezoneTrips> region10 = new HashSet<>();
        region10.add(n17);
        region10.add(n24);
        region10.add(n25);
        region10.add(n26);
        region10.add(n27);
        region10.add(n28);
        region10.add(n34);
        region10.add(n35);
        region10.add(n36);
        region10.add(n37);
        region10.add(n38);
        region10.add(n45);
        region10.add(n46);
        region10.add(n47);
        region10.add(n55);
        region10.add(n58);
        region10.add(n65);
        region10.add(n66);
        region10.add(n67);

        Set<FarezoneTrips> region11 = new HashSet<>();
        region11.add(n28);
        region11.add(n35);
        region11.add(n36);
        region11.add(n37);
        region11.add(n38);
        region11.add(n44);
        region11.add(n45);
        region11.add(n46);
        region11.add(n47);
        region11.add(n54);
        region11.add(n55);
        region11.add(n58);
        region11.add(n65);
        region11.add(n66);
        region11.add(n67);
        region11.add(n75);

        Set<FarezoneTrips> region12 = new HashSet<>();
        region12.add(n12);
        region12.add(n13);
        region12.add(n22);
        region12.add(n23);
        region12.add(n24);
        region12.add(n25);
        region12.add(n26);
        region12.add(n27);
        region12.add(n33);
        region12.add(n34);
        region12.add(n35);
        region12.add(n36);
        region12.add(n44);
        region12.add(n45);
        region12.add(n46);
        region12.add(n54);
        region12.add(n55);
        region12.add(n65);
        region12.add(n66);

        Set<FarezoneTrips> region13 = new HashSet<>();
        region13.add(n23);
        region13.add(n24);
        region13.add(n25);
        region13.add(n33);
        region13.add(n34);
        region13.add(n35);
        region13.add(n43);
        region13.add(n44);
        region13.add(n45);
        region13.add(n46);
        region13.add(n53);
        region13.add(n54);
        region13.add(n55);
        region13.add(n64);
        region13.add(n65);
        region13.add(n66);

        Set<FarezoneTrips> region14 = new HashSet<>();
        region14.add(n35);
        region14.add(n43);
        region14.add(n44);
        region14.add(n45);
        region14.add(n46);
        region14.add(n53);
        region14.add(n54);
        region14.add(n55);
        region14.add(n64);
        region14.add(n65);
        region14.add(n66);
        region14.add(n67);
        region14.add(n74);
        region14.add(n75);

        Set<FarezoneTrips> region15 = new HashSet<>();
        region15.add(n22);
        region15.add(n23);
        region15.add(n24);
        region15.add(n32);
        region15.add(n33);
        region15.add(n34);
        region15.add(n35);
        region15.add(n42);
        region15.add(n43);
        region15.add(n44);
        region15.add(n45);
        region15.add(n52);
        region15.add(n53);
        region15.add(n54);
        region15.add(n55);
        region15.add(n62);
        region15.add(n64);
        region15.add(n65);
        region15.add(n66);
        region15.add(n73);
        region15.add(n74);
        region15.add(n75);

        Set<FarezoneTrips> region16 = new HashSet<>();
        region16.add(n2);
        region16.add(n11);
        region16.add(n12);
        region16.add(n13);
        region16.add(n21);
        region16.add(n22);
        region16.add(n23);
        region16.add(n24);
        region16.add(n25);
        region16.add(n32);
        region16.add(n33);
        region16.add(n34);
        region16.add(n41);
        region16.add(n42);
        region16.add(n43);
        region16.add(n44);
        region16.add(n51);
        region16.add(n52);
        region16.add(n53);
        region16.add(n54);
        region16.add(n64);

        Set<FarezoneTrips> region17 = new HashSet<>();
        region17.add(n11);
        region17.add(n21);
        region17.add(n22);
        region17.add(n23);
        region17.add(n31);
        region17.add(n32);
        region17.add(n33);
        region17.add(n41);
        region17.add(n42);
        region17.add(n43);
        region17.add(n44);
        region17.add(n50);
        region17.add(n51);
        region17.add(n52);
        region17.add(n53);
        region17.add(n54);
        region17.add(n61);
        region17.add(n62);
        region17.add(n63);
        region17.add(n64);
        region17.add(n72);
        region17.add(n73);

        Set<FarezoneTrips> region18 = new HashSet<>();
        region18.add(n1);
        region18.add(n10);
        region18.add(n11);
        region18.add(n20);
        region18.add(n21);
        region18.add(n30);
        region18.add(n31);
        region18.add(n32);
        region18.add(n41);
        region18.add(n42);
        region18.add(n50);
        region18.add(n51);
        region18.add(n52);
        region18.add(n61);
        region18.add(n72);

        Set<FarezoneTrips> region19 = new HashSet<>();
        region19.add(n1);
        region19.add(n2);
        region19.add(n4);
        region19.add(n10);
        region19.add(n11);
        region19.add(n12);
        region19.add(n20);
        region19.add(n21);
        region19.add(n22);
        region19.add(n23);
        region19.add(n30);
        region19.add(n31);
        region19.add(n32);
        region19.add(n33);
        region19.add(n41);
        region19.add(n42);
        region19.add(n50);
        region19.add(n51);

        regionenMitTarifzonenUndFahrten.put(1, region1);
        regionenMitTarifzonenUndFahrten.put(2, region2);
        regionenMitTarifzonenUndFahrten.put(3, region3);
        regionenMitTarifzonenUndFahrten.put(4, region4);
        regionenMitTarifzonenUndFahrten.put(5, region5);
        regionenMitTarifzonenUndFahrten.put(6, region6);
        regionenMitTarifzonenUndFahrten.put(7, region7);
        regionenMitTarifzonenUndFahrten.put(8, region8);
        regionenMitTarifzonenUndFahrten.put(9, region9);
        regionenMitTarifzonenUndFahrten.put(10, region10);
        regionenMitTarifzonenUndFahrten.put(11, region11);
        regionenMitTarifzonenUndFahrten.put(12, region12);
        regionenMitTarifzonenUndFahrten.put(13, region13);
        regionenMitTarifzonenUndFahrten.put(14, region14);
        regionenMitTarifzonenUndFahrten.put(15, region15);
        regionenMitTarifzonenUndFahrten.put(16, region16);
        regionenMitTarifzonenUndFahrten.put(17, region17);
        regionenMitTarifzonenUndFahrten.put(18, region18);
        regionenMitTarifzonenUndFahrten.put(19, region19);

        return regionenMitTarifzonenUndFahrten;
    }
}
