import service.MenuService;

public class Main {

    static MenuService menuService = new MenuService();

    public static void main(String[] args) {
        menuService.displayMenu();
    }

}