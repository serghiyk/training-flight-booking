public class CommonMethods {

    public void sleepFor(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        }
        catch (InterruptedException e) {
            System.out.println("Exception");
        }
    }
}
