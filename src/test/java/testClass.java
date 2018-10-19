import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class testClass extends ConfigurationFile{

    CalendarSelector calendarSelector;

    @BeforeClass
    public void initClasses(){
        calendarSelector = new CalendarSelector();
        calendarSelector.open();
    }

    @Test(priority = 0)
    public void selectDepartureAddress(){
        Assert.assertTrue(calendarSelector.fillAndSelectDepartureAddress("Lviv"));
    }

    @Test(priority = 1)
    public void selectDestinationAddress(){
        Assert.assertTrue(calendarSelector.fillAndSelectDestinationAddress("London"));
    }


    @Test(priority = 2)
    public void selectDepartureDate() {
        int day = 7;
        int month = 2;
        int year = 2019;
        calendarSelector.selectDepartureDate(day, month, year);
        Assert.assertTrue(calendarSelector.isDepartureDateSelected(day, month, year));
    }

    @Test(priority = 3)
    public void selectReturnDate() {
        int day = 17;
        int month = 2;
        int year = 2019;
        calendarSelector.selectReturningDate(day, month, year);
        Assert.assertTrue(calendarSelector.isReturnDateSelected(day, month, year));
    }

//    @Test(priority = 4)
//    public void findFlight(){
//        calendarSelector.fillAndSelectDepartureAddress("Lviv");
//        calendarSelector.fillAndSelectDestinationAddress("London");
//        calendarSelector.selectDepartureDate(23, 11, 2018);
//        calendarSelector.selectReturningDate(28, 11, 2018);
//        calendarSelector.clickAtSearchButton();
//        Assert.assertTrue(calendarSelector.getURL().contains("https://www.expedia.com/Hotel-Search"));
//    }

    @Test(priority = 5)
    public void addMoreRooms(){
        Assert.assertTrue(calendarSelector.addRooms(3));
    }

    @Test(priority = 6)
    public void setAdultsInTheRoom(){
        calendarSelector.openTravelersPopUp();
        Assert.assertTrue(calendarSelector.setNumberOfAdultsForRoom(4, 1));
    }

    @Test(priority = 6)
    public void setChildrenInTheRoom(){
        calendarSelector.openTravelersPopUp();
        Assert.assertTrue(calendarSelector.setNumberOfChildrenForRoom(2, 1));
    }

    @Test(priority = 6)
    public void setInfantsInTheRoom(){
        calendarSelector.openTravelersPopUp();
        Assert.assertTrue(calendarSelector.setNumberOfInfantsForRoom(3, 3));
    }

    @Test(priority = 7)
    public void selectAge10ForTheChild(){
        calendarSelector.openTravelersPopUp();
        calendarSelector.setNumberOfChildrenForRoom(5, 1);
        Assert.assertTrue(calendarSelector.selectValueFromChildrenDropDown(1, "10", 4));
    }

    @Test(priority = 7)
    public void selectAgeUnder1ForTheInfant(){
        calendarSelector.openTravelersPopUp();
        calendarSelector.setNumberOfInfantsForRoom(3, 1);
        Assert.assertTrue(calendarSelector.selectValueFromInfantsDropDown(1, "Under 1", 2));
    }

    @Test(priority = 8)
    public void deleteRoomNo2(){
        calendarSelector.openTravelersPopUp();
        calendarSelector.addRooms(3);
        Assert.assertTrue(calendarSelector.isRoomDeleted(2));
    }


}