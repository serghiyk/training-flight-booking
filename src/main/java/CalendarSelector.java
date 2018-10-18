import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import java.util.Calendar;
import java.util.Random;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.url;

public class CalendarSelector {

    private SelenideElement departureLocation = $("#package-origin-hp-package");
    private SelenideElement destinationLocation = $("#package-destination-hp-package");

    private SelenideElement possibleAddressesBox = $("display-group-results");
    private ElementsCollection displayedAddressesResults = $$(By.xpath("//a[contains(@id, 'aria-option')]"));


    private SelenideElement departureApartmentField = $("#package-departing-hp-package");
    private SelenideElement returningApartmentField = $("#package-returning-hp-package");
    private SelenideElement calendarPopUp = $(By.xpath("//div[@class='datepicker-dropdown']/div"));
    private ElementsCollection twoMonthsDays = $$(By.xpath("//div[@class='datepicker-dropdown']/div/div//tbody//button"));
    private ElementsCollection leftMonthBoxDays = $$(By.xpath("//div[@class='datepicker-dropdown']/div/div[2]//tbody//button"));
    private ElementsCollection rightMonthBoxDays = $$(By.xpath("//div[@class='datepicker-dropdown']/div/div[3]//tbody//button"));
    private SelenideElement previousMonthButton = $(By.xpath("//div[@class='datepicker-dropdown']/div/button[1]"));
    private SelenideElement nextMonthButton = $(By.xpath("//div[@class='datepicker-dropdown']/div/button[2]"));

    private SelenideElement searchButton = $("#search-button-hp-package");

    CommonMethods commonMethods = new CommonMethods();

    public CalendarSelector open(){
        Selenide.open("/");
        return this;
    }


    private void enterDepartureAddress(String city_airport){
        departureLocation.click();
        departureLocation.sendKeys(city_airport);
        commonMethods.sleepFor(2000);
    }

    private void enterDestinationAddress(String city_airport){
        destinationLocation.click();
        destinationLocation.sendKeys(city_airport);
        commonMethods.sleepFor(2000);
    }

    private String chooseSuggestedAddress(SelenideElement departure_or_destinationField){
        Random rand = new Random();
        int displayedResults = displayedAddressesResults.size();
        int randomNumber = rand.nextInt(displayedResults);
        displayedAddressesResults.get(randomNumber).click();
        return departure_or_destinationField.getValue();
    }

//    private String getEnteredAddressFromField(SelenideElement fieldElement){
//        return fieldElement.getText();
//    }

    public boolean fillAndSelectDepartureAddress(String address){
        enterDepartureAddress(address);
        return chooseSuggestedAddress(departureLocation).contains(address);
    }

    public boolean fillAndSelectDestinationAddress(String address){
        enterDestinationAddress(address);
        return chooseSuggestedAddress(destinationLocation).contains(address);
    }



    public void openDepartureCalendar(){
//        if (!calendarPopUp.isDisplayed())
            departureApartmentField.click();
    }

    public void openReturningCalendar(){
//        if (!calendarPopUp.isDisplayed())
            returningApartmentField.click();
    }

    public int getCellYear(SelenideElement year){
        return Integer.parseInt(year.getAttribute("data-year"));
    }

    public int getCellMonth(SelenideElement month){
        return Integer.parseInt(month.getAttribute("data-month")) + 1;
    }

    public int getCellDay(SelenideElement day){
        return Integer.parseInt(day.getAttribute("data-day"));
    }

    private void goToPreviousMonth() {
        if (previousMonthButton.isDisplayed())
            previousMonthButton.click();
        else
            System.out.println("Previous Month button is not available");
    }

    private void goToNextMonth(){
        if (nextMonthButton.isDisplayed())
            nextMonthButton.click();
        else
            System.out.println("Next Month button is not available");
    }

    private SelenideElement isSomeDayInMonthAvailable(ElementsCollection fullMonthCollection){
        for (SelenideElement element: fullMonthCollection) {
            if (element.isEnabled()) {
                return element;
            }
        }
        return null;
    }

    public boolean isRequiredMonthDisplayed(int monthRequired){
        if (isSomeDayInMonthAvailable(leftMonthBoxDays) != null) {
            int currentMonth = getCellMonth(isSomeDayInMonthAvailable(leftMonthBoxDays));
            if (currentMonth == monthRequired)
                return true;
        }
        if (isSomeDayInMonthAvailable(rightMonthBoxDays) != null) {
            int currentMonth = getCellMonth(isSomeDayInMonthAvailable(rightMonthBoxDays));
            if (currentMonth == monthRequired)
                return true;
        }
        return false;
    }

    public boolean isRequiredYearDisplayed(int yearRequired){
        if (isSomeDayInMonthAvailable(leftMonthBoxDays) != null) {
            int currentYear = getCellYear(isSomeDayInMonthAvailable(leftMonthBoxDays));
            if (currentYear == yearRequired)
                return true;
        }
        if (isSomeDayInMonthAvailable(rightMonthBoxDays) != null) {
            int currentYear = getCellYear(isSomeDayInMonthAvailable(rightMonthBoxDays));
            if (currentYear == yearRequired)
                return true;
        }
        return false;
    }

    public void findMonth(int monthRequired){
        if (monthRequired < 1 || monthRequired > 12){
            System.out.println("Invalid month number");
        }
        else{
            while (!isRequiredMonthDisplayed(monthRequired))
                goToNextMonth();
        }
    }

    public void findYear(int yearRequired){
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        if (yearRequired < currentYear || yearRequired > currentYear + 20){
            System.out.println("Invalid year");
        }
        else{
            while (!isRequiredYearDisplayed(yearRequired))
                goToNextMonth();
        }
    }


    public int getFirstAvailableDay(ElementsCollection availableDaysList){
        System.out.println("first day: " + availableDaysList.get(0).getAttribute("data-day"));
        return Integer.parseInt(availableDaysList.get(0).getAttribute("data-day"));
    }

    public boolean isDayAvailable(ElementsCollection availableDaysList, int requiredDay){
        for (SelenideElement element : availableDaysList){
            if (element.getAttribute("data-day").equals(Integer.toString(requiredDay)))
                return true;
        }
        return false;
    }

    public void getRequiredDay(ElementsCollection collection, int requiredDay){
        for (SelenideElement element : collection) {
            if (element.getAttribute("data-day").equals(Integer.toString(requiredDay)))
                element.click();
        }
    }

    public int getDaysCountInMonth(ElementsCollection fullMonthCollection){
        return fullMonthCollection.size();
    }

    public SelenideElement findRequiredDay(int dayOfTheMonth, int month, int year){
        findYear(year);
        findMonth(month);

        for (SelenideElement element : twoMonthsDays){
            if (element.isEnabled())
                if (getCellMonth(element) == month && getCellDay(element) == dayOfTheMonth) {
                    return element;
            }
        }
        return null;
    }


//    public void selectFromLeftBox(int dayOfTheMonth){
//        if (getAvailableDaysCollection(leftMonthBoxDays).size() > 0){
//            if (isDayAvailable(leftMonthBoxDays, dayOfTheMonth)){
//                getRequiredDay(leftMonthBoxDays, dayOfTheMonth);
////                int indexOfRequiredDay = getIndexOfTheDay(leftMonthBoxDays, dayOfTheMonth);
////                leftMonthBoxDays.get(indexOfRequiredDay).click();
//            }
//            else{
//                System.out.println("No flights available for selected day");
//            }
//        }
//        else{
//            System.out.println("No available flights in thins month");
//        }
//    }
//
//    public void selectFromRightBox(int dayOfTheMonth){
//        if (getAvailableDaysCollection(rightMonthBoxDays).size() > 0){
//            if (isDayAvailable(rightMonthBoxDays, dayOfTheMonth)){
//                getRequiredDay(rightMonthBoxDays, dayOfTheMonth);
////                int indexOfRequiredDay = getIndexOfTheDay(rightMonthBoxDays, dayOfTheMonth);
////                rightMonthBoxDays.get(indexOfRequiredDay).click();
//            }
//            else{
//                System.out.println("No flights available for selected day");
//            }
//        }
//        else{
//            System.out.println("No available flights in this month");
//        }
//    }

    public void selectDayFromPopUp(int dayOfTheMonth, int month, int year) {
        if (calendarPopUp.isDisplayed()) {

            SelenideElement cellInCalendar = findRequiredDay(dayOfTheMonth, month, year);
            if (cellInCalendar != null) {
                cellInCalendar.click();
                commonMethods.sleepFor(1000);
            }
            else
                System.out.println("Element disabled");
        }
        else
            System.out.println("Calendar pop-up is not displayed");
    }

    public void selectDepartureDate(int dayOfTheMonth, int month, int year) {
        openDepartureCalendar();
        selectDayFromPopUp(dayOfTheMonth, month, year);
    }

    public void selectReturningDate(int dayOfTheMonth, int month, int year) {
        openReturningCalendar();
        selectDayFromPopUp(dayOfTheMonth, month, year);
    }

    public boolean isDepartureDateSelected(int day, int month, int year) {
        openDepartureCalendar();
        SelenideElement requiredElement = findRequiredDay(day, month, year);
        if (requiredElement != null)
            return requiredElement.getAttribute("class").contains("start");
        else
            return false;
    }

    public boolean isReturnDateSelected(int day, int month, int year) {
        openReturningCalendar();
        SelenideElement requiredElement = findRequiredDay(day, month, year);
        if (requiredElement != null)
            return requiredElement.getAttribute("class").contains("end");
        else
            return false;
    }


    public void clickAtSearchButton(){
        searchButton.click();
    }

    public String getURL(){
        return url();
    }


}
