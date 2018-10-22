import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class testClass extends ConfigurationFile{

    MainPage mainPage;

    @BeforeClass
    public void initClasses(){
        mainPage = new MainPage();
        mainPage.open();
    }

    @Test(priority = 0)
    public void selectDepartureAddress(){
        Assert.assertTrue(mainPage.fillAndSelectDepartureAddress("Lviv"));
    }

    @Test(priority = 1)
    public void selectDestinationAddress(){
        Assert.assertTrue(mainPage.fillAndSelectDestinationAddress("London"));
    }


    @Test(priority = 2)
    public void selectDepartureDate() {
        int day = 7;
        int month = 2;
        int year = 2019;
        mainPage.selectDepartureDate(day, month, year);
        Assert.assertTrue(mainPage.isDepartureDateSelected(day, month, year));
    }

    @Test(priority = 3)
    public void selectReturnDate() {
        int day = 17;
        int month = 2;
        int year = 2019;
        mainPage.selectReturningDate(day, month, year);
        Assert.assertTrue(mainPage.isReturnDateSelected(day, month, year));
    }

//    @Test(priority = 4)
//    public void findFlight(){
//        mainPage.fillAndSelectDepartureAddress("Lviv");
//        mainPage.fillAndSelectDestinationAddress("London");
//        mainPage.selectDepartureDate(23, 11, 2018);
//        mainPage.selectReturningDate(28, 11, 2018);
//        mainPage.clickAtSearchButton();
//        Assert.assertTrue(mainPage.getURL().contains("https://www.expedia.com/Hotel-Search"));
//    }

    @Test(priority = 5)
    public void addMoreRooms(){
        Assert.assertTrue(mainPage.addRooms(3));
    }

    @Test(priority = 6)
    public void setAdultsInTheRoom(){
        mainPage.openTravelersPopUp();
        Assert.assertTrue(mainPage.setNumberOfAdultsForRoom(4, 1));
    }

    @Test(priority = 6)
    public void setChildrenInTheRoom(){
        mainPage.openTravelersPopUp();
        Assert.assertTrue(mainPage.setNumberOfChildrenForRoom(2, 1));
    }

    @Test(priority = 6)
    public void setInfantsInTheRoom(){
        mainPage.openTravelersPopUp();
        Assert.assertTrue(mainPage.setNumberOfInfantsForRoom(3, 3));
    }

    @Test(priority = 7)
    public void selectAge10ForTheChild(){
        mainPage.openTravelersPopUp();
        mainPage.setNumberOfChildrenForRoom(5, 1);
        Assert.assertTrue(mainPage.selectValueFromChildrenDropDown(1, "10", 4));
    }

    @Test(priority = 7)
    public void selectAgeUnder1ForTheInfant(){
        mainPage.openTravelersPopUp();
        mainPage.setNumberOfInfantsForRoom(3, 1);
        Assert.assertTrue(mainPage.selectValueFromInfantsDropDown(1, "Under 1", 2));
    }

    @Test(priority = 8)
    public void deleteRoomNo2(){
        mainPage.openTravelersPopUp();
        mainPage.addRooms(3);
        Assert.assertTrue(mainPage.isRoomDeleted(2));
    }


}