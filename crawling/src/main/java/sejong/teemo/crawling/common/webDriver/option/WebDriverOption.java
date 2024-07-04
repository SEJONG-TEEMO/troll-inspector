package sejong.teemo.crawling.common.webDriver.option;

public enum WebDriverOption {
    REMOTE("remote"),
    NORMAL("normal");

    private final String value;

    WebDriverOption(String value) {
        this.value = value;
    }
}
