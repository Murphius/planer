package com.example.lkjhgf;

class AlteTests {
    /*
    @Test
    public void test4() {
        ArrayList<TripItem> trips;
        ArrayList<TicketToBuy> ticketToBuyArrayList;
        //Drei Fahrten innerhalb von 24h - 2x D 1x C
        trips = generateTripsWithoutUserInformation(0, 3);
        ticketToBuyArrayList = FarezoneD.optimisation(trips, timeTickets);
        FarezoneUtil.checkTicketsForOtherTrips(trips, ticketToBuyArrayList);
        Assert.assertEquals(ticketToBuyArrayList.size(), 1);
        Assert.assertEquals(ticketToBuyArrayList.get(0).getTripList().size(), 3);

        //Vier Fahrten - 3x innerhalb von 24h - 2x D 1x C - 1x außerhalb A2
        trips = generateTripsWithoutUserInformation(0, 4);
        ticketToBuyArrayList = FarezoneD.optimisation(trips, timeTickets);
        FarezoneUtil.checkTicketsForOtherTrips(trips, ticketToBuyArrayList);
        Assert.assertEquals(ticketToBuyArrayList.size(), 1);
        Assert.assertEquals(ticketToBuyArrayList.get(0).getTripList().size(), 3);
        Assert.assertEquals(trips.size(), 1);
    }

   /* @Test
    public void test5() {
        ArrayList<TicketToBuy> ticketToBuyArrayList = generateTicketsToBuy(0, 2);
        Assert.assertEquals(ticketToBuyArrayList.size(), 2);
        MyVRRprovider.sumUpTickets(ticketToBuyArrayList);
        Assert.assertEquals(ticketToBuyArrayList.size(), 1);
        Assert.assertEquals(ticketToBuyArrayList.get(0).getTripList().size(), 3);
    }

    @Test
    public void testMyVrrOptimise() {
        Activity activity = rule.getActivity();
        //Eine Fahrt mit einem Erwachsenen, Preisstufe D
        ArrayList<TripItem> tripItems = generateTripsWithUserInformation(1, 2);
        String id = tripItems.get(0).getTripID();
        HashMap<Fare.Type, ArrayList<TicketToBuy>> tickets01 = MainMenu.myProvider.optimise(tripItems, new HashMap<>(), activity);
        Assert.assertEquals(tripItems.size(), 1);
        Assert.assertEquals(tickets01.get(Fare.Type.ADULT).size(), 1);
        Assert.assertTrue(tickets01.get(Fare.Type.CHILD).isEmpty());
        Assert.assertEquals(tickets01.get(Fare.Type.ADULT).get(0).getTripList().size(), 1);
        Assert.assertEquals(tickets01.get(Fare.Type.ADULT).get(0).getTripList().get(0).getTripID(), id);
        Assert.assertEquals(tickets01.get(Fare.Type.ADULT).get(0).getTicket().getName(), "Einzelticket E");

        //Eine Fahrt mit einem Erwachsenem, einem Kind, Preisstufe D
        HashMap<Fare.Type, Integer> numP01 = new HashMap<>();
        numP01.put(Fare.Type.ADULT, 1);
        numP01.put(Fare.Type.CHILD, 1);
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        startCalendar.set(2020, Calendar.MAY, 23, 12, 15);
        endCalendar.set(2020, Calendar.MAY, 23, 12, 15 + 38);
        TripItem tripItem01 = new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", 1, numP01);
        tripItems.clear();
        tripItems.add(tripItem01);

        HashMap<Fare.Type, ArrayList<TicketToBuy>> tickets02 = MainMenu.myProvider.optimise(tripItems, new HashMap<>(), activity);
        Assert.assertEquals(tickets02.get(Fare.Type.ADULT).size(), 1);
        Assert.assertEquals(tickets02.get(Fare.Type.CHILD).size(), 1);
        Assert.assertEquals(tickets02.get(Fare.Type.ADULT).get(0).getTripList().size(), 1);
        Assert.assertEquals(tickets02.get(Fare.Type.CHILD).get(0).getTripList().size(), 1);
        Assert.assertEquals(tickets02.get(Fare.Type.ADULT).get(0).getTicket().getName(), "Einzelticket E");
        Assert.assertEquals(tickets02.get(Fare.Type.CHILD).get(0).getTicket().getName(), "Einzelticket K");

        //Eine Fahrt mit einem Erwachsenem, Preisstufe D
        //Eine Fahrt mit einem Erwachsenem & einem Kind, Preisstufe D
        numP01.put(Fare.Type.ADULT, 1);
        numP01.put(Fare.Type.CHILD, 1);
        HashMap<Fare.Type, Integer> numP02 = new HashMap<>();
        numP02.put(Fare.Type.ADULT, 1);
        numP02.put(Fare.Type.CHILD, 0);
        TripItem tripItem02 = new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", 1, numP02);
        startCalendar.set(2020, Calendar.MAY, 23, 15, 6);
        endCalendar.set(2020, Calendar.MAY, 23, 15, 6 + 38);
        TripItem tripItem03 = new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", 2, numP01);
        tripItems.clear();
        tripItems.add(tripItem02);
        tripItems.add(tripItem03);
        HashMap<Fare.Type, ArrayList<TicketToBuy>> tickets03 = MainMenu.myProvider.optimise(tripItems, new HashMap<>(), activity);
        Assert.assertEquals(tickets03.get(Fare.Type.ADULT).size(), 1);
        Assert.assertEquals(tickets03.get(Fare.Type.CHILD).size(), 1);
        Assert.assertEquals(tickets03.get(Fare.Type.ADULT).get(0).getTripList().size(), 2);
        Assert.assertEquals(tickets03.get(Fare.Type.CHILD).get(0).getTripList().size(), 1);
        Assert.assertEquals(tickets03.get(Fare.Type.ADULT).get(0).getTicket().getName(), "24-StundenTicket-1");
        Assert.assertEquals(tickets03.get(Fare.Type.CHILD).get(0).getTicket().getName(), "Einzelticket K");
    }

    @Test
    public void testcreateTripListForEachUser() {
        ArrayList<TripItem> trips = generateTripsWithUserInformation(0, 4);
        HashMap<Integer, ArrayList<TripItem>> hashMap = MainMenu.myProvider.createTripListForEachUser(trips, Fare.Type.ADULT);
        Assert.assertEquals(hashMap.keySet().size(), 3);
        Assert.assertEquals(hashMap.get(1).size(), 4);
        Assert.assertEquals(hashMap.get(2).size(), 3);
        Assert.assertEquals(hashMap.get(3).size(), 1);

        ArrayList<TicketToBuy> ticketToBuyArrayList = TimeOptimisation.optimierungPreisstufeD(hashMap.get(1), timeTickets);
        TimeOptimisation.checkTicketsForOtherTrips(hashMap.get(1), ticketToBuyArrayList);
        Assert.assertEquals(ticketToBuyArrayList.size(), 1);
        Assert.assertEquals(ticketToBuyArrayList.get(0).getTripList().size(), 3);
        Assert.assertEquals(hashMap.get(1).size(), 1);
    }

    @Test
    public void testVierStundenTicketValid() {
        TimeTicket vierHTicket = new TimeTicket(new int[]{720, 720, 720, 720, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE}, "4-StundenTicket-1", Fare.Type.ADULT, 4 * 60 * 60 * 1000, new int[]{3, 2, 2, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE}, 9, 3, true, 1);

        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();

        startCalendar.set(2020, Calendar.JUNE, 2, 15, 06);
        endCalendar.set(2020, Calendar.JUNE, 2, 15, 6 + 38);
        TripItem tripItem01 = new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", 1);
        TripItem tripItem02 = new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A2", 1);

        startCalendar.set(2020, Calendar.JUNE, 2, 8, 40);
        endCalendar.set(2020, Calendar.JUNE, 2, 9, 6);
        TripItem tripItem03 = new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A2", 1);

        startCalendar.set(2020, Calendar.JUNE, 6, 8, 06);
        endCalendar.set(2020, Calendar.JUNE, 6, 10, 6 + 38);
        TripItem tripItem04 = new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A2", 1);

        startCalendar.set(2020, Calendar.JUNE, 2, 23, 06);
        endCalendar.set(2020, Calendar.JUNE, 3, 1, 6 + 38);
        TripItem tripItem05 = new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A2", 1);

        startCalendar.set(2020, Calendar.JUNE, 2, 23, 06);
        endCalendar.set(2020, Calendar.JUNE, 2, 23, 6 + 38);
        TripItem tripItem06 = new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A2", 1);

        startCalendar.set(2020, Calendar.JUNE, 2, 23, 06);
        endCalendar.set(2020, Calendar.JUNE, 3, 3, 1);
        TripItem tripItem07 = new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A2", 1);

        Assert.assertFalse(vierHTicket.isValidTrip(tripItem01));
        Assert.assertTrue(vierHTicket.isValidTrip(tripItem02));
        Assert.assertFalse(vierHTicket.isValidTrip(tripItem03));
        Assert.assertTrue(vierHTicket.isValidTrip(tripItem04));
        Assert.assertTrue(vierHTicket.isValidTrip(tripItem05));
        Assert.assertTrue(vierHTicket.isValidTrip(tripItem06));
        Assert.assertFalse(vierHTicket.isValidTrip(tripItem07));
    }

    @Test
    public void testHappyHourTicketValidTrip() {
        TimeTicket happyHourTicket = new TimeTicket(new int[]{1370, 1370, 1370, 1370, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE}, "Happy Hour Ticket", Fare.Type.ADULT, 12 * 60 * 60 * 1000, new int[]{2, 2, 2, 2, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE}, 18, 6, false, 1);

        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();

        startCalendar.set(2020, Calendar.JUNE, 2, 15, 06);
        endCalendar.set(2020, Calendar.JUNE, 2, 15, 6 + 38);
        TripItem tripItem01 = new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", 1);
        TripItem tripItem02 = new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A2", 1);

        startCalendar.set(2020, Calendar.JUNE, 2, 18, 40);
        endCalendar.set(2020, Calendar.JUNE, 2, 19, 6);
        TripItem tripItem03 = new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A2", 1);

        startCalendar.set(2020, Calendar.JUNE, 6, 8, 06);
        endCalendar.set(2020, Calendar.JUNE, 7, 10, 6 + 38);
        TripItem tripItem04 = new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A2", 1);

        startCalendar.set(2020, Calendar.JUNE, 6, 23, 06);
        endCalendar.set(2020, Calendar.JUNE, 7, 1, 6 + 38);
        TripItem tripItem05 = new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A2", 1);

        startCalendar.set(2020, Calendar.JUNE, 2, 23, 06);
        endCalendar.set(2020, Calendar.JUNE, 3, 5, 6 + 38);
        TripItem tripItem06 = new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A2", 1);

        startCalendar.set(2020, Calendar.JUNE, 3, 23, 06);
        endCalendar.set(2020, Calendar.JUNE, 5, 3, 1);
        TripItem tripItem07 = new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "A2", 1);

        Assert.assertFalse(happyHourTicket.isValidTrip(tripItem01));
        Assert.assertFalse(happyHourTicket.isValidTrip(tripItem02));
        Assert.assertTrue(happyHourTicket.isValidTrip(tripItem03));
        Assert.assertFalse(happyHourTicket.isValidTrip(tripItem04));
        Assert.assertTrue(happyHourTicket.isValidTrip(tripItem05));
        Assert.assertTrue(happyHourTicket.isValidTrip(tripItem06));
        Assert.assertFalse(happyHourTicket.isValidTrip(tripItem07));
    }

    @Test
    public void loeschen_einer_fahrt_zukuenftigeTickets(){
        ArrayList<TripItem> trips = new ArrayList<>();
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        HashMap<Fare.Type, Integer> numP = new HashMap<>();
        numP.put(Fare.Type.CHILD, 0);
        numP.put(Fare.Type.ADULT, 1);

        startCalendar.set(2020, Calendar.JUNE, 22, 15, 06);
        endCalendar.set(2020, Calendar.MAY, Calendar.JUNE, 15, 6 + 38);
        TripItem tripItem01 = new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "C", 1, numP);
        trips.add(tripItem01);

        startCalendar.set(2020, Calendar.JUNE, 24, 15, 06);
        endCalendar.set(2020, Calendar.JUNE, 24, 15, 6 + 38);
        TripItem tripItem02 = new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", 2, numP);
        trips.add(tripItem02);

        startCalendar.set(2020, Calendar.JUNE, 25, 10, 06);
        endCalendar.set(2020, Calendar.JUNE, 25, 10, 6 + 38);
        TripItem tripItem03 = new TestTripItem(startCalendar.getTime(), endCalendar.getTime(), "D", 3, numP);
        trips.add(tripItem03);
    }
*/
}
