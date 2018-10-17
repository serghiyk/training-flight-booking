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

    @Test(priority = 4)
    public void findFlight(){
        calendarSelector.fillAndSelectDepartureAddress("Lviv");
        calendarSelector.fillAndSelectDestinationAddress("London");
        calendarSelector.selectDepartureDate(23, 11, 2018);
        calendarSelector.selectReturningDate(28, 11, 2018);
        calendarSelector.clickAtSearchButton();
        Assert.assertTrue(calendarSelector.getURL().contains("https://www.expedia.com/Hotel-Search"));
    }
}
