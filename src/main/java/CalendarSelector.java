import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import javax.swing.plaf.synth.SynthToolTipUI;
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

    // Travelers
    private SelenideElement travelersButton = $(By.xpath("//div[@id='traveler-selector-hp-package']//ul/li/button"));
    private SelenideElement travelersPopUp = $(By.xpath("//li[@class='open']/div/div"));

    private SelenideElement addNewRoom = $(By.xpath("//a[@class='secondary gcw-component gcw-component-initialized gcw-clone-field']"));

    private ElementsCollection roomsForTravelers = $$(By.xpath("//li[@class='open']//div[contains(@class, 'traveler-selector-room-data target-clone-field')]"));
    // *** For every room should be selected number of Adults, Children, Infants. Values can be increased and decreased by "+" and "-" buttons.
    // *   Also, if there is an Infant, new section will be displayed with additional selectors

    // Bellow need to ba applied to element from "roomsForTravelers" collection
    private String decreaseValueForTravelersXPath = "..//button[@class='uitk-step-input-button uitk-step-input-minus']";
    private String increaseValueForTravelersXPath = "..//button[@class='uitk-step-input-button uitk-step-input-plus']";
    private String currentNumberOfPersonsXPath = "..//span[@class='uitk-step-input-value']";
    private SelenideElement childrenSection = $(By.xpath("//div[@class='children-wrapper']"));
    private SelenideElement infantsSection = $(By.xpath("//div[@class='infants-wrapper']"));
    private String childrenAgeSelectorsXPath = "..//div[@class='children-wrapper']//select";
    private String infantsAgeSelectorsXPath = "..//div[@class='infants-wrapper']//select";






    private SelenideElement searchButton = $("#search-button-hp-package");

    private CommonMethods commonMethods = new CommonMethods();

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

    public boolean fillAndSelectDepartureAddress(String address){
        enterDepartureAddress(address);
        return chooseSuggestedAddress(departureLocation).contains(address);
    }

    public boolean fillAndSelectDestinationAddress(String address){
        enterDestinationAddress(address);
        return chooseSuggestedAddress(destinationLocation).contains(address);
    }


    private void openDepartureCalendar(){
        departureApartmentField.click();
    }

    private void openReturningCalendar(){
        returningApartmentField.click();
    }

    private int getCellYear(SelenideElement year){
        return Integer.parseInt(year.getAttribute("data-year"));
    }

    private int getCellMonth(SelenideElement month){
        return Integer.parseInt(month.getAttribute("data-month")) + 1;
    }

    private int getCellDay(SelenideElement day){
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

    public SelenideElement isSomeDayInMonthAvailable(ElementsCollection fullMonthCollection){
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

    private void findMonth(int monthRequired){
        if (monthRequired < 1 || monthRequired > 12){
            System.out.println("Invalid month number");
        }
        else{
            while (!isRequiredMonthDisplayed(monthRequired))
                goToNextMonth();
        }
    }

    private void findYear(int yearRequired){
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        if (yearRequired < currentYear || yearRequired > currentYear + 1){
            System.out.println("Invalid year");
        }
        else{
            while (previousMonthButton.isDisplayed())
                    goToPreviousMonth();
            while (!isRequiredYearDisplayed(yearRequired))
                goToNextMonth();
        }
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

    /****
    Adjusting rooms quantity, visitors
     ***/

    public void openTravelersPopUp(){
        if (!travelersPopUp.isDisplayed())
            travelersButton.click();
    }

    private boolean addRoom(){
        if (addNewRoom.isDisplayed()){
            addNewRoom.click();
            return true;
        }
        else {
            System.out.println("Maximum rooms number reached");
            return false;
        }
    }

    private SelenideElement getElementForRoom(int numberOfTheRoom){
        if (roomsForTravelers.size() < numberOfTheRoom){
            System.out.println("Please add more rooms. Room #" + numberOfTheRoom + " is not available");
            return null;
        }
        else
            return roomsForTravelers.get(numberOfTheRoom - 1);
    }

    private int getNumberOfAdultsInRoom(int numberOfTheRoom){
        SelenideElement room = getElementForRoom(numberOfTheRoom);
        return Integer.parseInt(room.$$(By.xpath(currentNumberOfPersonsXPath)).get(0).getText());
    }

    private int getNumberOfChildrenInRoom(int numberOfTheRoom){
        SelenideElement room = getElementForRoom(numberOfTheRoom);
        return Integer.parseInt(room.$$(By.xpath(currentNumberOfPersonsXPath)).get(1).getText());
    }

    private int getNumberOfInfantsInRoom(int numberOfTheRoom){
        SelenideElement room = getElementForRoom(numberOfTheRoom);
        return Integer.parseInt(room.$$(By.xpath(currentNumberOfPersonsXPath)).get(2).getText());
    }

    public boolean setNumberOfAdultsForRoom(int overallAdults, int roomNumber){
        SelenideElement room = getElementForRoom(roomNumber);
        if (room != null){
            int currentVisitors = getNumberOfAdultsInRoom(roomNumber);
            if (currentVisitors < overallAdults) {
                for (int i = currentVisitors; i < overallAdults; i++)
                    room.$$(By.xpath(increaseValueForTravelersXPath)).get(0).click();
            }
            else if (currentVisitors > overallAdults){
                for (int i = currentVisitors; i > overallAdults; i--)
                    room.$$(By.xpath(decreaseValueForTravelersXPath)).get(0).click();
            }
        }
        return overallAdults == getNumberOfAdultsInRoom(roomNumber);
    }

    public boolean setNumberOfChildrenForRoom(int overallChildren, int roomNumber){
        SelenideElement room = getElementForRoom(roomNumber);
        if (room != null){
            int currentVisitors = getNumberOfChildrenInRoom(roomNumber);
            if (currentVisitors < overallChildren) {
                for (int i = currentVisitors; i < overallChildren; i++)
                    room.$$(By.xpath(increaseValueForTravelersXPath)).get(1).click();
            }
            else if (currentVisitors > overallChildren){
                for (int i = currentVisitors; i > overallChildren; i--)
                    room.$$(By.xpath(decreaseValueForTravelersXPath)).get(1).click();
            }
        }
        return overallChildren == getNumberOfChildrenInRoom(roomNumber);
    }

    public boolean setNumberOfInfantsForRoom(int overallInfants, int roomNumber){
        SelenideElement room = getElementForRoom(roomNumber);
        if (room != null){
            int currentVisitors = getNumberOfInfantsInRoom(roomNumber);
            if (currentVisitors < overallInfants) {
                for (int i = currentVisitors; i < overallInfants; i++)
                    room.$$(By.xpath(increaseValueForTravelersXPath)).get(2).click();
            }
            else if (currentVisitors > overallInfants){
                for (int i = currentVisitors; i > overallInfants; i--)
                    room.$$(By.xpath(decreaseValueForTravelersXPath)).get(2).click();
            }
        }
        return overallInfants == getNumberOfInfantsInRoom(roomNumber);
    }


    // Dropdowns
    private ElementsCollection childrenDropdownCollection(int roomNumber){
        return getElementForRoom(roomNumber).$$(By.xpath(childrenAgeSelectorsXPath));
    }

    private ElementsCollection infantsDropdownCollection(int roomNumber){
        return getElementForRoom(roomNumber).$$(By.xpath(infantsAgeSelectorsXPath));
    }

    public int getNumberOfDisplayedSelectorsForCollection(ElementsCollection collectionOfDropDowns){
        int displayedSelectors = 0;
        for (SelenideElement selector : collectionOfDropDowns){
            if (selector.isDisplayed())
                displayedSelectors++;
        }
        return displayedSelectors;
    }

    private SelenideElement getRequiredDropDown(ElementsCollection childrenOrInfantsCollection, int numberOfTheVisitor){
        if (childrenOrInfantsCollection.size() < 1) {
            System.out.println("No dropdown available");
            return null;
        }
        else
            return childrenOrInfantsCollection.get(numberOfTheVisitor - 1);
    }

    public boolean selectValueFromDropdown(SelenideElement dropDown, String value){
        if (dropDown.isDisplayed()){
//            dropDown.click();
            try {
                dropDown.selectOption(value);
            }
            catch (NoSuchElementException e){
                System.out.println("Invalid value");
            }
            return dropDown.getText().equals(value);
        }
        else {
            System.out.println("Dropdown is not available");
            return false;
        }
    }

    public boolean selectValueFromChildrenDropDown(int roomNumber, String value, int dropdownNumber){
        if (dropdownNumber > getNumberOfDisplayedSelectorsForCollection(childrenDropdownCollection(roomNumber))){
            System.out.println("Required dropdown is not displayed");
            return false;
        }
        else {
            SelenideElement dropdown = getRequiredDropDown(childrenDropdownCollection(roomNumber), dropdownNumber);
            return selectValueFromDropdown(dropdown, value);
        }
    }

    public boolean selectValueFromInfantsDropDown(int roomNumber, String value, int dropdownNumber){
        if (dropdownNumber > getNumberOfDisplayedSelectorsForCollection(infantsDropdownCollection(roomNumber))){
            System.out.println("Required dropdown is not displayed");
            return false;
        }
        else {
            SelenideElement dropdown = getRequiredDropDown(infantsDropdownCollection(roomNumber), dropdownNumber);
            return selectValueFromDropdown(dropdown, value);
        }
    }


    public boolean addRooms(int totalRequiredNumberOfRooms) {
        openTravelersPopUp();
        if (roomsForTravelers.size() < totalRequiredNumberOfRooms && roomsForTravelers.size() <= 3){
            for (int i = roomsForTravelers.size(); i < totalRequiredNumberOfRooms; i ++)
                addRoom();
        }
        else if (totalRequiredNumberOfRooms > 3){
            System.out.println("3 rooms - maximum");
            return false;
        }
        return roomsForTravelers.size() == totalRequiredNumberOfRooms;
    }


    public void clickAtSearchButton(){
        searchButton.click();
    }

    public String getURL(){
        return url();
    }


}
